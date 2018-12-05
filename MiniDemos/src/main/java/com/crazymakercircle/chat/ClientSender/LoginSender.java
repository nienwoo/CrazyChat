package com.crazymakercircle.chat.ClientSender;

import com.crazymakercircle.chat.clientBuilder.LoginMsgBuilder;
import com.crazymakercircle.chat.common.bean.msg.ProtoMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("LoginSender")
public class LoginSender  extends BaseSender
{


    public void sendLoginMsg()
    {
        if(!isConnected())
        {
            log.info("还没有建立连接!");
            return;
        }
        log.info("开始登陆");
        ProtoMsg.Message message =
                LoginMsgBuilder.buildLoginMsg(getUser(),getSession());
        super.sendMsg(message);
    }


   }
