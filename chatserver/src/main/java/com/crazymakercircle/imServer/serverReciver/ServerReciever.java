package com.crazymakercircle.imServer.serverReciver;


import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.imServer.server.ServerSession;

/**
 * 操作类
 */
public interface ServerReciever
{

    ProtoMsg.HeadType op();

    void action(ServerSession ch, ProtoMsg.Message proto);

}
