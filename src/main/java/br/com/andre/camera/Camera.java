package br.com.andre.camera;

import br.com.andre.entities.Player;
import br.com.andre.map.GameMap;

public class Camera {
    private double x, y;
    private int width, height;
    private GameMap gameMap;

    public Camera(int width, int height, GameMap gameMap) {
        this.width = width;
        this.height = height;
        this.gameMap = gameMap;
        this.x = 0;
        this.y = 0;
    }

    public void update(Player player) {
        // Centralizar a câmera no jogador
        x = player.getX() - (double) width / 2 + (double) player.getWidth() / 2;
        y = player.getY() - (double) height / 2 + (double) player.getHeight() / 2;

        // Limitar a câmera dentro dos limites do mapa
        x = Math.max(0, Math.min(x, gameMap.getMapWidth() - width));
        y = Math.max(0, Math.min(y, gameMap.getMapHeight() - height));
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

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }
}