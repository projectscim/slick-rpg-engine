package tools;

import main.Engine;

import org.newdawn.slick.Graphics;

public class StaticUtils {

	/**
	 * Draws a string centered on the graphic context.
	 * 
	 * @param g
	 *            - the {@link Graphics} object
	 * @param str
	 *            - the string to draw
	 * @param x
	 *            - the x position
	 * @param y
	 *            - the y position
	 */
	public static void drawCentered(Graphics g, String str, float x, float y) {
		int strWidth = Engine.getContainer().getDefaultFont().getWidth(str);

		g.drawString(str, x - strWidth / 2, y);
	}

}
