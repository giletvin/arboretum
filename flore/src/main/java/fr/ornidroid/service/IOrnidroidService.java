package fr.ornidroid.service;

import java.util.List;

import fr.ornidroid.bo.Bird;
import fr.ornidroid.bo.MultiCriteriaSearchFormBean;
import fr.ornidroid.bo.SimpleBird;
import fr.ornidroid.bo.Taxon;
import fr.ornidroid.helper.OrnidroidException;

/**
 * The Interface IOrnidroidService.
 */
public interface IOrnidroidService {

	/**
	 * Creates the db if necessary.
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	void createDbIfNecessary() throws OrnidroidException;

	/**
	 * Gets the beak form id.
	 * 
	 * @param string
	 *            the string
	 * @return the beak form id
	 */
	Integer getBeakFormId(String string);

	/**
	 * Gets the beak forms.
	 * 
	 * @return the beak forms
	 */
	List<String> getBeakForms();

	/**
	 * Gets the matching birds and store the results in the history result
	 * 
	 * @param query
	 *            the query
	 * @return the matching birds
	 */
	List<SimpleBird> getMatchingBirds(String query);

	/**
	 * Gets the bird matches from the multi criteria search and store the
	 * results in the history result.
	 * 
	 * @param formBean
	 *            the form bean
	 * @return the bird matches from multi search criteria
	 */
	void getBirdMatchesFromMultiSearchCriteria(
			MultiCriteriaSearchFormBean formBean);

	/**
	 * Gets the categories to load the select menu of the bird categories.
	 * 
	 * @return the categories
	 */
	List<String> getCategories();

	/**
	 * Gets the category id.
	 * 
	 * @param categoryName
	 *            the category name
	 * @return the category id
	 */
	Integer getCategoryId(String categoryName);

	/**
	 * Gets the colour id.
	 * 
	 * @param colourName
	 *            the colour name
	 * @return the feather colour id
	 */
	Integer getColourId(String colourName);

	/**
	 * Gets the colours.
	 * 
	 * @return the colours
	 */
	List<String> getColours();

	/**
	 * Gets the countries.
	 * 
	 * @return the countries
	 */
	List<String> getCountries();

	/**
	 * Gets the country code.
	 * 
	 * @param countryName
	 *            the country name
	 * @return the country code
	 */
	String getCountryCode(String countryName);

	/**
	 * Gets the current bird. If a previous call to show bird detail was already
	 * done, get the bird without querying the db
	 * 
	 * @return the current bird
	 */
	Bird getCurrentBird();

	/**
	 * Gets the habitat id.
	 * 
	 * @param habitatName
	 *            the habitatName
	 * @return the habitat id
	 */
	Integer getHabitatId(String habitatName);

	/**
	 * Gets the habitats.
	 * 
	 * @return the habitats
	 */
	List<String> getHabitats();

	/**
	 * Gets the multi search criteria count results.
	 * 
	 * @param formBean
	 *            the form bean
	 * @return the multi search criteria count results
	 */
	int getMultiSearchCriteriaCountResults(MultiCriteriaSearchFormBean formBean);

	/**
	 * Gets the names of the bird in different languages.
	 * 
	 * @param id
	 *            the id
	 * @return List of taxon
	 */
	List<Taxon> getNames(int id);

	/**
	 * Gets the countries where the bird can be seen
	 * 
	 * @param id
	 *            : bird id
	 * @return List of countries (String)
	 */
	List<String> getGeographicDistribution(int id);

	/**
	 * Gets the oiseaux net link.
	 * 
	 * @param currentBird
	 *            the current bird
	 * @return the oiseaux net link
	 */
	String getOiseauxNetLink(Bird currentBird);

	/**
	 * Gets the remarkable sign id.
	 * 
	 * @param remarkableSignName
	 *            the remarkable sign name
	 * @return the remarkable sign id
	 */
	Integer getRemarkableSignId(String remarkableSignName);

	/**
	 * Gets the remarkable signs.
	 * 
	 * @return the remarkable signs
	 */
	List<String> getRemarkableSigns();

	/**
	 * Gets the size id.
	 * 
	 * @param sizeName
	 *            the size name
	 * @return the size id
	 */
	Integer getSizeId(String sizeName);

	/**
	 * Gets the sizes.
	 * 
	 * @return the sizes
	 */
	List<String> getSizes();

	/**
	 * Gets the wikipedia link of the bird, using the gui language en, fr or de
	 * .wikipedia.org
	 * 
	 * @param currentBird
	 *            the current bird
	 * @return the wikipedia link
	 */
	String getWikipediaLink(Bird currentBird);

	/**
	 * Checks for history.
	 * 
	 * @return true, if successful
	 */
	boolean hasHistory();

	/**
	 * Load bird details.
	 * 
	 * @param birdId
	 *            the bird id
	 */
	void loadBirdDetails(Integer birdId);

	/**
	 * Gets the xeno canto map url. Something like :
	 * http://www.xeno-canto.org/species/Grus-grus
	 * 
	 * @param currentBird
	 *            the current bird
	 * @return the xeno canto map url
	 */
	String getXenoCantoMapUrl(Bird currentBird);

	/**
	 * Gets the query result.
	 * 
	 * @return the query result
	 */
	List<SimpleBird> getQueryResult();

	/**
	 * Gets the release notes.
	 * 
	 * @return the release notes
	 */
	String getReleaseNotes();

}
