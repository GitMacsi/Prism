package com.prismplugin.prism.prismpckg.CommandService.Commands.player;

import com.prismplugin.prism.prismpckg.CommandService.commandInterface;
import com.prismplugin.prism.prismpckg.Models.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerSelfKill implements commandInterface{

    @Override
    public void execute(CommandSender sender, String[] args, String label) {
        Player p = (Player)sender;
        p.setHealth(Integer.parseInt(args[0])); //args test

    }

    @Override
    public boolean canExecute(CommandSender sender) {
        return sender instanceof Player;
    }

    @Override
    public String getCommandName() {
        return "selfkill";
    }

    @Override
    public void executeSubcommand(SubCommand subc, Player player) {

    }


}
