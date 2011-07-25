package entities;

import interfaces.Entity;

import java.util.HashMap;

import main.Engine;
import main.constants.Globals;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public abstract class AbstractEntity implements Entity {

	public static final int FRAME_DURATION = 120;

	private Vector2f speed = new Vector2f(0, 0);
	private Vector2f maxSpeed = new Vector2f(0, 0);
	// private Vector2f acceleration = new Vector2f(.2f, .2f);

	private int direction = Globals.DIRECTION_UP;

	private float x, y;
	private float width, height;
	private float scale = 1.0f;
	private float alpha = 1.0f;
	private float rotation = 0;

	private boolean fillMode = false;
	private boolean solid = true;
	private boolean stationary = false;
	private boolean moving = false;
	private boolean canMove = true;

	private String id = "undefined";
	private Image image;
	private Color color = Color.white;
	private Rectangle body;

	private Animation currentAnimation;
	private HashMap<String, Animation> animations = new HashMap<String, Animation>();

	public AbstractEntity(Image image) {
		this(image, 0, 0, image.getWidth(), image.getHeight());
	}

	public AbstractEntity(SpriteSheet sheet) {
		this(sheet.getSprite(0, 0), 0, 0, sheet.getSprite(0, 0).getWidth(), sheet.getSprite(0, 0).getHeight());

		initAnimations(sheet);
	}

	public AbstractEntity(Image image, float x, float y, float width, float height) {
		this(x, y, width, height);
		this.image = image;
	}

	public AbstractEntity(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		initEntity();
	}

	private void initEntity() {
		Engine.addEntity(this);
		body = new Rectangle(x, y, width, height);
	}

	protected void initAnimations(SpriteSheet sheet) {
		return;
	}

	public float getDistance(AbstractEntity other) {
		float dx = getBody().getCenterX() - other.getBody().getCenterX();
		float dy = getBody().getCenterY() - other.getBody().getCenterY();

		float distance = (float) Math.sqrt(dx * dx + dy * dy);

		return distance;
	}

	public Entity getCollidingEntity(int direction) {
		for (int i = 0; i < Engine.getEntities().size(); i++) {
			AbstractEntity entity = Engine.getEntities().get(i);

			// simple logical checks
			if (this.equals(entity) || !entity.isSolid()) {
				continue;
			}

			// two stationary entities can never collide
			if (this.isStationary() && entity.isStationary()) {
				continue;
			}

			float x = getX();
			float y = getY();

			switch (direction) {
				case Globals.DIRECTION_UP:
					y -= speed.y;
					break;
				case Globals.DIRECTION_DOWN:
					y += speed.y;
					break;
				case Globals.DIRECTION_LEFT:
					x -= speed.x;
					break;
				case Globals.DIRECTION_RIGHT:
					x += speed.x;
					break;
			}

			if (x + body.getWidth() > entity.x && y + body.getHeight() > entity.y && x < entity.x + entity.getBody().getWidth() && y < entity.y + entity.getBody().getHeight()) {
				this.onCollision(entity);
				entity.onCollision(this);

				return entity;
			}
		}

		return null;
	}

	@Override
	public void render(Graphics g) {
		Color c = new Color(color.r, color.g, color.b, alpha);

		if (currentAnimation != null) {
			currentAnimation.getCurrentFrame().setRotation(rotation);
			currentAnimation.draw(x, y, c);
		} else if (image != null) {
			image.draw(x, y, scale, c);
		} else {
			Color oldColor = g.getColor();
			if (fillMode) {
				g.setColor(c);
				g.fill(body);
			} else {
				g.setColor(c);
				g.draw(body);
			}
			g.setColor(oldColor);
		}

		if (Engine.isDebug()) {
			g.draw(body);
		}
	}

	@Override
	public void update(int delta) {
		detectScreenLeaving();

		if (animations.size() > 0) {
			switch (direction) {
				case Globals.DIRECTION_RIGHT:
					currentAnimation = animations.get(Globals.ANIMATION_RIGHT);
					break;
				case Globals.DIRECTION_LEFT:
					currentAnimation = animations.get(Globals.ANIMATION_LEFT);
					break;
				case Globals.DIRECTION_UP:
					currentAnimation = animations.get(Globals.ANIMATION_UP);
					break;
				case Globals.DIRECTION_DOWN:
					currentAnimation = animations.get(Globals.ANIMATION_DOWN);
					break;
			}

			if (currentAnimation != null) {
				if (moving) {
					currentAnimation.update(delta);
				} else {
					currentAnimation.setCurrentFrame(1);
				}
			}
		}

		updateBody();
	}

	public void moveX(boolean invert) {
		if (!canMove) {
			return;
		}

		if (invert) {
			direction = Globals.DIRECTION_LEFT;
			adjustX(-speed.x);
		} else {
			direction = Globals.DIRECTION_RIGHT;
			adjustX(speed.x);
		}

		setMoving(true);
	}

	public void moveY(boolean invert) {
		if (!canMove) {
			return;
		}

		if (invert) {
			direction = Globals.DIRECTION_UP;
			adjustY(-speed.y);
		} else {
			direction = Globals.DIRECTION_DOWN;
			adjustY(speed.y);
		}

		setMoving(true);
	}

	protected void updateBody() {
		body.setBounds(x, y, width, height);
	}

	@Override
	public void onCollision(Entity other) {
		// TODO implement basic collision response for every entity
	}

	public void detectScreenLeaving() {
		return;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void adjustX(float value) {
		this.x += value;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void adjustY(float value) {
		this.y += value;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Rectangle getBody() {
		return body;
	}

	public void setBody(Rectangle body) {
		this.body = body;
	}

	public boolean isFillMode() {
		return fillMode;
	}

	public void setFillMode(boolean fillMode) {
		this.fillMode = fillMode;
	}

	public boolean isSolid() {
		return solid;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public boolean isStationary() {
		return stationary;
	}

	public void setStationary(boolean stationary) {
		this.stationary = stationary;
	}

	public Vector2f getSpeed() {
		return speed;
	}

	public void setSpeed(Vector2f speed) {
		this.speed = speed;
		maxSpeed.x = speed.x;
		maxSpeed.y = speed.y;
	}

	public HashMap<String, Animation> getAnimations() {
		return animations;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public int getDirection() {
		return direction;
	}

	public boolean isCanMove() {
		return canMove;
	}

	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

}
