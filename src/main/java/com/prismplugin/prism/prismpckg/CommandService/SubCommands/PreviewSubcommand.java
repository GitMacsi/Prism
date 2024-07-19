package com.prismplugin.prism.prismpckg.CommandService.SubCommands;

import com.prismplugin.prism.prismpckg.CommandService.Commands.player.ClaimBiome;
import com.prismplugin.prism.prismpckg.CommandService.commandInterface;
import com.prismplugin.prism.prismpckg.Models.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PreviewSubcommand extends SubCommand {


    @Override
    public String getName() {
        return "preview";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }

    public Class<? extends commandInterface>getTargetClass(){
        return ClaimBiome.class;
    }
}