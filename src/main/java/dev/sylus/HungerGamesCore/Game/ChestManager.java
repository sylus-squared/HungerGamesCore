package dev.sylus.HungerGamesCore.Game;

import dev.sylus.HungerGamesCore.Enums.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ChestManager implements Listener {
    private final Set<Location> openedChests = new HashSet<>();
    private final List<LootItem> lootItems = new ArrayList<>();

    Game game;
    ConfigurationSection itemsSection;
    FileConfiguration lootConfig;

    public ChestManager(FileConfiguration lootConfigInstance, Game gameInstance){
        game = gameInstance;
        lootConfig = lootConfigInstance;

        if (game.getState() == GameState.gameState.SECONDHALF){
            itemsSection = lootConfig.getConfigurationSection("secondHalfLootItems");
        } else {
            itemsSection = lootConfig.getConfigurationSection("firstHalfLootItems");
        }

        if (itemsSection == null){
            Bukkit.getLogger().severe("lootItems does not exist in the config.yml");
        }

        for (String key : itemsSection.getKeys(false)){
            ConfigurationSection section = itemsSection.getConfigurationSection(key);
            lootItems.add(new LootItem(section));
        }
    }

    @EventHandler
    private void  onChestOpen(InventoryOpenEvent event){
        InventoryHolder holder = event.getInventory().getHolder();

        if (holder instanceof Chest){
            Chest chest = (Chest) holder;
            if (hasBeenOpened(chest.getLocation())) {
                return;
            }

        /*    if (Objects.equals(NBTEditor.getString(holder, "chestKey", "value"), "EGGTHROW")){
                if (game.getState() == GameState.gameState.SECONDHALF){
                    itemsSection = lootConfig.getConfigurationSection("secondHalfLootItems.COMMON");
                }
            }

         */

            markAsOpened(chest.getLocation());
            fill(chest.getBlockInventory());
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