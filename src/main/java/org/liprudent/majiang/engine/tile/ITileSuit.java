package org.liprudent.majiang.engine.tile;

/**
 * 花色牌（万、筒、条）
 */
public interface ITileSuit extends ITile {
	/**
	 * 花色牌包含（1-9）之间的数值
	 * @return 返回1-9之间牌面大小
	 */
	int getValue();
}
