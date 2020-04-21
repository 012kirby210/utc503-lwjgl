package main;

import org.lwjgl.glfw.GLFW;

import engine.Window;
import engine.render.Model;
import engine.render.Renderer;

import cnam.Polygone;
import org.joml.Vector3f;

public class Main {

	public static void main(String[] args) {
		Window window = new Window(300,300,60,"ma fenetre");
		window.setBackgroundColor(1.0f, 0.0f, 0.0f);
		window.create();
		
		Renderer renderer = new Renderer();
		
		//  +  .  +      |    +  .  
		//     .       --.--     .
		//     .  +      |    +  .  +
		//
		float[] model_ = new float[] { 
				-0.5f, 0.5f, 0.0f,
				0.5f, 0.5f, 0.0f,
				0.5f, -0.5f, 0.0f,
				
				/*-0.5f, -0.5f, 0.0f,
				0.5f, -0.5f, 0.0f,
				-0.5f, 0.5f, 0.0f,*/
		};
		
		// get a polygone with 6 side and a 0.5f of side length.
		Polygone p = new Polygone(7,0.5f);
		Vector3f[] triangles = p.triangulate();
		
		//Model model = new Model(model_);
		//model.create();
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
