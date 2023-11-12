package dev.sylus.HungerGamesCore.Events;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

public class EnchantingTable implements Listener {

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event){
        if(event.getInventory().getType().equals(InventoryType.ENCHANTING)){
            EnchantingInventory inv = (EnchantingInventory) event.getInventory();
            Player player = (Player) event.getPlayer();

            ItemStack i = new ItemStack(Material.LAPIS_LAZULI);
            i.setAmount(64);
            inv.setItem(1, i);
            player.updateInventory();
        }
    }
    @EventHandler
    public void onClickEvent(InventoryClickEvent event){
        if(event.getInventory().getType() == InventoryType.ENCHANTING){
            if(event.getCurrentItem().getType().equals(Material.LAPIS_LAZULI)){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        if (event.getInventory().getType().equals(InventoryType.ENCHANTING)){
            EnchantingInventory inv = (EnchantingInventory) event.getInventory();
            Player player = (Player) event.getPlayer();

            ItemStack i = new ItemStack(Material.AIR);
            i.setAmount(64);
            inv.setItem(1, i);
            player.updateInventory();
        }
    }

}
