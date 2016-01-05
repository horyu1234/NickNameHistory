/*
 * Copyright (c) 2014~2016 HoryuSystems All rights reserved.
 *
 * 본 저작물의 모든 저작권은 HoryuSystems 에 있습니다.
 *
 * 소스를 참고하여 다른 프로그램을 제작하는 것은 허용되지만,
 * 프로그램의 접두사, 기능등의 수정 및 배포는 불가능합니다.
 *
 * 기능을 거의 똑같이 하여 제작하는 행위등은 '참고하여 다른 프로그램을 제작한다는 것' 에 해당하지 않습니다.
 *
 * ============================================
 * 본 소스를 참고하여 프로그램을 제작할 시 해당 프로그램에 본 소스의 출처/라이센스를 공식적으로 안내를 해야 합니다.
 * 출처: https://github.com/horyu1234
 * 라이센스: Copyright (c) 2014~2016 HoryuSystems All rights reserved.
 * ============================================
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
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Notice
{
	public static List<String> Notices;
	
	public static void getNotices()
	{
		Notices = new ArrayList<String>();
		try
		{
			URL url = new URL("https://raw.githubusercontent.com/horyu1234/Checker/master/Notice/NickNameHistory");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), Charset.forName("UTF-8")));
			String str;
			while ((str = br.readLine()) != null)
			{
				if (str.contains("%notice%"))
				{
					Notices.add(str.replaceAll("%notice%", ""));
				}
			}
			br.close();
		}
		catch (Exception e)
		{
			System.out.println("[NickNameHistory] Failed to get notices list!");
		}
	}
}