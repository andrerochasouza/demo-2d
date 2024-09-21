package br.com.andre.map;

import br.com.andre.entities.Enemy;
import br.com.andre.entities.NPC;
import br.com.andre.portal.Portal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapStructure {
    private String name;
    private int[][] grid;
    private Map<Integer, TileDefinition> tileDefinitions;
    private List<Portal> portals;
    private List<Enemy> enemies;
    private List<NPC> npcs;

    public MapStructure(String name, int[][] grid, Map<Integer, TileDefinition> tileDefinitions) {
        this.name = name;
        this.grid = grid;
        this.tileDefinitions = tileDefinitions;
        this.portals = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.npcs = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    public TileDefinition getTileDefinition(int tileNumber) {
        return tileDefinitions.get(tileNumber);
    }

    public void setTileDefinitions(Map<Integer, TileDefinition> tileDefinitions) {
        this.tileDefinitions = tileDefinitions;
    }

    public Map<Integer, TileDefinition> getTileDefinitions() {
        return tileDefinitions;
    }

    public void addPortal(Portal portal) {
        portals.add(portal);
    }

    public List<Portal> getPortals() {
        return portals;
    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void addNPC(NPC npc) {
        npcs.add(npc);
    }

    public List<NPC> getNpcs() {
        return npcs;
    }
}