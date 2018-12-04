package com.crazymakercircle.chat.clientCommand;

import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service("ChatConsoleCommand")
public class ChatConsoleCommand implements ConsoleCommand {

  private   String toUserId;
    private  String message;

    @Override
    public void exec(Scanner scanner) {
        System.out.print("目标用户id：");

         toUserId = scanner.next();
        System.out.print("发送内容：");
        message = scanner.next();
      }


    @Override
    public String getKey()
    {
        return "2";
    }

    @Override
    public String getTip()
    {
        return "聊天";
    }

}
