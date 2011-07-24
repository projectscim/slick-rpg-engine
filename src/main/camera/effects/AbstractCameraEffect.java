package main.camera.effects;

import interfaces.CameraEffect;
import interfaces.Updateable;
import main.camera.Camera;

/**
 * An abstract camera effect to make the implementation of new camera effects
 * easier.
 * 
 * @author Benny
 */
public abstract class AbstractCameraEffect implements CameraEffect, Updateable {

	protected Camera camera = null;

	private boolean finished = false;
	private int duration, curDuration;

	/**
	 * Creates a new {@link AbstractCameraEffect} with the specified duration.
	 * 
	 * @param duration
	 *            - duration of the effect in ms
	 */
	public AbstractCameraEffect(int duration) {
		this.duration = duration;
	}

	@Override
	public void update(int delta) {
		execute(delta);

		curDuration += delta;
		if (curDuration >= duration) {
			setFinished(true);
		}
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

}
