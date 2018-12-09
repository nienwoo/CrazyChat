package com.crazymakercircle.imClient.clientHandler;


import com.crazymakercircle.imClient.ClientSender.LoginSender;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("ChatClientHandler")
public class ChatMsgHandler extends ChannelInboundHandlerAdapter
{

    private LoginSender sender;

    public ChatMsgHandler(LoginSender sender)
    {
        this.sender = sender;
    }

    static final Logger LOGGER = LoggerFactory.getLogger(ChatMsgHandler.class);


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
