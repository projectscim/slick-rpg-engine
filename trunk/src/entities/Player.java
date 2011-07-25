package entities;

import main.Engine;
import main.constants.Controls;
import main.constants.Globals;
import main.constants.MapObjects;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

public class Player extends AbstractEntity {

	public static final float PLAYER_SPEED = 2f;

	public Player(SpriteSheet sheet) {
		super(sheet);
		setId(MapObjects.ENTITY_PLAYER);
		setSpeed(new Vector2f(PLAYER_SPEED, PLAYER_SPEED));
	}

	public Player(Image image) {
		super(image);
		setId(MapObjects.ENTITY_PLAYER);
		setSpeed(new Vector2f(PLAYER_SPEED, PLAYER_SPEED));
	}

	@Override
	protected void initAnimations(SpriteSheet sheet) {
		Animation animation = new Animation(false);
		animation.setLooping(true);
		animation.addFrame(sheet.getSubImage(0, 0), FRAME_DURATION);
		animation.addFrame(sheet.getSubImage(1, 0), FRAME_DURATION);
		animation.addFrame(sheet.getSubImage(2, 0), FRAME_DURATION);
		getAnimations().put(Globals.ANIMATION_DOWN, animation);

		animation = new Animation(false);
		animation.setLooping(true);
		animation.addFrame(sheet.getSubImage(0, 3), FRAME_DURATION);
		animation.addFrame(sheet.getSubImage(1, 3), FRAME_DURATION);
		animation.addFrame(sheet.getSubImage(2, 3), FRAME_DURATION);
		getAnimations().put(Globals.ANIMATION_UP, animation);

		animation = new Animation(false);
		animation.setLooping(true);
		animation.addFrame(sheet.getSubImage(0, 1), FRAME_DURATION);
		animation.addFrame(sheet.getSubImage(1, 1), FRAME_DURATION);
		animation.addFrame(sheet.getSubImage(2, 1), FRAME_DURATION);
		getAnimations().put(Globals.ANIMATION_LEFT, animation);

		animation = new Animation(false);
		animation.setLooping(true);
		animation.addFrame(sheet.getSubImage(0, 2), FRAME_DURATION);
		animation.addFrame(sheet.getSubImage(1, 2), FRAME_DURATION);
		animation.addFrame(sheet.getSubImage(2, 2), FRAME_DURATION);
		getAnimations().put(Globals.ANIMATION_RIGHT, animation);
	}

	@Override
	public void detectScreenLeaving() {
		if (getY() + getHeight() / 2 > Engine.getMap().getHeight() && getDirection() == Globals.DIRECTION_DOWN) {
			if (Engine.getMap().hasTransition(Globals.DIRECTION_DOWN)) {
				Engine.getMap().transition(Globals.DIRECTION_DOWN);
			} else {
				setY(Engine.getMap().getHeight() - getHeight() / 2);
			}
		} else if (getY() + getHeight() / 2 < 0 && getDirection() == Globals.DIRECTION_UP) {
			if (Engine.getMap().hasTransition(Globals.DIRECTION_UP)) {
				Engine.getMap().transition(Globals.DIRECTION_UP);
			} else {
				setY(0 - getHeight() / 2);
			}
		} else if (getX() + getWidth() / 2 > Engine.getMap().getWidth() && getDirection() == Globals.DIRECTION_RIGHT) {
			if (Engine.getMap().hasTransition(Globals.DIRECTION_RIGHT)) {
				Engine.getMap().transition(Globals.DIRECTION_RIGHT);
			} else {
				setX(Engine.getMap().getWidth() - getWidth() / 2);
			}
		} else if (getX() + getWidth() / 2 < 0 && getDirection() == Globals.DIRECTION_LEFT) {
			if (Engine.getMap().hasTransition(Globals.DIRECTION_LEFT)) {
				Engine.getMap().transition(Globals.DIRECTION_LEFT);
			} else {
				setX(0 - getWidth() / 2);
			}
		}
	}

	@Override
	public void update(int delta) {
		super.update(delta);

		setMoving(false);

		if (Engine.isInputDown(Controls.MOVE_LEFT) && getCollidingEntity(Globals.DIRECTION_LEFT) == null) {
			moveX(true);
		}
		if (Engine.isInputDown(Controls.MOVE_RIGHT) && getCollidingEntity(Globals.DIRECTION_RIGHT) == null) {
			moveX(false);
		}
		if (Engine.isInputDown(Controls.MOVE_UP) && getCollidingEntity(Globals.DIRECTION_UP) == null) {
			moveY(true);
		}
		if (Engine.isInputDown(Controls.MOVE_DOWN) && getCollidingEntity(Globals.DIRECTION_DOWN) == null) {
			moveY(false);
		}
	}

}
