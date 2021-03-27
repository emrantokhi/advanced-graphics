package Models;

import org.joml.Vector3f;

import a2.Utils;

public class XAxis {

	private float[] axes; //indices
	private float[] axesTc; //texture coordinates
	private int textureFile; //from Utils file
	private Vector3f worldLoc = new Vector3f(); //world xyz position
	
	public XAxis() {
		float[] axesHold = {
				0.0f, 0.0f, 0.0f, 50.0f, 0.0f, 0.0f //x axis
		};
		
		float[] axesTcHold = {
				0.0f, 0.0f, 1.0f, 1.0f
		};
		
		axes = axesHold;
		axesTc = axesTcHold;
		worldLoc.set(0.0f, 0.0f, 0.0f);
		textureFile = Utils.loadTexture("Textures/red.jpg");
	}
	
	public float[] getIndices() {
		return axes;
	}
	
	public float[] getTextCord() {
		return axesTc;
	}
	
	public Vector3f getWorldLoc() {
		return worldLoc;
	}
	
	public int getTextureFile() {
		return textureFile;
	}
}