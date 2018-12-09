package com.crazymakercircle.imClient.client;

import com.crazymakercircle.cocurrent.FutureTaskScheduler;
import com.crazymakercircle.im.common.bean.User;
import com.crazymakercircle.imClient.ClientSender.ChatSender;
import com.crazymakercircle.imClient.ClientSender.LoginSender;
import com.crazymakercircle.imClient.clientCommand.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Data
@Service("CommandClient")
public class CommandClient
{

    @Autowired
    ChatConsoleCommand chatConsoleCommand;

    @Autowired
    LoginConsoleCommand loginConsoleCommand;


    @Autowired
    LogoutConsoleCommand logoutConsoleCommand;

    @Autowired
    ClientCommandGet clientCommandGet;

    private Map<String, BaseCommand> commandMap;

    private String commandshow;


    @Autowired
    private NettyConnector nettyConnector;

    private Channel channel;
    private ChatSender chatSender;

    @Autowired
    private LoginSender loginSender;


    private boolean connectFlag = false;
    private User user;

    GenericFutureListener<ChannelFuture> closeListener = (ChannelFuture f) ->
    {


        log.info(new Date() + ": 连接已经断开……");
        channel = f.channel();

        // 创建会话
        ClientSession session =
                channel.attr(ClientSession.SESSION).get();
        session.close();
        nettyConnector.close();

        //唤醒用户线程
        notifyUserThread();
    };


    GenericFutureListener<ChannelFuture> connectedListener = (ChannelFuture f) ->
    {
        final EventLoop eventLoop
                = f.channel().eventLoop();
        if (!f.isSuccess())
        {
            log.info("连接失败!在10s之后准备尝试重连!");
            eventLoop.schedule(
                    () -> nettyConnector.doConnect(),
                    10,
                    TimeUnit.SECONDS);

            connectFlag = false;
        }
        else
        {
            connectFlag = true;

            log.info("EchoClient客户端连接成功!");
            channel = f.channel();

            // 创建会话
            ClientSession s = new ClientSession(channel);
            s.setUser(CommandClient.this.getUser());
            s.setConnected(true);
            session = s;
            channel.closeFuture().addListener(closeListener);

            //唤醒用户线程
            notifyUserThread();
        }

    };
    private ClientSession session;


    public void initCommandMap()
    {
        commandMap = new HashMap<>();
        commandMap.put(clientCommandGet.getKey(), clientCommandGet);
        commandMap.put(chatConsoleCommand.getKey(), chatConsoleCommand);
        commandMap.put(loginConsoleCommand.getKey(), loginConsoleCommand);
        commandMap.put(logoutConsoleCommand.getKey(), logoutConsoleCommand);

        Set<Map.Entry<String, BaseCommand>> entrys =
                commandMap.entrySet();
        Iterator<Map.Entry<String, BaseCommand>> iterator =
                entrys.iterator();

        StringBuilder builder = new StringBuilder();
        while (iterator.hasNext())
        {
            BaseCommand next = iterator.next().getValue();

            builder.append(next.getKey())
                    .append("->")
                    .append(next.getTip())
                    .append(" | ");

        }
        commandshow = builder.toString();

        clientCommandGet.setAllCommandsShow(commandshow);


    }


    public void startConnectServer()
    {
        FutureTaskScheduler.add(() ->
        {
            nettyConnector.setConnectedListener(connectedListener);
            nettyConnector.doConnect();
        });
    }


    public synchronized void notifyUserThread()
    {
        //唤醒主线程
        this.notify();

    }

    public synchronized void waitUserThread()
    {

        //唤醒主线程
        try
        {
            this.wait();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

    }

    public void startUserThread()
            throws InterruptedException
    {
        while (true)
        {
            //建立连接
            while (connectFlag == false)
            {

                //开始连接
                startConnectServer();
                waitUserThread();
            }


            //处理命令
            while (null != session && session.isConnected())
            {

                Scanner scanner = new Scanner(System.in);
                clientCommandGet.exec(scanner);
                String key = clientCommandGet.getCommandInput();
                BaseCommand command = commandMap.get(key);

                if (null == command)
                {
                    System.err.println("无法识别[" + command + "]指令，请重新输入!");
                    continue;
                }

                command.exec(scanner);
                switch (key)
                {
                    case ChatConsoleCommand.KEY:
                        startOneChat(command);
                        break;

                    case LoginConsoleCommand.KEY:
                        startLogin((LoginConsoleCommand) command);
                        break;

                    case LogoutConsoleCommand.KEY:
                        startLogout(command);
                        break;

                }
            }
        }
    }

    private void startOneChat(BaseCommand command)
    {
        //登录
        if (!isLogin())
        {
            log.info("还没有登录，请先登录");
            return;
        }
//        loginSender.setUser();
        loginSender.sendLoginMsg();
        waitUserThread();

    }

    private void startLogin(LoginConsoleCommand command)
    {
        //登录
        if (!isConnectFlag())
        {
            log.info("连接异常，请重新建立连接");
            return;
        }
        User user = new User();
        user.setUid(command.getUserName());
        user.setToken(command.getPassword());
        user.setDevId("1111");
        loginSender.setUser(user);
        loginSender.setSession(session);
        loginSender.sendLoginMsg();
    }


    private void startLogout(BaseCommand command)
    {
        //登录
        if (!isLogin())
        {
            log.info("还没有登录，请先登录");
            return;
        }
    }


    public boolean isLogin()
    {
        if (null == session)
        {
            log.info("session is null");
            return false;
        }

        return session.isLogin();
    }

}
