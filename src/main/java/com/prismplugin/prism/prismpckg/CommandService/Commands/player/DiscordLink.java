package com.prismplugin.prism.prismpckg.CommandService.Commands.player;

import com.prismplugin.prism.prismpckg.CommandService.commandInterface;
import com.prismplugin.prism.prismpckg.Models.SubCommand;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.awt.*;

import static org.bukkit.Bukkit.getServer;
public class DiscordLink implements commandInterface {
    @Override
    public void execute(CommandSender sender, String[] args, String label) {
       getServer().getConsoleSender().sendMessage(Color.GREEN + "discord");

    }

    @Override
    public boolean canExecute(CommandSender sender) {
        return sender instanceof ConsoleCommandSender || sender instanceof Player;

    }

    @Override
    public String getCommandName() {
        return "discord";
    }

    public void executeSubcommand(SubCommand subc, Player player) {
        String[] dummy = new String[1];
        subc.perform(player,dummy);
    }

}