package TalkService;

import TalkBasic.Message;
import TalkBasic.MessageType;
import TalkBasic.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


//服务端，监听端口，等待客户端的连接，等待通讯
public class Service {
    private ServerSocket ss=null;
    //创建一个集合，存放多个用户，日日日如果这些用户登录，就认为是合法的。
//    private static HashMap<String,User> Users=new HashMap<>();
//    static{//在静态代码块，初始化users
//        Users.put("1",new User("1","1"));
//        Users.put("2",new User("2","2"));
//        Users.put("3",new User("3","3"));
//
//    }

    //验证用户是否有效的方法
//    private boolean checkUser(String userId,String password) {
//        User user = Users.get(userId);
//        //先验证用户是否存在
//        if (user == null) {
//            return false;
//        }
//        //如果用户名真确返回密码的boolean值
//        return user.getPassword().equals(password);
//
//    }

    public Service(){
        //端口可以写在配置文件中
        System.out.println("服务端在监听中...");
        LoginSQL loginSQL = new LoginSQL();
        try {
            ss =new ServerSocket(21341);
            while(true)
            {
                //当和某个客户端建立连接以后持续监听
                Socket socket =ss.accept();
                //得到socket关联的对象输入流
                ObjectInputStream ois =new ObjectInputStream(socket.getInputStream());
                User u=(User)ois.readObject();//读取客户端发送的User对象
                //得到socket关联的对象输出流
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                Message message=new Message();
                if(u.getReuser()!=null&&u.getRepwd()==null)
                {
                    //创建一个Message对象，验证数据库中的数据是否重复注册
                    RegisterSQL registerSQL = new RegisterSQL();
                    boolean checkR = registerSQL.CheckRegister(u.getUserId());
                    if(checkR)
                    {
                        message.setMesType(MessageType.MESSAGE_REGISTER_SUCCEED);
                    }
                    else
                    {
                        message.setMesType(MessageType.MESSAGE_REGISTER_FAIL);
                    }
                    oos.writeObject(message);
                }
                else if(u.getReuser()!=null&&u.getRepwd()!=null)
                {
                    RegisterSQL resql = new RegisterSQL();
                    resql.UserSQL(u.getReuser(),u.getRepwd());
                    ObjectOutputStream res =new ObjectOutputStream(socket.getOutputStream());
                    message.setContent("用户："+u.getReuser()+"注册成功！");
                    res.writeObject(message);
                }
                else
                {
                    //创建一个Message对象，准备回复客户端
                    //验证用户登录
                    loginSQL.UserSQL(u.getUserId(),u.getPassword());
                    if(loginSQL.CheckLogin()){
                        //合法用户，验证通过
                        message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                        //将message对象回复给客户端
                        oos.writeObject(message);
                        //创建一个线程和客户端保持通讯，该线程需要持有socket对象
                        ServiceConnectClientThread serviceConnectClientThread = new ServiceConnectClientThread(socket, u.getUserId());
                        serviceConnectClientThread.start();
                        //把该线程对象，放入一个集合中，进行管理
                        ManageClientThreads.addClientThread(u.getUserId(), serviceConnectClientThread);
                    }
                    else
                    {
                        //非法用户，登录失败
                        System.out.println("用户"+u.getUserId()+"企图非法登录！");
                        message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                        oos.writeObject(message);
                        //关闭socket
                        socket.close();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            //如果服务端退出了while循环，因此我们需要关闭ServerSocket
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
