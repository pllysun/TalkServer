package TalkService;

import TalkBasic.Message;
import TalkBasic.MessageType;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlManageInstanceGroupStatement;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

//该类的一个对象和某个客户端保持通讯
public class ServiceConnectClientThread extends Thread{
    private final Socket socket;
    private String userId;//连接到服务端的用户id

    public ServiceConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }
    public ServiceConnectClientThread(Socket socket)
    {
        this.socket = socket;
    }


    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {//服务端线程持续保持和客户端的通讯
        label:
        while(true)
        {
            if(socket==null)break;
            System.out.println(userId+"和服务端建立连接");
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                Message message =(Message)ois.readObject();
                //根据Message类型做相应的处理
                switch (message.getMesType())
                {
                    case MessageType.MESSAGE_GET_ONLINE_FRIEND->{
                        //客户端请求在线用户列表
                        System.out.println(message.getSender()+"请求在线列表");
                        String onlineUser = ManageClientThreads.getOnlineUser();
                        //返回一个message对象给客户端
                        Message ReturnMessage = new Message();
                        ReturnMessage.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                        ReturnMessage.setContent(onlineUser);
                        ReturnMessage.setGetter(message.getSender());
                        //返回给客户端
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(ReturnMessage);

                    }
                    case MessageType.MESSAGE_CLIENT_EXIT -> {
                        //收到客户端退出
                        System.out.println(message.getSender()+"退出通讯");
                        //将我们这个客户端线程从我们集合线程中移除
                        ManageClientThreads.removeServiceConnectClientThread(message.getSender());
                        //关闭连接
                        socket.close();
                        //退出循环
                        break label;
                    }
                    case MessageType.MESSAGE_COMM_MES -> {
                        //根据message获取getterId，然后在得到对应的线程
                        //ServiceConnectClientThread serviceConnectClientThread = ManageClientThreads.getServiceConnectClientThread(message.getGetter());
                        //得到对应socket的对象输出流，将message对象转发给指定的客户端
                        ObjectOutputStream oos = new ObjectOutputStream(ManageClientThreads.getServiceConnectClientThread(message.getGetter()).getSocket().getOutputStream());
                        oos.writeObject(message);//如果服务器转发消息的接收者不在线，可以通过数据库来储存，然后来达到离线留言的目的
                        System.out.println(message.getSender()+"发送给"+message.getGetter()+"的消息转发成功");
                    }
                    case MessageType.MESSAGE_TO_ALL_MES -> {
                        //遍历线程集合，取出除了自己的所有的socket
                        HashMap<String, ServiceConnectClientThread> hm = ManageClientThreads.getHm();

                        Iterator<String> iterator = hm.keySet().iterator();
                        while(iterator.hasNext())
                        {
                            String OnlineUerId = iterator.next();
                            if(!OnlineUerId.equals(message.getSender())){
                                //排除发送者的id
                                //进行转发
                                ObjectOutputStream oos = new ObjectOutputStream(hm.get(OnlineUerId).getSocket().getOutputStream());
                                oos.writeObject(message);
                            }
                        }
                    }
                    case MessageType.MESSAGE_FILE_MES -> {
                        //根据getter id获取对应的线程，讲message对象转发
                        ServiceConnectClientThread serviceConnectClientThread = ManageClientThreads.getServiceConnectClientThread(message.getGetter());
                        ObjectOutputStream oos = new ObjectOutputStream(serviceConnectClientThread.getSocket().getOutputStream());
                        oos.writeObject(message);
                    }
                    default -> System.out.println("其他类型，暂时不处理！");
                }
            } catch (Exception e) {
                ManageClientThreads.removeServiceConnectClientThread(userId);
                System.out.println("用户："+userId+"断开了连接！");
                break;
            }
        }
    }
}
