package br.com.andre.entities;

import br.com.andre.camera.Camera;
import br.com.andre.map.GameMap;
import br.com.andre.utils.ResourceLoader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Enemy extends Entity {
    private double speed = 2.0;
    private BufferedImage sprite;
    private Random rand;
    private int direction; // 0: cima, 1: baixo, 2: esquerda, 3: direita
    private int changeDirectionTimer;
    private String spritePath; // Adicionado campo para armazenar o caminho da sprite
    private double life;

    public Enemy(double x, double y, GameMap map, String spritePath, double life) {
        super(x, y, 32, 32, map);
        this.spritePath = spritePath; // Inicializar spritePath
        this.life = life;
        loadSprite(spritePath);
        rand = new Random();
        setRandomDirection();
    }

    private void loadSprite(String path) {
        // Carregar a sprite do inimigo
        sprite = ResourceLoader.loadImage(path);
    }

    public String getSpritePath() {
        return spritePath;
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

        switch (direction) {
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
        int tileSize = gameMap.getTileSize();
        int left = (int) newX / tileSize;
        int right = (int) (newX + width) / tileSize;
        int top = (int) newY / tileSize;
        int bottom = (int) (newY + height) / tileSize;

        return gameMap.isSolidTile(left, top) || gameMap.isSolidTile(right, top) ||
                gameMap.isSolidTile(left, bottom) || gameMap.isSolidTile(right, bottom);
    }

    @Override
    public void render(Graphics g, Camera camera) {
        if (sprite != null) {
            if (isVisible(camera)) {
                g.drawImage(sprite,
                        (int) (x - camera.getX()),
                        (int) (y - camera.getY()),
                        width, height, null);
            }
        } else {
            if (isVisible(camera)) {
                g.setColor(Color.RED);
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

    public double getLife() {
        return life;
    }

    public void setLife(double life) {
        this.life = life;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public Random getRand() {
        return rand;
    }

    public void setRand(Random rand) {
        this.rand = rand;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getChangeDirectionTimer() {
        return changeDirectionTimer;
    }

    public void setChangeDirectionTimer(int changeDirectionTimer) {
        this.changeDirectionTimer = changeDirectionTimer;
    }
}