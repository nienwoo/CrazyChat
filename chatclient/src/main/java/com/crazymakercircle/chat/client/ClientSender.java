package com.crazymakercircle.chat.client;

import com.crazymakercircle.chat.clientBuilder.ChatMsgBuilder;
import com.crazymakercircle.chat.clientBuilder.LoginMsgBuilder;
import com.crazymakercircle.chat.common.ServerSession;
import com.crazymakercircle.chat.common.bean.ChatMsg;
import com.crazymakercircle.chat.common.bean.User;
import com.crazymakercircle.chat.common.bean.msg.ProtoMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service("ClientSender")
public class ClientSender
{
    static final Logger LOGGER = LoggerFactory.getLogger(ClientSender.class);


    private User user;
    private ClientSession session;

    public void sendLoginMsg()
    {
        LOGGER.info("开始登陆");
        ProtoMsg.Message message = LoginMsgBuilder.buildLoginMsg(user);
        session.writeAndFlush(message);
    }

    public void sendChatMsg(String content, String touid)
    {
        LOGGER.info("开发发送消息");
        ChatMsg chatMsg = new ChatMsg(user);
        chatMsg.setContent(content);
        chatMsg.setMsgType(ChatMsg.MSGTYPE.TEXT);
        chatMsg.setTo(touid);
        chatMsg.setMsgId(System.currentTimeMillis());
        ProtoMsg.Message message = ChatMsgBuilder.buildChatMsg(chatMsg);
        session.writeAndFlush(message);

    }

    public boolean isLogin()
    {
        return  session.isLogin();
    }
}
