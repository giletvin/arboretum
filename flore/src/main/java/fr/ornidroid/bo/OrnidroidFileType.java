package fr.ornidroid.bo;

import fr.ornidroid.helper.BasicConstants;

/**
 * The Enum OrnidroidFileType.
 */
public enum OrnidroidFileType {

	/** The SOUND. */
	AUDIO,
	/** The PICTURE. */
	PICTURE,

	/** The wikipedia page. */
	WIKIPEDIA_PAGE;

	/** The Constant AUDIO_EXTENSION. */
	public final static String AUDIO_EXTENSION = ".mp3";

	/** The Constant FILE_TYPE_PARAM_NAME. */
	public final static String FILE_TYPE_INTENT_PARAM_NAME = "FILE_TYPE";
	/** The Constant PICTURE_EXTENSION. */
	public final static String PICTURE_EXTENSION = ".jpg";

	/**
	 * Gets the code. Must be the same as tab id
	 * 
	 * @param type
	 *            the type
	 * @return the code
	 */
	public static int getCode(final OrnidroidFileType type) {
		int code = 0;
		if (null != type) {
			switch (type) {
			case PICTURE:
				code = 0;
				break;
			case AUDIO:
				code = 1;
				break;
			case WIKIPEDIA_PAGE:
				code = 3;
				break;

			}
		}
		return code;
	}

	/**
	 * Gets the extension.
	 * 
	 * @param type
	 *            the type
	 * @return the extension
	 */
	public static String getExtension(final OrnidroidFileType type) {
		String extension = null;
		switch (type) {
		case PICTURE:
			extension = PICTURE_EXTENSION;
			break;
		case AUDIO:
			extension = AUDIO_EXTENSION;
			break;
		case WIKIPEDIA_PAGE:
			extension = BasicConstants.EMPTY_STRING;
		}
		return extension;
	}
}
