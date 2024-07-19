package com.prismplugin.prism.prismpckg.Utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prismplugin.prism.prismpckg.Main;
import com.prismplugin.prism.prismpckg.Models.Region;
import com.prismplugin.prism.prismpckg.Models.RegionChunk;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class RegionStorageUtil {
    public static Set<Region> regions = loadRegions(); //Store regions in memory on startup
    public static Set<RegionChunk> regionChunks = loadRegionChunks();

    public static Set<Long> getAllChunks(){
        Set<Long> allChunks = new HashSet<>();
        for(RegionChunk chunk : regionChunks){
            allChunks.add(chunk.getChunk());
        }
        return allChunks;
    }

    public static RegionChunk getRegionChunkFromChunk(Long chunk){
        for(RegionChunk currentRegionChunk : regionChunks){
            if (currentRegionChunk.getChunk() == chunk){
                return currentRegionChunk;
            }
        }
        return null;
    }

    public static Region createRegion(String id, String owner, Set<Long> chunks, HashMap<String, String> members,String biome, String world){
        Region newRegion = new Region(id,owner,chunks,members,biome,world);
        regions.add(newRegion);
        return newRegion;
    }

    public static RegionChunk createRegionChunk(String region_id,Long chunk, Set<Long> exceptions){
        RegionChunk newRegionChunk = new RegionChunk(chunk,region_id,exceptions);
        regionChunks.add(newRegionChunk);
        return newRegionChunk;
    }

    public static void deleteAllChunksOfRegion(String id){
        Region regionToBeDeleted = getRegionFromId(id);
        Set<RegionChunk> chunksToBeDeleted = new HashSet<>();
        for (RegionChunk regionChunk : regionChunks){
            if(regionChunk.getChunk_id().equalsIgnoreCase(id)){
                chunksToBeDeleted.add(regionChunk);
            }
        }
        regionChunks.removeAll(chunksToBeDeleted);
    }

    public static boolean hasSimilarRegion(String id){
        Region compareRegion = getRegionFromId(id);
        for(Region currentRegion : regions){
            if (!compareRegion.getId().equalsIgnoreCase(currentRegion.getId())){
            if (compareRegion.getBiome().equalsIgnoreCase(currentRegion.getBiome()) &&
                    compareRegion.getOwner().equalsIgnoreCase(currentRegion.getOwner()) &&
                    compareRegion.getWorld().equalsIgnoreCase(currentRegion.getWorld())){
                return true; // If a similar region is found, return true
                }
            }
        }
        return false; // If no similar regions are found, return false
    }

    public static boolean alreadyOwnsThatKindOfBiome(String biome,String name){
        for(Region currentRegion : regions){
            if(currentRegion.getBiome().equalsIgnoreCase(biome) && currentRegion.getOwner().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }



    public static void deleteRegion(String id){
        Region regionToBeDeleted = getRegionFromId(id);
        regions.remove(regionToBeDeleted);
        deleteAllChunksOfRegion(id);
    }

    public static void updateRegion(String id, Region newRegion) {
        Region regionToBeUpdated = getRegionFromId(id);
        regionToBeUpdated.setOwner(newRegion.getOwner());
        regionToBeUpdated.setMembers(newRegion.getMembers());
        regionToBeUpdated.setChunks(newRegion.getChunks());
    }

    public static void saveRegions(){
        Gson gson = new Gson();
        File file = new File(Main.getPlugin().getDataFolder().getAbsolutePath() + "/regions.json");
        try {
            Writer writer = new FileWriter(file, false);
            gson.toJson(regions, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveRegionChunks(){
        Gson gson = new Gson();
        File file = new File(Main.getPlugin().getDataFolder().getAbsolutePath() + "/regionchunks.json");
        try {
            Writer writer = new FileWriter(file, false);
            gson.toJson(regionChunks, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


   public static Region getRegionFromId(String id){
       for(Region currentRegion : regions){
           if (currentRegion.getId().equalsIgnoreCase(id)){
               return currentRegion;
           }
       }
       return null;
   }

   public static ArrayList<Region> getRegionsOfOwner(String owner){
       ArrayList<Region> regionsOfOwner = new ArrayList<Region>();
       for(Region currentRegion : regions){
           if (currentRegion.getOwner().equalsIgnoreCase(owner)){
               regionsOfOwner.add(currentRegion);
           }
       }
       if (regionsOfOwner.isEmpty()){return null;}else{return regionsOfOwner;}
   }

    public static ArrayList<Region> getRegionsOfPlayer(String name){
       ArrayList regionsOfPlayer = new ArrayList<Region>();
        for(Region currentRegion : regions){
            if (currentRegion.getMembers().containsKey(name)){
                regionsOfPlayer.add(currentRegion);
            }
        }
        if (regionsOfPlayer.isEmpty()){return null;}else{return regionsOfPlayer;}
    }

    public static Region getRegionOfChunk(Long chunk){
        for(Region currentRegion : regions){
            if (currentRegion.getChunks().contains(chunk)){
                return currentRegion;
            }
        }
        return null;
    }

    //load

    public static HashSet<Region> loadRegions() {
        Gson gson = new Gson();
        File file = new File(Main.getPlugin().getDataFolder().getAbsolutePath() + "/regions.json");
        if (!file.exists()) {
            file.getParentFile().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new HashSet<>();
        }
        try {
            Reader reader = new FileReader(file);
            Type type = new TypeToken<HashSet<Region>>() {}.getType();
            HashSet<Region> loadedRegions = gson.fromJson(reader, type);
            return loadedRegions != null ? loadedRegions : new HashSet<>();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }

    public static HashSet<RegionChunk> loadRegionChunks() {
        Gson gson = new Gson();
        File file = new File(Main.getPlugin().getDataFolder().getAbsolutePath() + "/regionchunks.json");
        if (!file.exists()) {
            file.getParentFile().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new HashSet<>();
        }
        try {
            Reader reader = new FileReader(file);
            Type type = new TypeToken<HashSet<RegionChunk>>() {}.getType();
            HashSet<RegionChunk> loadedRegionChunks = gson.fromJson(reader, type);
            return loadedRegionChunks != null ? loadedRegionChunks : new HashSet<>();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }
}
