package com.crazymakercircle.imClient.clientHandler;


import com.crazymakercircle.im.common.ProtoInstant;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.imClient.client.ClientSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@ChannelHandler.Sharable
@Service("LoginResponceHandler")
public class LoginResponceHandler extends ChannelInboundHandlerAdapter
{

    static final Logger LOGGER = LoggerFactory.getLogger(LoginResponceHandler.class);

    /**
     * 业务逻辑处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception
    {
        //判断消息实例
        LOGGER.info("msg:{}", msg.toString());
        if (null == msg || !(msg instanceof ProtoMsg.Message))
        {
            super.channelRead(ctx, msg);
            return;
        }

        //判断类型
        ProtoMsg.Message pkg = (ProtoMsg.Message) msg;
        ProtoMsg.HeadType headType = ((ProtoMsg.Message) msg).getType();
        if (!headType.equals(ProtoMsg.HeadType.LOGIN_RESPONSE))
        {
            super.channelRead(ctx, msg);
            return;
        }


       //判断返回是否成功
        ProtoMsg.LoginResponse info = pkg.getLoginResponse();

        ProtoInstant.ResultCodeEnum result =
                ProtoInstant.ResultCodeEnum.values()[info.getCode()];

        if (!result.equals(ProtoInstant.ResultCodeEnum.SUCCESS))
        {
            LOGGER.info(result.getDesc());
        }else
        {

            ClientSession session =
                    ctx.channel().attr(ClientSession.SESSION).get();

            session.setLogin(true);
            LOGGER.info("登录成功");
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
