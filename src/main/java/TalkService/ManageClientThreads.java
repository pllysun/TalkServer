package TalkService;

import java.util.HashMap;
import java.util.Iterator;

//该类用来管理和客户端通讯的线程集合
public class ManageClientThreads {
    private static HashMap<String,ServiceConnectClientThread> hm=new HashMap<>();

    //添加线程到集合
    public static void  addClientThread(String userId,ServiceConnectClientThread serviceConnectClientThread){
        hm.put(userId,serviceConnectClientThread);
    }
    //根据userid返回线程
    public static ServiceConnectClientThread getServiceConnectClientThread(String userId)
    {
        return hm.get(userId);
    }
    //从集合中移除某个线程
    public static void removeServiceConnectClientThread(String userId)
    {
        hm.remove(userId);
    }

    public static HashMap<String, ServiceConnectClientThread> getHm() {
        return hm;
    }
    //返回在线用户列表

    public static String getOnlineUser(){
        //集合遍历，只需要遍历hashmap的key
        Iterator<String>iterator=hm.keySet().iterator();
        StringBuilder onlineUserList= new StringBuilder();
        while(iterator.hasNext())
        {
            onlineUserList.append(iterator.next().toString()).append(" ");
        }
        return onlineUserList.toString();
    }
}
