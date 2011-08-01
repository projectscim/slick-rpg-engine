package main.constants;

public class Globals {

	// app settings
	public static final String APP_NAME = "RPGEngine";
	public static final int APP_WIDTH = 800;
	public static final int APP_HEIGHT = 600;

	public static final int TILE_SIZE = 32;
	public static final int COLLISION_DETECT_VALUE = 2;

	// directions
	public static final int DIRECTION_UP = 1;
	public static final int DIRECTION_DOWN = 2;
	public static final int DIRECTION_LEFT = 3;
	public static final int DIRECTION_RIGHT = 4;

	// directories
	public static final String DIRECTORY_FONTS = "resources/fonts";
	public static final String DIRECTORY_MAPS = "resources/maps";
	public static final String DIRECTORY_IMAGES = "resources/images";
	public static final String DIRECTORY_TILES = "resources/tiles";
	public static final String DIRECTORY_SFX = "resources/sfx";
	public static final String DIRECTORY_MUSIC = "resources/music";

	// animations
	public static final String ANIMATION_DOWN = "down";
	public static final String ANIMATION_UP = "up";
	public static final String ANIMATION_LEFT = "left";
	public static final String ANIMATION_RIGHT = "right";

	// state IDs
	public static final int STATE_WORLD = 10;
	public static final int STATE_LOADING = 20;

}
