/*
 * Copyright (c) 2015 HoryuSystems All rights reserved.
 *
 * 본 저작물의 모든 저작권은 HoryuSystems 에 있습니다.
 *
 * 소스를 참고하여 다른 프로그램을 제작하는 것은 허용되지만,
 * 프로그램의 접두사, 기능등의 수정 및 배포는 불가능합니다.
 *
 * 소스에 대한 피드백등은 언제나 환영합니다! 아래는 개발자 연락처입니다.
 *
 * Skype: horyu1234
 * KakaoTalk: horyu1234
 * Telegram: @horyu1234
 */

package com.horyu1234.nicknamehistory.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker
{
	public static double getVersion(String name)
	{
		String plugin_name = "@"+name+"@";
		try
		{
			URL url = new URL("https://raw.githubusercontent.com/horyu1234/Checker/master/Update");
			HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
			urlConn.setDoOutput(true);
			BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF8"));
			String inputLine;
			while ((inputLine = br.readLine()) != null)
			{
				if (inputLine.contains(plugin_name))
				{
					String version = inputLine.split(plugin_name)[1];
					return Double.parseDouble(version);
				}
			}
			br.close();
		}
		catch (Exception e)
		{
			System.out.println("UpdateChecker Exception: " + e.toString());
		}
		return 0.0D;
	}
}