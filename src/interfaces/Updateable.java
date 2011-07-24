package interfaces;

/**
 * Any updateable entity should implement this interface.
 * 
 * @author Benny
 */
public interface Updateable {

	/**
	 * Updates this entity.
	 * 
	 * @param delta
	 *            - ms since last update
	 */
	public void update(int delta);

}
