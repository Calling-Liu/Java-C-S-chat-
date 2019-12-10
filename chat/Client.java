package test;
/**
 * @author 17130130226 Liu Yilei
 * @paramn For Socket Client
 */
import  java.io.*;
import  java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import java.lang.Thread;

class MyClient {
    private  BufferedWriter bufferedWriter = null;
    private  BufferedReader bufferedReader = null;
    private  Socket socket = null;
    public String change_id;
    private Queue<String> messages = new LinkedList<String>();
    private Queue<String> private_messages = new LinkedList<String>();
    private  UserInterface my_user;
    private  PrivateUserInterface private_people = null;
    private  Queue<String> online_users_my = new LinkedList<String>();
    private  Queue<String> online_users_private = new LinkedList<String>();
    private  Queue<MyPrivateThread> private_thread = new LinkedList<MyPrivateThread>();
    private  int id_flag = 0;

    //继承Thread类,全局通信
    class MyClientThread extends Thread{
        Socket socket = null;
        InetAddress inetAddress=null;
        public MyClientThread(Socket socket, InetAddress inetAddress) {
            this.socket = socket;
            this.inetAddress=inetAddress;
        }
        @Override
        public void run(){
            try {
                while (true){
                    String try_message[];
                    String message = bufferedReader.readLine();
                    if(message.contains(",")){
                        try_message = message.split(",");
                        if (try_message[2].equals(change_id))
                        {
                            id_flag = 1;//自己是接收方
                        }
                        else
                        {
                            if (try_message[3].equals(change_id))
                            {
                                id_flag = 2;//自己发送方
                            }
                            else
                            {
                                continue;//不接受私聊消息
                            }
                        }
                        if(private_people != null) {
                            if(id_flag == 1) {
                                private_messages.offer("Private:" + try_message[0] + try_message[1]);
                            }
                            else {
                                private_messages.offer("I:" + try_message[1]);
                            }
                            private_people.add_text(private_messages, private_messages.size());
                            JDBCInterface online_change = new JDBCInterface();
                            online_users_private = online_change.Query_online();
                            private_people.change_online(online_users_private);
                            System.out.println(private_messages);
                        }
                        messages.offer("Private:" + try_message[0]+try_message[1]);
                    }else {
                        messages.offer(message);
                    }
                    my_user.add_text(messages,messages.size());
                    JDBCInterface online_change = new JDBCInterface();
                    online_users_my = online_change.Query_online();
                    my_user.change_online(online_users_my);
                    //System.out.println(online_users_my);
                }
            }catch (IOException e){
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //继承Thread类,私聊
    class MyPrivateThread extends Thread{
        Socket socket = null;
        InetAddress inetAddress=null;
        boolean flag_my;
        public MyPrivateThread(Socket socket, InetAddress inetAddress,boolean flag) {
            this.socket = socket;
            this.inetAddress=inetAddress;
            this.flag_my = flag;
        }
        @Override
        public void run(){
            while(flag_my)
            {
                try {
                    sleep(1000);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        public void stop_my(){
            flag_my = false;
        }
    }
    public  MyClient(String try_id) throws Exception{
        // 要连接的服务端IP地址和端口
        String host = "127.0.0.1";
        change_id = try_id;
        int port = 55533;
        try {
            socket =new Socket(host,port);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            my_user = new UserInterface(this);
            my_user.setTitle("ID:" + try_id);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //非私聊时
    public boolean SendMessage(String new_message) throws  Exception{
        try {
            JDBCInterface new_name = new JDBCInterface();
            bufferedWriter.write(new_name.Query_name(change_id) + ":,"+ new_message + "\n");
            bufferedWriter.flush();
            if (new_message.equals("Exit,1")) {
                my_user.dispose();
                socket.shutdownOutput();
                JDBCInterface change_status = new JDBCInterface();
                change_status.change(change_id);
                return false;
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    //私聊
    public boolean SendPrivateMessage(String new_message) throws  Exception{
        try {
            JDBCInterface new_name = new JDBCInterface();
            bufferedWriter.write(new_name.Query_name(change_id)+":,"+ new_message+"\n");
            bufferedWriter.flush();
            if (new_message.split(",")[0].equals("Exit")) {
                my_user.dispose();
                socket.shutdownOutput();
                JDBCInterface change_status = new JDBCInterface();
                change_status.change(change_id);
                return false;
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public Queue update_messages() throws  Exception{
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        while (true) {
            String message = bufferedReader.readLine();
            if(message.equals("end")) { break;}
            messages.offer(message);
        }
        return  messages;
    }
    public void get(){
        InetAddress inetAddress=socket.getInetAddress();
        MyClientThread new_connect= new MyClientThread(socket,inetAddress);
        new_connect.start();
    }
    public void get_private(PrivateUserInterface Private_chat_people){
        private_people = Private_chat_people;
        boolean my_flag = true;
        InetAddress inetAddress=socket.getInetAddress();
        MyPrivateThread new_connect= new MyPrivateThread(socket,inetAddress,my_flag);
        private_thread.offer(new_connect);
        new_connect.start();
    }
    public void return_public()
    {
        my_user.return_chat_house();
        private_people = null;
        private_messages =null;
        for(MyPrivateThread thread:private_thread)
        {
            thread.stop_my();
        }
    }
}
