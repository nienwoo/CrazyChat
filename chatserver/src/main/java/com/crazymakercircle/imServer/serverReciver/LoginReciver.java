package com.crazymakercircle.imServer.serverReciver;

import com.crazymakercircle.im.common.ProtoInstant;
import com.crazymakercircle.im.common.bean.User;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.imServer.server.ServerSession;
import com.crazymakercircle.imServer.server.SessionMap;
import com.crazymakercircle.imServer.serverBuilder.LoginResponceMsgBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Slf4j
@Service("LoginReciver")
public class LoginReciver extends AbstractServerReciver
{
    @Autowired
    LoginResponceMsgBuilder loginResponceMsgBuilder;

    @Override
    public ProtoMsg.HeadType op()
    {
        return ProtoMsg.HeadType.LOGIN_REQUEST;
    }

    @Override
    public void action(ServerSession session,
                       ProtoMsg.Message proto)
    {
        // 取出token验证
        ProtoMsg.LoginRequest info = proto.getLoginRequest();
        long seqNo= proto.getSequence();

        User user = User.fromMsg(info);

        //检查用户
        if (!userPass(user))
        {
            ProtoInstant.ResultCodeEnum resultcode =
                    ProtoInstant.ResultCodeEnum.NO_TOKEN;
            ProtoMsg.Message response =
                    loginResponceMsgBuilder.loginResponce(resultcode,seqNo);

            //发送之后，断开连接
            session.writeAndClose(response);
            return;
        }

        session.setUser(user);

        String sid = session.getSessionId();

        SessionMap.inst().addSession(sid, session);

        //登录成功
        ProtoInstant.ResultCodeEnum resultcode =
                ProtoInstant.ResultCodeEnum.SUCCESS;
        ProtoMsg.Message response =
                loginResponceMsgBuilder.loginResponce(resultcode,seqNo);
        session.write(response);
    }

    private boolean userPass(User user)
    {
        //校验用户
        //调用远程用户restfull 校验服务

        return true;

    }

}
