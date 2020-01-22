package org.accen.shell.command;

import java.io.IOException;
import java.io.OutputStream;

import org.accen.shell.command.annotation.CommandReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.beust.jcommander.Parameter;
/**
 * 连接coolqhttp服务器
 * @author <a href="1339liu@gmail.com">Accen</a>
 *
 */
@CommandReference("conn")
public class ConnectCommand extends LogCommand{
	@Parameter(names = {"-host","-h"})
	private String curHost;
	@Parameter(names = {"-token","-t"})
	private String curAccessToken;
	private boolean connected = false;
	private CoolqVersion curCoolqVersion;
	
	public CoolqVersion getCurCoolqVersion() {
		return curCoolqVersion;
	}
	public void setCurCoolqVersion(CoolqVersion curCoolqVersion) {
		this.curCoolqVersion = curCoolqVersion;
	}
	/**
	 * 判断是否已连接
	 * @return
	 */
	public boolean connected() {
		return connected;
	}
	@Override
	public String run(OutputStream print) {
		String rs = null;
		if(StringUtils.isEmpty(curHost)) {
			rs = "-host or -h paramater is must \n";
		}else {
			CoolqVersion coolqVersion = checkCoolqHttpConnect();
			if(coolqVersion!=null) {
				this.setCurCoolqVersion(coolqVersion);
				rs = "connect success!";
			}else {
				rs = "can not connect to the host,please try again later \n";	
			}
		}
		/*if(StringUtils.isEmpty(curAccessToken)) {
			try {
				print.write("-token or -t paramater is must \n".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
		
		try {
			print.write(rs.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rs;
	}
	private CoolqVersion checkCoolqHttpConnect() {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add(AUTHOR_HEADER, AUTHOR_HEADER_PREFIX+curAccessToken);
		HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
		try {
			CoolqVersion coolqVersion = restTemplate.postForObject(curHost+VERSION_INFO_PATH, requestEntity, CoolqVersion.class);
//			CoolqVersion coolqVersion = restTemplate.getForObject(curHost+VERSION_INFO_PATH, CoolqVersion.class, requestEntity);
			return coolqVersion;
		}catch (RestClientException e) {
			return null;
		}
		
	}
	private static final String VERSION_INFO_PATH = "/get_version_info";
	private static final String AUTHOR_HEADER = "Authorization";
	private static final String AUTHOR_HEADER_PREFIX = "Bearer ";
	
}
class CoolqVersion{
	private String coolqDirectory;//酷Q根目录
	private String coolqEdition;//酷Q版本
	private String pluginVersion;//HTTP API 插件版本
	private String pluginBuildNumber;//HTTP API 插件 build 号
	private String pluginBuildConfiguration;//HTTP API 插件编译配置，debug 或 release
	public String getCoolqDirectory() {
		return coolqDirectory;
	}
	public void setCoolqDirectory(String coolqDirectory) {
		this.coolqDirectory = coolqDirectory;
	}
	public String getCoolqEdition() {
		return coolqEdition;
	}
	public void setCoolqEdition(String coolqEdition) {
		this.coolqEdition = coolqEdition;
	}
	public String getPluginVersion() {
		return pluginVersion;
	}
	public void setPluginVersion(String pluginVersion) {
		this.pluginVersion = pluginVersion;
	}
	public String getPluginBuildNumber() {
		return pluginBuildNumber;
	}
	public void setPluginBuildNumber(String pluginBuildNumber) {
		this.pluginBuildNumber = pluginBuildNumber;
	}
	public String getPluginBuildConfiguration() {
		return pluginBuildConfiguration;
	}
	public void setPluginBuildConfiguration(String pluginBuildConfiguration) {
		this.pluginBuildConfiguration = pluginBuildConfiguration;
	}
	
}