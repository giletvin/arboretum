package fr.arboretum.data;

import java.util.List;

import android.database.Cursor;
import fr.arboretum.bo.MultiCriteriaSearchFormBean;
import fr.arboretum.bo.SimpleSubject;

/**
 * The Interface IOrnidroidDAO.
 */
public interface IDAO {

	public static final String LEAF_TYPE_TABLE = "type_feuille";
	public static final String ARBRE_LEAF_DISPOSITION_TABLE = "arbre_disposition_feuille";

	public static final String ARBRE_LEAF_TYPE_TABLE = "arbre_type_feuille";

	public static final String SUBJECT_TABLE = "arbre";

	/** The Constant DIRECTORY_NAME_COLUMN. */
	public static final String DIRECTORY_NAME_COLUMN = "directory_name";

	/** The Constant ID. */
	public static final String ID = "id";
	/** The Constant LANG_COLUMN_NAME. */
	public static final String LANG_COLUMN_NAME = "lang";
	/** The Constant NAME_FIELD_NAME. */
	public static final String NAME_COLUMN_NAME = "name";
	public static final String ORDRE_COLUMN_NAME = "ordre";
	public static final String LEAF_DISPOSITION_TABLE = "disposition_feuille";

	public static final String SCIENTIFIC_FAMILY_NAME_COLUMN = "scientific_family";

	public static final String SCIENTIFIC_FAMILY_TABLE = "scientific_family";

	public static final String SCIENTIFIC_NAME = "scientific_name";

	/**
	 * The Constant SEARCHED_TAXON. The taxon where diacritics are removed. Ex
	 * taxon "Bécassine" is "Becassine" in this column
	 */
	public static final String SEARCHED_TAXON = "searched_taxon";

	/** The Constant TAXON. */
	public static final String TAXON = "taxon";

	Cursor getLeafTypes();

	/**
	 * Returns a Cursor positioned at the subject specified by rowId.
	 * 
	 * @param rowId
	 *            id of subject to retrieve
	 * @return Cursor positioned to matching bird, or null if not found.
	 */
	Cursor getSubject(String rowId);

	/**
	 * Gets the matches from multi search criteria and stores the results in the
	 * history stack.
	 * 
	 * @param formBean
	 *            the form bean
	 * @return the bird matches from multi search criteria
	 */
	List<SimpleSubject> getMatchesFromMultiSearchCriteria(
			MultiCriteriaSearchFormBean formBean);

	/**
	 * Gets the subject names in different languages.
	 * 
	 * @param id
	 *            the id of the subject
	 * @return cursor on the results of the query. Can be null if something goes
	 *         wrong
	 */
	Cursor getSubjectNames(Integer id);

	/**
	 * Gets the categories.
	 * 
	 * @return the categories
	 * 
	 */
	Cursor getScientificFamilies();

	/**
	 * Gets the multi search criteria count results.
	 * 
	 * @param formBean
	 *            the form bean
	 * @return the multi search criteria count results
	 */
	int getMultiSearchCriteriaCountResults(MultiCriteriaSearchFormBean formBean);

	Cursor getLeafDispositions();

	/**
	 * Gets the matching birds.
	 * 
	 * @param query
	 *            the query
	 * @return the matching birds
	 */
	List<SimpleSubject> getMatchingSubjects(String query);

	/**
	 * Gets the release notes.
	 * 
	 * @return the release notes
	 */
	String getReleaseNotes();

}