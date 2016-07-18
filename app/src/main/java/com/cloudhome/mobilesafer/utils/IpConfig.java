package com.cloudhome.mobilesafer.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class IpConfig {
	

	public static Properties pro = new Properties();

	 static {
		
		InputStream in = IpConfig.class.getClassLoader().getResourceAsStream(
				"config.properties");
		try {
			pro.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getIp() {
	
		return pro.getProperty("ip");
	}


}
