package com.crazymakercircle.chat.client;

import com.crazymakercircle.chat.ClientSender.ChatSender;
import com.crazymakercircle.chat.ClientSender.LoginSender;
import com.crazymakercircle.chat.clientCommand.*;
import com.crazymakercircle.chat.common.bean.User;
import com.crazymakercircle.cocurrent.QueueTaskThread;
import io.netty.channel.*;
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
    private ChatSender sender;
    private LoginSender l;


    private boolean initFalg = true;
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
        CommandClient.this.notifyAll();
    };


    GenericFutureListener<ChannelFuture> connectedListener = (ChannelFuture f) ->
    {
        final EventLoop eventLoop
                = f.channel().eventLoop();
        if (!f.isSuccess())
        {
            log.info("与服务端断开连接!在10s之后准备尝试重连!");
            eventLoop.schedule(() -> nettyConnector.doConnect(), 10, TimeUnit.SECONDS);

            initFalg = false;
        }
        else
        {
            initFalg = true;
        }
        if (initFalg)
        {
            log.info("EchoClient客户端连接成功!");
            channel = f.channel();

            // 创建会话
            ClientSession session = new ClientSession(channel);
            channel.attr(ClientSession.SESSION).set(session);
            session.setUser(CommandClient.this.getUser());
            startUserThread();

            channel.closeFuture().addListener(closeListener);

            //唤醒用户线程
            CommandClient.this.notify();
        }

    };


    public CommandClient()
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


    public void startNettyClient()
    {
        QueueTaskThread.add(() ->
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
            while (initFalg == false)
            {

                //开始连接
                startNettyClient();
                waitUserThread();
            }


            //处理命令
            while (sender.isLogin())
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
                        startLogin(command);
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
        if (!sender.isLogin())
        {
            log.info("还没有登录，请先登录");
            return;
        }

        l.sendLoginMsg();
        waitUserThread();

    }

    private void startLogin(BaseCommand command)
    {
        //登录
        if (!sender.isLogin())
        {
            log.info("还没有登录，请先登录");
            return;
        }
    }


    private void startLogout(BaseCommand command)
    {
        //登录
        if (!sender.isLogin())
        {
            log.info("还没有登录，请先登录");
            return;
        }
    }


}
