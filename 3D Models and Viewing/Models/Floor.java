package Models;

import org.joml.Vector3f;

import a2.Utils;

public class Floor {

	private float[] floor; //indices
	private float[] floorTc; //texture coordinates
	private int textureFile; //from Utils file
	private Vector3f worldLoc = new Vector3f(); //world xyz position
	
	public Floor() {
		float[] floorHold = {
				10.0f, -0.5f, -10.0f, 10.0f, -0.5f, 10.0f, -10.0f, -0.5f, 10.0f, //triangle 1
				-10.0f, -0.5f, 10.0f, -10.0f, -0.5f, -10.0f, 10.0f, -0.5f, -10.0f //triangle 2
		};
		
		float[] floorTcHold = {
				1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f
		};
		
		floor = floorHold;
		floorTc = floorTcHold;
		worldLoc.set(0.0f, 0.0f, 0.0f);
		textureFile = Utils.loadTexture("Textures/floor.png");
	}
	
	public float[] getIndices() {
		return floor;
	}
	
	public float[] getTextCord() {
		return floorTc;
	}
	
	public Vector3f getWorldLoc() {
		return worldLoc;
	}
	
	public int getTextureFile() {
		return textureFile;
	}
}
