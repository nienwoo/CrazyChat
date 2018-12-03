package com.crazymakercircle.chat.serverProcess;

import com.crazymakercircle.chat.common.ServerSession;
import io.netty.channel.Channel;

public abstract class AbstractProc implements Proc
{
    protected String getKey(Channel ch)
    {
        return ch.attr(ServerSession.KEY_USER_ID).get();
    }

    protected void setKey(Channel ch, String key)
    {
        ch.attr(ServerSession.KEY_USER_ID).set(key);
    }

    protected void checkAuth(Channel ch) throws Exception
    {
        if (null == getKey(ch))
        {
            throw new Exception("此用户，没有登录成功");
        }


    }
}
