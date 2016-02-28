package fr.arboretum.ui.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.webkit.WebView;
import fr.arboretum.bo.MediaFile;
import fr.arboretum.bo.MediaFileType;
import fr.arboretum.R;

/**
 * The Class WikipediaFragment.
 */
@EFragment(R.layout.fragment_wikipedia)
public class WikipediaFragment extends AbstractFragment {

	@ViewById(R.id.wikipiedia_webview)
	WebView wikipediaWebView;

	@AfterViews
	void afterViews() {

		if (commonAfterViews()) {
			MediaFile wikipediaPage = service.getCurrentSubject()
					.getWikipediaPage();
			wikipediaWebView.loadUrl("file:///" + wikipediaPage.getPath());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.arboretum.ui.components.AbstractFragment#getFileType()
	 */
	@Override
	public MediaFileType getFileType() {
		return MediaFileType.WIKIPEDIA_PAGE;
	}

}
