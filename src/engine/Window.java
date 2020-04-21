package engine;

import java.nio.DoubleBuffer;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

// ref @ https://www.glfw.org/docs/3.3/quick.html

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

// Wrapping the window to make an engine :
// https://www.glfw.org/docs/3.0/group__window.html
public class Window {

	private int width, height, share, fps;
	private double frameTimeOffset, lastFrameTime;
	private long monitor;
	private String title;
	private long windowId;
	private Vector3f backgroundColor;

	private boolean keyPressed[];
	private boolean mousePressed[];

	// debug :
	private int frameNumber;

	// https://www.glfw.org/docs/3.3/monitor_guide.html
	private GLFWVidMode videoMode;

	public Window(int width, int height, int fps, String title) {
		this.width = width;
		this.height = height;
		this.title = title;
		this.monitor = 0;
		this.share = 0;
		this.keyPressed = new boolean[GLFW.GLFW_KEY_LAST];
		this.mousePressed = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
		this.fps = fps;
		this.frameTimeOffset = 1.0 / this.fps;

		//
		this.frameNumber = 0;
		this.backgroundColor = new Vector3f(0.0f, 0.0f, 0.0f);
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

		// https://www.glfw.org/docs/3.3/group__init.html#ga317aac130a235ab08c6db0834907d85e
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

		// the this.share variable wasn't set to any context, so let's associate the
		// opengl context to
		// the this.windowId identified window
		// https://www.lwjgl.org/guide ;
		// https://www.glfw.org/docs/3.1/group__context.html#ga1c04dc242268f827290fe40aa1c91157
		GLFW.glfwMakeContextCurrent(this.windowId);
		GL.createCapabilities();

		// set the video mode :

		this.videoMode = GLFW.glfwGetVideoMode(this.monitor);
		GLFW.glfwSetWindowPos(this.windowId, (this.videoMode.width() - this.width) / 2,
				(this.videoMode.height() - this.height) / 2);

		// GLFW.glfwSetWindowSizeLimits(this.windowId,
		// this.width, this.height, this.width, this.height);

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
		// the update frame is split into 2 sections

		// PAST EVENTS
		// record all the pressed key
		//
		for (int i = 0; i < GLFW.GLFW_KEY_LAST; i++) {
			this.keyPressed[i] = this.isKeyDown(i);
		}

		// record all the mouse button pressed
		for (int i = 0; i < GLFW.GLFW_MOUSE_BUTTON_LAST; i++) {
			this.mousePressed[i] = this.isMouseDown(i);
		}

		// all past key events are recorded before that line

		// CURRENT EVENTS
		GLFW.glfwPollEvents();

		this.signalFrameProcess();
		this.frameNumber++;
		// specify what color to use when the clear's method are called
		// https://developer.mozilla.org/en-US/docs/Web/API/WebGLRenderingContext/clearColor
		GL30.glClearColor(this.backgroundColor.x, this.backgroundColor.y, this.backgroundColor.y, 1.0f);
		// force the color_buffer_bit to be cleared
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}

	// https://www.glfw.org/docs/3.0/group__context.html
	// https://www.glfw.org/docs/3.3/window_guide.html
	public void swapBuffers() {
		GLFW.glfwSwapBuffers(this.windowId);
	}

	public void destroy() {
		GLFW.glfwTerminate();
	}

	// it's better to use the callback event system :
	// https://www.glfw.org/docs/latest/input_guide.html
	// but for now it will so the stuff
	// https://www.glfw.org/docs/3.2/group__input.html
	public boolean isKeyDown(int keyCode) {
		boolean is_down = GLFW.glfwGetKey(this.windowId, keyCode) == GLFW.GLFW_PRESS;
		return is_down;
	}

	public boolean isMouseDown(int mouseButton) {
		boolean is_down = GLFW.glfwGetMouseButton(this.windowId, mouseButton) == GLFW.GLFW_PRESS;
		return is_down;
	}

	public boolean isKeyPressed(int keyCode) {
		boolean is_pressed = GLFW.glfwGetKey(this.windowId, keyCode) == GLFW.GLFW_PRESS;
		// the update window function will set the keyPressed array array before polling
		// for event
		// so the producer/consumer scheme will be handled with a forced way anti-lock
		// pattern semaphores
		return is_pressed && !this.keyPressed[keyCode];
	}

	public boolean isKeyReleased(int keyCode) {
		boolean is_released = GLFW.glfwGetKey(this.windowId, keyCode) == GLFW.GLFW_RELEASE;
		return is_released && this.keyPressed[keyCode];
	}

	public boolean isMousePressed(int mouseButton) {
		boolean is_pressed = GLFW.glfwGetMouseButton(this.windowId, mouseButton) == GLFW.GLFW_PRESS;
		// the update window function will set the keyPressed array array before polling
		// for event
		// so the producer/consumer scheme will be handled with a forced way anti-lock
		// pattern semaphores
		return is_pressed && !this.mousePressed[mouseButton];
	}

	public boolean isMouseReleased(int mouseButton) {
		boolean is_released = GLFW.glfwGetMouseButton(this.windowId, mouseButton) == GLFW.GLFW_RELEASE;
		return is_released && this.mousePressed[mouseButton];
	}

	public double getMouseX() {
		DoubleBuffer x_position = BufferUtils.createDoubleBuffer(1);
		GLFW.glfwGetCursorPos(this.windowId, x_position, null);
		return x_position.get();
	}

	public double getMouseY() {
		DoubleBuffer y_position = BufferUtils.createDoubleBuffer(1);
		GLFW.glfwGetCursorPos(this.windowId, null, y_position);
		return y_position.get();
	}

	public double getTime() {
		return ((double) System.nanoTime()) / 1000000000;
	}

	public void signalFrameProcess() {
		this.lastFrameTime = this.getTime();
	}

	// signal the game loop that it needs to compute the frame
	public boolean requireUpdate() {
		double current_frame_time = this.getTime();
		double roof_time = this.lastFrameTime + this.frameTimeOffset;
		return current_frame_time > roof_time;
	}

	public void setBackgroundColor(float r, float g, float b) {
		this.backgroundColor.set(r, g, b);
	}

	public void safeTermination() {
		GLFW.glfwTerminate();
	}

}
