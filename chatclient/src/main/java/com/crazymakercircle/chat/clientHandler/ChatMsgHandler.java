package com.crazymakercircle.chat.clientHandler;


import com.crazymakercircle.chat.client.ClientSender;
import com.crazymakercircle.chat.common.ServerSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("ChatClientHandler")
public class ChatMsgHandler extends ChannelInboundHandlerAdapter
{

    private ClientSender sender;

    public ChatMsgHandler(ClientSender sender)
    {
        this.sender = sender;
    }

    static final Logger LOGGER = LoggerFactory.getLogger(ChatMsgHandler.class);

    private AttributeKey<ServerSession> sk = AttributeKey.valueOf("session");


    /**
     * 业务逻辑处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        LOGGER.info(msg.toString());
        if (sender.isLogin())
        {


        }
    }

    /**
     * 捕捉到异常
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        ctx.close();
    }

}
