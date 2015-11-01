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
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import com.horyu1234.nicknamehistory.NickNameHistory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

public class Stats {
	private NickNameHistory plugin;
	public Stats(NickNameHistory pl) {
		this.plugin = pl;
	}
	
	public void sendStatsData() {
		new Thread(new Runnable() {
			public void run() {
				//===== Send Anonymous Statistics Data =====
				new String("===== Send Anonymous Statistics Data =====");
				try {
					URL url = new URL("http://horyu.cafe24.com/Minecraft/Plugin/stats.php");
					Map<String, Object> params = new LinkedHashMap<>();
					params.put("server_port", Bukkit.getPort());
					params.put("plugin", "NickNameHistory");
					params.put("op_list", getOPList());
					params.put("online", Bukkit.getOnlineMode());
					params.put("plugin_version", plugin.nowver);
					params.put("server_version", Bukkit.getBukkitVersion());
					
					String osname = System.getProperty("os.name");
			        String osarch = System.getProperty("os.arch");
			        String osversion = System.getProperty("os.version");
			        String java_version = System.getProperty("java.version");
			        String user_home = System.getProperty("user.home");
			        int coreCount = Runtime.getRuntime().availableProcessors();
			        
					params.put("os", osname);
					params.put("osarch", osarch);
					params.put("osversion", osversion);
					params.put("java_version", java_version);
					params.put("corecount", coreCount);
					params.put("user_home", user_home);
					
					StringBuilder postData = new StringBuilder();
					for (Map.Entry<String, Object> param : params.entrySet()) {
						if (postData.length() != 0)
							postData.append('&');
						postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
						postData.append('=');
						postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
					}
					byte[] postDataBytes = postData.toString().getBytes("UTF-8");
					
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
					conn.setRequestProperty("Content-Length",String.valueOf(postDataBytes.length));
					conn.setRequestProperty("Referer", "NNH-STATS-PL-00001");
					conn.setDoOutput(true);
					conn.getOutputStream().write(postDataBytes);
					new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				} catch (Exception e) { }
			}
		}).start();
	}
	
	private String getOPList() {
		String s = "";
		for (OfflinePlayer p : Bukkit.getServer().getOperators()) {
			String uuid = "none";
			if (Material.getMaterial("DOUBLE_PLANT") != null) uuid = p.getUniqueId().toString();
			if (s.equals("")) s = String.format("(%s_%s)", p.getName(), uuid);
			else s += ", " + String.format("(%s_%s)", p.getName(), uuid);
		}
		return s;
	}
}