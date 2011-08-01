package tools;

import interfaces.TiledMapTransition;
import interfaces.TransitionAction;
import main.Engine;
import main.constants.MapObjects;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class MapChangeTransition implements TiledMapTransition {

	private TransitionAction afterCompleteAction = null;
	private TransitionAction afterFadeOutAction = null;

	private Color color = Color.black;

	private int fadeTime = 250;

	private boolean completeFadeOut = false;
	private boolean completeFadeIn = false;

	public MapChangeTransition(TransitionAction afterFadeOut, TransitionAction afterComplete) {
		Engine.getEntity(MapObjects.ENTITY_PLAYER).setCanMove(false);

		afterFadeOutAction = afterFadeOut;
		afterCompleteAction = afterComplete;
	}

	@Override
	public void executeAfterCompletion() {
		afterCompleteAction.execute();
	}

	@Override
	public boolean isComplete() {
		return (completeFadeIn && completeFadeOut);
	}

	@Override
	public void update(StateBasedGame game, GameContainer container, int delta) throws SlickException {
		if (!completeFadeOut) {
			color.a += delta * (1.0f / fadeTime);
			if (color.a > 1) {
				color.a = 1;
				completeFadeOut = true;
				afterFadeOutAction.execute();
			}
		}

		if (!completeFadeIn && completeFadeOut) {
			color.a -= delta * (1.0f / fadeTime);
			if (color.a < 0) {
				color.a = 0;
				completeFadeIn = true;
			}
		}
	}

	@Override
	public void postRender(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
		Color old = g.getColor();
		g.setColor(color);
		g.fillRect(0, 0, container.getWidth() * 2, container.getHeight() * 2);
		g.setColor(old);
	}

	@Override
	public void init(GameState firstState, GameState secondState) {
	}

	@Override
	public void preRender(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
	}

}
