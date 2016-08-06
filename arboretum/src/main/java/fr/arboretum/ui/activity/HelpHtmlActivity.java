package fr.arboretum.ui.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.webkit.WebView;
import fr.arboretum.R;
import fr.arboretum.ui.multicriteriasearch.MultiCriteriaSearchFieldType;

@EActivity(R.layout.help_html)
public class HelpHtmlActivity extends AbstractActivity {

	public static final String MultiCriteriaSearchFieldTypeIntentPrm = "MultiCriteriaSearchFieldType";

	@ViewById(R.id.wv_help)
	WebView wvHelp;

	@Extra(MultiCriteriaSearchFieldTypeIntentPrm)
	MultiCriteriaSearchFieldType fieldType;

	/**
	 * After views.
	 */
	@AfterViews
	void afterViews() {
		StringBuffer sbuf = new StringBuffer("file:///android_asset/");
		switch (fieldType) {

		case LEAF_TYPE:
			sbuf.append("help_type_feuille.html");
			break;
		default:
			break;
		}

		wvHelp.loadUrl(sbuf.toString());

	}

}
