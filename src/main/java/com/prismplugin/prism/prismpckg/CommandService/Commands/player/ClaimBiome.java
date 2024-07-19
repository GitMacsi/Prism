package com.prismplugin.prism.prismpckg.CommandService.Commands.player;

import com.prismplugin.prism.prismpckg.CommandService.commandInterface;
import com.prismplugin.prism.prismpckg.Utils.RegionStorageUtil;
import com.prismplugin.prism.prismpckg.Models.SubCommand;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.*;

public class ClaimBiome implements commandInterface {

    private static Set<Block> targetBlocks = new HashSet<>();
    private static Set<Block> connectedBlocks = new HashSet<>();
    private static Set<Chunk> savedChunks = new HashSet<>();
    private static Set<Block> exceptionBlocks = new HashSet<>();

    private static final int MAX_SEARCH_RADIUS = 10;

    public void execute(CommandSender sender, String[] args, String label) {
        displayHelpMessage((Player) sender);
    }

    void displayHelpMessage(Player player){
        player.sendMessage("Usage: xxxxxxxx x xxxxxxxxx x xxxxxxxx");
    }

    public static void create(Player player){
        Block targetBlock = player.getLocation().getBlock();
        int targetY = targetBlock.getY();

        collectBlocks(player, targetBlock, targetY, MAX_SEARCH_RADIUS);
        bfs(targetBlock, targetBlocks);
        saveAll(savedChunks, connectedBlocks, exceptionBlocks, targetY, player,targetBlock);
        flush();
    }

    public static void delete(Player player){ //placeholder
        Block targetBlock = player.getLocation().getBlock();
        int targetY = targetBlock.getY();

        collectBlocks(player, targetBlock, targetY, MAX_SEARCH_RADIUS);
        bfs(targetBlock, targetBlocks);
        saveAll(savedChunks, connectedBlocks, exceptionBlocks, targetY, player,targetBlock);
        flush();
    }


    private static void collectBlocks(Player player, Block targetBlock, int targetY, int maxRadius) {
        World targetWorld = player.getWorld();
        Chunk startChunk = player.getChunk();


        // Process blocks in the start chunk
        for (Block block : getBlocksInChunkAtY(startChunk, targetY)) {
            if (block.getBiome() == targetBlock.getBiome()) {
                targetBlocks.add(block);
            }
        }

        for (int radius = 1; radius <= maxRadius; radius++) {
            if (radius == maxRadius) {
                break;
            }
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
                break; // No biome blocks found at this radius, end the search
            }
        }
    }
    private static void bfs(Block startBlock, Set<Block> blocks) { //Breadth First Search
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


    private static void saveAll(Set<Chunk> savedChunks, Set<Block> connectedBlocks, Set<Block> exceptionBlocks, int targetY, Player player, Block targetBlock) {
        if(RegionStorageUtil.getRegionOfChunk(targetBlock.getChunk().getChunkKey()) != null && !RegionStorageUtil.getRegionChunkFromChunk(targetBlock.getChunk().getChunkKey()).getExceptions().contains(getBlockKeyOf(targetBlock))){
            player.sendMessage("This region is already claimed.");
            return;
        }
        if (RegionStorageUtil.alreadyOwnsThatKindOfBiome(targetBlock.getBiome().toString(),player.getName())){
            player.sendMessage("You already own a region of the " + targetBlock.getBiome().toString() + " biome." );
            return;
        }
        Set<Long> chunks = new HashSet<>();
        Set<Block> exceptions = new HashSet<>();
        String UniqueID = UUID.randomUUID().toString();
        HashMap<String,String> members = new HashMap();
        members.put(player.getName(),"owner");

        savedChunks.forEach(chunk -> {
            exceptionBlocks.addAll(getBlocksInChunkAtY(chunk, targetY));
        });
        exceptionBlocks.removeAll(connectedBlocks);
        exceptionBlocks.forEach(block -> {exceptions.add(block);});

        for(Chunk currentChunk : savedChunks){
            Set<Long> exceptionsOfCurrentChunk = new HashSet<>();
            for (Block currentBlock : exceptions){
                if (currentBlock.getChunk().getChunkKey() == currentChunk.getChunkKey()){
                    exceptionsOfCurrentChunk.add(getBlockKeyOf(currentBlock));
                    player.sendMessage(String.valueOf(getBlockKeyOf(currentBlock)));
                }
            }
            RegionStorageUtil.createRegionChunk(UniqueID,currentChunk.getChunkKey(),exceptionsOfCurrentChunk);
            chunks.add(currentChunk.getChunkKey());
        }

        RegionStorageUtil.createRegion(UniqueID,player.getName(),chunks,members,targetBlock.getBiome().toString(),targetBlock.getWorld().getName());
        RegionStorageUtil.saveRegionChunks();
        RegionStorageUtil.saveRegions();

        /*

         */



    }


    private static Set<Block> getBlocksInChunkAtY(Chunk chunk, int yLevel) {
        Set<Block> blocks = new HashSet<>();
        World world = chunk.getWorld();
        int chunkX = chunk.getX() << 4; // Same as chunk.getX() * 16
        int chunkZ = chunk.getZ() << 4; // Same as chunk.getZ() * 16                    <--- more useful than expected

        for (int x = chunkX; x < chunkX + 16; x++) {
            for (int z = chunkZ; z < chunkZ + 16; z++) {
                Block block = world.getBlockAt(x, yLevel, z);
                blocks.add(block);
            }
        }

        return blocks;
    }

    public static void flush() {
        savedChunks.clear();
        targetBlocks.clear();
        connectedBlocks.clear();
        exceptionBlocks.clear();
    }

    @Override
    public boolean canExecute(CommandSender sender) {
        return sender instanceof Player;                //<--- tell the executor the targets of the command
        //also make sure to revise the command executor and interface if you forgot
    }

    public static long getBlockKeyOf(Block block) {
        int x = block.getX();
        int z = block.getZ();
        long packedCoordinate = ((long) x & 0x7FFFFFF) | (((long) z & 0x7FFFFFF) << 27);
        return packedCoordinate;
    }

    @Override
    public String getCommandName() {
        return "claimbiome";
    }

    @Override
    public void executeSubcommand(SubCommand subc, Player player) {
        String[] dummy = new String[1];
        subc.perform((CommandSender) player,dummy);
    }
}

