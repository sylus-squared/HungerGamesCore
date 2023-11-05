package dev.sylus.HungerGamesCore.Utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ServerUtil {
    public ServerUtil(){

    }

    public void sendPlayerToServer(ProxiedPlayer player, String serverName){
        ServerInfo targetServer = ProxyServer.getInstance().getServerInfo(serverName);
        if (targetServer != null){
            player.connect(targetServer);
        } else {
            player.sendMessage(ChatColor.RED + "The requested server: " + serverName + " does not exist or is offline");
        }
    }
}
