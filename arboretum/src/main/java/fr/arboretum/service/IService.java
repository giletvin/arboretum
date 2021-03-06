package fr.arboretum.service;

import java.util.List;

import fr.arboretum.bo.MultiCriteriaSearchFormBean;
import fr.arboretum.bo.SimpleSubject;
import fr.arboretum.bo.Subject;
import fr.arboretum.bo.Taxon;
import fr.arboretum.helper.ApplicationException;
import fr.arboretum.helper.SupportedLanguage;

/**
 * The Interface IOrnidroidService.
 */
public interface IService {

	/**
	 * Creates the db if necessary.
	 * 
	 * @throws ApplicationException
	 *             the ornidroid exception
	 */
	void createDbIfNecessary() throws ApplicationException;

	Integer getLeafTypeId(String string);

	List<String> getLeafTypes();

	/**
	 * Gets the matching subjects and store the results in the history result
	 * 
	 * @param query
	 *            the query
	 * @return the matching subjects
	 */
	List<SimpleSubject> getMatchingSubjects(String query);

	/**
	 * Gets the matches from the multi criteria search and store the results in
	 * the history result.
	 * 
	 * @param formBean
	 *            the form bean
	 * @return the matches from multi search criteria
	 */
	void getMatchesFromMultiSearchCriteria(MultiCriteriaSearchFormBean formBean);

	/**
	 * Gets the categories to load the select menu of the scientific families.
	 * 
	 * @return the categories
	 * 
	 */
	List<String> getScientificFamilies();

	/**
	 * Gets the getScientificFamilyId.
	 * 
	 * @param pScientifFamilyName
	 *            the category name
	 * @return the scientific family id
	 * 
	 */
	Integer getScientificFamilyId(String pScientifFamilyName);

	/**
	 * Gets the current subject. If a previous call to show subject detail was
	 * already done, get the subject without querying the db
	 * 
	 * @return the current subject
	 */
	Subject getCurrentSubject();

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
	Integer getLeafDispositionId(String leafDispositionName);
	List<String> getLeafDispositions();

	/**
	 * Gets the wikipedia link of the subject, using the gui language en, fr or
	 * de .wikipedia.org
	 * 
	 * @param currentBird
	 *            the current bird
	 * @return the wikipedia link
	 * 
	 */
	String getWikipediaLink(Subject currentBird, SupportedLanguage lang);

	String getDocUrlLink(Subject currentBird);

	/**
	 * Checks for history.
	 * 
	 * @return true, if successful
	 */
	boolean hasHistory();

	/**
	 * Load subjectId details.
	 * 
	 * @param subjectId
	 *            the subjectId id
	 */
	void loadSubjectDetails(Integer subjectId);

	/**
	 * Gets the query result.
	 * 
	 * @return the query result
	 */
	List<SimpleSubject> getQueryResult();

	/**
	 * Gets the release notes.
	 * 
	 * @return the release notes
	 */
	String getReleaseNotes();

}
