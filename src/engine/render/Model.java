package engine.render;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Model {
	
	private int vertexArrayID, vertexBufferID, vertexCount;
	private float[] vertices;
	
	public Model(float vertice[])
	{
		this.vertices = vertice;
		// we expect the vertices be a 3 dimensional vector
		// so the vertex count will reflect the number of point
		this.vertexCount = this.vertices.length/3;
	}
	
	public void create()
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(this.vertices.length);
		buffer.put(vertices);
		// set the limits and reset the position after relative operation 
		// https://docs.oracle.com/javase/8/docs/api/java/nio/Buffer.html
		buffer.flip();
		// https://www.youtube.com/watch?v=Bcs56Mm-FJY
		// source code https://github.com/LWJGL/lwjgl/blob/master/src/templates/org/lwjgl/opengl/GL30.java#L1039
		// imply annotation processing : https://www.jmdoudoux.fr/java/dej/chap-annotations.htm
		// The original primitive dont return nothin actually
		// ( https://www.khronos.org/registry/OpenGL-Refpages/gl4/ )
		// so it could be a factory which wrap the boiling sequence and 
		// just return guid for the array (keeping the association) 
		this.vertexArrayID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(this.vertexArrayID);
		this.vertexBufferID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vertexBufferID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glEnableVertexAttribArray(0);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL30.glBindVertexArray(0);
		GL20.glDisableVertexAttribArray(0);
	}
	
	public void remove()
	{
		GL30.glDeleteVertexArrays(this.vertexArrayID);
		GL30.glDeleteBuffers(this.vertexBufferID);
	}

	public int getVertexArrayID() {
		return vertexArrayID;
	}

	public void setVertexArrayID(int vertexArrayID) {
		this.vertexArrayID = vertexArrayID;
	}

	public int getVertexBufferID() {
		return vertexBufferID;
	}

	public void setVertexBufferID(int vertexBufferID) {
		this.vertexBufferID = vertexBufferID;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void setVertexCount(int vertexCount) {
		this.vertexCount = vertexCount;
	}
	
	

}
