package com.crazymakercircle.chat.common.bean;

import com.crazymakercircle.chat.common.bean.msg.ProtoMsg;
import org.apache.commons.lang.StringUtils;

public class ChatMsg
{


    //消息类型  1：纯文本  2：音频 3：视频 4：地理位置 5：其他
   public enum MSGTYPE{
       TEXT,
       AUDIO,
       VIDEO,
       POS,
       OTHER;
   }

    public ChatMsg(User user)
    {
        this.user = user;
        this.setTime(System.currentTimeMillis());
        this.setFrom(user.getUid());
        this.setFromNick(user.getNickName());

    }

    private User user;

    private long   msgId;
    private String from;
    private String to;
    private long   time;
    private MSGTYPE msgType;
    private String content;
    private String url;          //多媒体地址
    private String property;     //附加属性
    private String fromNick;     //发送者昵称
    private String json;         //附加的json串

    public long getMsgId()
    {
        return msgId;
    }

    public String getFrom()
    {
        return from;
    }

    public String getTo()
    {
        return to;
    }

    public long getTime()
    {
        return time;
    }

    public MSGTYPE getMsgType()
    {
        return msgType;
    }

    public String getContent()
    {
        return content;
    }

    public String getUrl()
    {
        return url;
    }

    public String getProperty()
    {
        return property;
    }

    public String getFromNick()
    {
        return fromNick;
    }

    public User.PLATTYPE getPlatform()
    {
        return user.getPlatform();
    }

    public String getJson()
    {
        return json;
    }

    public void fillMsg(ProtoMsg.MessageRequest.Builder cb)
    {
        if(msgId >0){
            cb.setMsgId(msgId);
        }
        if(StringUtils.isNotEmpty(from)){
            cb.setFrom(from);
        }
        if(StringUtils.isNotEmpty(to)){
            cb.setTo(to);
        }
        if(time > 0){
            cb.setTime(time);
        }
        if(msgType !=null ){
            cb.setMsgType(msgType.ordinal());
        }
        if(StringUtils.isNotEmpty(content)){
            cb.setContent(content);
        }
        if(StringUtils.isNotEmpty(url)){
            cb.setUrl(url);
        }
        if(StringUtils.isNotEmpty(property)){
            cb.setProperty(property);
        }
        if(StringUtils.isNotEmpty(fromNick)){
            cb.setFromNick(fromNick);
        }

        if(StringUtils.isNotEmpty(json)){
            cb.setJson(json);
        }
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public void setMsgId(long msgId)
    {
        this.msgId = msgId;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public void setTo(String to)
    {
        this.to = to;
    }

    public void setTime(long time)
    {
        this.time = time;
    }

    public void setMsgType(MSGTYPE msgType)
    {
        this.msgType = msgType;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public void setProperty(String property)
    {
        this.property = property;
    }

    public void setFromNick(String fromNick)
    {
        this.fromNick = fromNick;
    }

    public void setJson(String json)
    {
        this.json = json;
    }
}
