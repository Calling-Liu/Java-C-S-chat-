package test;
/**
 * @author 17130130226 Liu YiLei
 * @paramn For Register
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

class Register extends JFrame implements ActionListener {
    private static final int FRAME_WIDTH = 500;
    private static final int FRAME_HEIGHT = 500;
    private static final int FRAME_X_ORIGIN = 150;
    private static final int FRAME_Y_ORIGIN = 250;
    private static final int BUTTON_WIDTH = 60;
    private static final int BUTTON_HEIGHT = 30;
    JButton  register;
    JLabel pass, check_pass;
    JTextField textField, textField1, textField2;
    JPasswordField textField3, textField4;

    public Register() {
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setTitle("Register");
        setLocation(FRAME_X_ORIGIN, FRAME_Y_ORIGIN);
        setLayout(null);    //不使用布局管理器

        //panel defined
        JPanel panel = new JPanel(new GridLayout(1, 1, 20, 100));
        panel.setBounds(100, 70, BUTTON_WIDTH + 230, BUTTON_HEIGHT + 16);
        JPanel panel1 = new JPanel(new GridLayout(1, 1, 20, 100));
        panel1.setBounds(100, 130, BUTTON_WIDTH + 230, BUTTON_HEIGHT + 16);
        JPanel panel2 = new JPanel(new GridLayout(1, 1, 20, 100));
        panel2.setBounds(100, 180, BUTTON_WIDTH + 230, BUTTON_HEIGHT + 16);
        JPanel panel3 = new JPanel(new GridLayout(1, 1, 20, 100));
        panel3.setBounds(100, 230, BUTTON_WIDTH + 230, BUTTON_HEIGHT + 16);
        JPanel panel4 = new JPanel(new GridLayout(1, 1, 20, 100));
        panel4.setBounds(100, 280, BUTTON_WIDTH + 230, BUTTON_HEIGHT + 16);


        //add register button
        register = new JButton("Register");
        register.setBounds(180, 350, BUTTON_WIDTH + 30, BUTTON_HEIGHT);
        add(register);

        //Label
        pass = new JLabel("password");
        pass.setBounds(40,230,BUTTON_WIDTH,BUTTON_HEIGHT + 16);
        add(pass);
        check_pass = new JLabel("check");
        check_pass.setBounds(40,280,BUTTON_WIDTH,BUTTON_HEIGHT + 16);
        add(check_pass);

        //TextField
        textField = new JTextField("name", 16);
        textField1 = new JTextField("sex", 16);
        textField2 = new JTextField("telephone", 16);
        textField3 = new JPasswordField("", 16);
        textField4 = new JPasswordField("", 16);


        //add action listener
        register.addActionListener(this);  //event listener

        //add TextField
        panel.add(textField);
        panel1.add(textField1);
        panel2.add(textField2);
        panel3.add(textField3);
        panel4.add(textField4);

        //add panel
        add(panel);
        add(panel1);
        add(panel2);
        add(panel3);
        add(panel4);
        setVisible(true);
    }
    //用来随机产生用户的id
    public String Get_id() throws Exception {
        int i;
        Random d = new Random();
        char[] choose = {'1','2','3','4','5','6','7','8','9','0','1'};
        char[] new_id = {'1','2','3','4','5','6','7','8','9','0','1'};
        while(true) {
            for (i = 0; i < 11; i++) {
                new_id[i] = choose[d.nextInt(11)];
            }
            String your_id = new String(String.valueOf(new_id));
            try {
                JDBCInterface you = new JDBCInterface();
                if (you.Query_Id(your_id)) {
                    return your_id;
                }
            } catch (Exception e) {
                return "Failed!";
            }
        }
    }
    public void actionPerformed(ActionEvent event) {
        JButton clickButton = (JButton) event.getSource();   //返回产生事件的对象
        if (clickButton == register) {
            setTitle("Register");
            String x;
            if(textField.getText().length() < 14)
            {
                if(textField1.getText().equals("man")||textField1.getText().equals("woman"))
                {
                    if(textField1.getText().equals("man")) x = new String("0");
                    else  x = new String("1");
                    if(textField2.getText().length() == 11)
                    {
                        if(String.valueOf(textField3.getPassword()).equals(String.valueOf(textField4.getPassword())) &&
                                String.valueOf(textField3.getPassword()).length() > 0 && String.valueOf(textField3.getPassword()).length() >0)
                        {
                            try {
                                String true_id = new String(Get_id());
                                JDBCInterface new_customer = new JDBCInterface();
                                boolean res =new_customer.New_Customer(true_id,textField.getText(),x,String.valueOf(textField3.getPassword()),textField2.getText());
                                if(res)
                                {
                                    setTitle("Successful!");
                                    dispose();
                                    UserInterface my_user = new UserInterface(new MyClient(true_id));
                                    my_user.setTitle("ID:"+true_id);
                                }
                                else
                                {
                                    setTitle("Retry!");
                                }
                            }catch (Exception e){
                                System.out.println("Failed!");
                            }
                        }
                    }
                }
            }
            else
            {
                setTitle("Retry!");
            }
        }
    }
}
