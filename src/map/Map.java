package map;

import java.io.Serializable;

public class Map implements Serializable {

	public int[][] tankPos, brickPos, stonePos, waterPos, grassPos;
	public int[] homePos, playerPos;
	public int[] player2Pos;

	public Map(int[][] tankPos, int[][] brickPos, int[][] stonePos, int[][] waterPos, int[][] grassPos, int[] homePos, int[] playerPos) {
		this.tankPos = tankPos;
		this.brickPos = brickPos;
		this.stonePos = stonePos;
		this.waterPos = waterPos;
		this.grassPos = grassPos;
		this.homePos = homePos;
		this.playerPos = playerPos;
	}
	//双人地图
	public Map(int[][] tankPos, int[][] brickPos, int[][] stonePos, int[][] waterPos, int[][] grassPos, int[] homePos, int[] playerPos,int[]player2Pos) {
		this.tankPos = tankPos;
		this.brickPos = brickPos;
		this.stonePos = stonePos;
		this.waterPos = waterPos;
		this.grassPos = grassPos;
		this.homePos = homePos;
		this.playerPos = playerPos;
		this.player2Pos=player2Pos;
	}
}