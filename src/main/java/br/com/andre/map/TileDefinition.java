package br.com.andre.map;

public class TileDefinition {
    private final String spriteName;
    private final boolean solid;
    private final boolean isEnemy;

    public TileDefinition(String spriteName, boolean solid, boolean isEnemy) {
        this.spriteName = spriteName;
        this.solid = solid;
        this.isEnemy = isEnemy;
    }

    public String getSpriteName() {
        return spriteName;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean isEnemy() {
        return isEnemy;
    }
}