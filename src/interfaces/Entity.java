package interfaces;

/**
 * An object in the game world. Could be a player, an enemy, an item, etc.
 * 
 * @author Benny
 */
public interface Entity extends Renderable {

	/**
	 * This entity's response to a collision with another entity.
	 * 
	 * @param other
	 *            - the other colliding entity
	 */
	public void onCollision(Entity other);

}
