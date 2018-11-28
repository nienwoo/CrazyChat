package com.crazymakercircle.chat.serverProcess;

import com.crazymakercircle.chat.common.bean.msg.ProtoMsg;
import com.crazymakercircle.chat.serverProcess.impl.LoginProc;
import com.crazymakercircle.chat.serverProcess.impl.ChatProc;

import java.util.HashMap;
import java.util.Map;

public class ProcFactory
{

	private static ProcFactory instance;

	public static Map<ProtoMsg.HeadType, Proc> factory
			= new HashMap<ProtoMsg.HeadType, Proc>();

	static {
		instance = new ProcFactory();
	}
	
	private ProcFactory(){
		try{

			Proc proc = new LoginProc();
			factory.put(proc.op(), proc);

			proc = new ChatProc();
			factory.put(proc.op(), proc);

		} catch (Exception e){
			e.printStackTrace();
		}
	}
	

	public static ProcFactory getInstance(){
		return instance;
	}

	public Proc getOperation(ProtoMsg.HeadType  type){
		return factory.get(type);
	}


}
