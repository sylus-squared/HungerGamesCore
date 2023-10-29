package dev.sylus.HungerGamesCore.Commands;

import dev.sylus.HungerGamesCore.Enums.ChestRarity;
import dev.sylus.HungerGamesCore.Enums.GameState;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class GiveChest implements TabCompleter, CommandExecutor {

    public GiveChest(){

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)){
            Bukkit.getLogger().log(Level.SEVERE, "Non player tried to execute a player only command");
            return true;
        }
        Player player = (Player) sender;

        if (args == null || args[0] == null){
            sender.sendMessage(ChatColor.RED + "Please specify a rarity");
            return true;
        }

        try {
            ItemStack item = new ItemStack(Material.CHEST);
            ItemMeta metta = item.getItemMeta();
            metta.setDisplayName(ChatColor.DARK_PURPLE + args[0] + " chest");
            item.setItemMeta(metta);
            NBTEditor.set(item, args[0], "chestKey");

            player.getInventory().addItem(item);
        } catch (IllegalArgumentException exception){
            sender.sendMessage(ChatColor.RED + exception.toString());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> suggestions = new ArrayList<>();

        // Add argument suggestions here
        if (args.length == 1) {
            suggestions.add(String.valueOf(ChestRarity.COMMON));
            suggestions.add(String.valueOf(ChestRarity.RARE));
            suggestions.add(String.valueOf(ChestRarity.EPIC));
        }
        return suggestions;
    }
}
