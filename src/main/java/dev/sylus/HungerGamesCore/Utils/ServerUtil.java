package dev.sylus.HungerGamesCore.Utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.sylus.HungerGamesCore.HungerGamesCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ServerUtil {
    HungerGamesCore main;
    public ServerUtil(HungerGamesCore mainInstance){
        main = mainInstance;
    }

    public void sendPlayerToServer(Player player, String serverName){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        // Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null); to do it for all players
        player.sendPluginMessage(main, "BungeeCord", out.toByteArray());
    }
}
