package com.crazymakercircle.chat.clientHandler;


import com.crazymakercircle.chat.client.ClientSession;
import com.crazymakercircle.chat.common.ProtoInstant;
import com.crazymakercircle.chat.common.bean.msg.ProtoMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("LoginResponceHandler")
public class LoginResponceHandler extends ChannelInboundHandlerAdapter
{

    static final Logger LOGGER = LoggerFactory.getLogger(LoginResponceHandler.class);


    /**
     * 业务逻辑处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        LOGGER.info("msg:{}", msg.toString());
        if (msg != null && msg instanceof ProtoMsg.Message)
        {
            ProtoMsg.Message pkg = (ProtoMsg.Message) msg;


            ProtoMsg.LoginResponse info = pkg.getLoginResponse();

            ProtoInstant.ResultCodeEnum result =
                    ProtoInstant.ResultCodeEnum.values()[info.getCode()];

            if (result.equals(ProtoInstant.ResultCodeEnum.SUCCESS))
            {
                ClientSession session =
                        ctx.channel().attr(ClientSession.SESSION).get();

                session.setLogin(true);
                LOGGER.info("登录成功");
            }


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
