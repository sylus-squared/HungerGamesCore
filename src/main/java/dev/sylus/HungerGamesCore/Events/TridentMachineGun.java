package dev.sylus.HungerGamesCore.Events;

import dev.sylus.HungerGamesCore.HungerGamesCore;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class TridentMachineGun extends BukkitRunnable implements Listener {
    Player player;
    HungerGamesCore main;
    boolean isRunning = false;

    public TridentMachineGun(HungerGamesCore mainInstance) {
        main = mainInstance;
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction().toString().contains("RIGHT") &&
                player.getInventory().getItemInMainHand().getType() == Material.TRIDENT &&
                player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("§cMACHINE GUNDENT")) {

            if (!isRunning) {
                isRunning = true;
                shootTridents(player);
            }
        }
    }

    private void shootTridents(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isHandRaised()) {
                    Location location = player.getLocation();
                    Vector direction = location.getDirection();
                    ItemStack trident = new ItemStack(Material.TRIDENT);
                    trident.addUnsafeEnchantment(Enchantment.LOYALTY, 1);

                    Trident tridentEntity = (Trident) player.launchProjectile(Trident.class);
                    player.playSound(player.getLocation(), Sound.ITEM_TRIDENT_THROW, 1, 1);
                    tridentEntity.setVelocity(direction.multiply(2.0));
                } else {
                    isRunning = false;
                    this.cancel();  // This cancels the task
                }
            }
        }.runTaskTimer(main, 0, 5);
    }

    @Override
    public void run() {

    }
}