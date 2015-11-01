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