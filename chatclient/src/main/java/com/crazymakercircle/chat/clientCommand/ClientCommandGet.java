package com.crazymakercircle.chat.clientCommand;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.*;
@Data
@Service("ClientCommandGet")
public class ClientCommandGet implements BaseCommand
{

    public static final String KEY="0";

    private String allCommandsShow;
    private String commandInput;

    @Override
    public void exec(Scanner scanner)
    {

        System.err.println(allCommandsShow);
        System.err.println("请输入某个操作指令：");
        //  获取第一个指令
        commandInput = scanner.next();


    }


    @Override
    public String getKey()
    {
        return KEY;
    }

    @Override
    public String getTip()
    {
        return "show 所有命令";
    }

}
