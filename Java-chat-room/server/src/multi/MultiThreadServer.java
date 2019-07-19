package multi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: 多线程版服务器
 * @Author: Waston
 * @Date: 2019/7/18 14:02
 */
public class MultiThreadServer {

    private static  ExecutorService executor ;

    public static void main(String[] args) {
        //java -jar chat-root-server.jar --port=8080 --thread=10
        System.out.println(Arrays.toString(args));

        int defaultPort = 8080;
        int defaultThread = 10;
        int port = defaultPort;
        int thread = defaultThread;
        for(String arg : args){
            if(arg.startsWith("--port=")){
                String portStr = arg.substring("--port=".length());
                try {
                    port = Integer.parseInt(portStr);
                }catch (NumberFormatException e){
                    port = defaultPort;
                }
            }
            if(arg.startsWith("--thread=")){
                String threadStr = arg.substring("--thread=".length());
                try {
                    thread = Integer.parseInt(threadStr);
                }catch (NumberFormatException e){
                    thread = defaultThread;
                }
            }
        }
        try {
            // 创建线程池
            executor = Executors.newFixedThreadPool(10, new ThreadFactory() {
                private final AtomicInteger id = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setName("Thread-Client-Handler-"+ id.getAndIncrement());
                    return t;
                }
            });

            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务器端启动"+serverSocket.getInetAddress()+":"
                    +serverSocket.getLocalPort());

            while(true){
                final Socket client = serverSocket.accept();
                // 使用线程池
                executor.execute(new ClientHandler(client));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
