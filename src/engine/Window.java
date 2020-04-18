package engine;

// ref @ https://www.glfw.org/docs/3.3/quick.html

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;


// Wrapping the window to make an engine :
// https://www.glfw.org/docs/3.0/group__window.html
public class Window {

	private int width,height,share;
	private long monitor;
	private String title;
	private long windowId;
	
	// https://www.glfw.org/docs/3.3/monitor_guide.html
	private GLFWVidMode videoMode;
	
	public Window(int width,int height,String title) {
		this.width = width;
		this.height = height;
		this.title = title;
		this.monitor = 0;
		this.share = 0;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getWindowId() {
		return windowId;
	}

	public void setWindowId(long windowId) {
		this.windowId = windowId;
	}

	public void create() {
		
		//https://www.glfw.org/docs/3.3/group__init.html#ga317aac130a235ab08c6db0834907d85e
		if (!GLFW.glfwInit()) {
			System.err.println("Error at the glfw initialisation time");
			System.exit(-1);
		}
		
		// initialization is passed, let's code:
		// uncomment for full screen :
		// this.monitor = GLFW.glfwGetPrimaryMonitor();
		this.monitor = 0;
		this.windowId = GLFW.glfwCreateWindow(this.width, this.height, this.title, this.monitor, this.share);
		
		this.monitor = GLFW.glfwGetPrimaryMonitor();
		if (this.windowId == 0) {
			System.err.println("Error at the glfw window creation time");
			System.exit(-1);
		}
		
		// set the video mode :
		
		this.videoMode = GLFW.glfwGetVideoMode(this.monitor);
		GLFW.glfwSetWindowPos(this.windowId, (this.videoMode.width() - this.width)/2,
				(this.videoMode.height() - this.height)/ 2);
		
		//GLFW.glfwSetWindowSizeLimits(this.windowId, 
		//		this.width, this.height, this.width, this.height);
		
		System.out.println(this.videoMode.width() + " x " + this.videoMode.height());
		System.out.println(this.width + ", " + this.height);
		
		GLFW.glfwShowWindow(this.windowId);
	}
	
	public void showWindow() {
		GLFW.glfwShowWindow(this.windowId);
	}
	
	public boolean isClosedWindow() {
		return GLFW.glfwWindowShouldClose(this.windowId);
	}
	
	public void update() {
		GLFW.glfwPollEvents();
	}
	
	// https://www.glfw.org/docs/3.0/group__context.html
	// https://www.glfw.org/docs/3.3/window_guide.html
	public void swapBuffers() {
		GLFW.glfwSwapBuffers(this.windowId);
	}
	public void destroy() {
		GLFW.glfwTerminate();
	}
	
}
