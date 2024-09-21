package br.com.andre.map;

import br.com.andre.camera.Camera;
import br.com.andre.entities.Enemy;
import br.com.andre.entities.NPC;
import br.com.andre.utils.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class GameMap {
    private MapLoader mapLoader;
    private MapStructure currentMap;
    public static final int TILE_SIZE = 64;

    // Armazena sprites carregadas para evitar recarregamentos
    private java.util.Map<String, BufferedImage> sprites;

    public GameMap(MapLoader mapLoader, String mapName) {
        this.mapLoader = mapLoader;
        sprites = new HashMap<>();
        loadSprites();
        setCurrentMap(mapName);
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

        // Carregar sprites de entidades
        for (MapStructure mapStruct : mapLoader.getMaps()) {
            for (Enemy enemy : mapStruct.getEnemies()) {
                String spriteName = enemy.getSpritePath();
                if (!sprites.containsKey(spriteName)) {
                    BufferedImage sprite = ResourceLoader.loadImage(spriteName);
                    if (sprite != null) {
                        sprites.put(spriteName, sprite);
                    } else {
                        System.err.println("Sprite do inimigo não encontrada: " + spriteName);
                    }
                }
            }

            for (NPC npc : mapStruct.getNpcs()) {
                String spriteName = npc.getSpritePath();
                if (!sprites.containsKey(spriteName)) {
                    BufferedImage sprite = ResourceLoader.loadImage(spriteName);
                    if (sprite != null) {
                        sprites.put(spriteName, sprite);
                    } else {
                        System.err.println("Sprite do NPC não encontrada: " + spriteName);
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

    public int getMapWidth() {
        if (currentMap == null) return 0;
        return currentMap.getGrid()[0].length * TILE_SIZE;
    }

    public int getMapHeight() {
        if (currentMap == null) return 0;
        return currentMap.getGrid().length * TILE_SIZE;
    }

    public void render(Graphics g, Camera camera) {
        if (currentMap == null) return;
        int[][] grid = currentMap.getGrid();

        // Determinar a área visível com base na câmera
        int startX = Math.max(0, (int) (camera.getX() / TILE_SIZE));
        int startY = Math.max(0, (int) (camera.getY() / TILE_SIZE));
        int endX = Math.min(grid[0].length, (int) ((camera.getX() + camera.getWidth()) / TILE_SIZE) + 1);
        int endY = Math.min(grid.length, (int) ((camera.getY() + camera.getHeight()) / TILE_SIZE) + 1);

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                int tileNumber = grid[y][x];
                TileDefinition tileDef = currentMap.getTileDefinition(tileNumber);
                if (tileDef != null) {
                    BufferedImage sprite = sprites.get(tileDef.getSpriteName());
                    if (sprite != null) {
                        g.drawImage(sprite,
                                x * TILE_SIZE - (int) camera.getX(),
                                y * TILE_SIZE - (int) camera.getY(),
                                TILE_SIZE, TILE_SIZE, null);
                    } else {
                        // Desenhar um retângulo se a sprite não estiver disponível
                        g.setColor(tileDef.isSolid() ? Color.DARK_GRAY : Color.LIGHT_GRAY);
                        g.fillRect(x * TILE_SIZE - (int) camera.getX(),
                                y * TILE_SIZE - (int) camera.getY(),
                                TILE_SIZE, TILE_SIZE);
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

    public int getTileSize() {
        return TILE_SIZE;
    }

    // Método para obter sprites
    public BufferedImage getSprite(String spriteName) {
        return sprites.get(spriteName);
    }
}