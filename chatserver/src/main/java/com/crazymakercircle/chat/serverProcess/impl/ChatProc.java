package com.crazymakercircle.chat.serverProcess.impl;

import com.crazymakercircle.chat.common.ProtoInstant;
import com.crazymakercircle.chat.common.ServerMsgBuilder;
import com.crazymakercircle.chat.common.ServerSession;
import com.crazymakercircle.chat.common.SessionMap;
import com.crazymakercircle.chat.common.bean.msg.ProtoMsg;
import com.crazymakercircle.chat.serverProcess.AbstractProc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ChatProc extends AbstractProc
{
    private Logger logger = LoggerFactory.getLogger(ChatProc.class);

    @Override
    public ProtoMsg.HeadType op()
    {
        return ProtoMsg.HeadType.MESSAGE_REQUEST;
    }

    @Override
    public void action(ServerSession ch, ProtoMsg.Message proto) throws Exception
    {
        // 聊天处理
        ProtoMsg.MessageRequest msg = proto.getMessageRequest();
        logger.info("chatMsg{}", "from="
                + msg.getFrom()
                + " , to=" + msg.getTo()
                + " , content=" + msg.getContent());
        // 获取接收方的chatID
        String to = msg.getTo();
        // int platform = msg.getPlatform();
        ServerSession session = SessionMap.sharedInstance().getSession(to);
        if (session == null)
        {
            ServerSession fromSession = SessionMap.sharedInstance().getSession(msg.getFrom());

            ProtoMsg.Message message = ServerMsgBuilder.buildChatResponse(proto.getSequence(), ProtoInstant.ResultCodeEnum.UNKNOW_ERROR);
            fromSession.writeAndClose(message);
        }
        else
        {
            // 将IM消息发送到接收方
            session.write(proto);
            ServerSession fromSession = SessionMap.sharedInstance().getSession(msg.getFrom());
            ProtoMsg.Message message = ServerMsgBuilder.buildChatResponse(proto.getSequence(), ProtoInstant.ResultCodeEnum.SUCCESS);
            fromSession.writeAndClose(message);
        }
    }

}
