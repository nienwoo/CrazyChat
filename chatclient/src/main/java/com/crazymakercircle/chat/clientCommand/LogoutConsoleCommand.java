package com.crazymakercircle.chat.clientCommand;

import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service("LogoutConsoleCommand")
public class LogoutConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner) {

    }


    @Override
    public String getKey()
    {
        return "10";
    }

    @Override
    public String getTip()
    {
        return "退出";
    }

}
