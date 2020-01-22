package org.accen.shell.command;

import java.io.OutputStream;

import org.accen.shell.command.annotation.CommandReference;

import com.beust.jcommander.Parameter;
/**
 * 发送消息
 * 形如send -mode group -to 123456 -c "hello shell"
 * @author <a href="1339liu@gmail.com">Accen</a>
 *
 */
@CommandReference("send")
public class SendMsgCommand extends LogCommand {
	/**
	 * 
	 */
	@Parameter(names = {"-mode","-m"})
	private String mode = "private";
	@Parameter(names = {"-to","-t"})
	private String to ;
	@Override
	public String run(OutputStream print) {
		// TODO Auto-generated method stub
		return null;
	}

}
