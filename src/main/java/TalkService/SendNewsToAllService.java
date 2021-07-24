package TalkService;

import TalkBasic.Message;
import TalkBasic.MessageType;
import TalkFrame.Utility.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class SendNewsToAllService implements Runnable {

    @Override
    public void run() {
        //持续推送新闻
        while (true) {
            System.out.println("请输入服务器想要推送的新闻/消息[输入exit表示退出推送服务]：");
            String news = Utility.readString(100);
            if("exit".equals(news))break;
            Message message = new Message();
            message.setMesType(MessageType.MESSAGE_TO_ALL_MES);
            message.setSender("服务器");
            message.setContent(news);
            message.setSendTime(new Date().toString());
            System.out.println("服务器推送消息给所有人说：" + news);

            //遍历当前所有的通信线程，得到socket，并发送message
            HashMap<String, ServiceConnectClientThread> hm = ManageClientThreads.getHm();

            for (String s : hm.keySet()) {
                String onLineUserId = s.toString();

                ServiceConnectClientThread serviceConnectClientThread = hm.get(onLineUserId);
                ObjectOutputStream oos = null;
                try {
                    oos = new ObjectOutputStream(serviceConnectClientThread.getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
