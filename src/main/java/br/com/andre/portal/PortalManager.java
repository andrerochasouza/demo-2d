package br.com.andre.portal;

import br.com.andre.core.Game;
import br.com.andre.entities.Player;
import br.com.andre.map.GameMap;
import br.com.andre.map.MapStructure;

import java.awt.Rectangle;
import java.util.List;

public class PortalManager {
    private GameMap gameMap;
    private Game game; // Referência ao jogo para trocar mapas

    public PortalManager(GameMap gameMap, Game game) {
        this.gameMap = gameMap;
        this.game = game;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public void checkPortals(Player player) {
        MapStructure currentMapStructure = gameMap.getCurrentMap();
        if (currentMapStructure == null) return;

        List<Portal> portals = currentMapStructure.getPortals();
        for (Portal portal : portals) {
            int portalPixelX = portal.getTileX() * gameMap.getTileSize();
            int portalPixelY = portal.getTileY() * gameMap.getTileSize();

            // Assumindo que o portal ocupa todo o tile
            Rectangle portalBounds = new Rectangle(portalPixelX, portalPixelY, gameMap.getTileSize(), gameMap.getTileSize());
            if (player.getBounds().intersects(portalBounds)) {
                // Trocar de mapa
                String destinationMapName = portal.getDestinationMap();

                // Reposicionar o jogador
                double destX = portal.getDestTileX() * gameMap.getTileSize() + (gameMap.getTileSize() - player.getWidth()) / 2;
                double destY = portal.getDestTileY() * gameMap.getTileSize() + (gameMap.getTileSize() - player.getHeight()) / 2;

                // Atualizar o mapa no jogo
                game.switchMap(destinationMapName);

                // Reposicionar o jogador
                player.setPosition(destX, destY);

                // Imprimir mensagem
                System.out.println("Portal para " + destinationMapName + " ativada.");

                // Resetar o PortalManager se necessário
                break; // Evitar múltiplas trocas de mapa de uma vez
            }
        }
    }
}