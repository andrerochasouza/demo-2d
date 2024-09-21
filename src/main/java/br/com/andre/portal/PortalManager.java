package br.com.andre.portal;

import br.com.andre.entities.Player;
import br.com.andre.map.Map;
import br.com.andre.map.MapStructure;

import java.util.List;

public class PortalManager {
    private Map map;

    public PortalManager(Map map) {
        this.map = map;
    }

    public void checkPortals(Player player) {
        MapStructure currentMap = map.getCurrentMap();
        if (currentMap == null) return;

        List<Portal> portals = currentMap.getPortals();
        for (Portal portal : portals) {
            int portalPixelX = portal.getTileX() * map.getTileSize();
            int portalPixelY = portal.getTileY() * map.getTileSize();

            // Verificar colisão com o portal (assumindo que o portal ocupa todo o tile)
            if (player.getBounds().intersects(
                    portalPixelX,
                    portalPixelY,
                    map.getTileSize(),
                    map.getTileSize())) {
                // Trocar de mapa
                String destinationMapName = portal.getDestinationMap(); // Ajustar conforme a necessidade

                // Reposicionar o jogador
                double destX = portal.getDestTileX() * map.getTileSize() + (map.getTileSize() - player.getWidth()) / 2;
                double destY = portal.getDestTileY() * map.getTileSize() + (map.getTileSize() - player.getHeight()) / 2;

                // Atualizar o mapa
                map.setCurrentMap(destinationMapName);
                player.setPosition(destX, destY);

                // Re-inicializar inimigos
                // Isto deve ser feito na classe Game, então vamos precisar de uma referência
                // Para simplificar, você pode emitir um evento ou usar um callback
                // Aqui, vamos apenas imprimir uma mensagem
                System.out.println("Porta para " + destinationMapName + " ativada.");

                // Resetar o PortalManager se necessário
                break; // Evitar múltiplas trocas de mapa de uma vez
            }
        }
    }
}