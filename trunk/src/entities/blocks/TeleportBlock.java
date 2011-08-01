package entities.blocks;

import interfaces.Block;
import main.Engine;
import main.constants.Globals;
import main.constants.MapObjects;

import org.newdawn.slick.Color;

import tools.ResourceManager;

import entities.AbstractEntity;

/**
 * The teleporter is defined as an object in the tiled map. It rotates and
 * lowers the alpha of the player until he's invisible. The player is then
 * teleported to the new location and of course made visible again.
 * 
 * @author Benny
 */
public class TeleportBlock extends AbstractEntity implements Block {

	private float targetX, targetY;

	public TeleportBlock(float x, float y, int width, int height) {
		super(x, y, width, height);
		setColor(Color.green);
		setSolid(false);
		setStationary(true);
		setFillMode(true);
	}

	@Override
	public void update(int delta) {
		super.update(delta);

		AbstractEntity player = Engine.getEntity(MapObjects.ENTITY_PLAYER);
		if (getDistance(player) <= 10) {
			if (!ResourceManager.getSound("teleport").playing()) {
				ResourceManager.getSound("teleport").play();
			}

			player.setCanMove(false);
			player.setRotation(player.getRotation() + 1 * delta);
			player.setAlpha(player.getAlpha() - .02f);
			player.setDirection(Globals.DIRECTION_DOWN);
			player.setX(getX());
			player.setY(getY());

			if (player.getAlpha() <= 0) {
				player.setX(targetX);
				player.setY(targetY);
				player.setAlpha(1);
				player.setCanMove(true);
				player.setRotation(0);
			}
		}
	}

	public float getTargetX() {
		return targetX;
	}

	public void setTargetX(float targetX) {
		this.targetX = targetX * Globals.TILE_SIZE;
	}

	public float getTargetY() {
		return targetY;
	}

	public void setTargetY(float targetY) {
		this.targetY = targetY * Globals.TILE_SIZE;
	}

}
