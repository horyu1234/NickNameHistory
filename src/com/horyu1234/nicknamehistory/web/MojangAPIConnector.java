package com.horyu1234.nicknamehistory.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Scanner;

import com.horyu1234.nicknamehistory.NickNameHistory;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.json.simple.parser.JSONParser;

import com.horyu1234.nicknamehistory.json.JSONObject;

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
						SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy년MM월dd일_hh시mm분ss초",Locale.KOREA);
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