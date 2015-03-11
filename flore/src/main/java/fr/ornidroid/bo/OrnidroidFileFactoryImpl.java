package fr.ornidroid.bo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import fr.ornidroid.helper.StringHelper;
import fr.ornidroid.helper.SupportedLanguage;

/**
 * The Class OrnidroidFileFactoryImpl.
 */
public class OrnidroidFileFactoryImpl {

	/** The Constant DEFAULT_VALUE. */
	private static final String DEFAULT_VALUE = "";

	/** The factory. */
	private static OrnidroidFileFactoryImpl factory;

	/**
	 * Gets the OrnidroidFileFactoryImpl factory.
	 * 
	 * @return the OrnidroidFileFactoryImpl factory
	 */
	public static OrnidroidFileFactoryImpl getFactory() {
		if (factory == null) {
			factory = new OrnidroidFileFactoryImpl();
		}
		return factory;
	}

	/**
	 * Instantiates a new ornidroid file factory impl.
	 */
	private OrnidroidFileFactoryImpl() {

	}

	/**
	 * Creates the ornidroid file.
	 * 
	 * @param path
	 *            the path
	 * @param fileType
	 *            the file type
	 * @param lang
	 *            the lang
	 * @return the ornidroid file
	 * @throws FileNotFoundException
	 *             if the file has no properties file: this reveals a bad
	 *             installation or a pb in the downloading of the files
	 */
	public OrnidroidFile createOrnidroidFile(final String path,
			final OrnidroidFileType fileType, final String lang)
			throws FileNotFoundException {
		OrnidroidFile file = null;
		switch (fileType) {
		case AUDIO:
			file = new AudioOrnidroidFile();
			break;
		case PICTURE:
			file = new PictureOrnidroidFile();
			break;
		case WIKIPEDIA_PAGE:
			file = new OrnidroidFile();
			break;
		}
		file.setPath(path);
		file.setType(fileType);

		file.setProperties(handleProperties(file, lang));

		return file;
	}

	/**
	 * Handle properties of the newly created File.
	 * 
	 * @param file
	 *            the file
	 * @param lang
	 *            the lang
	 * @return the map containing the keys and values of the properties of the
	 *         file
	 * @throws FileNotFoundException
	 *             if the file has no properties file
	 */
	private Map<String, String> handleProperties(final OrnidroidFile file,
			final String lang) throws FileNotFoundException {
		Map<String, String> properties = new HashMap<String, String>();
		switch (file.getType()) {
		case AUDIO:
			properties = initAudioProperties(file, lang);
			break;
		case PICTURE:
			properties = initImageProperties(file, lang);
			break;
		case WIKIPEDIA_PAGE:
			// do nothing
			break;
		}
		return properties;
	}

	/**
	 * Inits the audio properties.
	 * 
	 * @param file
	 *            the file
	 * @param lang
	 *            the lang
	 * @return the map
	 * @throws FileNotFoundException
	 *             if the file has no properties file
	 */
	private Map<String, String> initAudioProperties(final OrnidroidFile file,
			final String lang) throws FileNotFoundException {
		final Properties properties = loadPropertiesFile(file);
		final Map<String, String> ornidroidFileProperties = new HashMap<String, String>();
		if (properties != null) {
			final String recordist = (String) properties
					.get(OrnidroidFile.AUDIO_RECORDIST_PROPERTY);
			ornidroidFileProperties.put(OrnidroidFile.AUDIO_RECORDIST_PROPERTY,
					recordist);
			final String duration = (String) properties
					.get(OrnidroidFile.AUDIO_DURATION_PROPERTY);
			ornidroidFileProperties.put(OrnidroidFile.AUDIO_DURATION_PROPERTY,
					duration);
			final String title = (String) properties
					.get(OrnidroidFile.AUDIO_TITLE_PROPERTY);
			ornidroidFileProperties.put(OrnidroidFile.AUDIO_TITLE_PROPERTY,
					title);
			final String ref = (String) properties
					.get(OrnidroidFile.AUDIO_REF_PROPERTY);
			ornidroidFileProperties.put(OrnidroidFile.AUDIO_REF_PROPERTY, ref);
			String remarks = (String) properties
					.get(OrnidroidFile.AUDIO_REMARKS_PROPERTY
							+ OrnidroidFile.LANGUAGE_SEPARATOR + lang);
			if (StringHelper.isBlank(remarks)
					&& !SupportedLanguage.ENGLISH.getCode().equals(lang)) {
				// english is the default language for audio remarks (mainly
				// from xeno canto)
				remarks = (String) properties
						.get(OrnidroidFile.AUDIO_REMARKS_PROPERTY
								+ OrnidroidFile.LANGUAGE_SEPARATOR
								+ SupportedLanguage.ENGLISH.getCode());
			}
			ornidroidFileProperties.put(OrnidroidFile.AUDIO_REMARKS_PROPERTY,
					remarks);
		}

		return ornidroidFileProperties;
	}

	/**
	 * Inits the image properties.
	 * 
	 * @param file
	 *            the file
	 * @param lang
	 *            the lang
	 * @return the map, never null but can be empty
	 * @throws FileNotFoundException
	 *             if the file has no properties file
	 */
	private Map<String, String> initImageProperties(final OrnidroidFile file,
			final String lang) throws FileNotFoundException {
		final Map<String, String> ornidroidFileProperties = new HashMap<String, String>();
		final Properties properties = loadPropertiesFile(file);
		if (properties != null) {
			final String description = properties.getProperty(
					PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY
							+ OrnidroidFile.LANGUAGE_SEPARATOR + lang,
					DEFAULT_VALUE);
			ornidroidFileProperties.put(
					PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY,
					description);
			final String source = properties.getProperty(
					PictureOrnidroidFile.IMAGE_SOURCE_PROPERTY, DEFAULT_VALUE);
			ornidroidFileProperties.put(
					PictureOrnidroidFile.IMAGE_SOURCE_PROPERTY, source);
			final String author = properties.getProperty(
					PictureOrnidroidFile.IMAGE_AUTHOR_PROPERTY, DEFAULT_VALUE);
			ornidroidFileProperties.put(
					PictureOrnidroidFile.IMAGE_AUTHOR_PROPERTY, author);
			final String licence = properties.getProperty(
					PictureOrnidroidFile.IMAGE_LICENCE_PROPERTY, DEFAULT_VALUE);
			ornidroidFileProperties.put(
					PictureOrnidroidFile.IMAGE_LICENCE_PROPERTY, licence);
		}
		return ornidroidFileProperties;
	}

	/**
	 * Load properties file.
	 * 
	 * @param file
	 *            the file
	 * @return the properties
	 * @throws FileNotFoundException
	 *             if the file has no properties file
	 */
	private Properties loadPropertiesFile(final OrnidroidFile file)
			throws FileNotFoundException {
		Properties properties = null;
		final File propertiesFile = new File(file.getPath()
				+ OrnidroidFile.PROPERTIES_SUFFIX);
		if (propertiesFile.exists()) {
			try {
				final FileInputStream fis = new FileInputStream(propertiesFile);
				properties = new Properties();
				properties.load(fis);
			} catch (final FileNotFoundException e) {
				// should not occur
				throw new RuntimeException(e);
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new FileNotFoundException("properties file not found "
					+ file.getPath() + OrnidroidFile.PROPERTIES_SUFFIX);
		}
		return properties;
	}

}
