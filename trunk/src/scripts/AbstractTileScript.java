package scripts;

import interfaces.TileScript;

import java.util.HashMap;

import org.newdawn.slick.Sound;

import entities.blocks.ScriptBlock;

/**
 * A basic implementation of a tile script. Designed to create new scripts
 * easier and provide a common basis.
 * 
 * @author Benny
 */
public abstract class AbstractTileScript implements TileScript {

	private HashMap<String, Object> data = null;

	private ScriptBlock scriptBlock = null;
	private Sound sound = null;

	private boolean looping = false;
	private boolean complete = false;

	public AbstractTileScript(Sound sound, HashMap<String, Object> additionalData) {
		this.sound = sound;
		this.data = additionalData;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public Sound getSound() {
		return sound;
	}

	public void setSound(Sound sound) {
		this.sound = sound;
	}

	public ScriptBlock getScriptBlock() {
		return scriptBlock;
	}

	public void setScriptBlock(ScriptBlock scriptBlock) {
		this.scriptBlock = scriptBlock;
	}

	public boolean isLooping() {
		return looping;
	}

	public void setLooping(boolean looping) {
		this.looping = looping;
	}

	public HashMap<String, Object> getData() {
		return data;
	}

	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}

}
