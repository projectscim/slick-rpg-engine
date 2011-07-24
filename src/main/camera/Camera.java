package main.camera;

import interfaces.Updateable;
import main.Engine;
import main.camera.effects.AbstractCameraEffect;
import main.constants.Globals;
import entities.AbstractEntity;

/**
 * Simple camera. Needs to be locked on an entity, e.g. the player in most
 * cases. The camera also supports effects, like screen shaking for example.
 * 
 * @author Benny
 */
public class Camera implements Updateable {

	private float x, y;

	private boolean active = true;

	private AbstractEntity entity = null;
	private AbstractCameraEffect nextEffect = null;

	/**
	 * Creates a new {@link Camera}, following <code>entity</code>
	 * 
	 * @param entity
	 *            - the {@link AbstractEntity} to follow
	 */
	public Camera(AbstractEntity entity) {
		this.entity = entity;
		Engine.setCamera(this);
	}

	@Override
	public void update(int delta) {
		if (!active) {
			return;
		}

		// calculate x
		x = entity.getX() - (Globals.APP_WIDTH / 2);
		if (x < 0) {
			x = 0;
		}
		if (x >= Engine.getMap().getWidth() - Globals.APP_WIDTH) {
			x = Engine.getMap().getWidth() - Globals.APP_WIDTH;
		}

		// calculate y
		y = entity.getY() - (Globals.APP_HEIGHT / 2);
		if (y < 0) {
			y = 0;
		}
		if (y >= Engine.getMap().getHeight() - Globals.APP_HEIGHT) {
			y = Engine.getMap().getHeight() - Globals.APP_HEIGHT;
		}

		// play the queued effect if there is one
		if (nextEffect != null) {
			if (!nextEffect.isFinished()) {
				nextEffect.update(delta);
			} else {
				nextEffect = null;
			}
		}
	}

	public boolean isIdle() {
		return nextEffect == null ? true : false;
	}

	public void playEffect(AbstractCameraEffect effect) {
		nextEffect = effect;
		nextEffect.setCamera(this);
	}

	/**
	 * Returns this camera's followed entity object.
	 * 
	 * @return {@link AbstractEntity}
	 */
	public AbstractEntity getEntity() {
		return entity;
	}

	/**
	 * Sets this camera's followed entity.
	 * 
	 * @param entity
	 *            - an {@link AbstractEntity}
	 */
	public void setEntity(AbstractEntity entity) {
		this.entity = entity;
	}

	/**
	 * Returns the calculated X position of the camera.
	 * 
	 * @return float
	 */
	public float getX() {
		return x;
	}

	/**
	 * Returns the calculated Y position of the camera.
	 * 
	 * @return float
	 */
	public float getY() {
		return y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
