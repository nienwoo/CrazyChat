package com.crazymakercircle.chat.clientProcess.impl;

import com.crazymakercircle.chat.clientProcess.AbstractProc;
import com.crazymakercircle.chat.common.ServerSession;
import com.crazymakercircle.chat.common.bean.msg.ProtoMsg;
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

    }

}
