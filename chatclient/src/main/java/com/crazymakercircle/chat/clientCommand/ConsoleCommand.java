package com.crazymakercircle.chat.clientCommand;

import java.util.Scanner;

public interface ConsoleCommand {
    void exec(Scanner scanner);

    String getKey();
    String getTip();
}
