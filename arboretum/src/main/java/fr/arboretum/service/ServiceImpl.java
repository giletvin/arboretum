package fr.arboretum.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.SearchManager;
import android.database.Cursor;
import android.provider.BaseColumns;
import fr.arboretum.R;
import fr.arboretum.bo.MultiCriteriaSearchFormBean;
import fr.arboretum.bo.SimpleSubject;
import fr.arboretum.bo.Subject;
import fr.arboretum.bo.SubjectFactoryImpl;
import fr.arboretum.bo.Taxon;
import fr.arboretum.data.DAOImpl;
import fr.arboretum.data.DatabaseOpenHelper;
import fr.arboretum.data.IDAO;
import fr.arboretum.helper.ApplicationException;
import fr.arboretum.helper.BasicConstants;
import fr.arboretum.helper.Constants;
import fr.arboretum.helper.StringHelper;
import fr.arboretum.helper.SupportedLanguage;
import fr.arboretum.ui.picture.PictureHelper;

/**
 * The Class OrnidroidServiceImpl.
 */
public class ServiceImpl implements IService {

	/**
	 * The Class SelectFieldsValue : dto object to embed the map and the list of
	 * the select fields. The SQL queries handles the order by clause.
	 */
	private class SelectFieldsValue {

		/** The fields values. */
		private final List<String> fieldsValues;

		/** The map name id. */
		private final Map<String, Integer> mapNameId;

		/**
		 * Instantiates a new select fields value.
		 * 
		 * @param pMapNameId
		 *            the map name id
		 * @param pMapNameCode
		 *            the map name code
		 * @param pFieldValues
		 *            the field values
		 */
		public SelectFieldsValue(final Map<String, Integer> pMapNameId,
				final Map<String, String> pMapNameCode,
				final List<String> pFieldValues) {
			this.mapNameId = pMapNameId;
			this.fieldsValues = pFieldValues;
		}

		/**
		 * Gets the fields values.
		 * 
		 * @return the fields values
		 */
		protected List<String> getFieldsValues() {
			return this.fieldsValues;
		}

		/**
		 * Gets the map name id.
		 * 
		 * @return the map name id
		 */
		protected Map<String, Integer> getMapNameId() {
			return this.mapNameId;
		}
	}

	/** The service instance. */
	private static IService serviceInstance;

	/**
	 * Gets the single instance of OrnidroidServiceImpl.
	 * 
	 * @param pActivity
	 *            the activity
	 * @return single instance of OrnidroidServiceImpl
	 */
	protected static IService getInstance(final Activity pActivity) {
		if (null == serviceInstance) {
			serviceInstance = new ServiceImpl(pActivity);
		}
		return serviceInstance;
	}

	private final Activity activity;

	private List<String> leafTypesList;

	private Map<String, Integer> leafTypesMap;

	private List<String> scientificFamiliesList;

	private Map<String, Integer> scientificFamiliesMap;

	private Subject currentSubject;

	/** The data base open helper. */
	private final DatabaseOpenHelper dataBaseOpenHelper;

	/** The ornidroid dao. */
	private final IDAO ornidroidDAO;
	private List<String> leafDispositions;

	private Map<String, Integer> leafDispositionsMap;

	/** The query result. */
	private List<SimpleSubject> queryResult;

	/**
	 * Instantiates a new ornidroid service impl.
	 * 
	 * @param pActivity
	 *            the activity
	 */
	private ServiceImpl(final Activity pActivity) {
		this.activity = pActivity;
		this.dataBaseOpenHelper = new DatabaseOpenHelper(pActivity);
		this.ornidroidDAO = DAOImpl.getInstance(this.dataBaseOpenHelper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.arboretum.service.IOrnidroidService#createDbIfNecessary()
	 */
	public void createDbIfNecessary() throws ApplicationException {
		this.dataBaseOpenHelper.createDbIfNecessary();
	}

	public Integer getLeafTypeId(final String leafTypeName) {
		return this.leafTypesMap != null ? this.leafTypesMap.get(leafTypeName)
				: BasicConstants.DEFAULT_EMPTY_VALUE;
	}

	public List<String> getLeafTypes() {
		if (this.leafTypesMap == null) {
			// Find the names of the beak forms in the selected language
			final Cursor cursorQueryHabitats = this.ornidroidDAO.getLeafTypes();
			final SelectFieldsValue sfv = loadSelectFieldsFromCursor(
					cursorQueryHabitats, true);
			this.leafTypesMap = sfv.getMapNameId();
			this.leafTypesList = sfv.getFieldsValues();

		}
		return this.leafTypesList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.arboretum.service.IOrnidroidService#
	 * getBirdMatchesFromMultiSearchCriteria
	 * (fr.arboretum.bo.MultiCriteriaSearchFormBean)
	 */
	public void getMatchesFromMultiSearchCriteria(
			final MultiCriteriaSearchFormBean formBean) {
		queryResult = this.ornidroidDAO
				.getMatchesFromMultiSearchCriteria(formBean);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.arboretum.service.IOrnidroidService#getCategories()
	 */
	public List<String> getScientificFamilies() {
		if (this.scientificFamiliesMap == null) {
			final Cursor cursorQueryHabitats = this.ornidroidDAO
					.getScientificFamilies();
			final SelectFieldsValue sfv = loadSelectFieldsFromCursor(
					cursorQueryHabitats, true);
			this.scientificFamiliesMap = sfv.getMapNameId();
			this.scientificFamiliesList = sfv.getFieldsValues();

		}
		return this.scientificFamiliesList;
	}

	public Integer getScientificFamilyId(final String categoryName) {
		return this.scientificFamiliesMap != null ? this.scientificFamiliesMap
				.get(categoryName) : BasicConstants.DEFAULT_EMPTY_VALUE;
	}

	public Subject getCurrentSubject() {
		return this.currentSubject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.arboretum.service.IOrnidroidService#
	 * getMultiSearchCriteriaCountResults
	 * (fr.arboretum.bo.MultiCriteriaSearchFormBean)
	 */
	public int getMultiSearchCriteriaCountResults(
			final MultiCriteriaSearchFormBean formBean) {
		return this.ornidroidDAO.getMultiSearchCriteriaCountResults(formBean);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.arboretum.service.IOrnidroidService#getNames(int)
	 */
	public List<Taxon> getNames(final int id) {
		final Cursor cursor = this.ornidroidDAO.getSubjectNames(id);
		final List<Taxon> result = new ArrayList<Taxon>();
		if (cursor != null) {
			final int nbResults = cursor.getCount();
			for (int i = 0; i < nbResults; i++) {
				cursor.moveToPosition(i);
				final int langIndex = cursor
						.getColumnIndexOrThrow(IDAO.LANG_COLUMN_NAME);
				final int taxonIndex = cursor.getColumnIndexOrThrow(IDAO.TAXON);
				final Taxon taxon = new Taxon(cursor.getString(langIndex),
						cursor.getString(taxonIndex));
				result.add(taxon);
			}
			cursor.close();
		}
		return result;
	}
public Integer getLeafDispositionId(final String leafDispositionName) {
		return this.leafDispositionsMap != null ? this.leafDispositionsMap
				.get(leafDispositionName) : BasicConstants.DEFAULT_EMPTY_VALUE;
	}

	public List<String> getLeafDispositions() {
		if (this.leafDispositionsMap == null) {
			final Cursor cursorQueryHabitats = this.ornidroidDAO
					.getLeafDispositions();
			final SelectFieldsValue sfv = loadSelectFieldsFromCursor(
					cursorQueryHabitats, true);
			this.leafDispositionsMap = sfv.getMapNameId();
			this.leafDispositions = sfv.getFieldsValues();

		}
		return this.leafDispositions;
	}
	public String getWikipediaLink(final Subject currentSubject,
			final SupportedLanguage lang) {
		final StringBuffer sbuf = new StringBuffer();
		sbuf.append("<a href=\"http://");
		sbuf.append(lang.getCode());
		sbuf.append(".wikipedia.org/wiki/");
		sbuf.append(currentSubject.getScientificName().replaceAll(" ", "%20"));
		sbuf.append("\">");
		sbuf.append(Constants.getCONTEXT().getResources()
				.getString(R.string.wikipedia));
		sbuf.append(" (").append(lang.getCode()).append(") ");
		sbuf.append(currentSubject.getTaxon());
		sbuf.append("</a>");
		return sbuf.toString();
	}

	public String getDocUrlLink(final Subject currentSubject) {
		if (StringHelper.isNotBlank(currentSubject.getDocUrl())) {

			final StringBuffer sbuf = new StringBuffer();
			sbuf.append("<a href=\"");

			sbuf.append(currentSubject.getDocUrl());
			sbuf.append("\">");
			sbuf.append(Constants.getCONTEXT().getResources()
					.getString(R.string.doc_url));
			sbuf.append(currentSubject.getTaxon());
			sbuf.append("</a>");
			return sbuf.toString();
		} else {
			return BasicConstants.EMPTY_STRING;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.arboretum.service.IOrnidroidService#hasHistory()
	 */
	public boolean hasHistory() {
		return (queryResult != null && queryResult.size() > 0);
	}

	/**
	 * Load bird details.
	 * 
	 * @param subjectId
	 *            the bird id
	 */
	public void loadSubjectDetails(final Integer subjectId) {
		final Cursor cursor = this.ornidroidDAO
				.getSubject(subjectId.toString());

		loadDetailsFromCursor(cursor);

	}

	/**
	 * Load bird details from cursor and closes it
	 * 
	 * @param cursor
	 *            the cursor
	 */
	private void loadDetailsFromCursor(final Cursor cursor) {
		if (cursor != null) {
			cursor.moveToFirst();
			final int idIndex = cursor.getColumnIndexOrThrow(BaseColumns._ID);

			final int taxonIndex = cursor
					.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1);
			final int scientificNameIndex = cursor
					.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_2);

			final int directoryNameIndex = cursor
					.getColumnIndexOrThrow(IDAO.DIRECTORY_NAME_COLUMN);

			final SubjectFactoryImpl subjectFactory = new SubjectFactoryImpl();
			this.currentSubject = subjectFactory.createSubject(
					cursor.getInt(idIndex), cursor.getString(taxonIndex),
					cursor.getString(scientificNameIndex),

					cursor.getString(directoryNameIndex));
			// when a new subject arrives, clear the hashmap of stored bitmaps
			PictureHelper.resetLoadedBitmaps();
			cursor.close();
		}

	}

	/**
	 * Load select fields from cursor, used to populate the Spinners and closes
	 * the cursor
	 * 
	 * @param cursor
	 *            the cursor from the DAO, from a select query on the fields ID
	 *            and NAME
	 * @param intId
	 *            true if the id to map from the database is an integer. If
	 *            false, the id is a String (like in the country table)
	 * @return the map, NAME (String)-> ID (Integer)
	 */
	private SelectFieldsValue loadSelectFieldsFromCursor(final Cursor cursor,
			final boolean intId) {
		final Map<String, Integer> mapNameId = new HashMap<String, Integer>();
		final Map<String, String> mapNameCode = new HashMap<String, String>();
		// init the map and the list with "ALL" with id = 0
		mapNameId.put(this.activity.getString(R.string.search_all), -1);
		mapNameCode.put(this.activity.getString(R.string.search_all),
				BasicConstants.EMPTY_STRING);

		final List<String> fieldsValues = new ArrayList<String>();

		fieldsValues.add(this.activity.getString(R.string.search_all));
		if (cursor != null) {
			final int nbResults = cursor.getCount();
			for (int i = 0; i < nbResults; i++) {
				cursor.moveToPosition(i);
				final int idIndex = cursor.getColumnIndexOrThrow(IDAO.ID);
				final int nameIndex = cursor
						.getColumnIndexOrThrow(IDAO.NAME_COLUMN_NAME);

				if (intId) {
					mapNameId.put(cursor.getString(nameIndex),
							cursor.getInt(idIndex));
				} else {
					// in this case, if the id is a string (country code)
					mapNameCode.put(cursor.getString(nameIndex),
							cursor.getString(idIndex));
				}
				fieldsValues.add(cursor.getString(nameIndex));
			}
			cursor.close();
		}
		final SelectFieldsValue sfv = new SelectFieldsValue(mapNameId,
				mapNameCode, fieldsValues);
		return sfv;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.arboretum.service.IOrnidroidService#getMatchingBirds(java.lang
	 * .String)
	 */
	public List<SimpleSubject> getMatchingSubjects(String query) {
		queryResult = this.ornidroidDAO.getMatchingSubjects(query);
		return queryResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.arboretum.service.IOrnidroidService#getQueryResult()
	 */
	public List<SimpleSubject> getQueryResult() {
		return queryResult == null ? new ArrayList<SimpleSubject>()
				: queryResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.arboretum.service.IOrnidroidService#getReleaseNotes()
	 */
	public String getReleaseNotes() {
		return ornidroidDAO.getReleaseNotes();
	}
}
