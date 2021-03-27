package Camera;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class Camera {
	private float x;
	private float y;
	private float z;
	private float moveSpeed = 0.5f;
	
	private Vector4f c = new Vector4f(); //the position vector
	private Vector4f u = new Vector4f(); //u vector
	private Vector4f v = new Vector4f(); //v vector
	private Vector4f n = new Vector4f(); //n vector
	
	public Camera() {
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
		c.set(x, y, z, 1);
		u.set(1.0f, 0.0f, 0.0f, 0.0f);
		v.set(0.0f, 1.0f, 0.0f, 0.0f);
		n.set(0.0f, 0.0f, 1.0f, 0.0f);
	}
	
	public Camera(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		c.set(x, y, z, 1);
		u.set(1.0f, 0.0f, 0.0f, 0.0f);
		v.set(0.0f, 1.0f, 0.0f, 0.0f);
		n.set(0.0f, 0.0f, 1.0f, 0.0f);
	}
	
	public Camera(float x, float y, float z, Vector4f u, Vector4f v, Vector4f n) {
		this.x = x;
		this.y = y;
		this.z = z;
		c.set(x, y, z, 1);
		this.u = u;
		this.v = v;
		this.n = n;
	}
	
	//Setters
	public void setX(float x) {
		this.x = x;
		c.set(x, y, z, 1);
	}
	
	public void setY(float y) {
		this.y = y;
		c.set(x, y, z, 1);
	}
	
	public void setZ(float z) {
		this.z = z;
		c.set(x, y, z, 1);
	}
	
	public void setU(Vector4f u) {
		this.u = u;
	}
	
	public void setV(Vector4f v) {
		this.v = v;
	}
	
	public void setN(Vector4f n) {
		this.n = n;
	}
	
	//Getters
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	public float getZ() {
		return z;
	}
	
	public Vector4f getC() {
		return c;
	}
	
	//Return C but with x,y,z as negatives
	public Vector4f getNegC() {
		Vector4f neg = new Vector4f();
		neg.set(-1.0f * x, -1.0f * y, -1.0f * z, 1);
		return neg;
	}
	
	public Vector4f getU() {
		return u;
	}
	
	public Vector4f getV() {
		return v;
	}
	
	public Vector4f getN() {
		return n;
	}
	
	//returns translation matrix
	public Matrix4f getTranslationMatrix() {
		Matrix4f translationMatrix = new Matrix4f();
		Vector4f negativeC = getNegC(); //negative xyz values
		
		//create translation matrix (set translation from JOML)
		translationMatrix.setTranslation(negativeC.x(), negativeC.y(), negativeC.z());
		
		return translationMatrix;
	}
	
	////returns rotation matrix 
	public Matrix4f getRotationMatrix() {
		Matrix4f rotationMatrix = new Matrix4f();
		Vector4f bottomRow = new Vector4f();
		
		//Create the rotation matrix with u,v,n in rows
		bottomRow.set(0.0f, 0.0f, 0.0f, 1.0f);
		rotationMatrix.setRow(0, u);
		rotationMatrix.setRow(1, v);
		rotationMatrix.setRow(2, n);
		rotationMatrix.setRow(3, bottomRow);
		
		return rotationMatrix;
	}
	
	public Matrix4f getViewingTransform() {
		return getRotationMatrix().mul(getTranslationMatrix());
	}
	
	//movement functions
	//positive u direction
	public void moveRight() {
		/*
		c.set(c.add(u));
		x = c.x();
		*/
		x += moveSpeed;
		c.set(x, y, z, 1);
	}
	
	//negative u direction
	public void moveLeft() {
		/*
		c.set(c.sub(u));
		x = c.x();
		*/
		x -= moveSpeed;
		c.set(x, y, z, 1);
	}
	
	//positive n direction
	public void moveForward() { 
		/*
		c.set(c.add(n));
		z = c.z();
		*/
		z += moveSpeed;
		c.set(x, y, z, 1);
	}
	
	//negative n direction
	public void moveBackward() {
		/*
		c.set(c.sub(n));
		z = c.z();
		*/
		z -= moveSpeed;
		c.set(x, y, z, 1);
	}
	
	//positive v direction
	public void moveUp() {
		/*
		c.set(c.add(v));
		y = c.y();
		*/
		y += moveSpeed;
		c.set(x, y, z, 1);
	}
	
	//negative v direction
	public void moveDown() {
		/*
		c.set(c.sub(v));
		y = c.y();
		*/
		y -= moveSpeed;
		c.set(x, y, z, 1);
	}
	
	//Yaw right
	public void rotateRight() {
		//rotating just by a random angle in radians
		u.rotateY((float) (Math.PI/15.0));
		n.rotateY((float) (Math.PI/15.0));
	}
	
	//Yaw left
	public void rotateLeft() {
		u.rotateY((float) (-1.0f * (Math.PI/15.0)));
		n.rotateY((float) (-1.0f * (Math.PI/15.0)));
	}
	
	//Pitch up
	public void rotateUp() {
		v.rotateX((float) (Math.PI/15.0));
		n.rotateX((float) (Math.PI/15.0));
	}
	
	//pitch down
	public void rotateDown() {
		v.rotateX((float) (-1.0f * (Math.PI/15.0)));
		n.rotateX((float) (-1.0f * (Math.PI/15.0)));
	}
	
	/*
	public void zoom(int mouseWheel) {
		z += mouseWheel;
		c.set(x, y, z, 1);
	} */
}
