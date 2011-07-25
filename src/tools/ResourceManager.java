package tools;

import java.io.File;
import java.util.HashMap;

import main.constants.Globals;

import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.UnicodeFont;

/**
 * Static utility class which loads and caches all game resources on startup.
 * Within the game, these resources can then easily be accessed.
 * 
 * @author Benny
 */
public class ResourceManager {

	/**
	 * Enum of the resource types the engine can handle.
	 */
	public static enum Types {
		IMAGE, SOUND, MUSIC, MAP, FONT
	}

	private static boolean loaded = false;

	private static HashMap<String, Image> images = new HashMap<String, Image>();
	private static HashMap<String, Sound> sounds = new HashMap<String, Sound>();
	private static HashMap<String, Music> music = new HashMap<String, Music>();
	private static HashMap<String, String> maps = new HashMap<String, String>();
	private static HashMap<String, UnicodeFont> fonts = new HashMap<String, UnicodeFont>();

	/**
	 * Loads all of the game's resources.
	 * 
	 * @throws SlickException
	 */
	public static void load() throws SlickException {
		if (!loaded) {
			load(Types.IMAGE, Globals.DIRECTORY_IMAGES);
			load(Types.SOUND, Globals.DIRECTORY_SFX);
			load(Types.MUSIC, Globals.DIRECTORY_MUSIC);
			load(Types.MAP, Globals.DIRECTORY_MAPS);
			load(Types.FONT, Globals.DIRECTORY_FONTS);
			loaded = true;
		}
	}

	/**
	 * Loads a specific type of resource within the given path. Works
	 * recursively.
	 * 
	 * @param type
	 *            - the resource type
	 * @param path
	 *            - the main path to search in
	 * @throws SlickException
	 */
	private static void load(Types type, String path) throws SlickException {
		File file = new File(path);
		for (File curFile : file.listFiles()) {
			if (curFile.getPath().contains("svn")) {
				continue;
			}

			if (curFile.isDirectory()) {
				load(type, curFile.getPath());
			} else {
				String ref = curFile.getPath();
				String key = ref.substring(ref.lastIndexOf(System.getProperty("file.separator")) + 1, ref.lastIndexOf('.'));

				switch (type) {
					case IMAGE:
						images.put(key, new Image(curFile.getPath()));
						break;
					case MUSIC:
						music.put(key, new Music(curFile.getPath()));
						break;
					case SOUND:
						sounds.put(key, new Sound(curFile.getPath()));
						break;
					case FONT:
						fonts.put(key, FontFactory.createFont(curFile.getPath()));
						break;
					case MAP:
						maps.put(key, curFile.getPath());
						break;
				}
			}
		}
	}

	/**
	 * Returns a {@link Image} from the cache.
	 * 
	 * @param key
	 *            - the key of the image
	 * @return Image
	 */
	public static Image getImage(String key) {
		return images.get(key);
	}

	/**
	 * Returns a {@link Sound} from the cache.
	 * 
	 * @param key
	 *            - the key of the sound
	 * @return Sound
	 */
	public static Sound getSound(String key) {
		return sounds.get(key);
	}

	/**
	 * Returns a {@link Music} from the cache.
	 * 
	 * @param key
	 *            - the key of the music
	 * @return Music
	 */
	public static Music getMusic(String key) {
		return music.get(key);
	}

	/**
	 * Returns a {@link UnicodeFont} from the cache.
	 * 
	 * @param key
	 *            - the key of the font
	 * @return UnicodeFont
	 */
	public static UnicodeFont getFont(String key) {
		return fonts.get(key);
	}

	/**
	 * Returns a .tmx map reference from the cache.
	 * 
	 * @param key
	 *            - the key of the map reference
	 * @return String
	 */
	public static String getMap(String key) {
		return maps.get(key);
	}

}
