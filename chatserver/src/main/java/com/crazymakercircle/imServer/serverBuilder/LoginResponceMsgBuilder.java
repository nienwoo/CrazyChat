package com.crazymakercircle.imServer.serverBuilder;

import com.crazymakercircle.im.common.ProtoInstant;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import org.springframework.stereotype.Service;

@Service("LoginResponceMsgBuilder")
public class LoginResponceMsgBuilder
{

    /**
     * 登录应答 应答消息protobuf
     */
    public  ProtoMsg.Message loginResponce(
            ProtoInstant.ResultCodeEnum en,
            long seqId)
    {
        ProtoMsg.Message.Builder mb = ProtoMsg.Message.newBuilder()
                .setType(ProtoMsg.HeadType.MESSAGE_RESPONSE)  //设置消息类型
                .setSequence(seqId);  //设置应答流水，与请求对应

        ProtoMsg.LoginResponse.Builder b = ProtoMsg.LoginResponse.newBuilder()
                .setCode(en.getCode())
                .setInfo(en.getDesc())
                .setExpose(1);

        mb.setLoginResponse(b.build());
        return mb.build();
    }


}
