package org.liprudent.majiang.engine.tile;

/**
 * 麻将牌所有牌型，包括万筒条、东西南北、中发白
 *
 * @author David Kong
 */
public enum TileFamily {
    CHARACTER(TileKind.SUIT, 1),
    DOT(TileKind.SUIT, 2),
    BAMBOO(TileKind.SUIT, 3),
    EAST(TileKind.HONOR, 4),
    WEST(TileKind.HONOR, 5),
    NORTH(TileKind.HONOR, 6),
    SOUTH(TileKind.HONOR, 7),
    RED(TileKind.HONOR, 8),
    GREEN(TileKind.HONOR, 9),
    WHITE(TileKind.HONOR, 10),
    FOO(TileKind.SUIT, 11);

    final TileKind tileKind;
    final Integer tileOrder;

    TileFamily(TileKind kind, Integer order) {
        tileKind = kind;
        tileOrder = order;
    }

    public TileKind getTileKind() {
        return tileKind;
    }

    public Integer getTileOrder() {
        return tileOrder;
    }

    public String getChineseName() {
        switch (this) {
            case CHARACTER:
                return "万";
            case BAMBOO:
                return "条";
            case DOT:
                return "筒";
            case EAST:
                return "东";
            case NORTH:
                return "南";
            case WEST:
                return "西";
            case SOUTH:
                return "北";
            case RED:
                return "中";
            case GREEN:
                return "發";
            case WHITE:
                return "白";
            default:
                return "";
        }
    }
}
