package br.com.andre.map;

import br.com.andre.utils.ResourceLoader;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Tile {
    private BufferedImage image;
    private boolean solid;

    public Tile(String imagePath, boolean solid) {
        this.image = ResourceLoader.loadImage(imagePath);
        this.solid = solid;
    }

    public BufferedImage getImage() {
        return image;
    }

    public boolean isSolid() {
        return solid;
    }

    public void render(Graphics g, int x, int y, int tileSize) {
        if (image != null) {
            g.drawImage(image, x, y, tileSize, tileSize, null);
        }
    }
}