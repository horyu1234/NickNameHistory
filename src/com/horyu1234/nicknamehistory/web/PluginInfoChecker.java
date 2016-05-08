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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by horyu on 2016-05-08.
 */
public class PluginInfoChecker {
    public PluginInfo getInfo(NickNameHistory plugin) {
        PluginInfo versionInfo = new PluginInfo();
        String url_str = "http://minecraft.horyu.me/minecraft/" + plugin.getName() + "/" + plugin.getDescription().getVersion();

        try {
            URL url = new URL(url_str);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; ko) NickNameHistory/" + plugin.getDescription().getVersion() + " (Made By horyu1234)");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Content-Language", "ko-KR");
            httpURLConnection.setDoOutput(true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF8"));

            boolean start = false;
            String msg;
            while ((msg = bufferedReader.readLine()) != null) {
                if (msg.contains("start")) start = true;
                if (start) {
                    if (msg.startsWith("disable:"))
                        versionInfo.disable = Boolean.parseBoolean(msg.split("\"")[1].split("\"")[0]);
                    else if (msg.startsWith("disable_message:"))
                        versionInfo.disable_message = msg.split("\"")[1].split("\"")[0];
                    else if (msg.startsWith("md5:")) versionInfo.md5 = msg.split("\"")[1].split("\"")[0];
                    else if (msg.startsWith("notice:")) versionInfo.notices.add(msg.split("\"")[1].split("\"")[0]);
                    else if (msg.startsWith("notice_date:")) versionInfo.notice_date = msg.split("\"")[1].split("\"")[0];
                    else if (msg.equalsIgnoreCase("end")) {
                        bufferedReader.close();
                        return versionInfo;
                    }
                }
            }
            bufferedReader.close();
        }
        catch (Exception exception) {
            plugin.sendConsole("§c버전에 대한 정보를 가져오는 중 문제가 발생했습니다.");
            plugin.sendConsole("§c메시지: " + exception.toString());
        }
        return versionInfo;
    }

    public class PluginInfo {
        private boolean disable;
        private String disable_message;
        private String md5;
        private List<String> notices;
        private String notice_date;

        public PluginInfo() {
            this.disable = true;
            this.disable_message = "서버와 통신에 실패했습니다.";
            this.md5 = "없음";
            this.notices = new ArrayList<String>();
            this.notice_date = "없음";
        }

        public boolean isDisable() {
            return disable;
        }
        public String getDisable_message() {
            return disable_message;
        }
        public String getMd5() {
            return md5;
        }
        public List<String> getNotices() {
            return notices;
        }
        public String getNotice_date() {
            return notice_date;
        }

        public void setDisable(boolean disable) {
            this.disable = disable;
        }
        public void setDisable_message(String disable_message) {
            this.disable_message = disable_message;
        }
        public void setMd5(String md5) {
            this.md5 = md5;
        }
        public void setNotices(List<String> notices) {
            this.notices = notices;
        }
        public void setNotice_date(String notice_date) {
            this.notice_date = notice_date;
        }
    }
}