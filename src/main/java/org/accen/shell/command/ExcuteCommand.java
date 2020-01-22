package org.accen.shell.command;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.beust.jcommander.DynamicParameter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameterized;
import com.beust.jcommander.Strings;
import com.beust.jcommander.WrappedParameter;

public abstract class ExcuteCommand implements Command{
	private String[] args;
	private String parseEndCharsq;
	public String getParseEndCharSq() {
		return parseEndCharsq;
	}
	/**
	 * 初始化命令参数
	 */
	private void parseLine(String commandLine) {
		
//		args = commandLine.split(" ");
		JCommander jc = JCommander.newBuilder().addObject(this).build();
		List<String> paramList = new LinkedList<String>();
		parseParam(this, paramList);
		String params = Strings.join("|",paramList);
		Pattern splitePt = Pattern.compile("("+params+")");
		Matcher mt = splitePt.matcher(commandLine);
		List<String> argList = new LinkedList<String>();
		int valueLastIndexStart = 0;
		int valueLastIndexEnd = 0;
		while(mt.find()) {
			String curParam = mt.group(1);
			int curIndex = mt.start(1);valueLastIndexEnd = curIndex;
			if(valueLastIndexEnd>valueLastIndexStart) {
				argList.add(commandLine.substring(valueLastIndexStart, valueLastIndexEnd).trim());
			}
			valueLastIndexStart = curIndex + curParam.length();
			argList.add(curParam);
		}
		if(valueLastIndexStart < commandLine.length()) {
			argList.add(commandLine.substring(valueLastIndexStart).trim());
		}
		args = argList.toArray(new String[argList.size()]);
		jc.parse(args);
	}
	public static void parseParam(Object obj,List<String> argList) {
		List<Parameterized> parameterizeds = Parameterized.parseArg(obj);
		for(Parameterized parameterized:parameterizeds) {
			WrappedParameter wp = parameterized.getWrappedParameter();
			if (wp != null && wp.getParameter() != null) {
                Parameter annotation = wp.getParameter();
                argList.addAll(Arrays.asList(annotation.names()));
			} else if (parameterized.getDelegateAnnotation() != null) {
				Object delegateObject = parameterized.get(obj);
				parseParam(delegateObject, argList);
			} else if (wp != null && wp.getDynamicParameter() != null) {
				DynamicParameter dp = wp.getDynamicParameter();
				argList.addAll(Arrays.asList(dp.names()));
			}
		}
	}
	/**
	 * 执行前可自定义的操作
	 * @param commandLine 原始命令
	 * @param args 分割后的参数
	 */
	protected void runBefore(String commandLine,String[] args) {
		
	}
	/**
	 * 执行后可自定义的操作
	 * @param commandLine 原始命令
	 * @param args 分割后的参数
	 * @param runRs 执行后的返回值
	 */
	protected void runAfter(String commandLine,String[] args,String runRs) {
		
	}
	/**
	 * 实际的执行方法
	 * @param commandLine
	 */
	public void exc(String commandLine,OutputStream print) {
		//1.刷新参数
		refresh();
		//2.解析参数
		parseLine(commandLine);
		//3.自定义执行前的操作
		runBefore(commandLine,args);
		try {
			print.write("accen-shell>".getBytes(Charset.forName("utf-8")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//4.执行
		String rs = run(print);
		//5.自定义执行后的操作
		runAfter(commandLine,args,rs);
	}
	/**
	 * 刷新的方法，这里实现的是刷新所有被{@link Parameter}所修饰的参数(包括父类)，基本上可以满足大部分需求
	 */
	protected void refresh() {
		refreshPrameter(this, this.getClass());
	}
	private void refreshPrameter(Object obj,Class<?> clazz) {
		
		if(clazz.equals(Object.class)) {
			//已经到最底层的Object类了，不再继续处理
			return ;
		}else {
			//仍是子类
			Field[] allFields = clazz.getDeclaredFields();
			
			Arrays.asList(allFields).forEach(field -> {
				Parameter p = field.getAnnotation(Parameter.class);
				if (p != null) {
					try {
						field.setAccessible(true);
						if (field.getType() == int.class) {
							field.setInt(obj, 0);
						}else if(field.getType() == short.class) {
							field.setShort(obj, (short) 0);
						}else if(field.getType() == long.class) {
							field.setShort(obj, (short) 0);
						}else if(field.getType() == byte.class) {
							field.setByte(obj, (byte) 0);
						}else if(field.getType() == float.class) {
							field.setFloat(obj, 0);
						}else if(field.getType() == double.class) {
							field.setDouble(obj, 0);
						}else if(field.getType() == boolean.class) {
							field.setBoolean(obj, false);
						}else {
							field.set(obj, null);
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					
				}
			});
			//获取父类，继续
			refreshPrameter(obj,clazz.getSuperclass());
		}
	}
}
