package scripts;

import java.util.HashMap;

import main.Engine;
import main.constants.Globals;
import main.constants.MapObjects;

import org.newdawn.slick.Sound;

public class DoorScript extends AbstractTileScript {

	private States state = States.CLOSED;

	private enum States {
		OPEN, CLOSED
	}

	public DoorScript(Sound sound, HashMap<String, Object> data) {
		super(sound, data);
	}

	@Override
	public void execute() {
		int layer = Integer.valueOf(getData().get("layerIndex").toString());

		if (Engine.getEntity(MapObjects.ENTITY_PLAYER).getDistance(getScriptBlock()) <= 50) {
			if (state == States.CLOSED) {
				state = States.OPEN;
				getSound().play();

				int newTileID = Integer.valueOf(getData().get("newID").toString());

				Engine.getMap().getTiledMap().setTileId((int) getScriptBlock().getX() / Globals.TILE_SIZE, (int) getScriptBlock().getY() / Globals.TILE_SIZE, layer, newTileID);
			}
		} else {
			if (state == States.OPEN) {
				state = States.CLOSED;
				getSound().play();

				int oldTileID = Integer.valueOf(getData().get("originalID").toString());
				Engine.getMap().getTiledMap().setTileId((int) getScriptBlock().getX() / Globals.TILE_SIZE, (int) getScriptBlock().getY() / Globals.TILE_SIZE, layer, oldTileID);
			}
		}

	}
}
