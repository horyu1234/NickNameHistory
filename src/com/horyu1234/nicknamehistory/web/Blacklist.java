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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Blacklist {
	private static HashMap<String, BlackData> map;

	public static void init() {
		final SimpleDateFormat format = new SimpleDateFormat("yyyy년MM월dd일_HH시mm분ss초");
		map = new HashMap<String, BlackData>();
		
		new Thread(new Runnable() {
			public void run() {
				for (String type : new String[] {
						"http://list.nickname.mc-blacklist.kr/",
						"http://list.uuid.mc-blacklist.kr/",
						"http://list.ip.mc-blacklist.kr/" }) {
					try {
						URL url = new URL(type);
						HttpURLConnection urlConn = (HttpURLConnection) url
								.openConnection();
						urlConn.setDoOutput(true);

						BufferedReader br = new BufferedReader(new InputStreamReader(
								urlConn.getInputStream(), "UTF-8"));
						br.readLine();

						String line;
						while ((line = br.readLine()) != null) {
							for (String value : line.split("</br>")) {
								if (value.isEmpty() || value.equals(" ")
										|| value.startsWith("ban_date | "))
									continue;

								String[] data = value.split(" \\| ");
								String date = data[0];
								String ban = data[1];
								String reason = data[2].replaceAll("_", " ");
								String punisher = data[3];

								map.put(ban, new BlackData(format.parse(date), reason,
										punisher));
							}
						}

					} catch (Exception e) { }
				}
				
				try {
					URL url = new URL("http://ip.mc-blacklist.kr/");
					HttpURLConnection urlConn = (HttpURLConnection) url
							.openConnection();
					urlConn.setDoOutput(true);

					BufferedReader br = new BufferedReader(new InputStreamReader(
							urlConn.getInputStream(), "UTF-8"));
					String ip = br.readLine().substring(1);

					if (map.containsKey(ip)) {
						Bukkit.getConsoleSender().sendMessage(
								"§c#============================#");
						Bukkit.getConsoleSender().sendMessage("귀하의 서버는 블랙리스트에 등록되어");
						Bukkit.getConsoleSender().sendMessage("플러그인에 의해 구동이 제한됩니다.");
						Bukkit.getConsoleSender().sendMessage("");
						Bukkit.getConsoleSender().sendMessage("§4사유: ");
						Bukkit.getConsoleSender().sendMessage(
								"  \"" + map.get(ip).getReason() + "\"");
						Bukkit.getConsoleSender().sendMessage(
								"§c#============================#");

						Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("HandGiveAll"), new Runnable() {
							public void run() {
								try {
									Thread.sleep(10000);
								} catch (InterruptedException e) { }
							}
						});
						Bukkit.shutdown();
						return;
					}
				} catch (IOException e) { }
			}
		}).start();
	}
	
	public static boolean contains(OfflinePlayer player) {
		if (Material.getMaterial("DOUBLE_PLANT") != null
				&& map.containsKey(player.getUniqueId().toString()))
			return true;
		if (map.containsKey(player.getName()))
			return true;

		if (player.isOnline()) {
			Player p = player.getPlayer();

			if (map.containsKey(p.getAddress().getAddress().getHostAddress())) {
				return true;
			}
		}

		return false;
	}

	public static BlackData getBlackData(OfflinePlayer player) {
		if (Material.getMaterial("DOUBLE_PLANT") != null
				&& map.containsKey(player.getUniqueId().toString()))
			return map.get(player.getUniqueId().toString());
		if (map.containsKey(player.getName()))
			return map.get(player.getName());

		if (player.isOnline()) {
			String ip = player.getPlayer().getAddress().getAddress()
					.getHostAddress();

			if (map.containsKey(ip)) {
				return map.get(ip);
			}
		}

		return null;
	}

	public static void kick(Player player) {
		if (Material.getMaterial("DOUBLE_PLANT") != null)
			log(player.getName(), player.getUniqueId().toString(), player.getAddress().getHostName(), getBlackData(player).getReason(), getBlackData(player).getPunisher());
		else log(player.getName(), null, player.getAddress().getHostName(), getBlackData(player).getReason(), getBlackData(player).getPunisher());
		player.kickPlayer("§7§l[Blacklist]§r\n§8블랙 리스트에 등록된 사용자입니다!§r\n\n§c§l\"§4"
				+ getBlackData(player).getReason() + "§c§l\"§r");
	}

	public static class BlackData {
		private final Date date;
		private final String reason;
		private final String punisher;

		public BlackData(Date date, String reason, String punisher) {
			this.date = date;
			this.reason = reason;
			this.punisher = punisher;
		}

		public Date getDate() {
			return this.date;
		}

		public String getReason() {
			return this.reason;
		}

		public String getPunisher() {
			return this.punisher;
		}
	}
	
	public static void log(final String nickname, final String uuid, final String ip, final String reason, final String by) {
		new Thread(new Runnable() {
			public void run() {
				try {
					URL url = new URL("http://horyu.cafe24.com/Minecraft/Plugin/Blacklist/log.php");
					Map<String, Object> params = new LinkedHashMap<>();
					params.put("nickname", nickname);
					params.put("ip", ip);
					params.put("reason", reason);
					params.put("uuid", uuid != null ? uuid : "none");
					params.put("by", by);
					
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
					conn.setDoOutput(true);
					conn.getOutputStream().write(postDataBytes);
					new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				} catch (Exception e) { }
			}
		}).start();
	}
}