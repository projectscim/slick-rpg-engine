package entities.blocks;

import interfaces.Block;

import org.newdawn.slick.Color;

import scripts.AbstractTileScript;
import entities.AbstractEntity;

/**
 * A ScriptBlock executes special scripts within the game. This can be a door
 * which opens automatically when the player is approaching. This entity is
 * invisible and non-solid by default.
 * 
 * @author Benny
 */
public class ScriptBlock extends AbstractEntity implements Block {

	private AbstractTileScript script = null;

	public ScriptBlock(float x, float y, float width, float height) {
		super(x, y, width, height);
		setSolid(false);
		setColor(Color.yellow);
		setFillMode(true);
		setStationary(true);
	}

	@Override
	public void update(int delta) {
		super.update(delta);

		if (!script.isComplete()) {
			script.execute();
		}
	}

	/**
	 * Returns the associated {@link AbstractTileScript} of this block.
	 * 
	 * @return {@link AbstractTileScript}
	 */
	public AbstractTileScript getScript() {
		return script;
	}

	/**
	 * Sets the {@link AbstractTileScript} this block should use/execute.
	 * 
	 * @param script
	 *            - the {@link AbstractTileScript} to use
	 */
	public void setScript(AbstractTileScript script) {
		this.script = script;
		script.setScriptBlock(this);
	}

}
