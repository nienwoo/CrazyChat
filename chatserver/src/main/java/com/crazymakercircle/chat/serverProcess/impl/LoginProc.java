package com.crazymakercircle.chat.serverProcess.impl;

import com.crazymakercircle.chat.common.ProtoInstant;
import com.crazymakercircle.chat.common.ServerMsgBuilder;
import com.crazymakercircle.chat.common.ServerSession;
import com.crazymakercircle.chat.common.SessionMap;
import com.crazymakercircle.chat.common.bean.User;
import com.crazymakercircle.chat.common.bean.msg.ProtoMsg;
import com.crazymakercircle.chat.serverProcess.AbstractProc;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoginProc extends AbstractProc
{
    private Logger LOGGER = LoggerFactory.getLogger(LoginProc.class);

    @Override
    public ProtoMsg.HeadType op()
    {
        return ProtoMsg.HeadType.LOGIN_REQUEST;
    }

    @Override
    public void action(ServerSession session, ProtoMsg.Message proto) throws Exception
    {
        // 取出token验证
        ProtoMsg.LoginRequest info = proto.getLoginRequest();

        User user = User.fromMsg(info);

        if (StringUtils.isEmpty(user.getToken()))
        {
            ProtoInstant.ResultCodeEnum resultcode = ProtoInstant.ResultCodeEnum.NO_TOKEN;
            ProtoMsg.Message response =
                    ServerMsgBuilder.buildLoginResponce(resultcode, proto.getSequence(), "");
            session.writeAndClose(response);
            return;
        }

        session.setUser(user);
        SessionMap.sharedInstance().addSession(session.getSessionId(), session);

        String sid = session.getSessionId();

        ProtoInstant.ResultCodeEnum resultcode = ProtoInstant.ResultCodeEnum.SUCCESS;
        ProtoMsg.Message response =
                ServerMsgBuilder.buildLoginResponce(resultcode, proto.getSequence(), sid);
        session.write(response);
    }

}
