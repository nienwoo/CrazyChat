package com.crazymakercircle.chat.clientProcess;


import com.crazymakercircle.chat.common.ServerSession;
import com.crazymakercircle.chat.common.bean.msg.ProtoMsg;

/**
 * 操作类
 */
public interface Proc
{

    ProtoMsg.HeadType op();

    void action(ServerSession ch, ProtoMsg.Message proto) throws Exception;

}
