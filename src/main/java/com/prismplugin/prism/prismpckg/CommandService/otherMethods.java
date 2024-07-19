package com.prismplugin.prism.prismpckg.CommandService;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Bukkit.getServer;

public class otherMethods {
    private final Map<String, commandInterface> commandMap = new HashMap<>();
    public void loadCommandsOld() {
        Yaml yaml = new Yaml();
        try {
            File file = new File("src/main/resources/plugin.yml");
            Map<String, Object> data = yaml.load(new FileInputStream(file));
            if (data.containsKey("commands")) {
                Map<String, Map<String, Object>> commands = (Map<String, Map<String, Object>>) data.get("commands");
                for (String commandName : commands.keySet()) {
                    getServer().getConsoleSender().sendMessage(Color.GREEN + commandName);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadCmds(JavaPlugin plugin) {
        Map<String, Map<String, Object>> commands = plugin.getDescription().getCommands();
        for (String commandName : commands.keySet()) {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + commandName);
        }
    }

    public void displayAllCommandClasses(JavaPlugin plugin) {
        Map<String, Map<String, Object>> commands = plugin.getDescription().getCommands();
        if (!commands.isEmpty()) {
            for (Map.Entry<String, Map<String, Object>> entry : commands.entrySet()) {
                String commandName = entry.getKey();
                Map<String, Object> commandData = entry.getValue();
                Object commandClass = commandData.get("class");
                if (commandClass != null) {
                    System.out.println(commandName + " : " + commandClass);
                }
            }
        } else {
            System.out.println("No commands found.");
        }
    }
    String className = "com.prismplugin.prism.prismpckg.CommandService.commands.player.PlayerHelp";


    public void loadCommands(JavaPlugin plugin) {

        Map<String, Map<String, Object>> commands = plugin.getDescription().getCommands();
        if (!commands.isEmpty()) {
            for (Map.Entry<String, Map<String, Object>> entry : commands.entrySet()) {
                String commandName = entry.getKey();
                Map<String, Object> commandData = entry.getValue();
                Object commandClass = commandData.get("class");
                if (commandClass != null) {
                    try {
                        Class<?> clazz = Class.forName((String) commandClass);
                        if (commandInterface.class.isAssignableFrom(clazz)) {
                            commandInterface instance = (commandInterface) clazz.getDeclaredConstructor().newInstance();
                            this.commandMap.put(commandName, instance);
                            getServer().getConsoleSender().sendMessage("Added command '" + commandName + "' to the command map.");
                        } else {
                            getServer().getConsoleSender().sendMessage("Class does not implement the commandInterface: " + commandClass);
                        }
                    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                             NoSuchMethodException | java.lang.reflect.InvocationTargetException e) {
                        // Handle any exceptions here
                        e.printStackTrace();
                    }
                }
            }
            getServer().getConsoleSender().sendMessage("Command map 2 contents: " + commandMap);
        } else {
            getServer().getConsoleSender().sendMessage("No commands found.");
        }

    }
    /*
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return;
        }

        Player player = (Player) sender;

        if (args.length != 6) {
            player.sendMessage("Usage: /set <x1> <y1> <z1> <x2> <y2> <z2>");
            return;
        }

        try {
            int x1 = Integer.parseInt(args[0]);
            int y1 = Integer.parseInt(args[1]);
            int z1 = Integer.parseInt(args[2]);
            int x2 = Integer.parseInt(args[3]);
            int y2 = Integer.parseInt(args[4]);
            int z2 = Integer.parseInt(args[5]);

            Location loc1 = new Location(player.getWorld(), x1, y1, z1);
            Location loc2 = new Location(player.getWorld(), x2, y2, z2);
            Chunk chunk = loc1.getChunk();


            // Assuming you have the setBlocksBetweenPoints method defined in the same class
            setBlocksBetweenPoints(loc1, loc2, Material.REDSTONE_BLOCK);

            player.sendMessage("Blocks between the specified points have been set.");
        } catch (NumberFormatException e) {
            player.sendMessage("Please enter valid numbers for the coordinates.");
        }
    }

     */
    public void setBlocksBetweenPoints(Location loc1, Location loc2, Material material) {
        World world = loc1.getWorld();
        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    block.setType(material);
                }
            }
        }
    }
}

/*
    private void bfs(Block startBlock, Set<Block> blocks, Player player) {
        Queue<Block> queue = new LinkedList<>();
        Set<Block> visitedBlocks = new HashSet<>();
        World world = startBlock.getWorld();

        connectedBlocks.add(startBlock);
        queue.offer(startBlock);
        visitedBlocks.add(startBlock);

        while (!queue.isEmpty()) {
            Block currentBlock = queue.poll();

            int blockX = currentBlock.getX();
            int blockZ = currentBlock.getZ();

            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {                          //<--- bfs is complete i guess
                    if (dx == 0 && dz == 0) {
                        continue; // Skip the current block
                    }

                    int nextX = blockX + dx;
                    int nextZ = blockZ + dz;

                    Block nextBlock = world.getBlockAt(nextX, currentBlock.getY(), nextZ);

                    if (blocks.contains(nextBlock) && visitedBlocks.add(nextBlock)) {
                        connectedBlocks.add(nextBlock);
                        queue.offer(nextBlock);
                        savedChunks.add(nextBlock.getChunk());
                    }
                }
            }
        }
    }
     */




    /*
    private Block getBlockUnderPlayer(Player player) {
        Location playerLocation = player.getLocation();
        World world = playerLocation.getWorld();
        int playerX = playerLocation.getBlockX();
        int playerY = playerLocation.getBlockY();

        // Iterate vertically from the player's position downwards
        for (; playerY > 0; playerY--) {
            Block block = world.getBlockAt(playerX, playerY, playerLocation.getBlockZ());

            if (block.getType() != Material.AIR) {
                return block;
            }
        }

        player.sendMessage("No solid block found");
        return null; // Or return a default block if needed
    }
*/

/*
private void bfs(Block startBlock, List<Block> blocks, Player player) {
        Queue<Block> queue = new LinkedList<>();
        Set<Block> visitedBlocks = new HashSet<>();

        queue.offer(startBlock);
        visitedBlocks.add(startBlock);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!queue.isEmpty()) {
                    Block currentBlock = queue.poll();

                    int blockX = currentBlock.getX();
                    int blockZ = currentBlock.getZ();

                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dz = -1; dz <= 1; dz++) {
                            if (dx == 0 && dz == 0) {
                                continue; // Skip the current block
                            }

                            int nextX = blockX + dx;
                            int nextZ = blockZ + dz;

                            Block nextBlock = currentBlock.getWorld().getBlockAt(nextX, currentBlock.getY(), nextZ);

                            if (blocks.contains(nextBlock) && !visitedBlocks.contains(nextBlock)) {
                                visitedBlocks.add(nextBlock);
                                queue.offer(nextBlock);
                                connectedBlocks.add(nextBlock);
                                nextBlock.setType(Material.DIAMOND_BLOCK);
                            }
                        }
                    }
                } else {
                    // The BFS is completed, cancel the task
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 0);
    }
 */



/*
final int maxRadius = 20; // Set a reasonable maximum radius to prevent infinite loops

        boolean stopSearching = false;

        // Process blocks in the start chunk
        for (Block block : getBlocksInChunkAtY(startChunk, targetY)) {
            if (block.getBiome() == targetBlock.getBiome()) {
                targetBlocks.add(block);
            }
        }

        for (int radius = 1; radius <= maxRadius; radius++) {
            boolean anyBiomeBlocksFound = false;

            // Iterate through chunks in a chess grid pattern
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (Math.abs(x) == radius || Math.abs(z) == radius) { // Chunks on the edges of the square
                        Chunk nextChunk = targetWorld.getChunkAt(startChunk.getX() + x, startChunk.getZ() + z);
                        boolean chunkHasBiomeBlocks = false;

                        for (Block block : getBlocksInChunkAtY(nextChunk, targetY)) {
                            if (block.getBiome() == targetBlock.getBiome()) {
                                chunkHasBiomeBlocks = true;
                                anyBiomeBlocksFound = true;
                                targetBlocks.add(block);
                            }
                        }

                        if (!chunkHasBiomeBlocks) {
                            // Continue searching even if the chunk has no biome blocks
                        }
                    }
                }
            }

            if (!anyBiomeBlocksFound) {
                stopSearching = true;
                break; // No biome blocks found at this radius, end the search
            }
        }

        if (stopSearching) {
            // End of the search, carry on with further processing
        }



        JavaPlugin plugin = Main.getPlugin(Main.class);
    File customYml = new File(plugin.getDataFolder()+"/regions.yml");
    FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);

    public void saveCustomYml(FileConfiguration ymlConfig, File ymlFile) {
        try {
            ymlConfig.save(ymlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 */

