package main;

import org.lwjgl.glfw.GLFW;

import engine.Window;
import engine.render.Model;
import engine.render.Renderer;
import cnam.tp1.Polygone;
import cnam.tp1.exception.NotAPolygonException;

import org.joml.Vector3f;

public class Main {

	public static void main(String[] args) {
		Window window = new Window(300,300,60,"TP1 Cnam");
		int[] bgcolor = {0, 96, 100};
		window.setBackgroundColor(bgcolor[0]/255.0f, bgcolor[1]/255.0f, bgcolor[2]/255.0f);
		window.create();
		
		Renderer renderer = new Renderer();
		
		Polygone p = new Polygone(10,0.5f);
		Vector3f[] triangles = p.triangulate();
		
		//
		//	 Utilisation de presque clone.
		//
		//
		
		Polygone p2 = null;
		try {
			p2 = p.presqueClone(+10);
			p2.setEdge_length(0.3f);
		}catch(NotAPolygonException e) {
			System.out.println(e.getMessage());
		}
		
		triangles = p2.triangulate();
		
		//
		// 	Utilisation de estPlusPetitQue
		//
		//
		
		try {
			triangles = p2.estPlusPetitQue(p) ? 
					p2.triangulate() : p.triangulate();
		}catch(Exception e) {
			// fade away 
			System.out.println(e.getMessage());
			triangles = p.triangulate();
		}
		
		Model model = new Model(triangles);
		model.create();
		System.out.println(model.toString());
		
		System.out.println(p.toString());
		
		window.showWindow();
		
		// game loop : <=> tant que la fenetre n'est pas considérée comme fermée.
		while(!window.isClosedWindow()) {
			// do the update only when required : 
			if (window.requireUpdate()) {
				// put the computed frame to the display
				// start computing the back buffer
				// https://www.glfw.org/docs/3.3/group__window.html#ga15a5a1ee5b3c2ca6b15ca209a12efd14
				window.swapBuffers();
				// poll for event.
				window.update();
				renderer.render(model);
				// check for inputs :
				if (window.isKeyPressed(GLFW.GLFW_KEY_A)) {
					System.out.println("the key A was pressed");
				}
				if (window.isKeyReleased(GLFW.GLFW_KEY_A)) {
					System.out.println("the key A was released");
				}
				if (window.isMousePressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
					// where was the cursor once left button was pressed
					double x_position = window.getMouseX();
					double y_position = window.getMouseY();
					System.out.println("The mouse button left was pressed at the location : " +
					  " [ " + x_position + ", " + y_position + " ] ");
				}
				if (window.isMouseReleased(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
					// where was the cursor once the left button was left
					double x_position = window.getMouseX();
					double y_position = window.getMouseY();
					System.out.println("The mouse button left was released at the location : " +
					  " [ " + x_position + ", " + y_position + " ] ");
				}

			}
			
		}
		model.remove();
		window.safeTermination();
	}

}
