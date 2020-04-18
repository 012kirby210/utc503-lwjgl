package main;

import engine.Window;

public class Main {

	public static void main(String[] args) {
		Window window = new Window(200,150,"ma fenetre");
		window.create();
		window.showWindow();
		
		// game loop : <=> tant que la fenetre n'est pas considérée comme fermée.
		while(!window.isClosedWindow()) {
			// put the computed frame to the display
			// start computing the back buffer
			// https://www.glfw.org/docs/3.3/group__window.html#ga15a5a1ee5b3c2ca6b15ca209a12efd14
			window.swapBuffers();
			// poll for event.
			window.update();
		}
	}

}
