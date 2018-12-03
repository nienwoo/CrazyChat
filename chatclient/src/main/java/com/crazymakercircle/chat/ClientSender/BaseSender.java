package com.crazymakercircle.chat.ClientSender;

import com.crazymakercircle.chat.client.ClientSession;
import com.crazymakercircle.chat.clientBuilder.ChatMsgBuilder;
import com.crazymakercircle.chat.clientBuilder.LoginMsgBuilder;
import com.crazymakercircle.chat.common.bean.ChatMsg;
import com.crazymakercircle.chat.common.bean.User;
import com.crazymakercircle.chat.common.bean.msg.ProtoMsg;
import com.crazymakercircle.cocurrent.FutureTask;
import com.crazymakercircle.cocurrent.FutureTaskThread;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Data
@Slf4j
public class BaseSender
{


    private User user;
    private ClientSession session;

    public boolean isConnected()
    {
        if(null==session)
        {
            log.info("session is null");
            return false;
        }

        return session.isConnected();
    }

    public boolean isLogin()
    {
        if(null==session)
        {
            log.info("session is null");
            return false;
        }

        return session.isLogin();
    }

    public void sendMsg(ProtoMsg.Message message)
    {

        FutureTaskThread.add(new FutureTask<Boolean>()
        {
            @Override
            public Boolean execute() throws Exception
            {
                if(null==getSession()){
                    throw new Exception("session is null");
                }

                if (!isLogin())
                {
                    log.info("还没有登录!");
                    throw new Exception("还没有登录");
                }


                final Boolean[] isSuccess = {false};


                ChannelFuture f = getSession().sengPackage(message);
                f.addListener(new GenericFutureListener<Future<? super Void>>()
                {
                    @Override
                    public void operationComplete(Future<? super Void> future)
                            throws Exception
                    {
                        // 回调
                        isSuccess[0] = true;

                    }

                });



                try
                {
                    f.sync();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                    throw new Exception("error occur");
                }

                return isSuccess[0];
            }

            @Override
            public void onSuccess(Boolean b)
            {
                if(b)
                {
                    log.info("发送成功");
                }else {
                    log.info("发送失败");

                }
            }

            @Override
            public void onFailure(Throwable t)
            {
                log.info("发送消息出现异常");

            }
        });

    }
}
