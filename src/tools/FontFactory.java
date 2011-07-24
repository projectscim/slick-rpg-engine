package tools;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;

import main.constants.Globals;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

/**
 * Static utility class which loads, creates and holds the ingame fonts. At the
 * start of the engine, the font directory {@link Globals#DIRECTORY_FONTS} is
 * searched for .ttf files. Every file is then parsed and stored inside a
 * HashMap and fetchable via {@link FontFactory#getFont(String)}.
 * 
 * @author Benny
 */
public class FontFactory {

	public static final int SIZE_STANDARD = 12;

	private static HashMap<String, UnicodeFont> fonts = new HashMap<String, UnicodeFont>();

	/**
	 * Searches the font directory and creates a font for each file found.
	 */
	public static void createFonts() {
		File fonts = new File(Globals.DIRECTORY_FONTS);
		for (File fontFile : fonts.listFiles()) {
			if (fontFile.isDirectory()) {
				// TODO recursive file parsing
			} else {
				createFont(fontFile.getPath());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void createFont(String ref) {
		try {
			UnicodeFont font = new UnicodeFont(ref, SIZE_STANDARD, false, false);
			font.addAsciiGlyphs();
			font.getEffects().add(new ColorEffect(Color.black));
			font.loadGlyphs();

			// build name from .ttf file
			String name = ref.substring(ref.lastIndexOf(System.getProperty("file.separator")) + 1, ref.lastIndexOf('.'));
			fonts.put(name, font);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a font. <code>key</code> is the name of the font file without the
	 * ending.
	 * 
	 * @param key
	 *            - the file name of the font
	 * @return the {@link UnicodeFont}
	 */
	public static UnicodeFont getFont(String key) {
		return fonts.get(key);
	}

	/**
	 * Returns all stored fonts.
	 * 
	 * @return {@link HashMap}
	 */
	public static HashMap<String, UnicodeFont> getFonts() {
		return fonts;
	}

}
