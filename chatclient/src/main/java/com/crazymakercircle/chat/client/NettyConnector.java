package com.crazymakercircle.chat.client;

import com.crazymakercircle.chat.ClientSender.ChatSender;
import com.crazymakercircle.chat.ClientSender.LoginSender;
import com.crazymakercircle.chat.clientHandler.ChatMsgHandler;
import com.crazymakercircle.chat.common.bean.User;
import com.crazymakercircle.chat.common.codec.ProtobufDecoder;
import com.crazymakercircle.chat.common.codec.ProtobufEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Data
@Service("NettyConnector")
public class NettyConnector
{
    static final Logger LOGGER =
            LoggerFactory.getLogger(NettyConnector.class);
    // 服务器ip地址
    @Value("${server.ip}")
    private String host;
    // 服务器端口
    @Value("${server.port}")
    private int port;


    @Autowired
    private ChatMsgHandler chatClientHandler;

    private Channel channel;
    private ChatSender sender;
    private LoginSender l;

    /**
     * 唯一标记
     */
    private boolean initFalg = true;
    private User user;
    private GenericFutureListener<ChannelFuture> connectedListener;

    private Bootstrap b;
    private EventLoopGroup g;

    public void NettyConnector()
    {

        /**
         * 客户端的是Bootstrap，服务端的则是 ServerBootstrap。
         * 都是AbstractBootstrap的子类。
         **/

        b = new Bootstrap();
        /**
         * 通过nio方式来接收连接和处理连接
         */

        g = new NioEventLoopGroup();


    }

    /**
     * 重连
     */
    public void doConnect()
    {
        ChannelFuture f = null;
        try
        {
            if (b != null)
            {
                b.group(g);
                b.channel(NioSocketChannel.class);
                b.option(ChannelOption.SO_KEEPALIVE, true);
                b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
                b.remoteAddress(host, port);

                // 设置通道初始化
                b.handler(
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

                b.connect().addListener(connectedListener);


                // 阻塞
                // f.channel().closeFuture().sync();
            }
        } catch (Exception e)
        {
            LOGGER.info("客户端连接失败!" + e.getMessage());
        }

    }

    public void close()
    {
        g.shutdownGracefully();
    }


}
