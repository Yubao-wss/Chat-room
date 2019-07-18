package single;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * @Description: 单线程聊天室客户端
 * @Author: Waston
 * @Date: 2019/7/17 22:30
 */
public class SingleClient {
    public static void main(String[] args) {
        //客户端
        String host = "127.0.0.1";
        int port = 8080;
            try {
                //1. 创建客户端Socket和服务器建立连接
                final Socket socket = new Socket(host, port);

                //2. 客户端进行接收和发送数据
                //2.1 发送数据（write）
                OutputStream out = socket.getOutputStream();
                PrintStream printStream = new PrintStream(out);
                printStream.println("服务器我来了。。。");
                printStream.flush();

                //2.2 接收数据（read）
                InputStream in = socket.getInputStream();
                Scanner scanner = new Scanner(in);
                String message = scanner.next();
                System.out.println("从服务端接收的数据：" + message);

            } catch (IOException e) {

            }
        }
}
