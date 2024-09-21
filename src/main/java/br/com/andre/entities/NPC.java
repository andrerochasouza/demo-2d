package br.com.andre.entities;

import br.com.andre.camera.Camera;
import br.com.andre.map.GameMap;
import br.com.andre.utils.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class NPC extends Entity {
    private BufferedImage sprite;
    private double life;
    private String name;
    private String spritePath;

    public NPC(double x, double y, GameMap map, String spritePath, double life, String name) {
        super(x, y, 32, 32, map);
        this.spritePath = spritePath;
        this.life = life;
        this.name = name;
        loadSprite(spritePath);
    }

    private void loadSprite(String path) {
        // Carregar a sprite baseada na definição do tile
        sprite = ResourceLoader.loadImage(path);
    }

    @Override
    public void update() {
        // Implementar lógica de NPC, como interações
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
                g.setColor(Color.GREEN);
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

    public String getName() {
        return name;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public void setLife(double life) {
        this.life = life;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpritePath() {
        return spritePath;
    }

    public void setSpritePath(String spritePath) {
        this.spritePath = spritePath;
    }
}