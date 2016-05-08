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

package com.horyu1234.nicknamehistory;

/**
 * @category Minecraft Plugin
 * @author horyu1234 (https://horyu1234.com)
 * @version 1.4
 */

import com.horyu1234.nicknamehistory.web.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.*;
import java.io.File;

public class NickNameHistory extends JavaPlugin implements Listener
{
    public String prefix = "§6[§eNickNameHistory v1.4§6] §r";
    private MojangAPIConnector mojangapi;
    private String url1 = "https://api.mojang.com/user/profiles/";
    private String url2 = "https://api.mojang.com/users/profiles/minecraft/";
    private String url3 = "https://api.mojang.com/users/profiles/minecraft/";
    public double nowver = 1.4;
    private double newver;
    private PluginInfoChecker.PluginInfo pluginInfo;

    public void onDisable()
    {
        sendConsole("§a플러그인이 비활성화 되었습니다.");
        sendConsole("§a플러그인제작자: horyu1234");
    }
    public void onEnable()
    {
        Blacklist.init();
        Stats stats = new Stats(this);
        stats.sendStatsData();
        mojangapi = new MojangAPIConnector(this);
        getServer().getPluginManager().registerEvents(this, this);

        new UpdateChecker(this, Bukkit.getConsoleSender());
        PluginInfoChecker pluginInfoChecker = new PluginInfoChecker();
        pluginInfo = pluginInfoChecker.getInfo(this);

        if (!new File(getDataFolder(), "config.yml").exists())
        {
            sendConsole("§cCan't find config.yml");
            sendConsole("§cCreating...");
            saveDefaultConfig();
            sendConsole("§aComplete, reloaded config.yml");
            reloadConfig();
        }
        if (!getConfig().getBoolean("EULA"))
        {
            sendConsole("§f#==============================#");
            sendConsole("§cConfig 에서 플러그인의 EULA에 동의해주세요!!");
            sendConsole("§cEULA를 동의하지 않으시면 플러그인의 사용이 불가능합니다.");
            sendConsole("§f#==============================#");
            new Thread(new Runnable() {
                public void run() {
                    JOptionPane.showMessageDialog(null, "Config 에서 플러그인의 EULA 에 동의해주시기 바랍니다.", "NickNameHistory v1.4", JOptionPane.ERROR_MESSAGE);
                }
            }).start();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (checkDisable()) return;

        if (pluginInfo.getNotices().size() > 0)
        {
            sendConsole("§e=====§b§l[ §aNickNameHistory 공지 §b§l]§e=====");
            for (String notice : pluginInfo.getNotices())
                sendConsole(notice);
            sendConsole("§e===========================");
        }
        sendConsole("§a플러그인이 활성화 되었습니다.");
        sendConsole("§a플러그인제작자: horyu1234");
    }
    public void sendConsole(String msg)
    {
        Bukkit.getConsoleSender().sendMessage(prefix+msg);
    }

    public boolean onCommand(final CommandSender s, Command c, String l, final String[] a)
    {
        try
        {
            if (l.equalsIgnoreCase("nhn")) {
                if (pluginInfo.getNotices().size() > 0) {
                    s.sendMessage(prefix + "§e=====§b§l[ §aNickNameHistory 공지 §b§l]§e=====");
                    for (String notice : pluginInfo.getNotices())
                        s.sendMessage(prefix + notice);
                    s.sendMessage(prefix + "§e===========================");
                } else s.sendMessage(prefix + "§f공지 사항이 없습니다.");
            } else if (l.equalsIgnoreCase("nh")) {
                if (a[0].equalsIgnoreCase("name"))
                {
                    if (s.isOp() || s.hasPermission("nicknamehistory.name"))
                    {
                        if (a.length != 2) {
                            s.sendMessage(prefix+"§c닉네임을 입력해주세요.");
                            return false;
                        }

                        s.sendMessage("§e모장서버와 통신중입니다...");
                        new Thread(new Runnable() {
                            public void run() {
                                if (mojangapi.hasPaid(url3+a[1])) {
                                    String uuid = mojangapi.getUniqueIdFromMojang(url2+a[1]);
                                    s.sendMessage(String.format("§e%s(%s) §a님의 닉네임기록을 조회합니다...", a[1], uuid));
                                    mojangapi.getNicknameHistoryFromMojang(url1+uuid+"/names", s);
                                }
                                else s.sendMessage(prefix+"§c입력하신 닉네임은 정품이용자가 아닙니다.");
                            }
                        }).start();
                        return true;
                    }
                    else
                    {
                        s.sendMessage(prefix+"§c권한이 없습니다. [nicknamehistory.name]");
                        return false;
                    }
                }
                else if (a[0].equalsIgnoreCase("uuid"))
                {
                    if (s.isOp() || s.hasPermission("nicknamehistory.uuid"))
                    {
                        if (a.length != 2) {
                            s.sendMessage(prefix+"§cUUID를 입력해주세요.");
                            return false;
                        }

                        s.sendMessage("§e모장서버와 통신중입니다...");
                        new Thread(new Runnable() {
                            public void run() {
                                String uuid = getNUUID(a[1]);
                                s.sendMessage(String.format("§e%s §a님의 닉네임기록을 조회합니다...", uuid));
                                mojangapi.getNicknameHistoryFromMojang(url1+uuid+"/names", s);
                            }
                        }).start();
                        return true;
                    }
                    else
                    {
                        s.sendMessage(prefix+"§c권한이 없습니다. [nicknamehistory.uuid]");
                        return false;
                    }
                }
                else
                {
                    showHelp(s);
                    return false;
                }
            }
        }
        catch (Exception e)
        {
            showHelp(s);
            return false;
        }
        return true;
    }

    public void showHelp(CommandSender s)
    {
        s.sendMessage("§9=-=-=-=-= §3[NickNameHistory v1.4] §a사용법 (1/1) §9=-=-=-=-=");
        s.sendMessage("§6모든정보는 모장서버와 통신을 통해 받아옴으로, 서버에 접속한적이 없어도 조회가 가능합니다.");
        s.sendMessage("§a/nh name <닉네임> §f<닉네임>의 닉네임변경 기록을 봅니다.");
        s.sendMessage("§a/nh uuid <uuid> §f<UUID>의 닉네임변경 기록을 봅니다.");
        s.sendMessage("§a/nhn §fNickNameHistory 공지를 확인합니다.");
        s.sendMessage("");
        s.sendMessage("§a플러그인제작: horyu1234");
        s.sendMessage("§9=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
    }

    public String getNUUID(String uuid)
    {
        if (uuid.contains("-"))
            return uuid.replaceAll("-", "");
        else
            return uuid;
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e)
    {
        if (Blacklist.contains(e.getPlayer())) {
            e.setJoinMessage(null);
            Blacklist.kick(e.getPlayer());
            return;
        }

        new UpdateChecker(this, e.getPlayer());

        if (pluginInfo.getNotices().size() > 0)
        {
            getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                public void run() {
                    if (pluginInfo.getNotice_date().equals("없음")) {
                        e.getPlayer().sendMessage(prefix + "§f등록된 공지 사항이 있습니다. 확인하시려면 §a/nhn §f를 입력해주세요.");
                    } else {
                        e.getPlayer().sendMessage(prefix + "§e" + pluginInfo.getNotice_date() + "§f에 등록된 공지 사항이 있습니다. 확인하시려면 §a/nhn §f를 입력해주세요.");
                    }
                }
            }, 200L);
        }
    }

    public boolean checkDisable() {
        if (pluginInfo.isDisable()) {
            sendConsole("§c#==============================#");
            sendConsole("본 플러그인의 제작자가 플러그인의 구동을 비활성화하여");
            sendConsole("플러그인 구동이 제한됩니다.");
            sendConsole("");
            sendConsole("§4사유: ");
            sendConsole("  \"" + pluginInfo.getDisable_message() + "\"");
            sendConsole("§c#==============================#");
            new Thread(new Runnable() {
                public void run() {
                    JOptionPane.showMessageDialog(null, "본 플러그인의 제작자가 플러그인의 구동을 비활성화하여\n플러그인의 구동이 제한됩니다.", "NickNameHistory v" + nowver, JOptionPane.ERROR_MESSAGE);
                }
            }).start();
            getServer().getPluginManager().disablePlugin(this);
            return true;
        }
        return false;
    }
}