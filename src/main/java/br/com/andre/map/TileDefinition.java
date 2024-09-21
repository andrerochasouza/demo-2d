package br.com.andre.map;

public class TileDefinition {
    private String spriteName;
    private boolean solid;
    private boolean isEnemy;
    private boolean isPortal;
    private String portalName; // Nome do portal associado, se for um portal

    public TileDefinition(String spriteName, boolean solid, boolean isEnemy) {
        this(spriteName, solid, isEnemy, false, null);
    }

    public TileDefinition(String spriteName, boolean solid, boolean isEnemy, boolean isPortal, String portalName) {
        this.spriteName = spriteName;
        this.solid = solid;
        this.isEnemy = isEnemy;
        this.isPortal = isPortal;
        this.portalName = portalName;
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

    public boolean isPortal() {
        return isPortal;
    }

    public String getPortalName() {
        return portalName;
    }
}