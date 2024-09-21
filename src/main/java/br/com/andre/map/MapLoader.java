package br.com.andre.map;

import br.com.andre.portal.Portal;

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
                    if (currentMap != null) {
                        // Converter a grid para um array 2D
                        int rows = grid.size();
                        int cols = grid.get(0).length;
                        int[][] gridArray = new int[rows][cols];
                        for (int i = 0; i < rows; i++) {
                            gridArray[i] = grid.get(i);
                        }
                        currentMap.setGrid(gridArray);
                        currentMap.setTileDefinitions(tileDefs);
                        maps.add(currentMap);
                        // Resetar para o próximo mapa
                        grid = new ArrayList<>();
                        tileDefs = new HashMap<>();
                    }
                    // Iniciar um novo mapa
                    currentMapName = line.substring(4).trim();
                    currentMap = new MapStructure(currentMapName, new int[0][0], new HashMap<>());
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
                    boolean isPortal = false;
                    String portalName = null;
                    if (parts.length >= 6) { // Verificar se há propriedade de portal
                        isPortal = Boolean.parseBoolean(parts[5]);
                        if (isPortal && parts.length >= 7) {
                            portalName = parts[6];
                        }
                    }
                    TileDefinition tileDef = new TileDefinition(spriteName, solid, isEnemy, isPortal, portalName);
                    tileDefs.put(tileNumber, tileDef);
                } else if (line.startsWith("portal ")) {
                    // Processar definição de portal
                    String[] parts = line.split("#")[0].split("\\s+"); // Remover comentário
                    if (parts.length < 8) {
                        System.err.println("Definição de portal inválida: " + line);
                        continue;
                    }
                    String sourceMap = parts[1];
                    String portalName = parts[2];
                    int posX = Integer.parseInt(parts[3]);
                    int posY = Integer.parseInt(parts[4]);
                    String destinationMap = parts[5];
                    int destX = Integer.parseInt(parts[6]);
                    int destY = Integer.parseInt(parts[7]);

                    if (currentMap != null && sourceMap.equalsIgnoreCase(currentMap.getName())) {
                        // Adicionar portal ao mapa atual
                        currentMap.addPortal(new Portal(portalName, posX, posY, destinationMap, destX, destY));
                    } else {
                        // Se o portal não pertence ao mapa atual, buscar no mapa já carregado
                        MapStructure mapStructure = getMapByName(sourceMap);
                        if (mapStructure != null) {
                            mapStructure.addPortal(new Portal(portalName, posX, posY, destinationMap, destX, destY));
                        } else {
                            System.err.println("Mapa de origem do portal não encontrado: " + sourceMap);
                        }
                    }
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
            if (currentMap != null) {
                int rows = grid.size();
                int cols = grid.get(0).length;
                int[][] gridArray = new int[rows][cols];
                for (int i = 0; i < rows; i++) {
                    gridArray[i] = grid.get(i);
                }
                currentMap.setGrid(gridArray);
                currentMap.setTileDefinitions(tileDefs);
                maps.add(currentMap);
            }

            br.close();
            System.out.println("Mapas carregados: " + maps.size());
        } catch (Exception e){
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