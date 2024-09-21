package br.com.andre.portal;

public class Portal {
    private String name;
    private int tileX, tileY;
    private String destinationMap;
    private int destTileX, destTileY;

    public Portal(String name, int tileX, int tileY, String destinationMap, int destTileX, int destTileY) {
        this.name = name;
        this.tileX = tileX;
        this.tileY = tileY;
        this.destinationMap = destinationMap;
        this.destTileX = destTileX;
        this.destTileY = destTileY;
    }

    public String getName() {
        return name;
    }

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public String getDestinationMap() {
        return destinationMap;
    }

    public int getDestTileX() {
        return destTileX;
    }

    public int getDestTileY() {
        return destTileY;
    }
}