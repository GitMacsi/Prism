package com.prismplugin.prism.prismpckg.CommandService;

import com.prismplugin.prism.prismpckg.Models.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class commandExecutor implements org.bukkit.command.CommandExecutor, TabCompleter {

    public static final Map<String, commandInterface> commandMap = new HashMap<>();
    public static final Map<SubCommand, Class<? extends commandInterface>> subcommandMap = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        String commandName = command.getName();

        // Execute the command based on the command name
        if (commandMap.containsKey(commandName)) {
            commandInterface commandInterface = commandMap.get(commandName);
            if (commandInterface.canExecute(sender)) {
                if (args.length > 0) {
                    SubCommand subCommand = getSubCommandFromName(args[0]);
                    if (subCommand != null && commandMap.get(commandName).getClass().getName().equalsIgnoreCase(subCommand.getTargetClass().getName())) {
                        try {
                            subCommand.getTargetClass().newInstance().executeSubcommand(subCommand, (Player) sender);
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } else {
                        sender.sendMessage("Subcommand not recognized");
                    }
                } else {
                    commandInterface.execute(sender, args, label);
                }

            } else {
                sender.sendMessage("You do not have permission to execute this command.");
            }
        } else {
            sender.sendMessage("Command not recognized");
        }
        return true;
    }


    public SubCommand getSubCommandFromName(String name) {
        for (Map.Entry<SubCommand, Class<? extends commandInterface>> entry : subcommandMap.entrySet()) {
            if (name.equalsIgnoreCase(entry.getKey().getName())) {
                return entry.getKey();
            }
        }
        return null;
    }

    //Tab Complete

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String commandName = command.getName();
        List<String>  returnArgs = new ArrayList<>();
        if(args.length == 1){
            for (Map.Entry<SubCommand, Class<? extends commandInterface>> entry : subcommandMap.entrySet()) {
                try {
                    if (entry.getKey().getTargetClass().getDeclaredConstructor().newInstance().getCommandName().equalsIgnoreCase(commandName)) {
                      returnArgs.add(entry.getKey().getName());
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {

                }
            }
            return returnArgs;
        }
        return null;
    }
}





