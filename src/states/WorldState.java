package states;

import main.Engine;
import main.camera.effects.ShakingEffect;
import main.constants.Controls;
import main.constants.Globals;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import tools.ResourceManager;
import entities.Player;

public class WorldState extends BasicGameState {

	@Override
	public int getID() {
		return Globals.STATE_WORLD;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		new Player(new SpriteSheet(ResourceManager.getImage("character3"), 32, 32));

		Engine.loadMap(ResourceManager.getMap("testmap"));

		defineControls();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		Engine.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Engine.update(container, delta);

		// TODO remove debug controls
		if (Engine.isInputPressed(Controls.DEBUG_CAMERA_SHAKING)) {
			Engine.getCamera().playEffect(new ShakingEffect(3000));
		}
	}

	private void defineControls() {
		Engine.setDebugKey(Input.KEY_F1);
		Engine.defineControl(Controls.MOVE_UP, Input.KEY_W, Input.KEY_UP);
		Engine.defineControl(Controls.MOVE_DOWN, Input.KEY_S, Input.KEY_DOWN);
		Engine.defineControl(Controls.MOVE_LEFT, Input.KEY_A, Input.KEY_LEFT);
		Engine.defineControl(Controls.MOVE_RIGHT, Input.KEY_D, Input.KEY_RIGHT);

		// TODO remove debug controls
		Engine.defineControl(Controls.DEBUG_CAMERA_SHAKING, Input.KEY_F2);
	}

}
