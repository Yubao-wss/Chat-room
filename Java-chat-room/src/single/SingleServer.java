package single;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Description: 单线程聊天室服务端
 * @Author: Waston
 * @Date: 2019/7/17 22:35
 */
public class SingleServer {
    public static void main(String[] args) {

        //聊天室的服务器端

        //1. 创建ServerSocket
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("服务端启动 " + serverSocket.getInetAddress() + ":" + serverSocket.getLocalPort());

            //2.等待客户端连接
            //这一步是阻塞操作，直到客户端的连接到服务端才会继续执行
            while (true) {
                final Socket socket = serverSocket.accept();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("客户端连接：" + socket.getRemoteSocketAddress() + ":" + socket.getPort());
                            //3.服务端可以进行接收和发送数据
                            //3.1 从服务端接收数据（read）
                            InputStream in = socket.getInputStream();
                            Scanner scanner = new Scanner(in);
                            String message = scanner.next();
                            System.out.println("接收到客户端发送的数据：" + message);

                            //3.2 向服务端发送数据（write）
                            OutputStream out = socket.getOutputStream();
                            PrintStream printStream = new PrintStream(out);
                            printStream.println("你好客户端");
                            printStream.flush();

                        } catch (IOException e) {


                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            System.out.println("服务端发生异常：" + e.getMessage());
        }
    }
}
