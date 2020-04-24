package cnam.tp1;

import cnam.tp1.exception.NotAPolygonException;
import cnam.tp1.interfaces.IComparable;
import cnam.tp1.interfaces.IGeometrique;
import cnam.tp1.interfaces.ITriangulation;

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
public class Polygone implements ITriangulation,IGeometrique,IComparable<Polygone> {
	
	/** le scalaire valant la distance inter-sommet **/
	private float edge_length;
	/** le nombre de cotés du polygône **/
	private int edge_number;
	/** les coordonnées du polygone sur un même plan **/
	private Vector2f[] coordinates;
	/** la valeur du perimètre */
	private float perimeter;
	
	///
	/**		C O N S T R U C T O R S		**/
	///
	///
	
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
		this.perimeter = this.perimetre();
	}
	
	///
	/**		G E T T E R S / S E T T E R S		**/
	///
	///
	
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
		this.perimeter = this.perimetre();
		this.computeCoordinates();
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
	private void setEdge_number(int edge_number) {
		this.edge_number = edge_number;
		this.coordinates = new Vector2f[this.edge_number];
		this.computeCoordinates();
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
	
    ///
	/**		P U B L I C   M E T H O D S 		**/
	///
	///
	
	/**
	 * Construit un nouvel objet avec les caractéristiques
	 * de l'appelant excepté ne nombre de cotés du polygone
	 * paramètrable.
	 * @param n le nombre de coté à ajouter ou à retrancher
	 * @return le nouveau polygone
	 * @throws NotAPolygonException si le nombre de coté 
	 * est insuffisant pour construire nouveau polygone.
	 */
	public Polygone presqueClone(int n) throws NotAPolygonException {
		Polygone p ;
		int new_edge_number = this.edge_number + n;
		if (new_edge_number >= 3 ) {
			p = new Polygone(new_edge_number,this.edge_length);
		}else{
			String conjugatedSideNumber = (new_edge_number == 1)? " coté." : "cotés."; 
			String exceptionMessage = "Le polygône ne peut avoir moins de 3 cotés.\n" 
					+ "Actuellement il dispose de "
					+ new_edge_number
					+ conjugatedSideNumber;
			throw new NotAPolygonException(exceptionMessage);
		}
		return p;
	}
	
	///		I M P L E M E N T E R S 
	
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

	/**
	 * renvoie le perimetre sous forme de float 
	 */
	public float perimetre() {
		float perimeter = 0.0f;
		for (int i=0; i<this.edge_number ; i++) {
			perimeter+=this.edge_length;
		}
		return perimeter;
	}
	
	
	/**
	 * Compare les périmètres des polygones.
	 */
	public boolean estPlusGrandQue(Polygone a) throws NotAPolygonException {
		boolean result = false;
		try {
			Polygone b = (Polygone) a;
			result = this.mesure() > b.mesure(); 
		}catch(ClassCastException e) {
			String exceptionMessage = "L'argument n'est pas un polygone," 
					+ "c'est un/e " + this.getClass().getName();
			throw new NotAPolygonException(exceptionMessage);
		}
		return result;
	}

	/**
	 * Compare les périmètres des polygones.
	 */
	public boolean estPlusPetitQue(Polygone a) throws NotAPolygonException {
		boolean result = false;
		try {
			Polygone b = (Polygone) a;
			result = this.mesure() < b.mesure(); 
		}catch(ClassCastException e) {
			String exceptionMessage = "L'argument n'est pas un polygone," 
					+ "c'est un/e " + this.getClass().getName();
			throw new NotAPolygonException(exceptionMessage);
		}
		return result;
	}

	public double mesure() {
		return this.perimeter;
	}

	///
	/** 	P U B L I C   O V E R R I D E   	 **/
	///
	///
	
	@Override
	public String toString() {
		return "Polygone [edge_length=" + edge_length + ", edge_number=" + edge_number + ", coordinates="
				+ Arrays.toString(coordinates) 
				+ ", perimeter="
				+ this.perimeter 
				+ "]";
	}
	
	
	///
	/**		P R I V A T E    	**/
	///
	///
	
	/**
	 * Set the coordinates of the polygon.
	 */
	private void computeCoordinates() {
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
	
}
