package org.liprudent.majiang.engine.tile;

/**
 * 麻将牌所有花色，包括 万筒条、东西南北、中发白
 */
public enum TileFamily {
    EAST(TileKind.HONOR, 1),
    WEST(TileKind.HONOR, 2),
    NORTH(TileKind.HONOR, 3),
    SOUTH(TileKind.HONOR, 4),
    RED(TileKind.HONOR, 5),
    GREEN(TileKind.HONOR, 6),
    WHITE(TileKind.HONOR, 7),
    BAMBOO(TileKind.SUIT, 8),
    DOT(TileKind.SUIT, 9),
    CHARACTER(TileKind.SUIT, 10),
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


}
