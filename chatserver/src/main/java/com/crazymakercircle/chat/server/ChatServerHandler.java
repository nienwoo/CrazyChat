package com.crazymakercircle.chat.server;

import com.crazymakercircle.chat.common.ServerSession;
import com.crazymakercircle.chat.common.SessionMap;
import com.crazymakercircle.chat.common.bean.msg.ProtoMsg;
import com.crazymakercircle.chat.serverProcess.Proc;
import com.crazymakercircle.chat.serverProcess.ProcFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("ChatServerHandler")
public class ChatServerHandler extends ChannelInboundHandlerAdapter
{
    static final Logger LOGGER = LoggerFactory.getLogger(ChatServerHandler.class);

    /**
     * 建立连接时
     */
    @Override
    public void channelActive(final ChannelHandlerContext ctx)
    {
        LOGGER.info("CHANNEL_ACTIVE " + ctx.channel().remoteAddress());
        ServerSession session = new ServerSession(ctx);
        ctx.channel().attr(ServerSession.SESSION).set(session);
    }

    /**
     * 收到消息
     *
     * @param ctx
     * @param msg
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
        LOGGER.info("msg:{}", msg.toString());
        if (msg != null && msg instanceof ProtoMsg.Message)
        {
            ProtoMsg.Message pkg = (ProtoMsg.Message) msg;
            if (getSession(ctx) != null)
            {
                //根据
                ProtoMsg.HeadType headType = pkg.getType();
                //需要实现抽象的逻辑层的Handler处理
                Proc operation = ProcFactory.getInstance().getOperation(headType);
                if (null != operation)
                {
                    try
                    {
                        operation.action(getSession(ctx), pkg);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    LOGGER.warn("Not found serverProcess : {}", pkg.getType());
                }
            }
        }
    }


    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        //捕捉异常信息
        cause.printStackTrace();
        //出现异常时关闭channel
        ctx.close();
    }

    private ServerSession getSession(ChannelHandlerContext ctx)
    {
        return ctx.channel().attr(ServerSession.SESSION).get();
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        //logger.info("CHANNEL_INACTIVE " + factory.channel().remoteAddress());
        ServerSession session = getSession(ctx);
        if (session.isValid())
        {
            session.close();
            SessionMap.sharedInstance().removeSession(session.getSessionId());
        }
    }
}
