package org.accen.shell.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameter;
/**
 * 接受--log 可开启日志，注意开启日志时，不要配置console作为日志输出流
 * @author <a href="1339liu@gmail.com">Accen</a>
 *
 */
public abstract class LogCommand extends ExcuteCommand {
	@Parameter(names = {"--log"})
	private boolean log;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Override
	public void runBefore(String commandLine,String[] args) {
		if(log) {
			logger.info(">>"+commandLine);
		}
		
	}
	public void runAfter(String commandLine,String[] args,String runRs) {
		if(log) {
			logger.info("<<"+runRs);
		}
		
	}

}
