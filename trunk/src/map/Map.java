package map;

import interfaces.TransitionAction;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import main.Engine;
import main.camera.Camera;
import main.constants.Globals;
import main.constants.MapObjects;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.tiled.TiledMap;

import scripts.AbstractTileScript;
import tools.MapChangeTransition;
import tools.ResourceManager;
import entities.AbstractEntity;
import entities.blocks.BlockedBlock;
import entities.blocks.ScriptBlock;
import entities.blocks.TeleportBlock;

/**
 * A <code>Map</code> loads and holds a TiledMap object and is responsible for
 * drawing the layers in a appropriate way and order.
 * 
 * @author Benny
 */
public class Map {

	private static HashMap<String, Map> mapsCache = new HashMap<String, Map>();

	private TiledMap map = null;
	private HashMap<Integer, String> transitions = new HashMap<Integer, String>();

	private int widthTiles, heightTiles;
	private int width, height;

	private int entitiesLayerIndex = -1;
	private int maxLayerIndex = -1;

	/**
	 * Creates a new {@link Map}. The file is loaded once and then cached in
	 * case the user visits this map again.
	 * 
	 * @param mapFile
	 *            - the reference to the map file
	 * @param setActive
	 *            - wether to set this map active
	 * @throws SlickException
	 *             - if any errors occure while parsing/loading the map
	 */
	public Map(String mapFile, boolean setActive) throws SlickException {
		if (mapsCache.containsKey(mapFile)) {
			map = mapsCache.get(mapFile).getTiledMap();
		} else {
			map = new TiledMap(mapFile);
		}

		loadAdjacentMaps();
		setBounds();

		maxLayerIndex = map.getLayerCount();
		entitiesLayerIndex = map.getLayerIndex(MapObjects.LAYER_OBJECTS);

		loadObjects();

		if (Engine.getCamera() == null) {
			new Camera(Engine.getEntity(MapObjects.ENTITY_PLAYER));
		}

		if (setActive) {
			Engine.setMap(this);
		}

		addMapToCache(mapFile, this);
	}

	/**
	 * @see Map#Map(String, boolean)
	 */
	public Map(String mapFile) throws SlickException {
		this(mapFile, true);
	}

	/**
	 * Transitions from one map to another if the user walks out of the screen.
	 * 
	 * @param direction
	 *            - a direction constant
	 */
	public void transition(int direction) {
		Engine.getCamera().setActive(false);

		final String map = transitions.get(direction);
		if (map != null) {
			Engine.setTransition(new MapChangeTransition(new TransitionAction() {
				@Override
				public void execute() {
					Engine.getCamera().setActive(true);
					Engine.loadMap(map);
				}
			}, new TransitionAction() {
				@Override
				public void execute() {
					Engine.getEntity(MapObjects.ENTITY_PLAYER).setCanMove(true);
				}
			}));

			if (direction == Globals.DIRECTION_DOWN) {
				Engine.getEntity(MapObjects.ENTITY_PLAYER).setY(0 - Globals.TILE_SIZE / 2);
			} else if (direction == Globals.DIRECTION_UP) {
				Engine.getEntity(MapObjects.ENTITY_PLAYER).setY(Map.getFromCache(map).getHeight() - Globals.TILE_SIZE / 2);
			} else if (direction == Globals.DIRECTION_RIGHT) {
				Engine.getEntity(MapObjects.ENTITY_PLAYER).setX(0 - Globals.TILE_SIZE / 2);
			} else if (direction == Globals.DIRECTION_LEFT) {
				Engine.getEntity(MapObjects.ENTITY_PLAYER).setX(Map.getFromCache(map).getWidth() - Globals.TILE_SIZE / 2);
			}
		}
	}

	/**
	 * Loads the adjacent map files of the current map, if there any defined
	 */
	private void loadAdjacentMaps() {
		String[] ways = new String[] { "north", "east", "south", "west" };
		for (int i = 0; i < ways.length; i++) {
			String way = ways[i];
			String newMap = map.getMapProperty(way, null);
			if (newMap != null) {
				if (way.equals("north")) {
					transitions.put(Globals.DIRECTION_UP, ResourceManager.getMap(newMap));
				} else if (way.equals("east")) {
					transitions.put(Globals.DIRECTION_RIGHT, ResourceManager.getMap(newMap));
				} else if (way.equals("south")) {
					transitions.put(Globals.DIRECTION_DOWN, ResourceManager.getMap(newMap));
				} else if (way.equals("west")) {
					// transitions.put(Globals.DIRECTION_LEFT,
					// Globals.DIRECTORY_MAPS + "/" + newMap + ".tmx");
					transitions.put(Globals.DIRECTION_LEFT, ResourceManager.getMap(newMap));
				}
			}
		}
	}

	/**
	 * Renders the specified layer index of the current {@link Map}.
	 * 
	 * @param layerIndex
	 *            - the index of the layer to render
	 */
	public void render(int layerIndex) {
		if (layerIndex != MapObjects.LAYER_COLLISION) {
			map.render(0, 0, layerIndex);
		}
	}

	private void setBounds() {
		widthTiles = map.getWidth();
		heightTiles = map.getHeight();
		width = widthTiles * map.getTileWidth();
		height = heightTiles * map.getTileHeight();
	}

	private void loadObjects() {
		int groups = map.getObjectGroupCount();
		for (int groupID = 0; groupID < groups; groupID++) {
			int objectsInGroup = map.getObjectCount(groupID);
			for (int objectID = 0; objectID < objectsInGroup; objectID++) {
				int x = map.getObjectX(groupID, objectID);
				int y = map.getObjectY(groupID, objectID);
				int width = map.getObjectWidth(groupID, objectID);
				int height = map.getObjectHeight(groupID, objectID);

				if (map.getObjectType(groupID, objectID).equals(MapObjects.OBJECT_PLAYERSTART)) {
					AbstractEntity player = Engine.getEntity(MapObjects.ENTITY_PLAYER);
					if (player.getX() == 0 && player.getY() == 0) {
						player.setX(x);
						player.setY(y);
					}
				} else if (map.getObjectType(groupID, objectID).equals(MapObjects.OBJECT_TELEPORT)) {
					TeleportBlock tb = new TeleportBlock(x, y, width, height);
					tb.setTargetX(Integer.parseInt(map.getObjectProperty(groupID, objectID, "x", "0")));
					tb.setTargetY(Integer.parseInt(map.getObjectProperty(groupID, objectID, "y", "0")));
				}
			}
		}

		// load sripts
		int layers = map.getLayerCount();
		for (int layer = 0; layer < layers; layer++) {
			for (int x = 0; x < map.getWidth(); x++) {
				for (int y = 0; y < map.getHeight(); y++) {
					int tileID = map.getTileId(x, y, layer);

					String scriptName = map.getTileProperty(tileID, "script", null);
					if (scriptName != null) {
						try {
							HashMap<String, Object> data = new HashMap<String, Object>();

							// get all the properties for this tile
							Properties props = map.findTileSet(tileID).getProperties(tileID);
							Iterator<Entry<Object, Object>> iter = props.entrySet().iterator();
							while (iter.hasNext()) {
								Entry<Object, Object> entry = iter.next();
								String key = entry.getKey().toString();
								Object value = entry.getValue();

								data.put(key, value);
							}

							data.put("originalID", tileID);
							data.put("layerIndex", layer);

							if (data.containsKey("newX") && data.containsKey("newY")) {
								data.put("newID", tileID + Integer.parseInt(data.get("newX").toString()) + (Integer.parseInt(data.get("newY").toString()) * map.findTileSet(tileID).tilesAcross));
							}

							Class<?> scriptClass = Class.forName("scripts." + scriptName.substring(0, 1).toUpperCase() + scriptName.substring(1) + "Script");
							Constructor<?> constructor = scriptClass.getConstructor(Sound.class, HashMap.class);

							String soundRef = data.get("sfx").toString();
							AbstractTileScript script = (AbstractTileScript) constructor.newInstance(ResourceManager.getSound(soundRef), data);

							ScriptBlock scriptBlock = new ScriptBlock(x * map.getTileWidth(), y * map.getTileHeight(), Globals.TILE_SIZE, Globals.TILE_SIZE);
							scriptBlock.setScript(script);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		// load blocked tiles
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				Image imgBlock = map.getTileImage(x, y, MapObjects.LAYER_COLLISION);
				if (imgBlock != null) {
					new BlockedBlock(x * map.getTileWidth(), y * map.getTileHeight(), imgBlock.getWidth(), imgBlock.getHeight());
				}
			}
		}
	}

	/**
	 * Returns true if this {@link Map} has a transition (map) to the given
	 * direction.
	 * 
	 * @param direction
	 *            - a direction constant
	 * @return boolean
	 */
	public boolean hasTransition(int direction) {
		return transitions.containsKey(direction);
	}

	/**
	 * Returns true if the specified {@link Map} is already cached and ready to
	 * use.
	 * 
	 * @param key
	 *            - the reference of the map, also the key in the cache
	 * @return boolean
	 */
	public static boolean isCached(String key) {
		return mapsCache.containsKey(key);
	}

	/**
	 * Adds a {@link Map} to the static cache.
	 * 
	 * @param key
	 *            - the key/id for this {@link Map}
	 * @param map
	 *            - the actual {@link Map} object
	 */
	public static void addMapToCache(String key, Map map) {
		mapsCache.put(key, map);
	}

	/**
	 * Returns a {@link Map} from the cache or <code>null</code> if the
	 * {@link Map} wasn't found.
	 * 
	 * @param key
	 *            - the key/id for the {@link Map}
	 * @return {@link Map}
	 */
	public static Map getFromCache(String key) {
		return mapsCache.get(key);
	}

	public TiledMap getTiledMap() {
		return map;
	}

	public int getEntitiesLayerIndex() {
		return entitiesLayerIndex;
	}

	public int getMaxLayerIndex() {
		return maxLayerIndex;
	}

	public int getWidthTiles() {
		return widthTiles;
	}

	public void setWidthTiles(int widthTiles) {
		this.widthTiles = widthTiles;
	}

	public int getHeightTiles() {
		return heightTiles;
	}

	public void setHeightTiles(int heightTiles) {
		this.heightTiles = heightTiles;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
