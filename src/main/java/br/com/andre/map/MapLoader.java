package br.com.andre.map;

import br.com.andre.entities.Enemy;
import br.com.andre.entities.NPC;
import br.com.andre.portal.Portal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class MapLoader {
    private List<MapStructure> maps;
    private Map<String, EntityDefinition> entityDefinitions;

    public MapLoader(String structFilePath, String entityFilePath) {
        maps = new ArrayList<>();
        entityDefinitions = new HashMap<>();
        loadEntityDefinitions(entityFilePath);
        loadMaps(structFilePath);
    }

    private void loadEntityDefinitions(String entityFilePath) {
        try (InputStream is = getClass().getResourceAsStream(entityFilePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            if (is == null) {
                throw new IllegalArgumentException("Arquivo de entidades não encontrado: " + entityFilePath);
            }

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Ignorar linhas vazias e comentários
                }

                String[] parts = line.split("\\s+");
                if (parts.length < 3) {
                    System.err.println("Definição de entidade inválida: " + line);
                    continue;
                }

                String name = parts[0];
                String spritePath = parts[1];
                double life;
                try {
                    life = Double.parseDouble(parts[2]);
                } catch (NumberFormatException e) {
                    System.err.println("Erro ao parsear vida para entidade: " + line);
                    life = 10.0; // Valor padrão
                }

                entityDefinitions.put(name.toLowerCase(), new EntityDefinition(spritePath, life));
            }

            System.out.println("Definições de entidades carregadas: " + entityDefinitions.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMaps(String structFilePath) {
        try (InputStream is = getClass().getResourceAsStream(structFilePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            if (is == null) {
                throw new IllegalArgumentException("Arquivo de estrutura não encontrado: " + structFilePath);
            }

            String line;
            MapStructure currentMap = null;
            String currentMapName = null;
            List<int[]> grid = new ArrayList<>();
            Map<Integer, TileDefinition> tileDefs = new HashMap<>();
            boolean readingMapGrid = false;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Ignorar linhas vazias e comentários
                }

                if (line.startsWith("map ")) {
                    // Salvar o mapa anterior, se existir
                    if (currentMap != null) {
                        currentMap.setGrid(convertListTo2DArray(grid));
                        currentMap.setTileDefinitions(tileDefs);
                        maps.add(currentMap);
                        // Resetar para o próximo mapa
                        grid.clear();
                        tileDefs.clear();
                    }
                    // Iniciar um novo mapa
                    currentMapName = line.substring(4).trim();
                    currentMap = new MapStructure(currentMapName, new int[0][0], new HashMap<>());
                    readingMapGrid = false;
                } else if (line.equals("ibj")) {
                    readingMapGrid = true;
                } else if (line.startsWith("obj ")) {
                    processTileDefinition(line, tileDefs);
                } else if (line.startsWith("portal ")) {
                    processPortalDefinition(line, currentMap);
                } else if (line.startsWith("enemy ") || line.startsWith("npc ")) {
                    processEntityDefinition(line, currentMap);
                } else if (readingMapGrid) {
                    processGridLine(line, grid);
                }
            }

            // Adicionar o último mapa lido
            if (currentMap != null) {
                currentMap.setGrid(convertListTo2DArray(grid));
                currentMap.setTileDefinitions(tileDefs);
                maps.add(currentMap);
            }

            System.out.println("Mapas carregados: " + maps.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int[][] convertListTo2DArray(List<int[]> grid) {
        int rows = grid.size();
        if (rows == 0) return new int[0][0];
        int cols = grid.get(0).length;
        for (int[] row : grid) {
            if (row.length != cols) {
                throw new IllegalArgumentException("Inconsistência no número de colunas da grid.");
            }
        }
        int[][] gridArray = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            gridArray[i] = grid.get(i);
        }
        return gridArray;
    }

    private void processTileDefinition(String line, Map<Integer, TileDefinition> tileDefs) {
        String[] parts = line.split("#")[0].split("\\s+"); // Remover comentário
        if (parts.length < 4) { // Alterado de 5 para 4, já que isEnemy foi removido
            System.err.println("Definição de tile inválida: " + line);
            return;
        }
        try {
            int tileNumber = Integer.parseInt(parts[1]);
            String spriteName = parts[2];
            boolean solid = Boolean.parseBoolean(parts[3]);
            // isEnemy removido
            TileDefinition tileDef = new TileDefinition(spriteName, solid, false, false, null);
            tileDefs.put(tileNumber, tileDef);
        } catch (NumberFormatException e) {
            System.err.println("Erro ao parsear definição de tile: " + line);
        }
    }

    private void processPortalDefinition(String line, MapStructure currentMap) {
        String[] parts = line.split("#")[0].split("\\s+"); // Remover comentário
        if (parts.length < 8) {
            System.err.println("Definição de portal inválida: " + line);
            return;
        }
        try {
            String sourceMap = parts[1];
            String portalName = parts[2];
            int posX = Integer.parseInt(parts[3]);
            int posY = Integer.parseInt(parts[4]);
            String destinationMap = parts[5];
            int destX = Integer.parseInt(parts[6]);
            int destY = Integer.parseInt(parts[7]);

            Portal portal = new Portal(portalName, posX, posY, destinationMap, destX, destY);
            if (currentMap != null && sourceMap.equalsIgnoreCase(currentMap.getName())) {
                currentMap.addPortal(portal);
            } else {
                // Se o portal não pertence ao mapa atual, buscar no mapa já carregado
                MapStructure mapStructure = getMapByName(sourceMap);
                if (mapStructure != null) {
                    mapStructure.addPortal(portal);
                } else {
                    System.err.println("Mapa de origem do portal não encontrado: " + sourceMap);
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Erro ao parsear definição de portal: " + line);
        }
    }

    private void processEntityDefinition(String line, MapStructure currentMap) {
        String[] parts = line.split("#")[0].split("\\s+"); // Remover comentário
        if (parts.length < 3) {
            System.err.println("Definição de entidade inválida: " + line);
            return;
        }
        String entityType = parts[0].toLowerCase();
        String entityName = parts[1];
        int posX, posY;
        try {
            posX = Integer.parseInt(parts[2]);
            posY = Integer.parseInt(parts[3]);
        } catch (NumberFormatException e) {
            System.err.println("Erro ao parsear posição da entidade: " + line);
            return;
        }

        EntityDefinition def = entityDefinitions.get(entityName.toLowerCase());
        if (def == null) {
            System.err.println("Definição de entidade não encontrada para: " + entityName);
            return;
        }

        double life = def.getLife();
        String spritePath = "/images/" + def.getSpritePath();

        if (entityType.equals("enemy")) {
            Enemy enemy = new Enemy(posX * GameMap.TILE_SIZE,
                    posY * GameMap.TILE_SIZE,
                    null,
                    spritePath,
                    life);
            currentMap.addEnemy(enemy);
        } else if (entityType.equals("npc")) {
            NPC npc = new NPC(posX * GameMap.TILE_SIZE,
                    posY * GameMap.TILE_SIZE,
                    null,
                    spritePath,
                    life,
                    entityName);
            currentMap.addNPC(npc);
        } else {
            System.err.println("Tipo de entidade desconhecido: " + entityType);
        }
    }

    private void processGridLine(String line, List<int[]> grid) {
        String[] nums = line.split("\\s+");
        int[] row = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            try {
                row[i] = Integer.parseInt(nums[i]);
            } catch (NumberFormatException e) {
                row[i] = 0; // Valor padrão ou trate conforme necessário
                System.err.println("Erro ao parsear número na grid: " + nums[i]);
            }
        }
        grid.add(row);
    }

    public List<MapStructure> getMaps() {
        return maps;
    }

    public MapStructure getMapByName(String name) {
        for (MapStructure map : maps) {
            if (map.getName().equalsIgnoreCase(name)) {
                return map;
            }
        }
        return null;
    }

    // Classe auxiliar para armazenar definições de entidades
    private static class EntityDefinition {
        private String spritePath;
        private double life;

        public EntityDefinition(String spritePath, double life) {
            this.spritePath = spritePath;
            this.life = life;
        }

        public String getSpritePath() {
            return spritePath;
        }

        public double getLife() {
            return life;
        }
    }
}