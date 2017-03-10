package net.irock.mahjong.tile;

import static net.irock.mahjong.tile.TileType.*;

/**
 * 麻将牌所有牌型
 * 总共34中牌型，暂不包括 春夏秋冬 梅兰竹菊
 * @author David Kong
 * @date 2017/3/10
 */
public enum Tile {

    C1(0, CHARACTER, 1),
    C2(1, CHARACTER, 2),
    C3(2, CHARACTER, 3),
    C4(3, CHARACTER, 4),
    C5(4, CHARACTER, 5),
    C6(5, CHARACTER, 6),
    C7(6, CHARACTER, 7),
    C8(7, CHARACTER, 8),
    C9(8, CHARACTER, 9),

    D1(9, DOT, 1),
    D2(10, DOT, 2),
    D3(11, DOT, 3),
    D4(12, DOT, 4),
    D5(13, DOT, 5),
    D6(14, DOT, 6),
    D7(15, DOT, 7),
    D8(16, DOT, 8),
    D9(17, DOT, 9),

    B1(18, BAMBOO, 1),
    B2(19, BAMBOO, 2),
    B3(20, BAMBOO, 3),
    B4(21, BAMBOO, 4),
    B5(22, BAMBOO, 5),
    B6(23, BAMBOO, 6),
    B7(24, BAMBOO, 7),
    B8(25, BAMBOO, 8),
    B9(26, BAMBOO, 9),

    EAST(27, WIND, 0),//東
    SOUTH(28, WIND, 0),//南
    WEST(29, WIND, 0),//西
    NORTH(30, WIND, 0),//北

    WHITE(31, DRAGON, 0),//白
    GREEN(32, DRAGON, 0),//発
    RED(33, DRAGON, 0);//中

    private int code;
    private TileType type;
    private int number;

    Tile(int code, TileType type, int number) {
        this.code = code;
        this.type = type;
        this.number = number;
    }

    public static Tile valueOf(int c) {
        return Tile.values()[c];
    }

    public int getCode() {
        return code;
    }

    public TileType getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    /**
     * 是否带幺
     */
    public boolean isCarryOne() {
        return number == 0 || number == 1 || number == 9;
    }
}
