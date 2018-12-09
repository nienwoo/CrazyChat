package com.crazymakercircle.imServer.serverReciver;

import com.crazymakercircle.im.common.ProtoInstant;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.imServer.server.ServerSession;
import com.crazymakercircle.imServer.server.SessionMap;
import com.crazymakercircle.imServer.serverBuilder.ServerMsgBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ChatReciver extends AbstractServerReciver
{
    private Logger logger = LoggerFactory.getLogger(ChatReciver.class);

    @Override
    public ProtoMsg.HeadType op()
    {
        return ProtoMsg.HeadType.MESSAGE_REQUEST;
    }

    @Override
    public void action(ServerSession ch, ProtoMsg.Message proto)
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
        ServerSession session = SessionMap.inst().getSession(to);
        if (session == null)
        {
            ServerSession fromSession = SessionMap.inst().getSession(msg.getFrom());

            ProtoMsg.Message message =
                    ServerMsgBuilder.buildChatResponse(
                            proto.getSequence(),
                            ProtoInstant.ResultCodeEnum.UNKNOW_ERROR);
            fromSession.writeAndClose(message);
        }
        else
        {
            // 将IM消息发送到接收方
            session.write(proto);
            ServerSession fromSession = SessionMap.inst().getSession(msg.getFrom());
            ProtoMsg.Message message =
                    ServerMsgBuilder.buildChatResponse(
                            proto.getSequence(),
                            ProtoInstant.ResultCodeEnum.SUCCESS);
            fromSession.writeAndClose(message);
        }
    }

}
