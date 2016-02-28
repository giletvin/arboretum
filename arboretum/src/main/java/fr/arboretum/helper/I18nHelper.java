package fr.arboretum.helper;

import java.util.Locale;

/**
 * The Class I18nHelper.
 */
public class I18nHelper {

	public static final String FRENCH = "fr";
	public static final String ENGLISH = "en";

	private static I18nHelper SINGLETON;

	/**
	 * Gets the lang : en, fr or de
	 * 
	 * 
	 * @return the lang
	 */
	public static SupportedLanguage getLang() {
		if (SINGLETON == null) {
			SINGLETON = new I18nHelper();
		}
		return SINGLETON.getAndroidLang();
	}

	/** The android lang. */
	private final SupportedLanguage androidLang;

	/**
	 * Instantiates a new i18n helper.
	 */
	private I18nHelper() {
		final String langCode = Locale.getDefault().getISO3Language();
		this.androidLang = SupportedLanguage
				.getSupportedLanguageFromIso3Code(langCode);
	}

	/**
	 * Gets the android lang.
	 * 
	 * @return the android lang
	 */
	private SupportedLanguage getAndroidLang() {
		return this.androidLang;
	}
}
