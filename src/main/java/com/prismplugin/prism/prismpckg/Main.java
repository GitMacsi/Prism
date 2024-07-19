package com.prismplugin.prism.prismpckg;

import com.prismplugin.prism.prismpckg.CommandService.commandExecutor;
import com.prismplugin.prism.prismpckg.CommandService.commandInterface;

import com.prismplugin.prism.prismpckg.Models.SubCommand;
import com.prismplugin.prism.prismpckg.Utils.RegionStorageUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public final class Main extends JavaPlugin {

    private static Main plugin;

    public static Main getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        RegionStorageUtil.loadRegions();
        RegionStorageUtil.loadRegionChunks();

        registerEvents();
        registerCommands();
        registerSubCommands();
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "Plugin started");
    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }



    private void registerEvents() {
        // Get all classes in the classpath implementing CustomEvent
        Reflections reflections = new Reflections("com.prismplugin.prism.prismpckg.EventService.Events");
        Set<Class<? extends Listener>> eventClasses = reflections.getSubTypesOf(Listener.class);
        // Register each event class
        for (Class<? extends Listener> eventClass : eventClasses) {
            try {
                Listener listener = (Listener) eventClass.getDeclaredConstructor().newInstance();
                getServer().getPluginManager().registerEvents(listener, this);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerCommands() {
        Reflections reflections = new Reflections("com.prismplugin.prism.prismpckg.CommandService.Commands");
        Set<Class<? extends commandInterface>> commandClasses = reflections.getSubTypesOf(commandInterface.class);
        commandClasses.forEach(commandClass -> {
            try {
                commandInterface commandInstance = commandClass.getDeclaredConstructor().newInstance();
                commandExecutor.commandMap.put(commandInstance.getCommandName(), commandInstance);
                getCommand(commandInstance.getCommandName()).setExecutor(new commandExecutor());
                getCommand(commandInstance.getCommandName()).setTabCompleter(new commandExecutor());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        });


    }

    private void registerSubCommands(){
        Reflections reflections = new Reflections("com.prismplugin.prism.prismpckg.CommandService.SubCommands");
        Set<Class<? extends SubCommand>> subCommandClasses = reflections.getSubTypesOf(SubCommand.class);
        Set<SubCommand> subCommands = new HashSet<>();
        for (Class<? extends SubCommand> subCommandClass : subCommandClasses) {
            try {
                SubCommand subCommand = subCommandClass.getDeclaredConstructor().newInstance();
                subCommands.add(subCommand);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(SubCommand subCommand : subCommands){
            commandExecutor.subcommandMap.put(subCommand,subCommand.getTargetClass());
        }
    }
}

