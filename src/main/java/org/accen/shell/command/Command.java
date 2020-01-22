package org.accen.shell.command;

import java.io.OutputStream;

public interface Command {
//	public void help();
	/**
	 * 具体的执行，需要返回执行结果
	 * @return
	 */
	public String run(OutputStream print);

}
