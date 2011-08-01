package main;

import main.constants.Globals;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.StateBasedGame;

import states.LoadingState;
import states.WorldState;

public class Starter extends StateBasedGame {

	public Starter(String title) {
		super(title);
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		LoadingList.setDeferredLoading(true);

		Engine.init(this, container);

		addState(new LoadingState());
		addState(new WorldState());

		enterState(Globals.STATE_LOADING);
	}

	public static void main(String[] args) {
		try {
			AppGameContainer container = new AppGameContainer(new Starter(Globals.APP_NAME), Globals.APP_WIDTH, Globals.APP_HEIGHT, false);
			container.setVSync(true);
			container.setShowFPS(false);
			container.setTargetFrameRate(60);
			container.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
