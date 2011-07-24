package entities.blocks;

import org.newdawn.slick.Color;

import entities.AbstractEntity;

/**
 * Invisible entity in the game world. It's solid and thus no normal entity can
 * walk through it.
 * 
 * @author Benny
 */
public class Block extends AbstractEntity {

	public Block(float x, float y, float width, float height) {
		super(x, y, width, height);
		setColor(Color.red);
		setFillMode(true);
		setStationary(true);
	}

}
