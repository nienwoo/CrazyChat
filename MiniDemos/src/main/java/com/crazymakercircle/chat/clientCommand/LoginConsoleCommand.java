package com.crazymakercircle.chat.clientCommand;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Data
@Service("LoginConsoleCommand")
public class LoginConsoleCommand implements BaseCommand
{
    public static final String KEY="1";

    private String userName;
    private String password;

    @Override
    public void exec(Scanner scanner) {

        System.out.print("输入用户名登录: ");
        userName=scanner.nextLine();
        System.out.print("输入密码: ");
        password=scanner.nextLine();

    }

    @Override
    public String getKey()
    {
        return KEY;
    }

    @Override
    public String getTip()
    {
        return "登录";
    }

}
