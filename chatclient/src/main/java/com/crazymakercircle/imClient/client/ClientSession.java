package com.crazymakercircle.imClient.client;

import com.crazymakercircle.im.common.bean.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 实现服务器Socket Session会话
 */
@Slf4j
@Data
public class ClientSession
{

    //session唯一标示
    private final static String SESSION_UNIQUE_ID =
            "session_unique_id";


    public static final AttributeKey<ClientSession> SESSION =
            AttributeKey.valueOf("session");


    /**
     * 用户实现客户端会话管理的核心
     */
    private Channel channel;
    private User user;
    private final String sessionId;

    private boolean isConnected = false;
    private boolean isLogin = false;

    /**
     * session中存储的session 变量属性值
     */
    private Map<String, Object> map = new HashMap<String, Object>();

    public ClientSession(Channel channel)
    {
        this.channel = channel;
        this.sessionId = String.valueOf(-1);
        channel.attr(ClientSession.SESSION).set(this);
    }


    public String getRemoteAddress()
    {
        return channel.remoteAddress().toString();
    }


    public synchronized ChannelFuture sengPackage(Object pkg)
    {
        ChannelFuture f = channel.writeAndFlush(pkg);

        return f;
    }

    public synchronized void writeAndClose(Object pkg)
    {
        ChannelFuture future = channel.writeAndFlush(pkg);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    public synchronized void close()
    {
        isConnected=false;

        ChannelFuture future = channel.close();
        future.addListener(new ChannelFutureListener()
        {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception
            {
                if (future.isSuccess())
                {
                    log.error("连接顺利断开");
                }
            }
        });
    }



}