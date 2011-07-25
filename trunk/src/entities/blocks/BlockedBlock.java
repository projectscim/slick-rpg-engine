package entities.blocks;

import interfaces.Block;

import org.newdawn.slick.Color;

import entities.AbstractEntity;

/**
 * This entity indicates where no normal entity can walk.
 * 
 * @author Benny
 */
public class BlockedBlock extends AbstractEntity implements Block {

	public BlockedBlock(float x, float y, float width, float height) {
		super(x, y, width, height);
		setColor(Color.red);
		setFillMode(true);
		setStationary(true);
	}

}
