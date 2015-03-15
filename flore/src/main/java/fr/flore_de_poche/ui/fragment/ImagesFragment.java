package fr.flore_de_poche.ui.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import fr.flore.R;
import fr.flore_de_poche.bo.MediaFile;
import fr.flore_de_poche.bo.MediaFileType;
import fr.flore_de_poche.bo.PictureFile;
import fr.flore_de_poche.helper.BasicConstants;
import fr.flore_de_poche.ui.activity.ScrollableImageActivity;
import fr.flore_de_poche.ui.components.PictureInfoDialog;
import fr.flore_de_poche.ui.picture.PictureHelper;
import fr.flore_de_poche.ui.activity.ScrollableImageActivity_;

/**
 * The Class ImagesFragment.
 */
@EFragment(R.layout.fragment_images)
public class ImagesFragment extends AbstractFragment {

	/** The displayed picture id. */
	private int displayedPictureId;

	/** The number of pictures text view. */
	@ViewById(R.id.tv_nb_pictures)
	TextView numberOfPicturesTextView;

	/** The taxon. */
	@ViewById(R.id.tv_taxon)
	TextView taxon;

	/** The gesture listener. */
	View.OnTouchListener gestureListener;

	/** The zoom button. */
	@ViewById(R.id.zoom_button)
	ImageView zoomButton;
	/** The info button. */
	@ViewById(R.id.iv_info_button_picture)
	ImageView infoButton;

	/** The next button. */
	@ViewById(R.id.next_button)
	ImageView nextButton;

	/** The m picture. */
	@ViewById(R.id.picture)
	ImageView mPicture;

	/** The previous button. */
	@ViewById(R.id.previous_button)
	ImageView previousButton;

	/** The m picture description. */
	@ViewById(R.id.picture_description)
	TextView mPictureDescription;

	/**
	 * Load image and change picture description.
	 * 
	 * @param imagePosition
	 *            the image position
	 */
	private void loadImage(int imagePosition) {
		if (imagePosition < 0) {
			imagePosition = this.ornidroidService.getCurrentSubject()
					.getNumberOfPictures() - 1;
		}
		if (imagePosition >= this.ornidroidService.getCurrentSubject()
				.getNumberOfPictures()) {
			imagePosition = 0;
		}
		MediaFile pictureFile = ornidroidService.getCurrentSubject()
				.getPicture(imagePosition);
		if (pictureFile != null) {
			this.mPictureDescription
					.setText(pictureFile
							.getProperty(PictureFile.IMAGE_DESCRIPTION_PROPERTY));

			Bitmap bMap = PictureHelper.loadBitmap(pictureFile, getActivity()
					.getResources());
			if (bMap != null) {
				this.mPicture.setImageBitmap(bMap);

			}
			setCurrentMediaFilePosition(imagePosition);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.flore_de_poche.ui.components.AbstractFragment#getFileType()
	 */
	@Override
	public MediaFileType getFileType() {
		return MediaFileType.PICTURE;
	}

	@AfterViews
	void afterViews() {
		if (commonAfterViews()) {
			loadImage(0);
		}
		this.taxon.setText(ornidroidService.getCurrentSubject().getTaxon());
	}

	/**
	 * Returns the formatted text which displays the number of pictures for this
	 * bird and the current picture number.
	 * 
	 * @return the text
	 */
	private void updateNumberOfPicturesText() {
		final StringBuilder sb = new StringBuilder();
		sb.append(this.displayedPictureId + 1);
		sb.append(BasicConstants.SLASH_STRING);
		sb.append(this.ornidroidService.getCurrentSubject().getNumberOfPictures());

		this.numberOfPicturesTextView.setText(sb.toString());

	}

	/**
	 * Sets the current media file postion.
	 * 
	 * @param currentItem
	 *            the new current media file postion
	 */
	public void setCurrentMediaFilePosition(int currentItem) {
		this.displayedPictureId = currentItem;
		final MediaFile picture = this.ornidroidService.getCurrentSubject()
				.getPicture(currentItem);
		setCurrentMediaFile(picture);
		updateNumberOfPicturesText();
	}

	@Click(R.id.iv_info_button_picture)
	void infoButtonClicked() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		PictureInfoDialog pictureInfoDialog = new PictureInfoDialog();
		pictureInfoDialog.setOrnidroidFile(getCurrentMediaFile());
		pictureInfoDialog.show(fm, "pictureInfoDialog");

	}

	@Click({ R.id.picture, R.id.zoom_button })
	void pictureClicked() {
		final Intent intentImageFullSize = new Intent(getActivity(),
				ScrollableImageActivity_.class);
		intentImageFullSize.putExtra(
				ScrollableImageActivity.DISPLAYED_PICTURE_ID,
				displayedPictureId);
		getActivity().startActivity(
				intentImageFullSize.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}

	@Click(R.id.next_button)
	void nextButtonClicked() {
		loadImage(displayedPictureId + 1);
	}

	@Click(R.id.previous_button)
	void previousButtonClicked() {
		loadImage(displayedPictureId - 1);
	}

}
