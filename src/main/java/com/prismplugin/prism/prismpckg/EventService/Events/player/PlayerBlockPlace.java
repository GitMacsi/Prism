package com.prismplugin.prism.prismpckg.EventService.Events.player;

import com.prismplugin.prism.prismpckg.Models.Region;
import com.prismplugin.prism.prismpckg.Utils.RegionStorageUtil;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Set;

public class PlayerBlockPlace implements Listener {

    @EventHandler
    public void event(BlockPlaceEvent event){

    }

    public static long getBlockKeyOf(Block block) {
        int x = block.getX();
        int z = block.getZ();
        long packedCoordinate = ((long) x & 0x7FFFFFF) | (((long) z & 0x7FFFFFF) << 27);
        return packedCoordinate;
    }
}

