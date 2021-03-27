package a2;

import Commands.*;
import Models.Floor;
import Models.Snowman;
import Models.XAxis;
import Models.YAxis;
import Models.ZAxis;

/*import java.awt.event.KeyEvent;
import com.jogamp.common.nio.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.nio.*;
*/

import javax.swing.*;
import org.joml.Matrix4f;

import static com.jogamp.opengl.GL4.*;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.GLContext;
import java.nio.FloatBuffer;
import java.lang.Math;
import org.joml.*;
import com.jogamp.opengl.util.*;

import Camera.Camera;

//add ", MouseWheelListener" for mouse wheel listens
@SuppressWarnings("serial")
public class Starter extends JFrame implements GLEventListener
{	private GLCanvas myCanvas;
	private int renderingProgram;
	private int vao[] = new int[1];
	private int vbo[] = new int[20];
	private Camera camera;
	
	private Floor floor;
	private Snowman snowman;
	private ImportedModel santa;
	private int santaText;
	private ImportedModel sleigh;
	private int sleighText;
	
	private boolean showAxes;
	private XAxis x;
	private YAxis y;
	private ZAxis z;
	
	//values based on elapsed time
	private double startTime = 0.0f;
	private double elapsedTime;
	private double time;
	private double movement = 1.0;
	private boolean hidden = false;
	
	//For the display function
	private Matrix4fStack mvStack = new Matrix4fStack(3);
	private FloatBuffer values = Buffers.newDirectFloatBuffer(16);
	private Matrix4f perspMat = new Matrix4f(); //perspective matrix
	private Matrix4f viewMat = new Matrix4f(); //view matrix
	private Matrix4f modelMat = new Matrix4f(); //model matrix
	private Matrix4f modviewMat = new Matrix4f(); //model-view matrix
	private int modviewLoc, projLoc;
	private float aspect;
	

	public Starter()
	{	setTitle("Assignment 2");
		setSize(1280, 720);
		showAxes = true;
		myCanvas = new GLCanvas();
		myCanvas.addGLEventListener(this);
		this.add(myCanvas);
		this.setVisible(true);
		Animator animator = new Animator(myCanvas);
		animator.start();
		//this.addMouseWheelListener(this);
		setupControls();
		startDialog();
	}
	
	//Dialog for stating the controls
	public void startDialog() {
		System.out.println("\nDon't move the camera yet! Let Santa come back to town!\nControls:\n------------------");
		System.out.println("W: Move Forward\nS: Move Backwards\nA: Strafe Left");
		System.out.println("D: Strafe Right\nQ: Move Up\nE: Move Down");
		System.out.println("Up Arrow: Pitch Up\nDown Arrow: Pitch Down");
		System.out.println("Left Arrow: Pan Left\nRight Arrow: Pan Right\nSpace: Show/Turn off Axes");
	}

	public void display(GLAutoDrawable drawable)
	{	GL4 gl = (GL4) GLContext.getCurrentGL();
		gl.glClear(GL_COLOR_BUFFER_BIT);
		gl.glClear(GL_DEPTH_BUFFER_BIT);
		gl.glUseProgram(renderingProgram);
		gl.glClearColor(colorClamp(10.0f), colorClamp(161.0f), colorClamp(255.0f), 1.0f); //blue background color
		
		//Calculate elapsed time
		elapsedTime = System.currentTimeMillis() - startTime;
		time = elapsedTime/1000.0;
		
		modviewLoc = gl.glGetUniformLocation(renderingProgram, "mv_matrix");
		projLoc = gl.glGetUniformLocation(renderingProgram, "proj_matrix");
		
		aspect = (float) myCanvas.getWidth() / (float) myCanvas.getHeight();
		
		perspMat.setPerspective((float) Math.toRadians(60.0f), aspect, 0.1f, 1000.0f);
		
		viewMat.set(camera.getViewingTransform()); //Camera class computes view matrix
		
		//Draw Axes
		
		
		//draw floor with buffer 0-1----------------------------------
		modelMat.translation(floor.getWorldLoc());
		
		modviewMat.identity();
		modviewMat.mul(viewMat);
		modviewMat.mul(modelMat);
		
		gl.glUniformMatrix4fv(modviewLoc, 1, false, modviewMat.get(values));
		gl.glUniformMatrix4fv(projLoc, 1, false, perspMat.get(values));
		
		//floor indices
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); //3 for coming in pairs of 3
		gl.glEnableVertexAttribArray(0); //uniform variable location 0
		
		//floor texture coordinates
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
		gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0); //2 for coming in pairs of 2
		gl.glEnableVertexAttribArray(1); //uniform variable location 1
		
		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D, floor.getTextureFile());
		
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);
		
		gl.glDrawArrays(GL_TRIANGLES, 0, 6);
		//-----------------------------------------
		
		//draw snowman mid with buffer 2-3 -----------------------------
		modelMat.translation(-0.05f, 0.85f, 0.0f);
		modelMat.scale(0.35f);

		modviewMat.identity();
		modviewMat.mul(viewMat);
		modviewMat.mul(modelMat);
		
		gl.glUniformMatrix4fv(modviewLoc, 1, false, modviewMat.get(values));
		gl.glUniformMatrix4fv(projLoc, 1, false, perspMat.get(values));
		
		//snowman mid indices
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[2]);
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); //3 for coming in pairs of 3
		gl.glEnableVertexAttribArray(0); //uniform variable location 0
		
		//snowman mid texture coordinates
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[3]);
		gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0); //3 for coming in pairs of 3
		gl.glEnableVertexAttribArray(1); //uniform variable location 1
		
		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D, snowman.getMidTexture());
		
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);
		
		gl.glDrawArrays(GL_TRIANGLES, 0, 36);
		
		//draw snowman head with buffer 4-5
		modelMat.translation(-0.08f, 1.45f, 0.0f);
		modelMat.scale(0.25f);

		modviewMat.identity();
		modviewMat.mul(viewMat);
		modviewMat.mul(modelMat);
		
		gl.glUniformMatrix4fv(modviewLoc, 1, false, modviewMat.get(values));
		gl.glUniformMatrix4fv(projLoc, 1, false, perspMat.get(values));
		
		//snowman mid indices
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[4]);
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); //3 for coming in pairs of 3
		gl.glEnableVertexAttribArray(0); //uniform variable location 0
		
		//snowman mid texture coordinates
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[5]);
		gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0); //3 for coming in pairs of 3
		gl.glEnableVertexAttribArray(1); //uniform variable location 1
		
		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D, snowman.getHeadTexture());
		
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);
		
		gl.glDrawArrays(GL_TRIANGLES, 0, 36);
		
		//draw snowman base with buffer 6-7
		modelMat.translation(snowman.getBaseLoc());
		modelMat.scale(0.5f);

		modviewMat.identity();
		modviewMat.mul(viewMat);
		modviewMat.mul(modelMat);
		
		gl.glUniformMatrix4fv(modviewLoc, 1, false, modviewMat.get(values));
		gl.glUniformMatrix4fv(projLoc, 1, false, perspMat.get(values));
		
		//snowman base indices
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[6]);
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); //3 for coming in pairs of 3
		gl.glEnableVertexAttribArray(0); //uniform variable location 0
		
		//snowman mid texture coordinates
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[7]);
		gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0); //2 for coming in pairs of 3
		gl.glEnableVertexAttribArray(1); //uniform variable location 1
		
		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D, snowman.getBaseTexture());
		
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);
		
		gl.glDrawArrays(GL_TRIANGLES, 0, 36);
		
		//clear the mvStack so its back to identity matrix
		mvStack.clear();
		//multiplying by an identity matrix = the matrix
		mvStack.mul(camera.getViewingTransform()); 
		
		//Santa using buffers 8-10 (10 is normal vectors not used here)
		//movement based on elapsed time
		if(Math.sin(time) * 18 > 17.0) {
			hidden = true;
		} else if(Math.sin(time) * 18 < -17.95)
			hidden = false;
		if(hidden)
			movement = -18.0;
		else
			movement = Math.sin(time) * 18;

		mvStack.pushMatrix();
		mvStack.rotate((float)(Math.PI/2), 0.0f, 1.0f, 0.0f);
		mvStack.pushMatrix();
		mvStack.translate(5.0f, 4.7f, (float) movement);
		
		gl.glUniformMatrix4fv(modviewLoc, 1, false, mvStack.get(values));
		gl.glUniformMatrix4fv(projLoc, 1, false, perspMat.get(values));
		
		mvStack.popMatrix(); //Removes translation but keep rotation
		
		//santa indices
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[8]);
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); //3 for coming in pairs of 3
		gl.glEnableVertexAttribArray(0); //uniform variable location 0
				
		//santa texture coordinates
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[9]);
		gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0); //2 for coming in pairs of 3
		gl.glEnableVertexAttribArray(1); //uniform variable location 1
		
		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D, santaText);

		gl.glEnable(GL_DEPTH_TEST);
		gl.glFrontFace(GL_LEQUAL);
		gl.glDrawArrays(GL_TRIANGLES, 0, santa.getNumVertices());
		
		//Sleigh using buffers 11-13 (13 not used)
		mvStack.pushMatrix();
		mvStack.translate(5.0f, 4.3f, (float) movement);
		mvStack.scale(0.008f);

		gl.glUniformMatrix4fv(modviewLoc, 1, false, mvStack.get(values));
		gl.glUniformMatrix4fv(projLoc, 1, false, perspMat.get(values));
		
		mvStack.popMatrix();
		
		//santa indices
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[11]);
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); //3 for coming in pairs of 3
		gl.glEnableVertexAttribArray(0); //uniform variable location 0
				
		//santa texture coordinates
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[12]);
		gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0); //2 for coming in pairs of 3
		gl.glEnableVertexAttribArray(1); //uniform variable location 1
		
		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D, sleighText);

		gl.glEnable(GL_DEPTH_TEST);
		gl.glFrontFace(GL_LEQUAL);
		gl.glDrawArrays(GL_TRIANGLES, 0, sleigh.getNumVertices());
		
		//AXES
		if(showAxes) {
			//X Axis
			modviewMat.identity();
			modviewMat.mul(viewMat);
			modviewMat.mul(modelMat);
		
			gl.glUniformMatrix4fv(modviewLoc, 1, false, modviewMat.get(values));
			gl.glUniformMatrix4fv(projLoc, 1, false, perspMat.get(values));
		
			//snowman base indices
			gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[14]);
			gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); //3 for coming in pairs of 3
			gl.glEnableVertexAttribArray(0); //uniform variable location 0
		
			//snowman mid texture coordinates
			gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[15]);
			gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0); //2 for coming in pairs of 3
			gl.glEnableVertexAttribArray(1); //uniform variable location 1
		
			gl.glActiveTexture(GL_TEXTURE0);
			gl.glBindTexture(GL_TEXTURE_2D, x.getTextureFile());
		
			gl.glEnable(GL_DEPTH_TEST);
			gl.glDepthFunc(GL_LEQUAL);
		
			gl.glDrawArrays(GL_LINES, 0, 2);
			
			//Y Axis
			modviewMat.identity();
			modviewMat.mul(viewMat);
			modviewMat.mul(modelMat);
		
			gl.glUniformMatrix4fv(modviewLoc, 1, false, modviewMat.get(values));
			gl.glUniformMatrix4fv(projLoc, 1, false, perspMat.get(values));
		
			//snowman base indices
			gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[16]);
			gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); //3 for coming in pairs of 3
			gl.glEnableVertexAttribArray(0); //uniform variable location 0
		
			//snowman mid texture coordinates
			gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[17]);
			gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0); //2 for coming in pairs of 3
			gl.glEnableVertexAttribArray(1); //uniform variable location 1
		
			gl.glActiveTexture(GL_TEXTURE0);
			gl.glBindTexture(GL_TEXTURE_2D, y.getTextureFile());
		
			gl.glEnable(GL_DEPTH_TEST);
			gl.glDepthFunc(GL_LEQUAL);
		
			gl.glDrawArrays(GL_LINES, 0, 2);
			
			//Z Axis
			modviewMat.identity();
			modviewMat.mul(viewMat);
			modviewMat.mul(modelMat);
		
			gl.glUniformMatrix4fv(modviewLoc, 1, false, modviewMat.get(values));
			gl.glUniformMatrix4fv(projLoc, 1, false, perspMat.get(values));
		
			//snowman base indices
			gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[18]);
			gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); //3 for coming in pairs of 3
			gl.glEnableVertexAttribArray(0); //uniform variable location 0
		
			//snowman mid texture coordinates
			gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[19]);
			gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0); //2 for coming in pairs of 3
			gl.glEnableVertexAttribArray(1); //uniform variable location 1
		
			gl.glActiveTexture(GL_TEXTURE0);
			gl.glBindTexture(GL_TEXTURE_2D, z.getTextureFile());
		
			gl.glEnable(GL_DEPTH_TEST);
			gl.glDepthFunc(GL_LEQUAL);
		
			gl.glDrawArrays(GL_LINES, 0, 2);
		}
		
	}

	public void init(GLAutoDrawable drawable)
	{	
		santa = new ImportedModel("../Models/santa1.obj");
		santaText = Utils.loadTexture("Textures/santa2.png");
		sleigh = new ImportedModel("../Models/sleigh.obj");
		sleighText = Utils.loadTexture("Textures/santasleigh_texture.bmp");
		createVertices();
		renderingProgram = Utils.createShaderProgram(
				"a2/vertShader.glsl", "a2/fragShader.glsl");
		camera = new Camera(0.0f, 0.0f, 8.0f);
		startTime = System.currentTimeMillis();
		movement = Math.sin(startTime/1000.0) * 18;
	}
	
	private float colorClamp(float x) {
		return x/255.0f;
	}

	public static void main(String[] args) { new Starter(); }
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
	public void dispose(GLAutoDrawable drawable) {}
	
	
	public Camera getCamera() {
		return camera;
	}
	
	public void createVertices() {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		floor = new Floor();
		snowman = new Snowman();
		x = new XAxis();
		y = new YAxis();
		z = new ZAxis();
		
		gl.glGenVertexArrays(vao.length, vao, 0);
		gl.glBindVertexArray(vao[0]);
		gl.glGenBuffers(vbo.length, vbo, 0);
		
		//floor
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
		FloatBuffer floorBuf = Buffers.newDirectFloatBuffer(floor.getIndices());
		gl.glBufferData(GL_ARRAY_BUFFER, floorBuf.limit() * 4, floorBuf, GL_STATIC_DRAW);
		
		//floor texture
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
		FloatBuffer floorTextBuf = Buffers.newDirectFloatBuffer(floor.getTextCord());
		gl.glBufferData(GL_ARRAY_BUFFER, floorTextBuf.limit() * 4, floorTextBuf, GL_STATIC_DRAW);
		
		//snowman mid 
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[2]);
		FloatBuffer snowmanMidBuf = Buffers.newDirectFloatBuffer(snowman.getMid());
		gl.glBufferData(GL_ARRAY_BUFFER, snowmanMidBuf.limit() * 4, snowmanMidBuf, GL_STATIC_DRAW);
		
		//snowman mid texture
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[3]);
		FloatBuffer snowmanMidTexBuf = Buffers.newDirectFloatBuffer(snowman.getMidTc());
		gl.glBufferData(GL_ARRAY_BUFFER, snowmanMidTexBuf.limit() * 4, snowmanMidTexBuf, GL_STATIC_DRAW);
		
		//snowman head
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[4]);
		FloatBuffer snowmanHeadBuf = Buffers.newDirectFloatBuffer(snowman.getHead());
		gl.glBufferData(GL_ARRAY_BUFFER, snowmanHeadBuf.limit() * 4, snowmanHeadBuf, GL_STATIC_DRAW);
				
		//snowman head texture
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[5]);
		FloatBuffer snowmanHeadTexBuf = Buffers.newDirectFloatBuffer(snowman.getHeadTc());
		gl.glBufferData(GL_ARRAY_BUFFER, snowmanHeadTexBuf.limit() * 4, snowmanHeadTexBuf, GL_STATIC_DRAW);
		
		//snowman base
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[6]);
		FloatBuffer snowmanBaseBuf = Buffers.newDirectFloatBuffer(snowman.getBase());
		gl.glBufferData(GL_ARRAY_BUFFER, snowmanBaseBuf.limit() * 4, snowmanBaseBuf, GL_STATIC_DRAW);
						
		//snowman base texture
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[7]);
		FloatBuffer snowmanBaseTexBuf = Buffers.newDirectFloatBuffer(snowman.getBaseTc());
		gl.glBufferData(GL_ARRAY_BUFFER, snowmanBaseTexBuf.limit() * 4, snowmanBaseTexBuf, GL_STATIC_DRAW);
		
		//santa model
		int numSantaVertices = santa.getNumVertices();
		Vector3f[] santaVertices = santa.getVertices();
		Vector2f[] santaTc = santa.getTexCoords();
		Vector3f[] santaNormals = santa.getNormals();
		
		float[] santaPVals = new float[numSantaVertices * 3];
		float[] santaTVals = new float[numSantaVertices * 2];
		float[] santaNVals = new float[numSantaVertices * 3];
		
		for(int i = 0; i < numSantaVertices; i++) {
			santaPVals[i*3] = (float) (santaVertices[i]).x();
			santaPVals[i*3+1] = (float) (santaVertices[i]).y();
			santaPVals[i*3+2] = (float) (santaVertices[i]).z();
			santaTVals[i*2]   = (float) (santaTc[i]).x();
			santaTVals[i*2+1] = (float) (santaTc[i]).y();
			santaNVals[i*3]   = (float) (santaNormals[i]).x();
			santaNVals[i*3+1] = (float) (santaNormals[i]).y();
			santaNVals  [i*3+2] = (float) (santaNormals[i]).z();
		}
		
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[8]);
		FloatBuffer santaVertBuf = Buffers.newDirectFloatBuffer(santaPVals);
		gl.glBufferData(GL_ARRAY_BUFFER, santaVertBuf.limit() * 4, santaVertBuf, GL_STATIC_DRAW);
		
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[9]);
		FloatBuffer santaTextBuf = Buffers.newDirectFloatBuffer(santaTVals);
		gl.glBufferData(GL_ARRAY_BUFFER, santaTextBuf.limit() * 4, santaTextBuf, GL_STATIC_DRAW);
		
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[10]);
		FloatBuffer santaNormBuf = Buffers.newDirectFloatBuffer(santaNVals);
		gl.glBufferData(GL_ARRAY_BUFFER, santaNormBuf.limit() * 4, santaNormBuf, GL_STATIC_DRAW);
		
		//sleigh model
		int numSleighVertices = sleigh.getNumVertices();
		Vector3f[] sleighVertices = sleigh.getVertices();
		Vector2f[] sleighTc = sleigh.getTexCoords();
		Vector3f[] sleighNormals = sleigh.getNormals();
		
		float[] sleighPVals = new float[numSleighVertices * 3];
		float[] sleighTVals = new float[numSleighVertices * 2];
		float[] sleighNVals = new float[numSleighVertices * 3];
		
		for(int i = 0; i < numSleighVertices; i++) {
			sleighPVals[i*3] = (float) (sleighVertices[i]).x();
			sleighPVals[i*3+1] = (float) (sleighVertices[i]).y();
			sleighPVals[i*3+2] = (float) (sleighVertices[i]).z();
			sleighTVals[i*2]   = (float) (sleighTc[i]).x();
			sleighTVals[i*2+1] = (float) (sleighTc[i]).y();
			sleighNVals[i*3]   = (float) (sleighNormals[i]).x();
			sleighNVals[i*3+1] = (float) (sleighNormals[i]).y();
			sleighNVals  [i*3+2] = (float) (sleighNormals[i]).z();
		}
		
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[11]);
		FloatBuffer sleighVertBuf = Buffers.newDirectFloatBuffer(sleighPVals);
		gl.glBufferData(GL_ARRAY_BUFFER, sleighVertBuf.limit() * 4, sleighVertBuf, GL_STATIC_DRAW);
		
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[12]);
		FloatBuffer sleighTextBuf = Buffers.newDirectFloatBuffer(sleighTVals);
		gl.glBufferData(GL_ARRAY_BUFFER, sleighTextBuf.limit() * 4, sleighTextBuf, GL_STATIC_DRAW);
		
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[13]);
		FloatBuffer sleighNormBuf = Buffers.newDirectFloatBuffer(sleighNVals);
		gl.glBufferData(GL_ARRAY_BUFFER, sleighNormBuf.limit() * 4, sleighNormBuf, GL_STATIC_DRAW);
		
		//AXES
		//X AXIS
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[14]);
		FloatBuffer xAxisBuf = Buffers.newDirectFloatBuffer(x.getIndices());
		gl.glBufferData(GL_ARRAY_BUFFER, xAxisBuf.limit() * 4, xAxisBuf, GL_STATIC_DRAW);
	
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[15]);
		FloatBuffer xAxisTcBuf = Buffers.newDirectFloatBuffer(x.getTextCord());
		gl.glBufferData(GL_ARRAY_BUFFER, xAxisTcBuf.limit() * 4, xAxisTcBuf, GL_STATIC_DRAW);
		
		//Y AXIS
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[16]);
		FloatBuffer yAxisBuf = Buffers.newDirectFloatBuffer(y.getIndices());
		gl.glBufferData(GL_ARRAY_BUFFER, yAxisBuf.limit() * 4, yAxisBuf, GL_STATIC_DRAW);
	
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[17]);
		FloatBuffer yAxisTcBuf = Buffers.newDirectFloatBuffer(y.getTextCord());
		gl.glBufferData(GL_ARRAY_BUFFER, yAxisTcBuf.limit() * 4, yAxisTcBuf, GL_STATIC_DRAW);
		
		//Z AXIS
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[18]);
		FloatBuffer zAxisBuf = Buffers.newDirectFloatBuffer(z.getIndices());
		gl.glBufferData(GL_ARRAY_BUFFER, zAxisBuf.limit() * 4, zAxisBuf, GL_STATIC_DRAW);
	
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[19]);
		FloatBuffer zAxisTcBuf = Buffers.newDirectFloatBuffer(z.getTextCord());
		gl.glBufferData(GL_ARRAY_BUFFER, zAxisTcBuf.limit() * 4, zAxisTcBuf, GL_STATIC_DRAW);
		
	}

	//Set up the controls using the commands from the Command package
	public void setupControls() {
		//new Commands based off the commands in the Command Package
		MoveRightCommand moveRight = new MoveRightCommand(this);
		MoveLeftCommand moveLeft = new MoveLeftCommand(this);
		MoveUpCommand moveUp = new MoveUpCommand(this);
		MoveDownCommand moveDown = new MoveDownCommand(this);
		MoveForwardCommand moveForward = new MoveForwardCommand(this);
		MoveBackwardCommand moveBackward = new MoveBackwardCommand(this);
		RotateRightCommand rotateRight = new RotateRightCommand(this);
		RotateLeftCommand rotateLeft = new RotateLeftCommand(this);
		RotateUpCommand rotateUp = new RotateUpCommand(this);
		RotateDownCommand rotateDown = new RotateDownCommand(this);
		ShowAxesCommand showAxesCmd = new ShowAxesCommand(this);
		
		//get the content pane of the JFrame
		JComponent contentPane = (JComponent) this.getContentPane();
		//get the foxus is in window input map for the content pane
		int mapName = JComponent.WHEN_IN_FOCUSED_WINDOW;
		InputMap imap = contentPane.getInputMap(mapName);
		//Create the keystroke object
		KeyStroke aKey = KeyStroke.getKeyStroke('a');
		KeyStroke dKey = KeyStroke.getKeyStroke('d');
		KeyStroke wKey = KeyStroke.getKeyStroke('w');
		KeyStroke sKey = KeyStroke.getKeyStroke('s');
		KeyStroke eKey = KeyStroke.getKeyStroke('e'); 
		KeyStroke qKey = KeyStroke.getKeyStroke('q');
		KeyStroke upArrowKey = KeyStroke.getKeyStroke("UP");
		KeyStroke downArrowKey = KeyStroke.getKeyStroke("DOWN");
		KeyStroke leftArrowKey = KeyStroke.getKeyStroke("LEFT");
		KeyStroke rightArrowKey = KeyStroke.getKeyStroke("RIGHT");
		KeyStroke spaceKey = KeyStroke.getKeyStroke("SPACE");
		
		//attach the keystroke to the action
		imap.put(aKey, "moveLeft");
		imap.put(dKey, "moveRight");
		imap.put(wKey, "moveForward");
		imap.put(sKey, "moveBackward");
		imap.put(qKey, "moveUp");
		imap.put(eKey, "moveDown");
		imap.put(upArrowKey, "rotateUp");
		imap.put(downArrowKey, "rotateDown");
		imap.put(leftArrowKey, "rotateLeft");
		imap.put(rightArrowKey, "rotateRight");
		imap.put(spaceKey, "showAxes");
		
		// get the action map for the content pane
		ActionMap amap = contentPane.getActionMap();
		//put the commands into the content pane's action map
		amap.put("moveRight", moveRight);
		amap.put("moveLeft", moveLeft);
		amap.put("moveUp", moveUp);
		amap.put("moveDown", moveDown);
		amap.put("moveForward", moveForward);
		amap.put("moveBackward", moveBackward);
		amap.put("rotateUp", rotateUp);
		amap.put("rotateDown", rotateDown);
		amap.put("rotateLeft", rotateLeft);
		amap.put("rotateRight", rotateRight);
		amap.put("showAxes", showAxesCmd);
		//have the JFrame request keyboard focus
		this.requestFocus();
	}
	
	public void showAxes() {
		showAxes = !showAxes;
	}

	/*
	//Handling the mousewheel event in order to 
	//increase/decrease size
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		camera.zoom(e.getWheelRotation());
	} */
}