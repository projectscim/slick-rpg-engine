package main;

import interfaces.Block;
import interfaces.TiledMapTransition;

import java.util.HashMap;
import java.util.Vector;

import main.camera.Camera;
import main.constants.Controls;
import main.constants.MapObjects;
import map.Map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.Transition;

import tools.ResourceManager;
import entities.AbstractEntity;

/**
 * Static utility main class of the Engine. Keeps track of all the entities and
 * objects in the world.
 * 
 * @author Benny
 */
public class Engine {

	private static Map map = null;
	private static StateBasedGame game = null;
	private static GameContainer container = null;
	private static Camera camera = null;

	private static HashMap<String, int[]> inputs = new HashMap<String, int[]>();
	private static Vector<AbstractEntity> entities = new Vector<AbstractEntity>();

	private static Transition transition = null;

	private static boolean debug = false;

	/**
	 * Loads a map from a file.
	 * 
	 * @param string
	 *            - the reference to the map
	 * @param setActive
	 *            - wether to set this map active
	 */
	public static void loadMap(String string, boolean setActive) {
		clearEntities();

		try {
			new Map(string, setActive);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see Engine#loadMap(String, boolean)
	 */
	public static void loadMap(String string) {
		Engine.loadMap(string, true);
	}

	/**
	 * Clears all the entities from the current world except for the actual
	 * player.
	 */
	public static void clearEntities() {
		AbstractEntity player = getEntity(MapObjects.ENTITY_PLAYER);
		entities.removeAllElements();
		entities.add(player);
	}

	/**
	 * Defines controls with the associated key codes.
	 * 
	 * @param command
	 *            - the command, like "jump" or "move up"
	 * @param codes
	 *            - the input code, use constants from {@link Input}
	 */
	public static void defineControl(String command, int... codes) {
		inputs.put(command, codes);
	}

	/**
	 * Adds an entity to the game world.
	 * 
	 * @param e
	 *            - the entity to add
	 */
	public static void addEntity(AbstractEntity e) {
		if (entities.contains(e)) {
			return;
		}

		entities.add(e);
	}

	/**
	 * Returns all current entities of the game world.
	 * 
	 * @return {@link Vector}
	 */
	public static Vector<AbstractEntity> getEntities() {
		return entities;
	}

	/**
	 * Returns the entity with the specific ID or <code>null</code> if no entity
	 * was found.
	 * 
	 * @param id
	 *            - the unique entity ID
	 * @return {@link AbstractEntity}
	 */
	public static AbstractEntity getEntity(String id) {
		for (int i = 0; i < entities.size(); i++) {
			AbstractEntity entity = (AbstractEntity) entities.get(i);
			if (entity.getId().equals(id)) {
				return entity;
			}
		}

		return null;
	}

	/**
	 * Inits the engine. Does basic stuff like font setup.
	 * 
	 * @param game
	 *            - the {@link StateBasedGame} object
	 * @param container
	 *            - the {@link GameContainer} of the app
	 * @throws SlickException
	 */
	public static void init(StateBasedGame game, GameContainer container) throws SlickException {
		Engine.game = game;
		Engine.container = container;

		ResourceManager.load();

		container.setDefaultFont(ResourceManager.getFont("visitor"));
	}

	/**
	 * Renders the current map file layer by layer.
	 * 
	 * @param g
	 *            - graphic context
	 * @throws SlickException
	 */
	public static void render(Graphics g) throws SlickException {
		g.setFont(container.getDefaultFont());

		if (camera != null) {
			g.translate(-camera.getX(), -camera.getY());
		}

		if (transition != null) {
			transition.preRender(game, container, g);
		}

		map.render(MapObjects.LAYER_ONE);
		map.render(MapObjects.LAYER_TWO);

		// render all the entities but blocks
		for (int i = 0; i < entities.size(); i++) {
			AbstractEntity entity = (AbstractEntity) entities.get(i);
			if (!(entity instanceof Block)) {
				entity.render(g);
			}
		}

		map.render(MapObjects.LAYER_THREE);
		map.render(MapObjects.LAYER_FOUR);

		// render blocks for debug purposes
		if (isDebug()) {
			for (int i = 0; i < entities.size(); i++) {
				AbstractEntity entity = (AbstractEntity) entities.get(i);
				if (entity instanceof Block) {
					entity.render(g);
				}
			}
		}

		map.render(MapObjects.LAYER_COLLISION);

		if (transition != null) {
			transition.postRender(game, container, g);
		}

		if (camera != null) {
			g.translate(camera.getX(), camera.getY());
		}

		// render debug info
		if (isDebug()) {
			g.drawString("Entities: " + Engine.getEntities().size(), 10, 30);
			g.drawString("Moveable: " + Engine.getEntity(MapObjects.ENTITY_PLAYER).isCanMove(), 10, 50);
		}
	}

	/**
	 * Updates the current map file, including all entities.
	 * 
	 * @param container
	 *            - the {@link GameContainer}
	 * @param delta
	 *            - ms since last update
	 * @throws SlickException
	 */
	public static void update(GameContainer container, int delta) throws SlickException {
		if (isInputPressed(Controls.DEBUG)) {
			setDebug(!isDebug());
		}

		if (transition != null) {
			transition.update(game, container, delta);
			if (transition.isComplete()) {
				if (transition instanceof TiledMapTransition) {
					((TiledMapTransition) transition).executeAfterCompletion();
				}
				transition = null;
			}
		}

		camera.update(delta);

		container.setShowFPS(isDebug());

		for (int i = 0; i < entities.size(); i++) {
			AbstractEntity entity = (AbstractEntity) entities.get(i);
			entity.update(delta);
		}
	}

	/**
	 * Sets the current map to render/use.
	 * 
	 * @param map
	 *            - a {@link Map} object
	 */
	public static void setMap(Map map) {
		Engine.map = map;
	}

	/**
	 * Returns the current <code>Map</code>
	 * 
	 * @return {@link Map}
	 */
	public static Map getMap() {
		return map;
	}

	/**
	 * Returns true if a specific command is down, that means one of the
	 * associated input keys is hold down.
	 * 
	 * @param command
	 *            - the command to check
	 * @return boolean
	 */
	public static boolean isInputDown(String command) {
		int[] checked = inputs.get(command);
		if (checked == null) {
			return false;
		}

		for (int i = 0; i < checked.length; i++) {
			if (container.getInput().isKeyDown(checked[i])) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns true if a speficic command is pressed, that means one of the
	 * associated input keys was pressed.
	 * 
	 * @param command
	 *            - the command to check
	 * @return boolean
	 */
	public static boolean isInputPressed(String command) {
		int[] checked = inputs.get(command);
		if (checked == null) {
			return false;
		}

		for (int i = 0; i < checked.length; i++) {
			if (container.getInput().isKeyPressed(checked[i])) {
				return true;
			} else if (checked[i] == Input.MOUSE_LEFT_BUTTON || checked[i] == Input.MOUSE_RIGHT_BUTTON) {
				if (container.getInput().isMousePressed(checked[i])) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Sets the global debug key to toggle the debug mode.
	 * 
	 * @param code
	 *            - constant of {@link Input} class
	 */
	public static void setDebugKey(int code) {
		Engine.defineControl(Controls.DEBUG, code);
	}

	/**
	 * Returns wether debug mode is currently on.
	 * 
	 * @return boolean
	 */
	public static boolean isDebug() {
		return debug;
	}

	/**
	 * Sets the debug mode.
	 * 
	 * @param debug
	 *            - true to switch debug mode on
	 */
	private static void setDebug(boolean debug) {
		Engine.debug = debug;
	}

	/**
	 * Returns the active camera object or null if no cam exists.
	 * 
	 * @return {@link Camera}
	 */
	public static Camera getCamera() {
		return camera;
	}

	/**
	 * Sets a new camera for this world.
	 * 
	 * @param camera
	 *            - a {@link Camera} object
	 */
	public static void setCamera(Camera camera) {
		Engine.camera = camera;
	}

	public static Transition getTransition() {
		return transition;
	}

	public static void setTransition(Transition transition) {
		Engine.transition = transition;
	}

	public static GameContainer getContainer() {
		return container;
	}

}
