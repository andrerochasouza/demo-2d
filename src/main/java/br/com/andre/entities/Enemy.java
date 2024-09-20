package br.com.andre.entities;

import br.com.andre.map.Map;
import br.com.andre.utils.ResourceLoader;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;


public class Enemy extends Entity {

    private double speed = 2.0;
    private BufferedImage sprite;
    private Random rand;
    private int direction; // 0: cima, 1: baixo, 2: esquerda, 3: direita
    private int changeDirectionTimer;

    public Enemy(double x, double y, Map map, String path) {
        super(x, y, 32, 32, map);
        loadSprite(path);
        rand = new Random();
        setRandomDirection();
    }

    private void loadSprite(String path) {
        // Carregar a sprite do inimigo
        sprite = ResourceLoader.loadImage(path);
    }

    @Override
    public void update() {
        if (changeDirectionTimer <= 0) {
            setRandomDirection();
        } else {
            changeDirectionTimer--;
        }

        double newX = x;
        double newY = y;

        switch(direction) {
            case 0: // Cima
                newY -= speed;
                break;
            case 1: // Baixo
                newY += speed;
                break;
            case 2: // Esquerda
                newX -= speed;
                break;
            case 3: // Direita
                newX += speed;
                break;
        }

        // Verificar colisões horizontalmente
        if (!isColliding(newX, y)) {
            x = newX;
        } else {
            setRandomDirection();
        }

        // Verificar colisões verticalmente
        if (!isColliding(x, newY)) {
            y = newY;
        } else {
            setRandomDirection();
        }
    }

    private void setRandomDirection() {
        direction = rand.nextInt(4);
        changeDirectionTimer = rand.nextInt(60) + 30; // Mudar de direção a cada 30-90 ticks
    }

    private boolean isColliding(double newX, double newY) {
        // Verificar os quatro cantos da entidade
        int tileSize = map.TILE_SIZE;
        int left = (int)newX / tileSize;
        int right = (int)(newX + width) / tileSize;
        int top = (int)newY / tileSize;
        int bottom = (int)(newY + height) / tileSize;

        if (map.isSolidTile(left, top) || map.isSolidTile(right, top) ||
                map.isSolidTile(left, bottom) || map.isSolidTile(right, bottom)) {
            return true;
        }

        return false;
    }

    @Override
    public void render(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, (int)x, (int)y, width, height, null);
        } else {
            // Desenhar um retângulo vermelho se a sprite não estiver disponível
            g.setColor(java.awt.Color.RED);
            g.fillRect((int)x, (int)y, width, height);
        }
    }
}