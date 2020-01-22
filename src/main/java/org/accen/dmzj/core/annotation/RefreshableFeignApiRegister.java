package org.accen.dmzj.core.annotation;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import org.accen.dmzj.util.ApplicationContextUtil;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import feign.Client;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
@Component
public class RefreshableFeignApiRegister extends FeignApiRegister { // 扫描的接口路�?

	public Feign.Builder getFeignBuilder(Class<? extends Encoder> encoderClass
			,Class<? extends Decoder> decoderClass
			,Class<? extends Client> clientClass
			,long maxPeriod
			,int maxAttempts
			,Map<String, Collection<String>> headerMap) {
		Feign.Builder builder = null;
		try {
			builder = Feign.builder()
					.encoder(encoderClass.getDeclaredConstructor().newInstance())
					.client(clientClass.getDeclaredConstructor(SSLSocketFactory.class,HostnameVerifier.class).newInstance(null,null))
					.decoder(decoderClass.getDeclaredConstructor().newInstance())
					.requestInterceptor(template->template.headers(headerMap))
					.options(new Request.Options(1000, 3500)).retryer(new Retryer.Default(5000, maxPeriod, maxAttempts));
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return builder;
	}
	
	/**
	 * 重新载入此feign
	 * @param feignClass
	 * @param feiApi
	 * @param headers
	 */
	public void refreshFeign(Class<?> feignClass,FeignApi feiApi,Map<String, Collection<String>> headerMap) {
		FeignApi originFeign = feignClass.getAnnotation(FeignApi.class);
		if(originFeign!=null) {
			FeignApi newFeign = originFeign;
			if(feiApi!=null) {
				newFeign = feiApi;
				String url = newFeign.host();
				if(!StringUtils.isEmpty(url)) {
					if(!(url.startsWith("http://")||url.startsWith("https://"))) {
						url = "http://"+url;
					}
					Feign.Builder builder = getFeignBuilder(newFeign.encoder()
							,newFeign.decoder()
							,newFeign.client()
							,newFeign.maxPeriod()
							,newFeign.maxAttempts()
							,headerMap);
					(((ConfigurableApplicationContext)ApplicationContextUtil.getContext()).getBeanFactory()).registerSingleton(feignClass.getName(), builder.target(feignClass, url));
				}
			}
		}
		
	}
}
