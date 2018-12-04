package com.crazymakercircle.chat.client;

import com.crazymakercircle.chat.ClientSender.ChatSender;
import com.crazymakercircle.chat.ClientSender.LoginSender;
import com.crazymakercircle.chat.common.bean.User;
import com.crazymakercircle.cocurrent.QueueTaskThread;
import io.netty.channel.*;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Data
@Service("EchoClient")
public class ChatClient
{
    static final Logger LOGGER =
            LoggerFactory.getLogger(ChatClient.class);


    @Autowired
    private NettyConnector nettyConnector;

    private Channel channel;
    private ChatSender sender;
    private LoginSender l;


    private boolean initFalg = true;
    private User user;

    GenericFutureListener<ChannelFuture> closeListener = (ChannelFuture f) ->
    {


        LOGGER.info(new Date() + ": 连接已经断开……");
        channel = f.channel();

        // 创建会话
        ClientSession session =
                channel.attr(ClientSession.SESSION).get();
        session.close();
        nettyConnector.close();

        //唤醒用户线程
        ChatClient.this.notifyAll();
    };


    GenericFutureListener<ChannelFuture> connectedListener = (ChannelFuture f) ->
    {
        final EventLoop eventLoop
                = f.channel().eventLoop();
        if (!f.isSuccess())
        {
            LOGGER.info("与服务端断开连接!在10s之后准备尝试重连!");
            eventLoop.schedule(() -> nettyConnector.doConnect(), 10, TimeUnit.SECONDS);

            initFalg = false;
        }
        else
        {
            initFalg = true;
        }
        if (initFalg)
        {
            LOGGER.info("EchoClient客户端连接成功!");
            channel = f.channel();

            // 创建会话
            ClientSession session = new ClientSession(channel);
            channel.attr(ClientSession.SESSION).set(session);
            session.setUser(ChatClient.this.getUser());
            startUserThread();

            channel.closeFuture().addListener(closeListener);

            //唤醒用户线程
            ChatClient.this.notify();
        }

    };


    public void start()
    {
        QueueTaskThread.add(() ->
        {
            nettyConnector.setConnectedListener(connectedListener);
            nettyConnector.doConnect();
        });
    }


    public void notifyUserThread()
    {
        synchronized (this)
        {
            //唤醒主线程
            this.notify();
        }
    }

    public void startUserThread()
            throws InterruptedException
    {
        while(true)
        {
            //建立连接
            while (initFalg == false)
            {
                synchronized (this)
                {
                    //开始连接
                    start();
                    this.wait();
                }
            }

            //登录
            while (!sender.isLogin())
            {

                synchronized (this)
                {
                    l.sendLoginMsg();

                    //开始连接
                    start();
                    this.wait();
                }
            }


            //处理命令
            while (!sender.isLogin())
            {

            }
        }
    }


}
