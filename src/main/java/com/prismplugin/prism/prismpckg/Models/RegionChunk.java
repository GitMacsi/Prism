package com.prismplugin.prism.prismpckg.Models;

import java.util.Set;

public class RegionChunk {
    public long getChunk() {
        return chunk;
    }

    public void setChunk(long chunk) {
        this.chunk = chunk;
    }

    public String getChunk_id() {
        return region_id;
    }

    public void setChunk_id(int chunk_id) {
        this.region_id = region_id;
    }

    public Set<Long> getExceptions() {
        return exceptions;
    }

    public void setExceptions(Set<Long> exceptions) {
        this.exceptions = exceptions;
    }

    long chunk;
    String region_id;
    Set<Long> exceptions;

    public RegionChunk(long chunk, String region_id, Set<Long> exceptions){
        this.chunk = chunk;
        this.region_id = region_id;
        this.exceptions = exceptions;
    }

}
