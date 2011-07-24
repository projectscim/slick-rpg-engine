package main.camera.effects;

/**
 * The shaking effect can be used for explosions or anything alike.
 * 
 * @author Benny
 */
public class ShakingEffect extends AbstractCameraEffect {

	/**
	 * @see AbstractCameraEffect#AbstractCameraEffect(int)
	 */
	public ShakingEffect(int duration) {
		super(duration);
	}

	@Override
	public void execute(int delta) {
		float x = (float) (Math.sin(delta * 1000) * 0.5);
		float y = (float) Math.sin(delta * 1000);
		camera.setX(camera.getX() + x);
		camera.setY(camera.getY() + y);
	}

}
