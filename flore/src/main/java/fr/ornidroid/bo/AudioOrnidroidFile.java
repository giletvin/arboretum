package fr.ornidroid.bo;

import java.util.HashMap;
import java.util.Map;

import fr.ornidroid.helper.StringHelper;

/**
 * The Class AudioOrnidroidFile.
 */
public class AudioOrnidroidFile extends OrnidroidFile {

	/** The Constant LINE_1. */
	public final static String LINE_1 = "line_1";

	/** The Constant LINE_2. */
	public final static String LINE_2 = "line_2";

	/**
	 * Gets the properties for screen.
	 * 
	 * @return the properties for screen
	 */
	public Map<String, String> getPropertiesForScreen() {
		Map<String, String> mapForScreen = new HashMap<String, String>();
		StringBuffer line1 = new StringBuffer();
		StringBuffer line2 = new StringBuffer();
		final String space = " ";
		String title = getProperty(OrnidroidFile.AUDIO_TITLE_PROPERTY);
		String duration = getProperty(OrnidroidFile.AUDIO_DURATION_PROPERTY);
		String recordist = getProperty(OrnidroidFile.AUDIO_RECORDIST_PROPERTY);
		String ref = getProperty(OrnidroidFile.AUDIO_REF_PROPERTY);
		String remarks = getProperty(OrnidroidFile.AUDIO_REMARKS_PROPERTY);

		if (StringHelper.isBlank(title)) {
			title = extractFilenameFromPath();
		}
		line1.append(title);
		line1.append(space);
		line1.append(duration);
		line2.append(recordist);
		line2.append(space);
		line2.append(ref);
		line2.append(space);
		line2.append(remarks);

		mapForScreen.put(LINE_1, line1.toString());
		mapForScreen.put(LINE_2, line2.toString());
		return mapForScreen;
	}

}
