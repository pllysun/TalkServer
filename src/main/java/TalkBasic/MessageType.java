package TalkBasic;

//在接口中定义了一些常量，不同的常量值表示不同的消息类型
public interface MessageType {
    String MESSAGE_LOGIN_SUCCEED="1";//表示登录成功
    String MESSAGE_LOGIN_FAIL="2";//表示登录失败
    String MESSAGE_COMM_MES="3";//普通的信息包
    String MESSAGE_GET_ONLINE_FRIEND="4";//要求返回在线用户列表
    String MESSAGE_RET_ONLINE_FRIEND="5";//返回在线用户列表
    String MESSAGE_CLIENT_EXIT="6";//客户端请求退出
    String MESSAGE_REGISTER_SUCCEED = "7";//注册用户无重复消息
    String MESSAGE_REGISTER_FAIL = "8";//注册用户重复消息


}
