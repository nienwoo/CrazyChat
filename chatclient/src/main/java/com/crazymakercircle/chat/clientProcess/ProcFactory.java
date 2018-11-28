package com.crazymakercircle.chat.clientProcess;

import com.crazymakercircle.chat.clientProcess.impl.ChatProc;
import com.crazymakercircle.chat.clientProcess.impl.LoginProc;
import com.crazymakercircle.chat.common.bean.msg.ProtoMsg;

import java.util.HashMap;
import java.util.Map;

public class ProcFactory
{

    private static ProcFactory instance;

    public static Map<ProtoMsg.HeadType, Proc> factory
            = new HashMap<ProtoMsg.HeadType, Proc>();

    static
    {
        instance = new ProcFactory();
    }

    private ProcFactory()
    {
        try
        {

            Proc proc = new LoginProc();
            factory.put(proc.op(), proc);

            proc = new ChatProc();
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

    public Proc getOperation(ProtoMsg.HeadType type)
    {
        return factory.get(type);
    }


}
