package fr.arboretum.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.greenrobot.event.EventBus;
import fr.arboretum.R;
import fr.arboretum.bo.MultiCriteriaSearchFormBean;
import fr.arboretum.event.EventType;
import fr.arboretum.event.GenericEvent;
import fr.arboretum.helper.Constants;
import fr.arboretum.service.IService;
import fr.arboretum.service.ServiceFactory;
import fr.arboretum.ui.adapter.MyCustomAdapter;
import fr.arboretum.ui.multicriteriasearch.MultiCriteriaSearchFieldType;
import fr.arboretum.ui.multicriteriasearch.MultiCriteriaSelectField;
import fr.arboretum.ui.multicriteriasearch.OnSpinnersItemSelected;

/**
 * The Class MultiCriteriaSearchActivity.
 */
@EActivity(R.layout.multicriteriasearch)
public class MultiCriteriaSearchActivity extends AbstractActivity {
	/** The field list. */
	private final List<MultiCriteriaSelectField> fieldList = new ArrayList<MultiCriteriaSelectField>();

	/** The form bean. */
	private final MultiCriteriaSearchFormBean formBean = new MultiCriteriaSearchFormBean();

	/** The nb results text view. */
	@ViewById(R.id.search_nb_results)
	TextView nbResultsTextView;

	/** The ornidroid service. */
	private final IService ornidroidService = ServiceFactory.getService(this);
	@InstanceState
	boolean queryRunning = false;

	/** The progress bar. */
	@ViewById
	ProgressBar pbarSearchMulti;

	/** The reset form button. */
	@ViewById(R.id.reset_form)
	ImageView resetFormButton;

	/** The show results clickable area. */
	@ViewById(R.id.search_show_results)
	LinearLayout showResultsClickableArea;

	/**
	 * Find matching birds from mcs.
	 */
	@Background
	public void findMatchingBirdsFromMCS() {
		this.ornidroidService.getMatchesFromMultiSearchCriteria(this.formBean);
		// post the event in the EventBus
		EventBus.getDefault().post(new GenericEvent(EventType.MC_SEARCH));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	public void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	public void onStop() {
		super.onStop();
		EventBus.getDefault().unregister(this);
	}

	/**
	 * Gets the form bean.
	 * 
	 * @return the form bean
	 */
	public MultiCriteriaSearchFormBean getFormBean() {
		return this.formBean;
	}

	/**
	 * Gets the ornidroid service.
	 * 
	 * @return the ornidroid service
	 */
	public IService getOrnidroidService() {
		return this.ornidroidService;
	}

	/**
	 * Open results activity.
	 */
	@UiThread
	void openResultsActivity() {
		MainActivity_.intent(this)
				.extra(MainActivity_.SHOW_SEARCH_FIELD_INTENT_PRM, false)
				.start();
	}

	/**
	 * Onshow results clickable area click.
	 */
	@Click(R.id.search_show_results)
	public void onShowResultsClick() {
		if (!queryRunning) {
			queryRunning = true;
			findMatchingBirdsFromMCS();
			pbarSearchMulti.setVisibility(View.VISIBLE);
		}
	}

	@UiThread
	public void onEventMainThread(GenericEvent event) {
		if (event.eventType.equals(EventType.MC_SEARCH)) {
			queryRunning = false;
			openResultsActivity();
			pbarSearchMulti.setVisibility(View.GONE);
		}
	}

	/**
	 * After views.
	 */
	@AfterViews
	void afterViews() {
		if (queryRunning) {
			pbarSearchMulti.setVisibility(View.VISIBLE);
		}

		initSelectField(MultiCriteriaSearchFieldType.LEAF_TYPE);
		initSelectField(MultiCriteriaSearchFieldType.LEAF_DISPOSITION);
		initSelectField(MultiCriteriaSearchFieldType.SCIENTIFIC_FAMILY);
		/*
		 * updateSearchCountResults(this.ornidroidService
		 * .getMultiSearchCriteriaCountResults(this.formBean));
		 */
	}

	/**
	 * Returns the type of the field to which the adapter view belongs.
	 * 
	 * @param parent
	 *            the parent
	 * @return the select type
	 */
	public MultiCriteriaSearchFieldType getSelectType(
			final AdapterView<?> parent) {
		if (MultiCriteriaSelectField.class.isInstance(parent.getParent())) {
			final MultiCriteriaSelectField field = (MultiCriteriaSelectField) parent
					.getParent();
			return field.getFieldType();

		}
		return null;
	}

	/**
	 * Update the search count results button.
	 * 
	 * @param countResults
	 *            the count results
	 */
	public void updateSearchCountResults(final int countResults) {
		this.nbResultsTextView.setText(countResults + Constants.BLANK_STRING
				+ this.getText(R.string.search_results));
	}

	/**
	 * Inits the select field.
	 * 
	 * @param selectFieldType
	 *            the select field type
	 */
	private void initSelectField(
			final MultiCriteriaSearchFieldType selectFieldType) {
		// TODO : c'est ici qu'on customize les menus déroulants
		ArrayAdapter<String> dataAdapter = null;
		MultiCriteriaSelectField field = null;
		switch (selectFieldType) {
		case SCIENTIFIC_FAMILY:
			field = (MultiCriteriaSelectField) findViewById(R.id.search_scientific_family_field);
			// field.setIconResource(R.drawable.tous);
			dataAdapter = new ArrayAdapter<String>(this,
					R.layout.row_spinner_without_icons,
					this.ornidroidService.getScientificFamilies());

			break;
		case LEAF_TYPE:
			field = (MultiCriteriaSelectField) findViewById(R.id.search_leaf_type_field);

			dataAdapter = new MyCustomAdapter(this, R.layout.row_spinner_icons,
					this.ornidroidService.getLeafTypes(), selectFieldType);
			break;
					case LEAF_DISPOSITION:
			field = (MultiCriteriaSelectField) findViewById(R.id.search_leaf_disposition_field);

			dataAdapter = new MyCustomAdapter(this, R.layout.row_spinner_icons,
					this.ornidroidService.getLeafDispositions(),
					selectFieldType);
			break;
			

		}
		field.setFieldType(selectFieldType);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		field.getSpinner().setAdapter(dataAdapter);
		field.getSpinner().setOnItemSelectedListener(
				new OnSpinnersItemSelected(this));
		this.fieldList.add(field);

	}

	/**
	 * Reset all the select fields in the form.
	 */
	@Click(R.id.reset_form)
	void resetForm() {
		for (final MultiCriteriaSelectField field : this.fieldList) {
			field.reset();
		}
	}
}
