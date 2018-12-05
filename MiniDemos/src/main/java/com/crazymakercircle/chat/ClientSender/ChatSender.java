package com.crazymakercircle.chat.ClientSender;

import com.crazymakercircle.chat.clientBuilder.ChatMsgBuilder;
import com.crazymakercircle.chat.common.bean.ChatMsg;
import com.crazymakercircle.chat.common.bean.User;
import com.crazymakercircle.chat.common.bean.msg.ProtoMsg;
import com.crazymakercircle.cocurrent.FutureTask;
import com.crazymakercircle.cocurrent.FutureTaskThread;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("ChatSender")
public class ChatSender extends BaseSender
{

    public void sendChatMsg(String content, String touid)
    {
       log.info("发送消息 startNettyClient");
        ChatMsg chatMsg = new ChatMsg(getUser());
        chatMsg.setContent(content);
        chatMsg.setMsgType(ChatMsg.MSGTYPE.TEXT);
        chatMsg.setTo(touid);
        chatMsg.setMsgId(System.currentTimeMillis());
        ProtoMsg.Message message =
                ChatMsgBuilder.buildChatMsg(chatMsg,getUser(),getSession());

        super.sendMsg(message);
    }

    @Override
    protected void sendSucced(ProtoMsg.Message message)
    {
        log.info("发送成功:"+message.getMessageRequest().getContent());
    }

    @Override
    protected void sendException(ProtoMsg.Message message)
    {
        log.info("发送异常:"+message.getMessageRequest().getContent());
    }
    @Override
    protected void sendfailed(ProtoMsg.Message message)
    {
        log.info("发送失败:"+message.getMessageRequest().getContent());
    }
}
