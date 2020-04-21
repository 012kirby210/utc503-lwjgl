package cnam;

import cnam.interfaces.ITriangulation;

import java.util.Arrays;

import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * 
 * Les polygones construits sont des polygones réguliers.
 * Convexe.
 * @author 012kirby210@gmail.com
 *
 */
public class Polygone implements ITriangulation {
	private float edge_length;
	private int edge_number;
	private Vector2f[] coordinates;
	
	/**
	 * Constructeur par défaut d'un polygone.
	 * 
	 * Définit la distance inter-sommets
	 * à 1.0f et le nombre de coté à 4.
	 * 
	 */
	public Polygone() {
		this(4,1.0f);
	}
	
	/**
	 * Constructeur d'un polygone.
	 * 
	 * @param distance la distance inter-sommets.
	 */
	public Polygone(float distance) {
		this(4,distance);
	}
	
	/**
	 * Constructeur d'un polygone.
	 * @param number le nombre de cotés.
	 */
	public Polygone(int number) {
		this(number,1.0f);
	}
	
	/**
	 * Constructeur d'un polygone.
	 * @param number le nombre de coté.
	 * @param distance la distance inter-sommets.
	 */
	public Polygone(int number, float distance) {
		float abs_distance = Math.abs(distance);
		int abs_number = Math.abs(number);
		distance = abs_distance == 0 ? 1.0f : abs_distance;
		number = abs_number > 2 ? abs_number : 3;
		this.edge_length = 	distance;
		this.edge_number = number;
		this.coordinates = new Vector2f[number];
		
		this.computeCoordinates();
	}
	
	
	/**
	 * @return the edge_length
	 */
	public float getEdge_length() {
		return edge_length;
	}

	/**
	 * @param edge_length the edge_length to set
	 */
	public void setEdge_length(float edge_length) {
		this.edge_length = edge_length;
	}

	/**
	 * @return the edge_number
	 */
	public int getEdge_number() {
		return edge_number;
	}

	/**
	 * @param edge_number the edge_number to set
	 */
	public void setEdge_number(int edge_number) {
		this.edge_number = edge_number;
	}

	
	
	public Vector3f[] triangulate() {
		
		int vertices_number = this.coordinates.length;
		// Foreach split by a diagonal, any two subsequent subset S1, S2
		// is composes by n1 + n2 - 2 = n vertices where n is the total number 
		// of vertices.
		// Colinearly, 2 triangles is composed by 3 + 3 vertices and 
		// any square-gone is splitable into 2 triangles.
		// using induction and reccurence n_triangle = n_vertices - 2
		int triangles_number = (vertices_number - 2);
		float z = 0.0f;
		// every Triangles is composed by 3 vertices:
		Vector3f[] result = new Vector3f[3*triangles_number];
		Vector3f starting_vertex = new Vector3f(this.coordinates[0].x,this.coordinates[0].y,z); 
		for (int i =1,j=0; i<vertices_number-1; i++) {
			// we take the next 2 vertices from i and with the starting one
			// it will make a triangle.
			Vector3f middle_vertex = new Vector3f(this.coordinates[i].x, this.coordinates[i].y,z);
			Vector3f last_vertex = new Vector3f(this.coordinates[i+1].x, this.coordinates[i+1].y,z);
			
			// store the triangle :
			result[j++] = starting_vertex;
			result[j++] = middle_vertex;
			result[j++] = last_vertex;
		}
		return result;
	}

	@Override
	public String toString() {
		return "Polygone [edge_length=" + edge_length + ", edge_number=" + edge_number + ", coordinates="
				+ Arrays.toString(coordinates) + "]";
	}
	
	// *****************************************
	//     P R I V A T E
	// *****************************************
	
	/**
	 * Set the coordinates of the polygon.
	 */
	private void computeCoordinates()
	{
		// We assume the center on 0,0
		// Algo : we use the trigonometric circle
		// We set the first vertex @ cos(2PI)
		// Then we put the other using the trancendants 
		// functions.
		// Then we use homothetie to scale it.
		double x,y;

		// for a scale of 1, (the trigo circle radius)
		// we got a side length of :
		// 
		// let's say radius = R , and A = Math.cos(2*Math.PI/this.edge_number)
		// then cosined_radius = R*A
		// let's say E is the inter vertices distance
		// [ By Pythagore ]
		// then E² = R² - R²A² + (R - RA)² = R² -R²A² + R²(1-A)²
		// it follows E² = 2R²(1-A) 
		// which is equivalent to R² = E²/(2(1-A))
		// then R = E/sqrt(2*(1-A))
		double A = Math.cos(2*Math.PI/this.edge_number);
		double radius_scaled = this.edge_length / Math.sqrt(2*(1-A));
		
		for (int i=0; i < this.edge_number; i++) {
			x = radius_scaled * Math.cos(2*Math.PI * i/this.edge_number);
			y = radius_scaled * Math.sin(2*Math.PI * i/this.edge_number);
			Vector2f v = new Vector2f((float) x,(float) y);
			this.coordinates[i] = v;
		}
		
	}

	/**
	 * @return the coordinates
	 */
	public Vector2f[] getCoordinates() {
		return coordinates;
	}

	/**
	 * @param coordinates the coordinates to set
	 */
	public void setCoordinates(Vector2f[] coordinates) {
		this.coordinates = coordinates;
	}
	
	
}
