package test;
/**
 * @author 17130130226 liuyilei
 * @paramn For test Java MySql
 */
import java.util.*;
import java.sql.*;
import java.lang.*;

public class JDBCInterface{
    //用来登录
    public  int Query(String try_id,String try_password) throws Exception{
        //加载数据库驱动程序
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException cne){
            cne.printStackTrace();
        }
        String dburl = "jdbc:mysql://127.0.0.1:3307/chat?&useSSL=false&serverTimezone=UTC";
        //用来查询的语句
        String sql = "SELECT id,password,status FROM  my_user";
        //用来插入的语句
        //String sql = "insert into students values('17130130226','Liu YiLei',20)";
        //用来更新的语句
        String sql1 = "update my_user set status=1 where id = '"+ try_id + "'";
        try {
            Connection conn = DriverManager.getConnection(dburl, "root", "123456");
            Statement stmt = conn.createStatement();
            //这里是插入操作
            //stmt.executeUpdate(sql);
            //这里是更新操作
            //stmt.executeUpdate(sql);
            //这里是查询操作
            ResultSet res = stmt.executeQuery(sql);
            while (res.next()) {
                String id = new String(String.valueOf(res.getString("id")));
                String password = new String(String.valueOf(res.getString("password")));
                if (id.equals(try_id) && try_password.equals(password)){
                    if(1 != res.getInt("status")){
                        stmt.executeUpdate(sql1);
                        return 0;
                    }else {
                        return -2;
                    }
                }
            }
            return -1;

        }catch (SQLException se){
            se.printStackTrace();
        }
        return  -1;
    }
    //用来注册查询是否id已经被使用 true表示未被使用
    public  boolean Query_Id(String you_id) throws Exception{
        //加载数据库驱动程序
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException cne){
            cne.printStackTrace();
        }
        String dburl = "jdbc:mysql://127.0.0.1:3307/chat?&useSSL=false&serverTimezone=UTC";
        //用来查询的语句
        String sql = "SELECT id FROM  my_user";
        try {
            Connection conn = DriverManager.getConnection(dburl, "root", "123456");
            Statement stmt = conn.createStatement();
            //这里是插入操作
            //stmt.executeUpdate(sql);
            //这里是更新操作
            //stmt.executeUpdate(sql);
            //这里是查询操作
            ResultSet res = stmt.executeQuery(sql);
            while (res.next()) {
                String id = new String(String.valueOf(res.getString("id")));
                if (id.equals(you_id)){
                    return false;
                }
            }
            return true;
        }catch (SQLException se){
            se.printStackTrace();
        }
        return  false;
    }
    //用来注册查询是否id已经被使用 true表示未被使用
    public  boolean New_Customer(String new_id,String new_name,String new_sex,String new_password,String new_telephone) throws Exception{
        //加载数据库驱动程序
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException cne){
            cne.printStackTrace();
        }
        String dburl = "jdbc:mysql://127.0.0.1:3307/chat?&useSSL=false&serverTimezone=UTC";
        //用来查询的语句
        String sql = "insert into my_user values('"+new_id+"','"+new_name+"',"+new_sex+",'"+new_password+"','"+new_telephone+"',0,1)";
        try {
            Connection conn = DriverManager.getConnection(dburl, "root", "123456");
            Statement stmt = conn.createStatement();
            //这里是插入操作
            stmt.executeUpdate(sql);
            return true;
        }catch (SQLException se){
            se.printStackTrace();
        }
        return  false;
    }
    //用来更改登录状态
    public  boolean change(String try_id) throws Exception{
        //加载数据库驱动程序
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException cne){
            cne.printStackTrace();
        }
        String dburl = "jdbc:mysql://127.0.0.1:3307/chat?&useSSL=false&serverTimezone=UTC";
        //用来更新的语句
        String sql = "update my_user set status=0 where id = '"+ try_id + "'";
        try {
            Connection conn = DriverManager.getConnection(dburl, "root", "123456");
            Statement stmt = conn.createStatement();
            //这里是更新操作
            stmt.executeUpdate(sql);
            }catch (SQLException se){
            se.printStackTrace();
        }
        return  false;
    }
    //用来查询姓名
    public  String  Query_name(String you_id) throws Exception{
        String name = new String();
        //加载数据库驱动程序
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException cne){
            cne.printStackTrace();
        }
        String dburl = "jdbc:mysql://127.0.0.1:3307/chat?&useSSL=false&serverTimezone=UTC";
        //用来查询的语句
        String sql = "SELECT name FROM  my_user where id = '" + you_id + "'";
        try {
            Connection conn = DriverManager.getConnection(dburl, "root", "123456");
            Statement stmt = conn.createStatement();
            //这里是插入操作
            //stmt.executeUpdate(sql);
            //这里是更新操作
            //stmt.executeUpdate(sql);
            //这里是查询操作
            ResultSet res = stmt.executeQuery(sql);
            while (res.next()) {
                name = String.valueOf(res.getString("name"));
            }
            return name;
        }catch (SQLException se){
            se.printStackTrace();
        }
        return "Failed";
    }
    //用来查询在线人数、在线的人
    public  Queue<String>  Query_online() throws Exception{
        Queue<String> online = new LinkedList<String>();
        //加载数据库驱动程序
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException cne){
            cne.printStackTrace();
        }
        String dburl = "jdbc:mysql://127.0.0.1:3307/chat?&useSSL=false&serverTimezone=UTC";
        //用来查询的语句
        String sql = "SELECT id,name,status FROM  my_user";
        try {
            Connection conn = DriverManager.getConnection(dburl, "root", "123456");
            Statement stmt = conn.createStatement();

            ResultSet res = stmt.executeQuery(sql);
            while (res.next()) {
                online.offer(res.getString("id")+","+res.getString("name")+","+res.getString("status"));
            }
            return online;
        }catch (SQLException se){
            se.printStackTrace();
        }
        return null;
    }
}