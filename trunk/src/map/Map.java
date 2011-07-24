package map;

import interfaces.TransitionAction;

import java.util.HashMap;
import java.util.Vector;

import main.Engine;
import main.camera.Camera;
import main.constants.Globals;
import main.constants.MapObjects;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import tools.MapChangeTransition;
import entities.AbstractEntity;
import entities.blocks.Block;

/**
 * A <code>Map</code> loads and holds a TiledMap object and is responsible for
 * drawing the layers in a appropriate way and order.
 * 
 * @author Benny
 */
public class Map {

	private TiledMap map = null;

	private int widthTiles, heightTiles;
	private int width, height;

	private static HashMap<String, Map> mapsCache = new HashMap<String, Map>();
	private HashMap<String, MapObject> objects = new HashMap<String, MapObject>();
	private HashMap<Integer, String> transitions = new HashMap<Integer, String>();

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

		if (setActive) {
			Engine.setMap(this);
		}

		maxLayerIndex = map.getLayerCount();
		entitiesLayerIndex = map.getLayerIndex(MapObjects.LAYER_OBJECTS);

		loadObjects();

		if (Engine.getCamera() == null) {
			new Camera(Engine.getEntity(MapObjects.ENTITY_PLAYER));
		}

		mapsCache.put(mapFile, this);
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
					transitions.put(Globals.DIRECTION_UP, Globals.DIRECTORY_MAPS + "/" + newMap + ".tmx");
				} else if (way.equals("east")) {
					transitions.put(Globals.DIRECTION_RIGHT, Globals.DIRECTORY_MAPS + "/" + newMap + ".tmx");
				} else if (way.equals("south")) {
					transitions.put(Globals.DIRECTION_DOWN, Globals.DIRECTORY_MAPS + "/" + newMap + ".tmx");
				} else if (way.equals("west")) {
					transitions.put(Globals.DIRECTION_LEFT, Globals.DIRECTORY_MAPS + "/" + newMap + ".tmx");
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

	public MapObject getObject(String type) {
		return objects.get(type);
	}

	public Vector<MapObject> getObjects(String... type) {
		// TODO implement this
		return null;
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
				if (map.getObjectType(groupID, objectID).equals(MapObjects.OBJECT_PLAYERSTART)) {
					MapObject playerStart = new MapObject(MapObjects.OBJECT_PLAYERSTART, map.getObjectX(groupID, objectID), map.getObjectY(groupID, objectID), map.getObjectWidth(groupID, objectID), map.getObjectHeight(groupID, objectID));
					objects.put(MapObjects.OBJECT_PLAYERSTART, playerStart);

					AbstractEntity player = Engine.getEntity(MapObjects.ENTITY_PLAYER);
					if (player.getX() == 0 && player.getY() == 0) {
						player.setX(playerStart.getX());
						player.setY(playerStart.getY());
					}
				}
			}
		}

		// load blocked tiles
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				Image imgBlock = map.getTileImage(x, y, MapObjects.LAYER_COLLISION);
				if (imgBlock != null) {
					Block block = new Block(x * map.getTileWidth(), y * map.getTileHeight(), imgBlock.getWidth(), imgBlock.getHeight());
					Engine.addEntity(block);
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
