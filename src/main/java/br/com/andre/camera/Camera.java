package br.com.andre.camera;

import br.com.andre.entities.Player;
import br.com.andre.map.Map;

public class Camera {
    private double x, y;
    private int width, height;
    private Map map;

    public Camera(int width, int height, Map map) {
        this.width = width;
        this.height = height;
        this.map = map;
        this.x = 0;
        this.y = 0;
    }

    public void update(Player player) {
        // Centralizar a câmera no jogador
        x = player.getX() - (double) width / 2 + (double) player.getWidth() / 2;
        y = player.getY() - (double) height / 2 + (double) player.getHeight() / 2;

        // Limitar a câmera dentro dos limites do mapa
        x = Math.max(0, Math.min(x, map.getMapWidth() - width));
        y = Math.max(0, Math.min(y, map.getMapHeight() - height));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}