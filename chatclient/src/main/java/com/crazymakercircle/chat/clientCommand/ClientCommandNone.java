package com.crazymakercircle.chat.clientCommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("ClientCommandNone")
public class ClientCommandNone implements ConsoleCommand
{

    @Autowired
    ChatConsoleCommand chatConsoleCommand;

    @Autowired
    LoginConsoleCommand loginConsoleCommand;

    @Autowired
    LogoutConsoleCommand logoutConsoleCommand;

    private Map<String, ConsoleCommand> commandMap;

    private String commandshow;

    public ClientCommandNone()
    {
        commandMap = new HashMap<>();
        commandMap.put(chatConsoleCommand.getKey(), chatConsoleCommand);
        commandMap.put(loginConsoleCommand.getKey(), loginConsoleCommand);
        commandMap.put(logoutConsoleCommand.getKey(), logoutConsoleCommand);

        Set<Map.Entry<String, ConsoleCommand>> entrys =
                commandMap.entrySet();
        Iterator<Map.Entry<String, ConsoleCommand>> iterator =
                entrys.iterator();

        StringBuilder builder=new StringBuilder();
        while (iterator.hasNext())
        {
            ConsoleCommand next =  iterator.next().getValue();

            builder.append(next.getKey())
                    .append("->")
                    .append(next.getTip())
                    .append(" | ");

        }
        commandshow=builder.toString();

    }

    @Override
    public void exec(Scanner scanner)
    {

        System.err.println(commandshow);
        System.err.println("请输入某个操作指令：");
        //  获取第一个指令
        String command = scanner.next();

        ConsoleCommand consoleCommand = commandMap.get(command);

        if (consoleCommand != null)
        {
            consoleCommand.exec(scanner);
        }
        else
        {
            System.err.println("无法识别[" + command + "]指令，请重新输入!");
        }
    }


    @Override
    public String getKey()
    {
        return "0";
    }

    @Override
    public String getTip()
    {
        return "展示所有命令";
    }

}
