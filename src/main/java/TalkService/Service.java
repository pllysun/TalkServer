package TalkService;

import TalkBasic.Message;
import TalkBasic.MessageType;
import TalkBasic.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


//服务端，监听端口，等待客户端的连接，等待通讯
public class Service {
    public Service(){
        //端口可以写在配置文件中
        System.out.println("服务端在监听中...");
        //启动服务推送新闻线程
        new Thread(new SendNewsToAllService()).start();
        ServerSocket ss = null;
        Socket socket=null;
        ObjectInputStream ois=null;
        ObjectOutputStream oos=null;
        ObjectOutputStream res=null;
        try {
            ss =new ServerSocket(21341);
            while(true)
            {
                //当和某个客户端建立连接以后持续监听
                socket = ss.accept();
                //得到socket关联的对象输入流
                ois=new ObjectInputStream(socket.getInputStream());
                User u=(User)ois.readObject();//读取客户端发送的User对象
                //得到socket关联的对象输出流
                oos= new ObjectOutputStream(socket.getOutputStream());
                Message message=new Message();
                if(u.getReuser()!=null&&u.getRepwd()==null)
                {
                    //创建一个Message对象，验证数据库中的数据是否重复注册
                    RegisterSQL registerSQL = new RegisterSQL();
                    boolean checkR = registerSQL.CheckRegister(u.getReuser());
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
//                    res=new ObjectOutputStream(socket.getOutputStream());
//                    res.reset();
//                    message.setContent("用户："+u.getReuser()+"注册成功！");
//                    res.writeObject(message);
                }
                else
                {
                    LoginSQL loginSQL = new LoginSQL();
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
                assert ss != null;
                ss.close();
                assert socket != null;
                socket.close();
                assert ois != null;
                ois.close();
                assert oos != null;
                oos.close();
                assert false;
                res.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


