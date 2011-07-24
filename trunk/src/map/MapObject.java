package map;

/**
 * A <code>MapObject</code> is an object on a <code>Map</code>, e.g. the start
 * point of the player entity or a teleporter.
 * 
 * @author Benny
 */
public class MapObject {

	private String type;
	private float x, y;
	private float width, height;

	public MapObject(String type, float x, float y, float width, float height) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
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
}
