package tools;

import java.awt.Color;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

/**
 * Static utility class to create {@link UnicodeFont} objects.
 * 
 * @author Benny
 */
public class FontFactory {

	public static final int SIZE_STANDARD = 14;

	@SuppressWarnings("unchecked")
	public static UnicodeFont createFont(String ref, Color color) {
		try {
			UnicodeFont font = new UnicodeFont(ref, SIZE_STANDARD, false, false);
			font.getEffects().add(new ColorEffect(color));
			font.addAsciiGlyphs();
			font.loadGlyphs();
			return font;
		} catch (SlickException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static UnicodeFont createFont(String ref) {
		return createFont(ref, Color.black);
	}

}
