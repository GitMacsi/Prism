package com.prismplugin.prism.prismpckg.CommandService;


import com.prismplugin.prism.prismpckg.Models.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface commandInterface {
    void execute(CommandSender sender, String[] args, String label);
    boolean canExecute(CommandSender sender);
    String getCommandName();
     void executeSubcommand(SubCommand subc, Player player);
}
