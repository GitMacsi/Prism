package com.prismplugin.prism.prismpckg.EventService.Events.player;

import com.prismplugin.prism.prismpckg.Utils.RegionStorageUtil;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import static com.prismplugin.prism.prismpckg.Utils.RegionStorageUtil.getRegionChunkFromChunk;
import static com.prismplugin.prism.prismpckg.Utils.RegionStorageUtil.getRegionFromId;

public class PlayerBlockBreak implements Listener {


    @EventHandler
    public void event(BlockBreakEvent event){
    if (getRegionChunkFromChunk(event.getBlock().getChunk().getChunkKey()) != null &&
            !getRegionChunkFromChunk(event.getBlock().getChunk().getChunkKey()).getExceptions().contains(getBlockKeyOf(event.getBlock())) &&
            !getRegionFromId(getRegionChunkFromChunk(event.getBlock().getChunk().getChunkKey()).getChunk_id()).getOwner().equalsIgnoreCase(event.getPlayer().getName()))
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You are not allowed to break blocks in this region");
        }
    }

    public static long getBlockKeyOf(Block block) {
        int x = block.getX();
        int z = block.getZ();
        long packedCoordinate = ((long) x & 0x7FFFFFF) | (((long) z & 0x7FFFFFF) << 27);
        return packedCoordinate;
    }
}
