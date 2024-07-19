package com.prismplugin.prism.prismpckg.Models;

import com.prismplugin.prism.prismpckg.CommandService.commandInterface;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class SubCommand {

    public abstract String getName();


    public abstract List<String> getAliases();


    public abstract String getDescription();


    public abstract String getSyntax();


    public abstract void perform(CommandSender sender, String[] args);


    public abstract List<String> getSubcommandArguments(Player player, String[] args);

    public abstract Class<? extends commandInterface>getTargetClass();

}
