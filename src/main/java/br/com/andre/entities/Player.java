package br.com.andre.entities;

import br.com.andre.camera.Camera;
import br.com.andre.input.KeyManager;
import br.com.andre.map.Map;
import br.com.andre.utils.ResourceLoader;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    private double speed = 4.0;
    private BufferedImage sprite;

    private KeyManager keyManager;

    public Player(double x, double y, Map map, KeyManager keyManager) {
        super(x, y, 32, 32, map);
        this.keyManager = keyManager;
        loadSprite();
    }

    private void loadSprite() {
        // Carregar a sprite baseada na definição do tile
        sprite = ResourceLoader.loadImage("/images/player.png");
    }

    @Override
    public void update() {
        double newX = x;
        double newY = y;

        if (keyManager.up) {
            newY -= speed;
        }
        if (keyManager.down) {
            newY += speed;
        }
        if (keyManager.left) {
            newX -= speed;
        }
        if (keyManager.right) {
            newX += speed;
        }

        // Verificar colisões horizontalmente
        if (!isColliding(newX, y)) {
            x = newX;
        }

        // Verificar colisões verticalmente
        if (!isColliding(x, newY)) {
            y = newY;
        }
    }

    private boolean isColliding(double newX, double newY) {
        // Verificar os quatro cantos da entidade
        int tileSize = map.getTileSize();
        int left = (int) newX / tileSize;
        int right = (int) (newX + width) / tileSize;
        int top = (int) newY / tileSize;
        int bottom = (int) (newY + height) / tileSize;

        if (map.isSolidTile(left, top) || map.isSolidTile(right, top) ||
                map.isSolidTile(left, bottom) || map.isSolidTile(right, bottom)) {
            return true;
        }

        return false;
    }

    @Override
    public void render(Graphics g, Camera camera) {
        if (sprite != null) {
            // Apenas renderizar se estiver dentro da área visível
            if (isVisible(camera)) {
                g.drawImage(sprite,
                        (int) (x - camera.getX()),
                        (int) (y - camera.getY()),
                        width, height, null);
            }
        } else {
            // Desenhar um retângulo azul se a sprite não estiver disponível
            if (isVisible(camera)) {
                g.setColor(java.awt.Color.BLUE);
                g.fillRect((int) (x - camera.getX()), (int) (y - camera.getY()), width, height);
            }
        }
    }

    private boolean isVisible(Camera camera) {
        return x + width > camera.getX() &&
                x < camera.getX() + camera.getWidth() &&
                y + height > camera.getY() &&
                y < camera.getY() + camera.getHeight();
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}