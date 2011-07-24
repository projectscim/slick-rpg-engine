package interfaces;

import org.newdawn.slick.Graphics;

/**
 * Any renderable entity should implement this interface.
 * 
 * @author Benny
 */
public interface Renderable extends Updateable {

	/**
	 * Renders this entity.
	 * 
	 * @param g
	 *            - Graphic context
	 */
	public void render(Graphics g);

}
