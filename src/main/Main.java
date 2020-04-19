package main;

import org.lwjgl.glfw.GLFW;

import engine.Window;

public class Main {

	public static void main(String[] args) {
		Window window = new Window(300,150,60,"ma fenetre");
		window.create();
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
	}

}
