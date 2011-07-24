package interfaces;

/**
 * A camera effect can be added to the current camera of the world and will be
 * executed in the next update cycle. A camera effect can be a shake from an
 * explosion for example.
 * 
 * @author Benny
 */
public interface CameraEffect {

	/**
	 * Executes/plays this camera effect.
	 * 
	 * @param delta
	 *            - ms since last update
	 */
	public void execute(int delta);

}
