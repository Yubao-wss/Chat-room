package multi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 处理客户端业务
 * @Author: Waston
 * @Date: 2019/7/18 14:04
 */
public class ClientHandler implements Runnable {

    private static final Map<String,Socket> SOCKET_MAP = new
            ConcurrentHashMap<>();

    private final Socket client;
    private String currentName;//当前客户端注册名称

    public ClientHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        // TODO 业务实现
        try {
            InputStream in = this.client.getInputStream();
            // 按行读取消息
            Scanner scanner = new Scanner(in);
            while(true){
                String line = scanner.nextLine();
                if(line.startsWith("register")){
                    //注册流程
                    String[] segments = line.split(":");
                    if(segments.length == 2 && segments[0].equals("register")){
                        String name = segments[1];
                        register(name);
                    }

                    continue;
                }
                if(line.startsWith("private")){
                    //私聊流程
                    String[] segments = line.split(":");
                    if(segments.length == 3 && segments[0].equals("private")){
                        String name = segments[1];
                        String message = segments[2];
                        privateChat(name,message);
                    }


                    continue;
                }
                if(line.startsWith("group")){
                    //群聊流程
                    String[] segments = line.split(":");
                    if(segments.length == 2 && segments[0].equals("group")){
                        String message = segments[1];
                        groupChat(message);
                    }


                    continue;
                }
                if(line.equals("quit")){
                    //退出流程
                    quit();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void register(String name){
        //注册
        //name -> socket
        //key -> value
        SOCKET_MAP.put(name,this.client);
        this.currentName = name;
        this.sendMessage(this.client,"恭喜 "+name+" 注册成功");
        printOnlineClient();
    }

    /**
     * 显示当前在线客户端名称
     */
    private void printOnlineClient(){
        System.out.println("当前在线的客户端名称列表：");
        for(String key:SOCKET_MAP.keySet()){
            System.out.println(key);
        }
    }

    /**
     * 向客户端发送消息的方法
     * @param socket
     * @param message
     */
    private void sendMessage(Socket socket,String message){
        try {
            OutputStream out = socket.getOutputStream();
            PrintStream printStream = new PrintStream(out);
            printStream.println(message);
            printStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void groupChat(String message){
        //群聊
        //发送消息给当前在线的客户端（不包括自己）
        for(Map.Entry<String,Socket> entry : SOCKET_MAP.entrySet()){
            Socket socket = entry.getValue();
            if(socket != this.client){
                this.sendMessage(socket,this.currentName + " 说 " + message);
            }
        }
    }

    private void privateChat(String name,String message){
        //私聊
        Socket socket = SOCKET_MAP.get(name);
        if(socket != null){
            this.sendMessage(socket,this.currentName+" 说 "+message);
        }
    }

    private void quit(){
        //退出
        //使用迭代器
        Iterator<Map.Entry<String,Socket>> iterator = SOCKET_MAP.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String,Socket> entry = iterator.next();
            if(entry.getValue() == this.client){
                System.out.println(entry.getKey() + " 退出");
                iterator.remove();
                break;
            }
        }
        printOnlineClient();
    }
}
