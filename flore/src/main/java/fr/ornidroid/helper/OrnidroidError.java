package fr.ornidroid.helper;

/**
 * The Enum OrnidroidErrors.
 */
public enum OrnidroidError {

	/** The add custom media error. */
	ADD_CUSTOM_MEDIA_ERROR,
	/** error on changing ornidroid home. */
	CHANGE_ORNIDROID_HOME,
	/** The DATABAS e_ no t_ found. */
	DATABASE_NOT_FOUND,
	/** The N o_ error. */
	NO_ERROR,

	/** The ORNIDROI d_ connectio n_ problem. */
	ORNIDROID_CONNECTION_PROBLEM,
	/** The ORNIDROI d_ downloa d_ error. */
	ORNIDROID_DOWNLOAD_ERROR, /**
	 * The ORNIDROI d_ downloa d_ erro r_ medi a_ doe
	 * s_ no t_ exist.<br>
	 * if we try to download media files from a bird that doesn't have any
	 * */
	ORNIDROID_DOWNLOAD_ERROR_MEDIA_DOES_NOT_EXIST,
	/**
	 * The ORNIDROI d_ hom e_ no t_ found.
	 */
	ORNIDROID_HOME_NOT_FOUND,
	/** The unzip package. */
	UNZIP_PACKAGE;

	/**
	 * Gets the error code.
	 * 
	 * @param error
	 *            the error
	 * @return the error code
	 */
	public static int getErrorCode(final OrnidroidError error) {
		int code = 0;
		switch (error) {
		case NO_ERROR:
			code = 0;
			break;
		case DATABASE_NOT_FOUND:
			code = 1;
			break;
		case ORNIDROID_CONNECTION_PROBLEM:
			code = 2;
			break;
		case ORNIDROID_DOWNLOAD_ERROR:
			code = 3;
			break;
		case ORNIDROID_DOWNLOAD_ERROR_MEDIA_DOES_NOT_EXIST:
			code = 4;
			break;
		case ORNIDROID_HOME_NOT_FOUND:
			code = 5;
			break;
		case CHANGE_ORNIDROID_HOME:
			code = 6;
			break;
		case ADD_CUSTOM_MEDIA_ERROR:
			code = 7;
			break;
		}
		return code;
	}

	/**
	 * Gets the ornidroid error.
	 * 
	 * @param code
	 *            the code
	 * @return the ornidroid error
	 */
	public static OrnidroidError getOrnidroidError(final int code) {
		OrnidroidError error = NO_ERROR;
		switch (code) {
		case 0:
			error = NO_ERROR;
			break;
		case 1:
			error = DATABASE_NOT_FOUND;
			break;
		case 2:
			error = ORNIDROID_CONNECTION_PROBLEM;
			break;
		case 3:
			error = ORNIDROID_DOWNLOAD_ERROR;
			break;
		case 4:
			error = ORNIDROID_DOWNLOAD_ERROR_MEDIA_DOES_NOT_EXIST;
			break;
		case 5:
			error = ORNIDROID_HOME_NOT_FOUND;
			break;
		case 6:
			error = CHANGE_ORNIDROID_HOME;
			break;
		case 7:
			error = ADD_CUSTOM_MEDIA_ERROR;
			break;
		}
		return error;

	}
}
