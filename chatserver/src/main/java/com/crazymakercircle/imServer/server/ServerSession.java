package com.crazymakercircle.imServer.server;

import com.crazymakercircle.im.common.bean.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 实现服务器Socket Session会话
 */
@Data
@Slf4j
public class ServerSession
{

    //session唯一标示

    public static final AttributeKey<String> KEY_USER_ID =
            AttributeKey.valueOf("key_user_id");

    public static final AttributeKey<ServerSession> SESSION =
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

    public ServerSession(Channel ctx)
    {
        this.channel = ctx;
        this.sessionId = buildNewSessionId();
    }

    public String getSessionId()
    {
        return sessionId;
    }

    private static String buildNewSessionId()
    {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    public String getRemoteAddress()
    {
        return channel.remoteAddress().toString();
    }

    public synchronized void set(String key, Object value)
    {
        map.put(key, value);
    }


    public synchronized <T> T get(String key)
    {
        return (T) map.get(key);
    }


    public boolean isValid()
    {
        return getUser() != null ? true : false;
    }

    public synchronized void write(Object pkg)
    {
        channel.writeAndFlush(pkg);
    }

    public synchronized void writeAndClose(Object pkg)
    {
        ChannelFuture future = channel.writeAndFlush(pkg);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    public synchronized void close()
    {
        ChannelFuture future = channel.close();
        future.addListener(new ChannelFutureListener()
        {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception
            {
                if (future.isSuccess())
                {
                    log.error("CHANNEL_CLOSED ");
                }
            }
        });
    }


    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
        user.setSessionId(sessionId);
    }
}
