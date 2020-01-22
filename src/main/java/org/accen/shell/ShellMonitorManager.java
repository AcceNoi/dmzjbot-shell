package org.accen.shell;

import java.util.HashMap;
import java.util.Map;

import org.accen.shell.command.ExcuteCommand;
import org.accen.shell.command.annotation.CommandReference;
import org.accen.shell.command.exception.CommandFormatException;
import org.accen.shell.command.exception.CommandNotFoundException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
@Component
public class ShellMonitorManager implements BeanPostProcessor{
	private Map<String, ExcuteCommand> commandMap = new HashMap<String, ExcuteCommand>();
	/**
	 * 注册command，如果map中已注册有同名的command，则返回false
	 * @param name
	 * @param monitor
	 * @return
	 */
	public boolean register(String name,ExcuteCommand monitor) {
		if(commandMap.containsKey(name)) {
			return false;
		}else {
			commandMap.put(name, monitor);
			return true;
		}
	}
	/**
	 * 从其他输入流读取后调用此方法进行通知
	 * @param command
	 * @throws CommandFormatException 
	 * @throws CommandNotFoundException 
	 */
	public void printShell(String command) throws CommandFormatException, CommandNotFoundException {
		String commandTrim = command.trim();
		if(commandTrim.indexOf(" ")<0) {
			throw new CommandFormatException();
		}
		int cmdOptionSplitIndex = commandTrim.indexOf(" ");
		noticeMonitors(commandTrim.substring(0, cmdOptionSplitIndex), commandTrim.substring(cmdOptionSplitIndex).trim());
	}
	private void noticeMonitors(String cmd,String options) throws CommandNotFoundException {
		if(commandMap.containsKey(cmd)) {
			commandMap.get(cmd).exc(options, System.out);
		}else {
			throw new CommandNotFoundException();
		}
	}
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		CommandReference cmdRef = bean.getClass().getAnnotation(CommandReference.class);
		if(cmdRef != null && bean instanceof ExcuteCommand) {
			ExcuteCommand cmd = (ExcuteCommand) bean;
			this.register(beanName, cmd);
		}
		return bean;
	}
	public String getCmdPrefix() {
		return cmdPrefix();
	}
	protected String cmdPrefix() {
		return "shell>";
	}
}
