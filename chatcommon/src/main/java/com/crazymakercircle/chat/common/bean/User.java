package com.crazymakercircle.chat.common.bean;

import com.crazymakercircle.chat.common.bean.msg.ProtoMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class User
{
    private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

    String uid;
    String devId;
    String token;
    String nickName;
    PLATTYPE platform;

    // windows,mac,android, ios, web , other
    public enum PLATTYPE
    {
        WINDOWS, MAC, ANDROID, IOS, WEB, OTHER;
    }

    private String sessionId;

    public String getSessionId()
    {
        return sessionId;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getDevId()
    {
        return devId;
    }

    public void setDevId(String devId)
    {
        this.devId = devId;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public PLATTYPE getPlatform()
    {
        return platform;
    }

    public void setPlatform(PLATTYPE platform)
    {
        this.platform = platform;
    }

    public void setPlatform(int platform)
    {
        PLATTYPE[] values = PLATTYPE.values();
        for (int i = 0; i < values.length; i++)
        {
            if (values[i].ordinal() == platform)
            {
                this.platform = values[i];
            }
        }

    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "uid='" + uid + '\'' +
                ", devId='" + devId + '\'' +
                ", token='" + token + '\'' +
                ", nickName='" + nickName + '\'' +
                ", platform=" + platform +
                '}';
    }

    public static User fromMsg(ProtoMsg.LoginRequest info)
    {
        User user = new User();
        user.uid = new String(info.getUid());
        user.devId = new String(info.getDeviceId());
        user.token = new String(info.getToken());
        user.setPlatform(info.getPlatform());
        LOGGER.info("登录中: {}", user.toString());
        return user;

    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }
}
