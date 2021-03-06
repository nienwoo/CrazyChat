/**
 * Created by 尼恩 at 疯狂创客圈
 */

package com.crazymakercircle.chat.clientBuilder;

import com.crazymakercircle.chat.common.bean.User;
import com.crazymakercircle.chat.common.bean.msg.ProtoMsg;


/**
 * 登陆消息Builder
 */
public class LoginMsgBuilder extends BaseBuilder
{
    private final User user;

    public LoginMsgBuilder(User user)
    {
        super(ProtoMsg.HeadType.LOGIN_REQUEST);
        this.user = user;
    }

    public ProtoMsg.Message build()
    {
        ProtoMsg.Message message = buildCommon(-1);
        ProtoMsg.LoginRequest.Builder lb = ProtoMsg.LoginRequest.newBuilder()
                .setDeviceId(user.getDevId())
                .setPlatform(user.getPlatform().ordinal())
                .setToken(user.getToken())
                .setUid(user.getUid());
        return message.toBuilder().setLoginRequest(lb).build();
    }

    public static ProtoMsg.Message buildLoginMsg(User user)
    {
        LoginMsgBuilder builder = new LoginMsgBuilder(user);
        return builder.build();

    }
}


