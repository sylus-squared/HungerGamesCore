package dev.sylus.HungerGamesCore.Game;

import dev.sylus.HungerGamesCore.Files.Files;
import dev.sylus.HungerGamesCore.HungerGamesCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

public class ChestManager implements Listener {
    private final Set<Location> openedChests = new HashSet<>();
    private final List<LootItem> lootItems = new ArrayList<>();

    ConfigurationSection itemsSection;
    String chestName;
    Files files;
    Set<String> keys;
    HungerGamesCore main;

    public ChestManager(Files filesInstance, HungerGamesCore hungerGamesCoreInstance){
        files = filesInstance;
        main = hungerGamesCoreInstance;
    }

    @EventHandler
    private void onChestOpen(InventoryOpenEvent event){
        InventoryHolder holder = event.getInventory().getHolder();

        if (holder instanceof Chest){
            Chest chest = (Chest) holder;
            if (hasBeenOpened(chest.getLocation())) {
                return;
            }
            if (!(main.getCanOpenChests())){
                event.setCancelled(true);
                return;
            }

            chestName = chest.getCustomName();
            if (chestName == null || chestName.equals("chest")){
                chestName = "COMMON";
            }
            chestName = chestName.replaceAll("§[a-f0-9]", "");
            chestName = chestName.replace(" chest", "");

            itemsSection = files.getConfig("worldData.yml").getConfigurationSection("firstHalfItems." + chestName);

            if (itemsSection != null){
                keys = itemsSection.getKeys(false);
                for (String key : keys){
                    ConfigurationSection section = itemsSection.getConfigurationSection(key);
                    lootItems.add(new LootItem(section));
                }

                markAsOpened(chest.getLocation());
                fill(chest.getBlockInventory());
            } else {
                Bukkit.getLogger().log(Level.SEVERE, "No keys found in section " + chestName);
            }

        } else if (holder instanceof DoubleChest) {
            DoubleChest chest = (DoubleChest) holder;
            if (hasBeenOpened(chest.getLocation())) {
                return;
            }

            markAsOpened(chest.getLocation());
            fill(chest.getInventory());
        }
    }

    public void fill(Inventory inventory){
        inventory.clear();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Set<LootItem> used = new HashSet<>();

        for (int slotIndex = 0; slotIndex < inventory.getSize(); slotIndex++){
            LootItem randomItem = lootItems.get(random.nextInt(lootItems.size()));

            if (used.contains(randomItem)) {
                continue;
            }
            used.add(randomItem);
            if (randomItem.shouldFill(random)){
                ItemStack itemStack = randomItem.make(random);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                if (itemStack.equals(Material.SHIELD)){
                    Damageable damageable = (Damageable) itemMeta;
                    damageable.damage(333);
                    itemStack.setItemMeta((ItemMeta) damageable);
                } else{
                    itemMeta.setUnbreakable(true);
                }
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(slotIndex, itemStack);
            }
        }
    }

    public void markAsOpened(Location location){
        openedChests.add(location);
    }

    public boolean hasBeenOpened(Location location){
        return openedChests.contains(location);
    }

    public void resetChests(){
       this.openedChests.clear();

        for (Player players: Bukkit.getOnlinePlayers()){
            players.sendTitle("§cChests refilled", "", 10, 20, 10);
            players.playSound(players.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
        }
    }
}