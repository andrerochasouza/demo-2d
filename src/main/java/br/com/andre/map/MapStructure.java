package br.com.andre.map;

import java.util.Map;

public class MapStructure {
    private final String name;
    private final int[][] grid;
    private final Map<Integer, TileDefinition> tileDefinitions;

    public MapStructure(String name, int[][] grid, Map<Integer, TileDefinition> tileDefinitions) {
        this.name = name;
        this.grid = grid;
        this.tileDefinitions = tileDefinitions;
    }

    public String getName() {
        return name;
    }

    public int[][] getGrid() {
        return grid;
    }

    public TileDefinition getTileDefinition(int tileNumber) {
        return tileDefinitions.get(tileNumber);
    }

    public Map<Integer, TileDefinition> getTileDefinitions() {
        return tileDefinitions;
    }
}