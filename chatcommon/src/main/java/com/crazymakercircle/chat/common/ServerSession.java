package com.crazymakercircle.chat.common;

import com.crazymakercircle.chat.common.bean.User;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 实现服务器Socket Session会话
 */
public class ServerSession
{

    //session唯一标示
    private final static String SESSION_UNIQUE_ID = "session_unique_id";


    private Logger LOGGER = LoggerFactory.getLogger(ServerSession.class);

    public static final AttributeKey<String> KEY_USER_ID = AttributeKey.valueOf("key");

    public static final AttributeKey<ServerSession> SESSION = AttributeKey.valueOf("session");


    /**
     * 用户实现客户端会话管理的核心
     */
    private ChannelHandlerContext ctx;
    private User user;
    private final String sessionId;

    private boolean isConnected = false;
    private boolean isLogin = false;

    /**
     * session中存储的session 变量属性值
     */
    private Map<String, Object> map = new HashMap<String, Object>();

    public ServerSession(ChannelHandlerContext ctx)
    {
        this.ctx = ctx;
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
        return ctx.channel().remoteAddress().toString();
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
        ctx.writeAndFlush(pkg);
    }

    public synchronized void writeAndClose(Object pkg)
    {
        ChannelFuture future = ctx.writeAndFlush(pkg);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    public synchronized void close()
    {
        ChannelFuture future = ctx.close();
        future.addListener(new ChannelFutureListener()
        {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception
            {
                if (future.isSuccess())
                {
                    LOGGER.error("CHANNEL_CLOSED ");
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
