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

import com.horyu1234.nicknamehistory.NickNameHistory;
import com.horyu1234.nicknamehistory.json.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Scanner;

public class MojangAPIConnector
{
	public NickNameHistory plugin;
	
	public MojangAPIConnector(NickNameHistory pl)
	{
		plugin = pl;
	}
	
	public boolean getNicknameHistoryFromMojang(String url, CommandSender s)
	{
		s.sendMessage("§a=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		Object obj = getJSON(url);
		if (obj == null)
		{
			s.sendMessage("§cJSON 을 가져오는 중 오류가 발생했습니다!");
			s.sendMessage("§eNickNameHistory v1.3 §bBy §6horyu1234");
			s.sendMessage("§a=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			return false;
		}
		String jsontxt = obj.toString();
		if (jsontxt.contains("\",\""))
		{
			s.sendMessage("§6닉네임 §f| §6바꾼날짜");
			//닉네임 바꿈
			for (String str : jsontxt.split("\\},\\{"))
			{
				String sstr;
				str = str.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\{", "").replaceAll("\\}", "");
				sstr = "{"+str+"}";
				try
				{
					JSONObject json = new JSONObject(sstr);
					if (json.has("changedToAt"))
					{
						long t = json.getLong("changedToAt");
						SimpleDateFormat simpleDate = new SimpleDateFormat(plugin.getConfig().getString("DateFormat"), Locale.KOREA);
						s.sendMessage("§e"+json.getString("name")+" §f| §e"+simpleDate.format(t));
					}
					else
						s.sendMessage("§e"+json.getString("name"));
				}
				catch (Exception e)
				{
					s.sendMessage("§cJSON을 변환하는 중 오류가 발생했습니다!");
					s.sendMessage("§eNickNameHistory v1.3 §bBy §6horyu1234");
					s.sendMessage("§a=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
					return false;
				}
			}
			s.sendMessage("");
			s.sendMessage("§3시간은 한국시간 기준입니다.");
		}
		else
			s.sendMessage("§c해당 플레이어는 닉네임을 바꾼적이 없습니다.");
		s.sendMessage("§eNickNameHistory v1.3 §bBy §6horyu1234");
		s.sendMessage("§a=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		return true;
	}
	
	public String getUniqueIdFromMojang(String url)
	{
		Object obj = getJSON(url);
		if (obj == null)
		{
			Bukkit.broadcastMessage("§cJSON 을 가져오는 중 오류가 발생했습니다!");
			return null;
		}
		
		String jsontxt = obj.toString();
		try
		{
			JSONObject json = new JSONObject(jsontxt);
			return json.getString("id");
		}
		catch (Exception e)
		{
			Bukkit.broadcastMessage(plugin.prefix+"§cJSON을 변환하는 중 오류가 발생했습니다!");
		}
		return null;
	}
	
	public Object getJSON(String url_s)
	{
		try
		{
			URL url = new URL(url_s);
			URLConnection connection = url.openConnection();
			Scanner jsonscanner = new Scanner(connection.getInputStream(), "UTF-8");
			String json = jsonscanner.next();
			JSONParser parser = new JSONParser();
			jsonscanner.close();
			return parser.parse(json);
		}
		catch (Exception e)
		{
			Bukkit.broadcastMessage(plugin.prefix+"§cJSON 을 가져오는데 문제가 발생했습니다!");
		}
		return null;
	}
	
	public boolean hasPaid(String url_s)
	{
		try
		{
			URL url = new URL(url_s);
			HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
			urlConn.setDoOutput(true);
			BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
			String inputLine;
			while ((inputLine = br.readLine()) != null)
			{
				return inputLine.contains("true") ? true : false;
			}
			br.close();
		}
		catch (Exception e)
		{
			System.out.println("WebConnect Exception: " + e.toString());
		}
		return false;
	}
}