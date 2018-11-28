package com.crazymakercircle.chat.client;

import com.crazymakercircle.chat.clientHandler.ChatMsgHandler;
import com.crazymakercircle.chat.common.ServerSession;
import com.crazymakercircle.chat.common.bean.User;
import com.crazymakercircle.chat.common.bean.msg.ProtoMsg;
import com.crazymakercircle.chat.common.codec.ProtobufDecoder;
import com.crazymakercircle.chat.common.codec.ProtobufEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Data
@Service("EchoClient")
public class ChatClient
{
    static final Logger LOGGER =
            LoggerFactory.getLogger(ChatClient.class);
    // 服务器ip地址
    @Value("${server.ip}")
    private String host;
    // 服务器端口
    @Value("${server.port}")
    private int port;

    // 通过nio方式来接收连接和处理连接
    private EventLoopGroup group = new NioEventLoopGroup();

    @Autowired
    private ChatMsgHandler chatClientHandler;

    private Channel channel;
    private ClientSender sender;

    /**
     * 唯一标记
     */
    private boolean initFalg = true;
    private User user;

    /**
     * 客户端的是Bootstrap，服务端的则是 ServerBootstrap。
     * 都是AbstractBootstrap的子类。
     **/
    public void run()
    {
        doConnect(new Bootstrap(), group);
    }

    /**
     * 重连
     */
    public void doConnect(Bootstrap bootstrap, EventLoopGroup eventLoopGroup)
    {
        ChannelFuture f = null;
        try
        {
            if (bootstrap != null)
            {
                bootstrap.group(eventLoopGroup);
                bootstrap.channel(NioSocketChannel.class);
                bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
                bootstrap.remoteAddress(host, port);

                // 设置通道初始化
                bootstrap.handler(
                        new ChannelInitializer<SocketChannel>()
                        {
                            public void initChannel(SocketChannel ch) throws Exception
                            {
                                ch.pipeline().addLast(new ProtobufDecoder());
                                ch.pipeline().addLast(new ProtobufEncoder());
                                ch.pipeline().addLast(chatClientHandler);

                            }
                        }
                );
                LOGGER.info(new Date() + "客户端开始登录[疯狂创客圈IM]");

                f = bootstrap.connect().addListener((ChannelFuture futureListener) ->
                {
                    final EventLoop eventLoop = futureListener.channel().eventLoop();
                    if (!futureListener.isSuccess())
                    {
                        LOGGER.info("与服务端断开连接!在10s之后准备尝试重连!");
                        eventLoop.schedule(() -> doConnect(new Bootstrap(), eventLoop), 10, TimeUnit.SECONDS);

                        initFalg = false;
                    }
                    else
                    {
                        initFalg = true;
                    }
                    if (initFalg)
                    {
                        LOGGER.info("EchoClient客户端连接成功!");

                        LOGGER.info(new Date() + ": 连接成功，启动控制台线程……");
                        channel = futureListener.channel();

                        // 创建会话
                        ClientSession session = new ClientSession(channel);
                        channel.attr(ClientSession.SESSION).set(session);
                        session.setUser(ChatClient.this.getUser());
                        startConsoleThread();
                    }

                });

                // 阻塞
                f.channel().closeFuture().sync();
            }
        } catch (Exception e)
        {
            LOGGER.info("客户端连接失败!" + e.getMessage());
        }

    }

    private void startConsoleThread()
    {
        new Thread(() ->
        {


            while (!sender.isLogin())
            {
                sender.sendLoginMsg();
                try
                {
                    Thread.sleep(500);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            while (sender.isLogin() && !Thread.interrupted())
            {

                System.out.println("输入消息发送至服务端: ");

                Scanner sc = new Scanner(System.in);

                String line = sc.nextLine();

                String[] toAndMsg = line.split("\\s+|\\s+");

                sender.sendChatMsg(toAndMsg[1], toAndMsg[0]);
            }
        }).start();
    }


}
