package com.crazymakercircle.chat.clientBuilder;

import com.crazymakercircle.chat.common.bean.msg.ProtoMsg;

/**
 * 基础 Builder
 *
 * @author 尼恩 at  疯狂创客圈
 */
public class BaseBuilder
{
    protected ProtoMsg.HeadType type;
    private long seqId;

    public BaseBuilder(ProtoMsg.HeadType type)
    {
        this.type = type;
    }

    /**
     * 构建消息 基础部分
     */
    public ProtoMsg.Message buildCommon(long seqId)
    {
        this.seqId = seqId;

        ProtoMsg.Message.Builder mb = ProtoMsg.Message.newBuilder()
                .setType(type)
                .setSequence(seqId);
        return mb.buildPartial();
    }

}
