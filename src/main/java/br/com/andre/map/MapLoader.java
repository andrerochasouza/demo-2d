package br.com.andre.map;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Map;

public class MapLoader {
    private List<MapStructure> maps;

    public MapLoader(String structFilePath) {
        maps = new ArrayList<>();
        loadMaps(structFilePath);
    }

    private void loadMaps(String structFilePath) {
        try {
            InputStream is = getClass().getResourceAsStream(structFilePath);
            if (is == null) {
                throw new IllegalArgumentException("Arquivo de estrutura não encontrado: " + structFilePath);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
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
                    // Se já havia um mapa sendo lido, salvar o anterior
                    if (currentMapName != null) {
                        int rows = grid.size();
                        int cols = grid.get(0).length;
                        int[][] gridArray = new int[rows][cols];
                        for (int i = 0; i < rows; i++) {
                            gridArray[i] = grid.get(i);
                        }
                        MapStructure mapStructure = new MapStructure(currentMapName, gridArray, tileDefs);
                        maps.add(mapStructure);
                        // Resetar para o próximo mapa
                        grid = new ArrayList<>();
                        tileDefs = new HashMap<>();
                    }
                    currentMapName = line.substring(4).trim();
                    readingMapGrid = false;
                } else if (line.equals("ibj")) {
                    readingMapGrid = true;
                } else if (line.startsWith("obj ")) {
                    // Processar definição de tile
                    String[] parts = line.split("#")[0].split("\\s+"); // Remover comentário
                    if (parts.length < 5) {
                        System.err.println("Definição de tile inválida: " + line);
                        continue;
                    }
                    int tileNumber = Integer.parseInt(parts[1]);
                    String spriteName = parts[2];
                    boolean solid = Boolean.parseBoolean(parts[3]);
                    boolean isEnemy = Boolean.parseBoolean(parts[4]);
                    TileDefinition tileDef = new TileDefinition(spriteName, solid, isEnemy);
                    tileDefs.put(tileNumber, tileDef);
                } else if (readingMapGrid) {
                    // Processar linha da grid
                    String[] nums = line.split("\\s+");
                    int[] row = new int[nums.length];
                    for (int i = 0; i < nums.length; i++) {
                        row[i] = Integer.parseInt(nums[i]);
                    }
                    grid.add(row);
                }
            }

            // Adicionar o último mapa lido
            if (currentMapName != null) {
                int rows = grid.size();
                int cols = grid.get(0).length;
                int[][] gridArray = new int[rows][cols];
                for (int i = 0; i < rows; i++) {
                    gridArray[i] = grid.get(i);
                }
                MapStructure mapStructure = new MapStructure(currentMapName, gridArray, tileDefs);
                maps.add(mapStructure);
            }

            br.close();
            System.out.println("Mapas carregados: " + maps.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}