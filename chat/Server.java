package test;
/**
 * @author 17130130226 Liu Yilei
 * @paramn For Socket Server
 */
import  java.io.*;
import  java.net.*;
import java.util.LinkedList;
import java.util.Queue;

class MyServer {
    private  static Queue<String> messages = new LinkedList<String>();
    private Queue<BufferedWriter> writer = new LinkedList<BufferedWriter>();
    private Queue<BufferedReader> reader = new LinkedList<BufferedReader>();

    //继承Thread类
    class MyThread extends Thread {
        Socket socket = null;
        InetAddress inetAddress = null;

        public MyThread(Socket socket, InetAddress inetAddress) {
            this.socket = socket;
            this.inetAddress = inetAddress;
        }

        @Override
        public void run() {
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                reader.offer(bufferedReader);
                while (true) {
                    //stand here!
                    String information[];
                    information = bufferedReader.readLine().split(",");
                    System.out.println(information[2]);
                    if(information[2].equals("3"))
                    {
                        for (BufferedWriter assign : writer){
                            assign.write(information[0] + ","+ information[1] + "," + information[3] + ","+information[4] + "\n");
                            System.out.println(information[0] + ","+ information[1] + "," + information[3] + ","+information[4] + "\n");
                            assign.flush();
                        }
                    }
                    if (0 == Integer.parseInt(information[2])) {
                        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
                        writer.offer(bufferedWriter);
                        for (String s : messages) {
                            bufferedWriter.write(s + "\n");
                            bufferedWriter.flush();
                        }
                        bufferedWriter.write("end" + "\n");
                        bufferedWriter.flush();
                    }
                    if (information[1].equals("Exit")) {
                        System.out.println(inetAddress + ": Disconnect!");
                        break;
                    }
                    if(information[2].equals("3")) continue;
                    if (!information[1].equals("")) {
                        for (BufferedWriter assign : writer) {
                            assign.write(information[0] + information[1] + "\n");
                            assign.flush();
                        }
                        messages.offer(information[0] + information[1]);
                    }
                    //System.out.println(messages);
                }
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void ForServer()throws Exception {
        // 监听指定的端口
        int port = 55533;
        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Waiting for connecting");
            while (true) {
                //在这里等待
                Socket socket = server.accept();
                //获取连接
                InetAddress inetAddress=socket.getInetAddress();
                MyThread new_connect= new MyThread(socket,inetAddress);
                new_connect.start();
            }
            } catch(Exception e){
                e.printStackTrace();
            }

    }
}
public class Server {
    public static void main(String[] args) throws Exception{
        MyServer server_socket = new MyServer();
        try {
            server_socket.ForServer();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
