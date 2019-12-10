package test;
/**
 * @author 17130130226 Liu YiLei
 * @paramn For Sign In
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Sign extends JFrame implements ActionListener {
    private static final int FRAME_WIDTH = 500;
    private static final int FRAME_HEIGHT = 400;
    private static final int FRAME_X_ORIGIN = 150;
    private static final int FRAME_Y_ORIGIN = 250;
    private static final int BUTTON_WIDTH = 60;
    private static final int BUTTON_HEIGHT = 30;
    JButton in, register;
    JTextField textField;
    JPasswordField textField1;
    public Sign() {
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setTitle("Sign In");
        setLocation(FRAME_X_ORIGIN, FRAME_Y_ORIGIN);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);    //不使用布局管理器

        //panel defined
        JPanel panel = new JPanel(new GridLayout(1, 1, 20, 100));
        panel.setBounds(100, 70, BUTTON_WIDTH + 230, BUTTON_HEIGHT + 16);
        JPanel panel1 = new JPanel(new GridLayout(1, 1, 20, 100));
        panel1.setBounds(100, 130, BUTTON_WIDTH + 230, BUTTON_HEIGHT + 16);

        //in sign in button
        in = new JButton("Sign in");
        in.setBounds(150, 260, BUTTON_WIDTH + 90, BUTTON_HEIGHT + 10);
        add(in);

        //add register button
        register = new JButton("Register");
        register.setBounds(0, 330, BUTTON_WIDTH + 30, BUTTON_HEIGHT);
        add(register);

        //TextField
        textField = new JTextField("id", 16);
        textField1 = new JPasswordField("password", 16);


        //add action listener
        in.addActionListener(this);      //add the frame as an action
        register.addActionListener(this);  //event listener

        //add TextField
        panel.add(textField);
        panel1.add(textField1);

        //add panel
        add(panel);
        add(panel1);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {
        JButton clickButton = (JButton) event.getSource();   //返回产生事件的对象
        int id_flag;//判断是否重复登录
        if (clickButton == register) {
            setTitle("Register");
            dispose();
            Register register_user = new Register();
        } else {
            JDBCInterface user = new JDBCInterface();
            String try_id = new String(textField.getText());
            String try_password = new String(String.valueOf(textField1.getPassword()));
            try {
                id_flag = user.Query(try_id, try_password);
                if (id_flag == 0){
                    setTitle("successfully!");
                    dispose();
                    MyClient server_socket = new MyClient(try_id);
                } else {
                    if(id_flag == -1)
                        setTitle("Right id and password please!");
                    else
                        setTitle("This Id is in use!");
                }
            }catch (Exception e){
                System.out.println("Failed!");
            }
        }
    }
}
