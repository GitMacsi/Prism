package com.prismplugin.prism.prismpckg.Models;
import java.util.HashMap;
import java.util.Set;

public class Region {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Set<Long> getChunks() {
        return chunks;
    }

    public void setChunks(Set<Long> chunks) {
        this.chunks = chunks;
    }

    public HashMap<String, String> getMembers() {
        return members;
    }

    public void setMembers(HashMap<String, String> members) {
        this.members = members;
    }

    String id;
    String owner;
    Set<Long> chunks;
    HashMap<String,String> members;
    String biome;
    String world;

    public String getBiome() {
        return biome;
    }

    public void setBiome(String biome) {
        this.biome = biome;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public Region(String id, String owner, Set<Long> chunks, HashMap<String,String> members, String biome, String world){
        this.id = id;
        this.owner = owner;
        this.chunks = chunks;
        this.members = members;
        this.biome = biome;
        this.world = world;
    }
}
