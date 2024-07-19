package com.prismplugin.prism.prismpckg.CommandService.Commands.player;

import com.prismplugin.prism.prismpckg.CommandService.commandInterface;
import com.prismplugin.prism.prismpckg.Models.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class PlayerHelp implements commandInterface {
    @Override
    public void execute(CommandSender sender, String[] args, String label) {
            Player player = (Player) sender;
            player.sendMessage(String.valueOf(player.getBodyYaw()));
    }

    @Override
    public boolean canExecute(CommandSender sender) {
        return sender instanceof Player;
    }

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public void executeSubcommand(SubCommand subc, Player player) {

    }


}