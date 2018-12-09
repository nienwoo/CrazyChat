package com.crazymakercircle.imClient.clientCommand;

import org.springframework.stereotype.Service;
import java.util.Scanner;

@Service("ChatConsoleCommand")
public class ChatConsoleCommand implements BaseCommand
{

    private String toUserId;
    private String message;
    public static final String KEY="2";

    @Override
    public void exec(Scanner scanner)
    {
        System.out.print("目标用户id：");

        toUserId = scanner.next();
        System.out.print("发送内容：");
        message = scanner.next();
    }


    @Override
    public String getKey()
    {
        return KEY;
    }

    @Override
    public String getTip()
    {
        return "聊天";
    }

}
