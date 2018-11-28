/**
 * Created by 尼恩 at 疯狂创客圈
 */

package com.crazymakercircle.chat.clientBuilder;

import com.crazymakercircle.chat.common.bean.ChatMsg;
import com.crazymakercircle.chat.common.bean.msg.ProtoMsg;

/**
 * 聊天消息Builder
 */
public class ChatMsgBuilder extends BaseBuilder
{


    private ChatMsg chatMsg;


    public ChatMsgBuilder(ChatMsg chatMsg)
    {
        super(ProtoMsg.HeadType.LOGIN_REQUEST);
        this.chatMsg = chatMsg;
    }


    public ProtoMsg.Message build()
    {
        ProtoMsg.Message message = buildCommon(-1);
        ProtoMsg.MessageRequest.Builder cb
                = ProtoMsg.MessageRequest.newBuilder();

        chatMsg.fillMsg(cb);
        return message.toBuilder().setMessageRequest(cb).build();
    }

    public static ProtoMsg.Message buildChatMsg(ChatMsg chatMsg)
    {
        ChatMsgBuilder builder = new ChatMsgBuilder(chatMsg);
        return builder.build();

    }
}