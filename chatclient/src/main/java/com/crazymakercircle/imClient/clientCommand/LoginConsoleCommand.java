package com.crazymakercircle.imClient.clientCommand;

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

        System.out.println("请输入用户名登录: ");
        userName=scanner.next();
        System.out.println("您输入的用户名是: "+userName);

        System.out.println("请输入密码: ");
        password=scanner.next();
        System.out.println("您请输入密码是: "+password);


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
