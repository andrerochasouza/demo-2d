package br.com.andre.map;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Map {
    private final int[][] mapData;
    private final Tile floorTile;
    private final Tile wallTile;
    private final int TILE_SIZE = 64;

    public Map(String floorPath, String wallPath) {
        // Carregar tiles
        floorTile = new Tile(floorPath, false);
        wallTile = new Tile(wallPath, true);

        // Definir o mapa (exemplo simples)
        // 0: Chão, 1: Parede
        mapData = new int[][] {
                {1,1,1,1,1,1,1,1,1,1,1,1},
                {1,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,1,1,0,1,1,0,1,1,0,1},
                {1,0,1,0,0,0,1,0,0,1,0,1},
                {1,0,1,1,1,1,1,1,1,1,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,1},
                {1,1,1,1,1,1,1,1,1,1,1,1}
        };
    }

    public void render(Graphics g) {
        for (int y = 0; y < mapData.length; y++) {
            for (int x = 0; x < mapData[y].length; x++) {
                int tileType = mapData[y][x];
                if (tileType == 0) {
                    floorTile.render(g, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE);
                } else if (tileType == 1) {
                    wallTile.render(g, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }

    public boolean isSolidTile(int x, int y) {
        if (y < 0 || y >= mapData.length || x < 0 || x >= mapData[0].length) {
            return true; // Fora do mapa é considerado sólido
        }
        return mapData[y][x] == 1;
    }

    public BufferedImage getBackground() {
        // Retorna a imagem de fundo, se houver. Pode ser null ou uma imagem específica.
        return null;
    }
}