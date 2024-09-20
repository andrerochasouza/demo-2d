package br.com.andre.entities;

import br.com.andre.map.Map;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Entity {
    protected double x, y;
    protected int width, height;
    protected Map map;

    public Entity(double x, double y, int width, int height, Map map) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.map = map;
    }

    public abstract void update();
    public abstract void render(Graphics g);

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }
}