package dev.sylus.HungerGamesCore.Tasks;

import dev.sylus.HungerGamesCore.Enums.GameState;
import dev.sylus.HungerGamesCore.Game.Game;
import dev.sylus.HungerGamesCore.HungerGamesCore;
import dev.sylus.HungerGamesCore.Utils.ServerUtil;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class EndingTimer extends BukkitRunnable {
    Game game;
    int timer = 20;
    HungerGamesCore main;
    Player player;
    World world;
    Location location;
    Boolean isDraw;
    ServerUtil serverUtil;

    public EndingTimer(Game gameInstance, HungerGamesCore mainInstance, Boolean isDrawInstance, ServerUtil serverUtilInstacne){
        game = gameInstance;
        main = mainInstance;
        isDraw = isDrawInstance;
        serverUtil = serverUtilInstacne;
    }


    @Override
    public void run() {
        if (!(game.getState() == GameState.gameState.ENDING)){
            Bukkit.getLogger().log(Level.WARNING, "End game timer called outside of end game");
            this.cancel();
            return;
        }

        if (timer == 0){
            this.cancel();
            // Send the players back to the lobby server, will need bungeecord for this
            for (Player players: Bukkit.getOnlinePlayers()){
                serverUtil.sendPlayerToServer(players, "lobby");
            }
            return;
        }
        timer--;

        player = game.getLastPlayer();

        world = player.getWorld();
        location = player.getLocation();

        Firework firework = (Firework) world.spawn(location, Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        FireworkEffect.Builder builder = FireworkEffect.builder();

        fireworkMeta.addEffect(builder.flicker(true).withColor(Color.PURPLE, Color.WHITE).build());
        fireworkMeta.addEffect(builder.trail(false).build());
        //  fireworkMeta.addEffect(builder.withFade(Color.ORANGE).build()); scrapped :(
        fireworkMeta.addEffect(builder.with(FireworkEffect.Type.BALL).build());
        fireworkMeta.setPower(1);
        firework.setFireworkMeta(fireworkMeta);

        firework = (Firework) world.spawn(location, Firework.class);
        fireworkMeta = firework.getFireworkMeta();

        fireworkMeta.addEffect(builder.flicker(true).withColor(Color.RED).build());
        fireworkMeta.addEffect(builder.trail(true).build());
        fireworkMeta.addEffect(builder.with(FireworkEffect.Type.BALL_LARGE).build());
        fireworkMeta.setPower(5);
        firework.setFireworkMeta(fireworkMeta);

        firework = (Firework) world.spawn(location, Firework.class);
        fireworkMeta = firework.getFireworkMeta();

        fireworkMeta.addEffect(builder.flicker(true).withColor(Color.RED).build());
        fireworkMeta.addEffect(builder.trail(true).build());
        fireworkMeta.addEffect(builder.with(FireworkEffect.Type.BALL_LARGE).build());
        fireworkMeta.setPower(5);
        firework.setFireworkMeta(fireworkMeta);
        }


}
