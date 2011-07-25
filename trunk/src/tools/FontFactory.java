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

	public static final int SIZE_STANDARD = 12;

	@SuppressWarnings("unchecked")
	public static UnicodeFont createFont(String ref) {
		try {
			UnicodeFont font = new UnicodeFont(ref, SIZE_STANDARD, false, false);
			font.addAsciiGlyphs();
			font.getEffects().add(new ColorEffect(Color.black));
			font.loadGlyphs();
			return font;
		} catch (SlickException e) {
			e.printStackTrace();
		}

		return null;
	}

}
