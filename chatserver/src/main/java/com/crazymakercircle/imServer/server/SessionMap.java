package com.crazymakercircle.imServer.server;

import io.netty.channel.socket.SocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Data
public class SessionMap
{

    private static SessionMap singleInstance = new SessionMap();

    private ConcurrentHashMap<String, ServerSession> map =
            new ConcurrentHashMap<String, ServerSession>();

    public static SessionMap inst()
    {
        return singleInstance;
    }

    public void addSession(
            String sessionId, ServerSession session)
    {
        map.put(sessionId, session);
        log.info("SESSION_ADD " + sessionId
                + "   total: " + map.size());
    }

    /**
     * 获取session对象
     *
     * @param sessionId
     * @return
     */
    public ServerSession getSession(String sessionId)
    {
        if (map.containsKey(sessionId))
        {
            return map.get(sessionId);
        }
        else
        {
            return null;
        }
    }

    /**
     * 删除session
     *
     * @param sessionId
     */
    public void removeSession(String sessionId)
    {
        if (!map.containsKey(sessionId))
        {
            return;
        }
        map.remove(sessionId);
        log.info("SESSION_REMOVED " + sessionId
                + "   total: " + map.size());
    }


    public void bindSession(SocketChannel channel)
    {
        log.info("CHANNEL_ACTIVE " + channel.remoteAddress());
        ServerSession session = new ServerSession(channel);
        channel.attr(ServerSession.SESSION).set(session);
    }
}
