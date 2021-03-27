package Models;

import org.joml.Vector3f;

import a2.Utils;

public class Snowman {
	
	private float[] base; //indices 
	private float[] baseTc; //texture coordinates
	private float[] mid; //indices
	private float[] midTc; //texture coordinates
	private float[] head; //indices
	private float[] headTc; //texture coordinates
	
	private Vector3f baseLoc = new Vector3f();
	private Vector3f midLoc = new Vector3f();
	private Vector3f headLoc = new Vector3f();
	
	private int baseTexture;
	private int midTexture;
	private int headTexture;
	
	public Snowman() {
		float[] baseHold = 
			{  -2.0f,  1.0f, -1.0f, -2.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, //back side bottom half
				1.0f, -1.0f, -1.0f, 1.0f,  1.0f, -1.0f, -2.0f,  1.0f, -1.0f, //back side top half
				1.0f, -1.0f, -1.0f, 1.0f, -1.0f,  1.0f, 1.0f,  1.0f, -1.0f, //right bottom half
				1.0f, -1.0f,  1.0f, 1.0f,  1.0f,  1.0f, 1.0f,  1.0f, -1.0f, //right top half
				1.0f, -1.0f,  1.0f, -2.0f, -1.0f,  1.0f, 1.0f,  1.0f,  1.0f, //front bottom half
				-2.0f, -1.0f,  1.0f, -2.0f,  1.0f,  1.0f, 1.0f,  1.0f,  1.0f, //front top half
				-2.0f, -1.0f,  1.0f, -2.0f, -1.0f, -1.0f, -2.0f,  1.0f,  1.0f, //left bottom half
				-2.0f, -1.0f, -1.0f, -2.0f,  1.0f, -1.0f, -2.0f,  1.0f,  1.0f, //left top half
				-2.0f, -1.0f,  1.0f,  1.0f, -1.0f,  1.0f,  1.0f, -1.0f, -1.0f, //underneath bottom half
				1.0f, -1.0f, -1.0f, -2.0f, -1.0f, -1.0f, -2.0f, -1.0f,  1.0f, //underneath top half
				-2.0f,  1.0f, -1.0f, 1.0f,  1.0f, -1.0f, 1.0f,  1.0f,  1.0f, //above bottom half
				1.0f,  1.0f,  1.0f, -2.0f,  1.0f,  1.0f, -2.0f,  1.0f, -1.0f //above top half
			};
		
		float[] baseTcHold = {
				0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, //back side bottom half
				0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, //back side top half
				0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, //right bottom half
				0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, //right top half
				1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, //front bottom half
				0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, //front top half
				0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, //left bottom half
				0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, //left top half
				0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, //underneath bottom half
				0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, //underneath top half
				0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, //above bottom half
				0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f  //above top half
		};
		
		float[] midTcHold = {
				0.5f, 1.0f, 0.5f, 0.0f, 0.0f, 0.0f, //back side bottom half
				0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f, //back side top half
				0.5f, 0.0f, 0.0f, 0.0f, 0.5f, 1.0f, //right bottom half
				0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f, //right top half
				1.0f, 0.0f, 0.5f, 0.0f, 1.0f, 1.0f, //front bottom half
				0.5f, 0.0f, 0.5f, 1.0f, 1.0f, 1.0f, //front top half
				0.5f, 0.0f, 0.0f, 0.0f, 0.5f, 1.0f, //left bottom half
				0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f, //left top half
				0.0f, 1.0f, 0.5f, 1.0f, 0.5f, 0.0f, //underneath bottom half
				0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, //underneath top half
				0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, //above bottom half
				0.0f, 1.0f, 0.5f, 1.0f, 0.5f, 0.0f  //above top half				
		};
		
		base = baseHold.clone();
		mid = base.clone();
		head = base.clone();
		
		baseTc = baseTcHold.clone();
		midTc = midTcHold.clone();
		headTc = midTcHold.clone();
		
		baseLoc.set(0.0f, 0.0f, 0.0f);
		midLoc.set(0.0f, 1.0f, 0.0f);
		headLoc.set(0.0f, 2.0f, 0.0f);
		
		baseTexture = Utils.loadTexture("Textures/snow.jpg");
		midTexture = Utils.loadTexture("Textures/snowMid1.jpg");
		headTexture = Utils.loadTexture("Textures/snowHead.jpg");
		
	}
	
	//GETTERS
	public float[] getBase() {
		return base;
	}
	
	public float[] getMid() {
		return mid;
	}
	
	public float[] getHead() {
		return head;
	}
	
	public float[] getBaseTc() {
		return baseTc;
	}
	
	public float[] getMidTc() {
		return midTc;
	}
	
	public float[] getHeadTc() {
		return headTc;
	}
	
	public int getBaseTexture() {
		return baseTexture;
	}
	
	public int getMidTexture() {
		return midTexture;
	}
	
	public int getHeadTexture() {
		return headTexture;
	}
	
	public Vector3f getBaseLoc() {
		return baseLoc;
	}
	
	public Vector3f getMidLoc() {
		return midLoc;
	}
	
	public Vector3f getHeadLoc() {
		return headLoc;
	}
	
}
