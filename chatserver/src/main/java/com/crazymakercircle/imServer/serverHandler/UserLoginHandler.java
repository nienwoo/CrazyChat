package com.crazymakercircle.imServer.serverHandler;

import com.crazymakercircle.cocurrent.FutureTaskScheduler;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.imServer.server.ServerSession;
import com.crazymakercircle.imServer.server.SessionMap;
import com.crazymakercircle.imServer.serverReciver.LoginReciver;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("UserLoginHandler")
@ChannelHandler.Sharable
public class UserLoginHandler
        extends ChannelInboundHandlerAdapter
{
    @Autowired
    LoginReciver loginReciver;

    /**
     * 收到消息
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception
    {
        log.info("msg:{}", msg.getClass());
        if (null == msg
                || !(msg instanceof ProtoMsg.Message)
                || null == getSession(ctx)
                )
        {
            super.channelRead(ctx, msg);
            return;
        }
        ServerSession session = getSession(ctx);

        ProtoMsg.Message pkg = (ProtoMsg.Message) msg;

        //取得请求类型
        ProtoMsg.HeadType headType = pkg.getType();

        if (!headType.equals(loginReciver.op()))
        {
            super.channelRead(ctx, msg);
            return;
        }
        //处理登录的逻辑
        FutureTaskScheduler.add(() ->
        {
            loginReciver.action(session, pkg);
        });

    }

    private ServerSession getSession(ChannelHandlerContext ctx)
    {
        ServerSession session =
                ctx.channel().attr(ServerSession.SESSION).get();
        return session;
    }


    public void exceptionCaught(
            ChannelHandlerContext ctx, Throwable cause)
    {
        //捕捉异常信息
        cause.printStackTrace();
        //出现异常时关闭channel
        ctx.close();
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx)
            throws Exception
    {
        //logger.info("CHANNEL_INACTIVE " + factory.channel().remoteAddress());


        ServerSession session =
                ctx.channel().attr(ServerSession.SESSION).get();

        if (session.isValid())
        {
            session.close();
            SessionMap.inst().removeSession(session.getSessionId());
        }
    }
}
