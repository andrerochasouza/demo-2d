package br.com.andre.entities;

import br.com.andre.camera.Camera;
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
    public abstract void render(Graphics g, Camera camera);

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}