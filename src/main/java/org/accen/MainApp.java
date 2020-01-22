package org.accen;
import java.util.Scanner;

import org.accen.dmzj.util.ApplicationContextUtil;
import org.accen.shell.ShellMonitorManager;
import org.accen.shell.command.ConnectCommand;
import org.accen.shell.command.exception.CommandFormatException;
import org.accen.shell.command.exception.CommandNotFoundException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MainApp {
	
	public static void main(String[] args) {
		System.setProperty("user.timezone","GMT +08");
		ApplicationContext app = SpringApplication.run(MainApp.class, args);
		ApplicationContextUtil.setContext(app);
		Scanner scanner = new Scanner(System.in);
		ShellMonitorManager manager = app.getBean(ShellMonitorManager.class);
		System.out.print(manager.getCmdPrefix());
		while(true) {
			String cmdLine = scanner.nextLine();
			try {
				manager.printShell(cmdLine);
			} catch (CommandFormatException e) {
				System.out.println("command format failed. please check the paramters");
			} catch (CommandNotFoundException e) {
				System.out.println("command not found. please check the command");
			}
			System.out.print(manager.getCmdPrefix());
		}
	}

}
