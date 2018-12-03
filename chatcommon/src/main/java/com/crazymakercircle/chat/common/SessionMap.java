package com.crazymakercircle.chat.common;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;


public class SessionMap
{

    private Logger logger = LoggerFactory.getLogger(SessionMap.class);
    private static SessionMap instance = new SessionMap();

    private ConcurrentHashMap<String, ServerSession> map = new ConcurrentHashMap<String, ServerSession>();

    public static SessionMap sharedInstance()
    {
        return instance;
    }

    public void addSession(String sessionId, ServerSession session)
    {
        map.put(sessionId, session);
        logger.info("SESSION_ADD " + sessionId + "   total: " + map.size());
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
        logger.info("SESSION_REMOVED " + sessionId + "   total: " + map.size());
    }

    /**
     * uid+fevId
     *
     * @param keys
     * @return
     */
    public static String getSessionId(String... keys)
    {
        return StringUtils.join(keys, "-");
    }


}
