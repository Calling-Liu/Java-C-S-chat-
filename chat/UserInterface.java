package test;
/**
 * @author 17130130226 Liu YiLei
 * @paramn For Use
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;

class UserInterface extends JFrame implements ActionListener{
    private static final int FRAME_WIDTH = 880;
    private static final int FRAME_HEIGHT = 600;
    private static final int FRAME_X_ORIGIN = 350;
    private static final int FRAME_Y_ORIGIN = 200;
    private static final int BUTTON_WIDTH = 60;
    private static final int BUTTON_HEIGHT = 30;
    JButton send;
    JTextField textField;
    public  String new_message;
    public MyClient send_client;
    //消息存储
    private Queue<String> messages = new LinkedList<String>();
    String own_message;
    //JTextArea textArea;
    JList<String> list = new JList<String>();
    JScrollPane scrollPane;
    //for my box
    private Queue<String> online_information= new LinkedList<String>();
    //online people
    private  int online_number = 0;
    private JComboBox<String> chat_users;
    private JLabel online_label;


    //用来检测是否关闭窗口的类
    class WindowHandle implements WindowListener{
        @Override
        public void windowOpened(WindowEvent windowEvent) {
        }
        @Override
        public void windowClosing(WindowEvent windowEvent) {
            try {
                JDBCInterface window_change = new JDBCInterface();
                window_change.change(send_client.change_id);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        @Override
        public void windowClosed(WindowEvent windowEvent) {
        }
        @Override
        public void windowIconified(WindowEvent windowEvent) {
        }
        @Override
        public void windowDeiconified(WindowEvent windowEvent){
        }
        @Override
        public void windowActivated(WindowEvent windowEvent){
        }
        @Override
        public void windowDeactivated(WindowEvent windowEvent){
        }
    }
    public boolean add_text(Queue<String> text,int size){
        String[] new_text = new String[size];
        int i = 0;
        for(String s:text)
        {
            new_text[i] = s;
            i = i +1;
        }
        list.setListData(new_text);
        scrollPane.repaint(5);
        list.repaint(5);
        return  true;
    }
    //change online people
    public boolean change_online(Queue<String> text){
        int i = 1;
        online_number = 0;
        String []user_chat = new String[text.size()+1];
        user_chat[0] = "Public chat";
        for(String online_item:text)
        {
            String choose[];
            choose = online_item.split(",");
            if(choose[2].equals("1")) {
                online_number = online_number + 1;
                if(!choose[0].equals(send_client.change_id)) {
                    user_chat[i] = choose[0] + " " + choose[1] + ": ,online";
                    i = i + 1;
                }
            }
            else {
                user_chat[i] = choose[0] + " " + choose[1] + ": ,outline";
                i = i + 1;
            }
        }
        chat_users.setModel(new DefaultComboBoxModel<String>(user_chat));
        chat_users.repaint(5);

        online_label.setText("online:"+String.valueOf(online_number));
        online_label.repaint(5);
        return  true;
    }
    //return public
    public void return_chat_house()
    {
        this.setVisible(true);
    }
    //Function
    public UserInterface(MyClient my_client) throws Exception {
        send_client = my_client;
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setTitle("Chat House");
        setLocation(FRAME_X_ORIGIN, FRAME_Y_ORIGIN);
        setLayout(null);    //不使用布局管理器
        setBackground(Color.GRAY);

        new_message = "," + messages.size();
        if (!send_client.SendMessage(new_message)) dispose();

        messages = send_client.update_messages();

        list.setListData(new String[]{"Welcome!","Send a message to start chatting!"});


        // 创建滚动面板，垂直、水平滚动条一直显示
        scrollPane = new JScrollPane(
                list,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
        );
        scrollPane.setBackground(Color.BLUE);
        scrollPane.setBounds(0,0,480,530);
        add(scrollPane);

        //panel defined
        JPanel panel = new JPanel(new GridLayout(1,1,20,100));
        panel.setBounds(0, 530, BUTTON_WIDTH + 350 , BUTTON_HEIGHT);

        //add box
        JDBCInterface my_users_online = new JDBCInterface();
        online_information = my_users_online.Query_online();
        int i = 1;
        String []user_chat = new String[online_information.size()+1];
        user_chat[0] = "Public chat";
        for(String online_item:online_information)
        {
            String choose[];
            choose = online_item.split(",");
            if(choose[2].equals("1")) {
                online_number = online_number + 1;
                if(!choose[0].equals(my_client.change_id)){
                    user_chat[i] = choose[0] + " " + choose[1] + ": ,online";
                    i = i + 1;
                }
            }
            else {
                user_chat[i] = choose[0] + " " + choose[1] + ": ,outline";
                i = i + 1;
            }
        }
        chat_users = new JComboBox<String>(user_chat);
        chat_users.setBounds(590,10,BUTTON_WIDTH + 100,BUTTON_HEIGHT);
        chat_users.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                    if(!chat_users.getSelectedItem().equals("Public chat")&& String.valueOf(chat_users.getSelectedItem()).split(",")[1].equals("online")){
                        setVisible(false);
                        try {
                            PrivateUserInterface new_private_chat = new PrivateUserInterface(send_client,String.valueOf(chat_users.getSelectedItem()).split(" ")[0]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //System.out.println("选中: " + chat_users.getSelectedItem());
                    }
                }
            }
        });
        // 设置默认选中的条目
        chat_users.setSelectedIndex(0);
        add(chat_users);

        //add online label
        online_label = new JLabel();
        online_label.setBounds(770,0,BUTTON_WIDTH + 50,BUTTON_HEIGHT);
        online_label.setText("online:" + String.valueOf(online_number));
        add(online_label);

        //add chat label
        JLabel chat_target = new JLabel();
        chat_target.setBounds(490,10,BUTTON_WIDTH+40,BUTTON_HEIGHT);
        chat_target.setText("目前聊天对象:");
        add(chat_target);

        //send button
        send = new JButton("send");
        send.setBounds(400, 530, BUTTON_WIDTH + 10 , BUTTON_HEIGHT);
        add(send);

        //TextField
        textField = new JTextField("",8);


        //add action listener
        send.addActionListener(this);      //add the frame as an action

        //add TextField
        panel.add(textField);

        //add panel
        add(panel);
        setVisible(true);
        addWindowListener(new WindowHandle());
        //System.out.println(messages);
        send_client.get();

    }
    public void actionPerformed(ActionEvent event)
    {
            JButton clickButton = (JButton) event.getSource();   //返回产生事件的对象
            if (clickButton == send) {
                try {
                    new_message = textField.getText() + "," +"1";
                    if (!send_client.SendMessage(new_message)) dispose();
                    own_message = textField.getText();
                    textField.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }
}
