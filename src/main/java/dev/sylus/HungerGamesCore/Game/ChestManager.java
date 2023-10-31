package dev.sylus.HungerGamesCore.Game;

import dev.sylus.HungerGamesCore.Enums.GameState;
import dev.sylus.HungerGamesCore.Files.Files;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
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

    Game game;
    ConfigurationSection itemsSection;
    FileConfiguration lootConfig;
    String chestName;
    Files files;
    Set<String> keys;

    public ChestManager(Game gameInstance, Files filesInstance){
        game = gameInstance;
        files = filesInstance;
    }

    @EventHandler
    private void onChestOpen(InventoryOpenEvent event){
        InventoryHolder holder = event.getInventory().getHolder();

        if (holder instanceof Chest){
            Chest chest = (Chest) holder;
            if (hasBeenOpened(chest.getLocation())) {
                return;
            }
            chestName = chest.getCustomName();
            Bukkit.getLogger().log(Level.WARNING, chestName);
            if (chestName == null){
                Bukkit.getLogger().log(Level.SEVERE, "Chest name is null");
                chestName = "COMMON";
            }
            chestName = chestName.replaceAll("§[a-f0-9]", "");
            chestName = chestName.replace(" chest", "");

            if (game.getState() == GameState.gameState.SECONDHALF){
                itemsSection = files.getConfig("worldData.yml").getConfigurationSection("secondHalfItems." + chestName); // Using worldData allows for maps to have different loot tables
            } else {
                itemsSection = files.getConfig("worldData.yml").getConfigurationSection("firstHalfItems." + chestName);
            }
            Bukkit.getLogger().log(Level.WARNING, String.valueOf(itemsSection));

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
                itemMeta.setUnbreakable(true);
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
        openedChests.clear();

        for (Player players: Bukkit.getOnlinePlayers()){
            players.sendTitle("§cChests refilled", "", 10, 20, 10);
            players.playSound(players.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
        }

    }
}