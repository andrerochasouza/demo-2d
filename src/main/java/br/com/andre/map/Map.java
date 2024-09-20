package br.com.andre.map;

import br.com.andre.utils.ResourceLoader;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Map {
    private MapStructure currentMap;
    private MapLoader mapLoader;
    public final int TILE_SIZE = 64;

    // Armazena sprites carregadas para evitar recarregamentos
    private java.util.Map<String, BufferedImage> sprites;

    public Map(String structFilePath) {
        mapLoader = new MapLoader(structFilePath);
        sprites = new HashMap<>();
        loadSprites();
        // Definir o mapa inicial, por exemplo, o primeiro mapa carregado
        if (!mapLoader.getMaps().isEmpty()) {
            setCurrentMap(mapLoader.getMaps().get(0).getName());
        }
    }

    private void loadSprites() {
        // Iterar sobre todas as definições de tiles e carregar sprites
        for (MapStructure mapStructure : mapLoader.getMaps()) {
            for (TileDefinition tileDef : mapStructure.getTileDefinitions().values()) {
                String spriteName = tileDef.getSpriteName();
                if (!sprites.containsKey(spriteName)) {
                    BufferedImage sprite = ResourceLoader.loadImage("/images/" + spriteName + ".png");
                    if (sprite != null) {
                        sprites.put(spriteName, sprite);
                    } else {
                        System.err.println("Sprite não encontrada: " + spriteName + ".png");
                    }
                }
            }
        }
    }

    public void setCurrentMap(String mapName) {
        MapStructure mapStructure = mapLoader.getMapByName(mapName);
        if (mapStructure != null) {
            currentMap = mapStructure;
            System.out.println("Mapa atual: " + mapName);
        } else {
            System.err.println("Mapa não encontrado: " + mapName);
        }
    }

    public MapStructure getCurrentMap() {
        return currentMap;
    }

    public void render(Graphics g) {
        if (currentMap == null) return;
        int[][] grid = currentMap.getGrid();
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                int tileNumber = grid[y][x];
                TileDefinition tileDef = currentMap.getTileDefinition(tileNumber);
                if (tileDef != null) {
                    BufferedImage sprite = sprites.get(tileDef.getSpriteName());
                    if (sprite != null) {
                        g.drawImage(sprite, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                    } else {
                        // Desenhar um retângulo se a sprite não estiver disponível
                        if (tileDef.isSolid()) {
                            g.setColor(java.awt.Color.DARK_GRAY);
                        } else {
                            g.setColor(java.awt.Color.LIGHT_GRAY);
                        }
                        g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                }
            }
        }
    }

    public boolean isSolidTile(int x, int y) {
        if (currentMap == null) return false;
        if (y < 0 || y >= currentMap.getGrid().length || x < 0 || x >= currentMap.getGrid()[0].length) {
            return true; // Fora do mapa é considerado sólido
        }
        int tileNumber = currentMap.getGrid()[y][x];
        TileDefinition tileDef = currentMap.getTileDefinition(tileNumber);
        if (tileDef != null) {
            return tileDef.isSolid();
        }
        return false;
    }
}