package com.horyu1234.nicknamehistory.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DisableChecker
{
	public static boolean isDisable(String name)
	{
		String plugin_name = "@"+name+"@";
		try
		{
			URL url = new URL("https://raw.githubusercontent.com/horyu1234/Checker/master/Disable");
			HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
			urlConn.setDoOutput(true);
			BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			String inputLine;
			while ((inputLine = br.readLine()) != null)
			{
				if (inputLine.contains(plugin_name))
				{
					String data = inputLine.split(plugin_name)[1];
					if (data.contains("false"))
					{
						return false;
					}
				}
			}
			br.close();
		}
		catch (Exception e)
		{
			System.out.println("DisableChecker Exception: " + e.toString());
		}
		return true;
	}
	
	public static String getReason(String name)
	{
		String plugin_name = "@"+name+"@";
		try
		{
			URL url = new URL("https://github.com/horyu1234/Checker/blob/master/Disable");
			HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
			urlConn.setDoOutput(true);
			BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF8"));
			String inputLine;
			while ((inputLine = br.readLine()) != null)
			{
				if (inputLine.contains(plugin_name))
				{
					String data = inputLine.split(plugin_name)[1];
					if (data.contains("true^"))
					{
						return data.replace("true\\^", "").replaceAll("_", " ");
					}
				}
			}
			br.close();
		}
		catch (Exception e)
		{
			System.out.println("DisableChecker Exception: " + e.toString());
		}
		return null;
	}
}