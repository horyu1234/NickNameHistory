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
import org.bukkit.command.CommandSender;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker implements Runnable
{
	private NickNameHistory plugin;
	private CommandSender sender;
	private String url_str;

	public UpdateChecker(final NickNameHistory plugin, final CommandSender sender) {
		this.plugin = plugin;
		this.sender = sender;
		this.url_str = "http://minecraft.horyu.me/minecraft/" + plugin.getName() + "/version";
		new Thread(this).start();
	}

	public void run() {
		try {
			URL url = new URL(url_str);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; ko) HandGiveAll/" + plugin.getDescription().getVersion() + " (Made By horyu1234)");
			httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpURLConnection.setRequestProperty("Content-Language", "ko-KR");
			httpURLConnection.setDoOutput(true);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF8"));

			String msg;
			while ((msg = bufferedReader.readLine()) != null) {
				double plugin_version = Double.parseDouble(msg);
				bufferedReader.close();

				if (plugin_version > plugin.nowver) {
					sender.sendMessage(plugin.prefix + "§b#==============================#");
					sender.sendMessage(plugin.prefix + "§f플러그인의 새로운 업데이트가 발견되었습니다!");
					sender.sendMessage(plugin.prefix + "§c현재버전: " + plugin.nowver);
					sender.sendMessage(plugin.prefix + "§a새로운버전: " + plugin_version);
					sender.sendMessage(plugin.prefix + "§e플러그인 다운로드 링크: https://horyu1234.com/NickNameHistory");
					sender.sendMessage(plugin.prefix + "§b#==============================#");
					new Thread(new Runnable() {
						public void run() {
							JOptionPane.showMessageDialog(null, "플러그인의 새로운 업데이트가 발견되었습니다!", "HandGiveAll v" + plugin.nowver, JOptionPane.INFORMATION_MESSAGE);
						}
					}).start();
				} else if (plugin_version == plugin.nowver) {
					sender.sendMessage(plugin.prefix + "§f새로운 버전이 없습니다.");
				} else {
					sender.sendMessage(plugin.prefix + "§f#==============================#");
					sender.sendMessage(plugin.prefix + "§c플러그인의 버전을 확인하는데 문제가 발생했습니다.");
					sender.sendMessage(plugin.prefix + "§f#==============================#");
				}
				return;
			}
			bufferedReader.close();
		}
		catch (Exception exception) {
			plugin.sendConsole("§c업데이트를 확인하는 중 문제가 발생했습니다.");
			plugin.sendConsole("§c메시지: " + exception.getMessage());
		}
	}
}