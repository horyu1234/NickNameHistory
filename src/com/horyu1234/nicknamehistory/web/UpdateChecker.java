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