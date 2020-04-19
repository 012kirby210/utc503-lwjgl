package engine.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import engine.render.Model;

public class Renderer {

	public void render(Model model)
	{
		GL30.glBindVertexArray(model.getVertexArrayID());
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
		GL30.glBindVertexArray(0);
	}
}
