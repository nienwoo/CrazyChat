package com.crazymakercircle.imServer.serverReciver;


import com.crazymakercircle.im.common.bean.msg.ProtoMsg;

import java.util.HashMap;
import java.util.Map;

public class ProcFactory
{

    private static ProcFactory instance;

    public static Map<ProtoMsg.HeadType, ServerReciever> factory
            = new HashMap<ProtoMsg.HeadType, ServerReciever>();

    static
    {
        instance = new ProcFactory();
    }

    private ProcFactory()
    {
        try
        {

            ServerReciever proc = new LoginReciver();
            factory.put(proc.op(), proc);

            proc = new ChatReciver();
            factory.put(proc.op(), proc);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static ProcFactory getInstance()
    {
        return instance;
    }

    public ServerReciever getOperation(ProtoMsg.HeadType type)
    {
        return factory.get(type);
    }


}
