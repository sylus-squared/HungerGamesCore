package dev.sylus.HungerGamesCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class GiveMachineTrident implements CommandExecutor {

    public GiveMachineTrident(){

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "Only players can use this command");
            Bukkit.getLogger().log(Level.WARNING, "Non player tried to execute a player only command");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Trying to give a trident");
        ItemStack trident = new ItemStack(Material.TRIDENT);
        ItemMeta meta = trident.getItemMeta();
        meta.setDisplayName("§cMACHINE GUNDENT");
        List<String> lore = new ArrayList<>();
        lore.add("§7A trident that has the power");
        lore.add("§7of 4 tridents per second :)");
        lore.add("§f");
        lore.add("§e§lHOLD RIGHT CLICK§r§f: shoots a LOT of tridents");
        lore.add("§cADMIN WEAPON");

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setLore(lore);
        trident.setItemMeta(meta);
        trident.addUnsafeEnchantment(Enchantment.PIERCING, 100);
        ((Player) sender).getInventory().addItem(trident);
        return true;
    }
}
