package io.github.joao100101.ameizintags.commands;

import io.github.joao100101.ameizintags.AmeizinTags;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Comandos implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("§cEsse comando deve ser usado apenas in-game.");
            return true;
        }

        Player p = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("tags") || alias.equalsIgnoreCase("tag")){
            AmeizinTags.getTagMenu().openInventory(p);
        }

        return false;
    }
}
