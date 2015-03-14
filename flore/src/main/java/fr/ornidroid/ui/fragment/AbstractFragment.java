package fr.ornidroid.ui.fragment;

import java.io.File;
import java.util.List;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import de.greenrobot.event.EventBus;
import fr.flore.R;
import fr.ornidroid.bo.OrnidroidFile;
import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.event.CheckForUpdateEvent;
import fr.ornidroid.event.DownloadEvent;
import fr.ornidroid.event.EventType;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.OrnidroidError;
import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.service.IIOService;
import fr.ornidroid.service.IService;
import fr.ornidroid.service.IOServiceImpl;
import fr.ornidroid.service.ServiceFactory;
import fr.ornidroid.ui.activity.AddCustomMediaActivity_;
import fr.ornidroid.ui.activity.HomeActivity_;
import fr.ornidroid.ui.activity.NewBirdActivity;
import fr.ornidroid.ui.activity.NewBirdActivity_;

/**
 * The Class AbstractFragment.
 */
@EFragment
public abstract class AbstractFragment extends Fragment {

	/** The download in progress type. */
	@InstanceState
	DownloadType downloadInProgressType = DownloadType.NO_DOWNLOAD;

	@InstanceState
	boolean checkForUpdatesInProgress = false;
	/**
	 * Gets the current (selected) media file if picture : the displayed image,
	 * if sound, the played mp3.
	 */
	private OrnidroidFile currentMediaFile;

	/** The pb download in progress. */
	@ViewById(R.id.pb_download_in_progress)
	ProgressBar pbDownloadInProgress;

	/** The pb download all in progress. */
	@ViewById(R.id.pb_download_all_in_progress)
	ProgressBar pbDownloadAllInProgress;

	/** The ornidroid service. */
	IService ornidroidService = ServiceFactory
			.getService(getActivity());

	/** The ornidroid io service. */
	IIOService ornidroidIOService = new IOServiceImpl();

	/** Layouts. */
	@ViewById(R.id.fragment_main_content)
	LinearLayout fragmentMainContent;

	/** The download banner. */
	@ViewById(R.id.download_banner)
	View downloadBanner;

	/** The bt download only for bird. */
	@ViewById(R.id.bt_download_only_for_bird)
	Button btDownloadOnlyForBird;

	/** The bt download all. */
	@ViewById(R.id.bt_download_all)
	Button btDownloadAll;

	/** The no media message. */
	@ViewById(R.id.tv_no_media_message)
	TextView noMediaMessage;
	/** The update files button. */
	@ViewById(R.id.iv_update_files_button)
	ImageView updateFilesButton;

	/** The add custom media button. */
	@ViewById(R.id.iv_add_custom_media)
	ImageView addCustomMediaButton;

	/** The remove custom media button. */
	@ViewById(R.id.iv_remove_custom_media)
	ImageView removeCustomMediaButton;

	/**
	 * Gets the file type.
	 * 
	 * @return the file type
	 */
	public abstract OrnidroidFileType getFileType();

	/**
	 * Gets the media home directory.
	 * 
	 * @return the media home directory
	 */
	public String getMediaHomeDirectory() {
		String mediaHomeDirectory = null;
		switch (getFileType()) {
		case AUDIO:
			mediaHomeDirectory = Constants.getOrnidroidHomeAudio();
			break;
		case PICTURE:
			mediaHomeDirectory = Constants.getOrnidroidHomeImages();
			break;
		case WIKIPEDIA_PAGE:
			mediaHomeDirectory = Constants.getOrnidroidHomeWikipedia();
		}
		return mediaHomeDirectory;
	}

	/**
	 * Load media files locally.
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	public void loadMediaFilesLocally() throws OrnidroidException {
		if (this.ornidroidService.getCurrentBird() != null) {
			final File fileDirectory = new File(
					Constants.getOrnidroidBirdHomeMedia(
							this.ornidroidService.getCurrentBird(),
							getFileType()));
			this.ornidroidIOService.checkAndCreateDirectory(fileDirectory);

			this.ornidroidIOService.loadMediaFiles(getMediaHomeDirectory(),
					this.ornidroidService.getCurrentBird(), getFileType());
		}
	}

	/**
	 * Download from internet button clicked.
	 */
	@Click(R.id.bt_download_only_for_bird)
	void downloadFromInternetButtonClicked() {
		startDownload();
	}

	/**
	 * Download all button clicked.
	 */
	@Click(R.id.bt_download_all)
	void downloadAllButtonClicked() {
		Dialog dialog = new AlertDialog.Builder(this.getActivity())
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.warning)
				.setMessage(R.string.download_zip_package_warn_detail)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int whichButton) {
								dialog.dismiss();
								startDownloadAll();
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int whichButton) {
								dialog.dismiss();
							}
						}).create();
		dialog.show();
	}

	/**
	 * Adds the custom picture clicked.
	 */
	@Click(R.id.iv_add_custom_media)
	void addCustomPictureClicked() {
		final Intent intent = new Intent(getActivity(),
				AddCustomMediaActivity_.class);
		intent.putExtra(OrnidroidFileType.FILE_TYPE_INTENT_PARAM_NAME,
				getFileType());
		intent.putExtra(Constants.BIRD_DIRECTORY_PARAMETER_NAME,
				this.ornidroidService.getCurrentBird().getBirdDirectoryName());
		startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}

	/**
	 * Removes the custom picture clicked.
	 */
	@Click(R.id.iv_remove_custom_media)
	void removeCustomPictureClicked() {
		try {
			this.ornidroidIOService
					.removeCustomMediaFile(this.currentMediaFile);
			Toast.makeText(
					getActivity(),
					this.getResources().getString(
							R.string.remove_custom_media_success),
					Toast.LENGTH_LONG).show();
			final Intent intent = new Intent(getActivity(),
					NewBirdActivity_.class);
			// put the uri so that the BirdInfoActivity reloads correctly
			// the bird
			intent.setData(getActivity().getIntent().getData());
			// put an extra info to let the BirdInfoActivity know which tab
			// to open
			intent.putExtra(NewBirdActivity.INTENT_TAB_TO_OPEN,
					OrnidroidFileType.getCode(getFileType()));
			startActivity(intent);
			getActivity().finish();
		} catch (final OrnidroidException e) {
			Toast.makeText(
					getActivity(),
					this.getResources().getString(
							R.string.add_custom_media_error)
							+ e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Update files button clicked.
	 */
	@Click(R.id.iv_update_files_button)
	void updateFilesButtonClicked() {
		checkForUpdates(true);
	}

	/**
	 * Start download.
	 */
	@Background
	void startDownload() {
		if (downloadInProgressType == DownloadType.NO_DOWNLOAD) {
			downloadInProgressType = DownloadType.DOWNLOAD_ONE;
			resetScreenBeforeDownload();

			Exception exception = null;
			try {
				AbstractFragment.this.ornidroidIOService
						.downloadMediaFiles(getMediaHomeDirectory(),
								AbstractFragment.this.ornidroidService
										.getCurrentBird(), getFileType());
			} catch (final OrnidroidException e) {
				exception = e;
			} finally {
				// post the event in the EventBus
				EventBus.getDefault().post(
						new DownloadEvent(EventType.DOWNLOAD_ONE, exception,
								getFileType()));
			}

		}
	}

	/**
	 * Reset screen before download.
	 */
	@UiThread
	void resetScreenBeforeDownload() {
		this.btDownloadOnlyForBird.setVisibility(View.GONE);
		this.btDownloadAll.setVisibility(View.GONE);
		switch (downloadInProgressType) {
		case DOWNLOAD_ALL:
			pbDownloadAllInProgress.setVisibility(View.VISIBLE);
			break;
		case DOWNLOAD_ONE:
			pbDownloadInProgress.setVisibility(View.VISIBLE);

			this.noMediaMessage.setText(R.string.download_please_wait);
			break;
		default:
			break;
		}
	}

	/**
	 * Check for updates. Fires a dialog box if updates are found. If manual
	 * check, let the user know that no updates are found.
	 * 
	 * @param manualCheck
	 *            true if the user checks manually.
	 * 
	 */
	@Background
	void checkForUpdates(final boolean manualCheck) {
		if (!checkForUpdatesInProgress) {
			checkForUpdatesInProgress = true;
			Exception exception = null;
			boolean updatesToDo = false;
			List<String> filesToDownload;
			try {
				filesToDownload = this.ornidroidIOService.filesToUpdate(
						getMediaHomeDirectory(),
						ornidroidService.getCurrentBird(), getFileType());
				updatesToDo = (filesToDownload.size() > 0);

			} catch (Exception e) {
				exception = e;
			} finally {
				EventBus.getDefault().post(
						new CheckForUpdateEvent(exception, updatesToDo,
								manualCheck, getFileType()));
				checkForUpdatesInProgress = false;
				// onCheckUpdateTaskEnded(manualCheck, updatesToDo, exception);
			}
		}

	}

	/**
	 * updates periodically the progress bar when a download or installation of
	 * zip package is running.
	 */
	@Background(delay = 2000)
	void manageDownloadProgressBar() {
		while (downloadInProgressType == DownloadType.DOWNLOAD_ALL) {
			updateDownloadAllProgressBar();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {

			}
		}
	}

	/**
	 * Update download progress bar in UIThread
	 */
	@UiThread
	void updateDownloadAllProgressBar() {
		// TODO : il pourrait y avoir des NPE ici après un retournement
		// d'écran ??
		if (ornidroidIOService.getZipDownloadProgressPercent(getFileType()) < 100) {
			noMediaMessage.setText(R.string.download_package_in_progress);
			pbDownloadAllInProgress.setProgress(ornidroidIOService
					.getZipDownloadProgressPercent(getFileType()));
		} else {
			noMediaMessage.setText(R.string.install_package_in_progress);
			pbDownloadAllInProgress.setMax(BasicConstants
					.getNbOfFilesInPackage(getFileType()));
			pbDownloadAllInProgress.setProgress(ornidroidIOService
					.getInstallProgressPercent(getFileType()));
		}
	}

	/**
	 * Start download a zip package.
	 */
	@Background
	void startDownloadAll() {
		if (downloadInProgressType == DownloadType.NO_DOWNLOAD) {
			downloadInProgressType = DownloadType.DOWNLOAD_ALL;
			resetScreenBeforeDownload();
			boolean isEnoughFreeSpace = true;
			try {
				isEnoughFreeSpace = this.ornidroidIOService
						.isEnoughFreeSpace(getFileType());
				if (isEnoughFreeSpace) {
					Exception exception = null;
					try {
						manageDownloadProgressBar();

						String zipname = this.ornidroidIOService
								.getZipname(getFileType());
						try {
							this.ornidroidIOService.downloadZipPackage(zipname,
									Constants.getOrnidroidHome());
						} catch (OrnidroidException e) {
							exception = e;
						}
					} finally {
						// post the event in the EventBus
						EventBus.getDefault().post(
								new DownloadEvent(EventType.DOWNLOAD_ZIP,
										exception, getFileType()));
					}
				} else {
					notEnoughFreeSpaceForPackageDownloadDialog();
				}
			} catch (IllegalArgumentException e) {
				// Fix #128
				// post the event in the EventBus
				EventBus.getDefault().post(
						new DownloadEvent(EventType.DOWNLOAD_ZIP, e,
								getFileType()));
			}

		}
	}

	/**
	 * Show a dialog box when there's not enough free space for package download
	 * and installation.
	 */
	@UiThread
	void notEnoughFreeSpaceForPackageDownloadDialog() {
		Dialog dialog = new AlertDialog.Builder(this.getActivity())
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.download_zip_package_error)
				.setMessage(R.string.download_zip_not_enough_space)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int whichButton) {
								dialog.dismiss();
								reloadActivity();
							}
						}).create();
		dialog.show();
	}

	@UiThread
	void onEventMainThread(CheckForUpdateEvent event) {
		if (getFileType().equals(event.fileType)) {
			if (event.getException() != null) {
				Toast.makeText(getActivity(), R.string.updates_check_error,
						Toast.LENGTH_LONG).show();
			} else {
				if (event.updatesAvailable) {
					AlertDialog dialog = new AlertDialog.Builder(
							this.getActivity())
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setTitle(R.string.updates_available)
							.setNegativeButton(R.string.cancel,
									new DialogInterface.OnClickListener() {
										public void onClick(
												final DialogInterface dialog,
												final int whichButton) {
										}
									})
							.setPositiveButton(R.string.ok,
									new DialogInterface.OnClickListener() {
										public void onClick(
												final DialogInterface dialog,
												final int whichButton) {
											startDownload();
										}
									}).create();
					dialog.show();
				} else {
					if (event.manualCheck) {
						Toast.makeText(getActivity(), R.string.updates_none,
								Toast.LENGTH_LONG).show();
					}
				}
			}
		}

	}

	/**
	 * On download zip package task ended.
	 * 
	 * @param event
	 *            the event
	 */
	@UiThread
	void onEventMainThread(DownloadEvent event) {
		// #126 : check we are in the fragment that should really do something
		// with this event
		if (getFileType().equals(event.fileType)) {
			downloadInProgressType = DownloadType.NO_DOWNLOAD;
			if (event.getException() == null) {
				reloadActivity();
			} else {
				if (event.eventType != null) {
					switch (event.eventType) {
					case DOWNLOAD_ONE:
						errorDialogDownloadOne(event.getException());
						break;
					case DOWNLOAD_ZIP:
						errorDialogDownloadZip(event.getExceptionMessage());
						break;
					default:
						errorDialogUnknowReason(event.getExceptionMessage());
						break;
					}
				} else {
					errorDialogUnknowReason(event.getExceptionMessage());
				}
			}
		}

	}

	void errorDialogUnknowReason(String exceptionMessage) {
		String downloadErrorText = getActivity().getResources().getString(
				R.string.unknown_error)
				+ BasicConstants.CARRIAGE_RETURN + exceptionMessage;
		Dialog dialog = new AlertDialog.Builder(this.getActivity())
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.warning)
				.setMessage(downloadErrorText)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int whichButton) {
								dialog.dismiss();
								reloadActivity();
							}
						}).create();
		dialog.show();
	}

	void errorDialogDownloadZip(String exceptionMessage) {
		String downloadErrorText = getActivity().getResources().getString(
				R.string.download_zip_package_error_detail)
				+ BasicConstants.CARRIAGE_RETURN + exceptionMessage;
		Dialog dialog = new AlertDialog.Builder(this.getActivity())
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.download_zip_package_error)
				.setMessage(downloadErrorText)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int whichButton) {
								dialog.dismiss();
								reloadActivity();
							}
						}).create();
		dialog.show();
	}

	void errorDialogDownloadOne(Exception exception) {
		String downloadErrorText = getErrorMessage((OrnidroidException) exception);
		Dialog dialog = new AlertDialog.Builder(this.getActivity())
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.download_birds_file)
				.setMessage(downloadErrorText)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int whichButton) {
								dialog.dismiss();
								reloadActivity();
							}
						}).create();
		dialog.show();

	}

	/**
	 * Reload activity.
	 */
	private void reloadActivity() {
		if (this.getActivity() != null) {
			final Intent intentBirdInfo = new Intent(this.getActivity(),
					NewBirdActivity_.class);
			intentBirdInfo.putExtra(NewBirdActivity.INTENT_TAB_TO_OPEN,
					OrnidroidFileType.getCode(this.getFileType()));
			startActivity(intentBirdInfo
					.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		}
	}

	/**
	 * Sets the currently selected media file and makes the remove media button
	 * appear or disappear accordingly.
	 * 
	 * @param selectedFile
	 *            the new selected file
	 */
	public void setCurrentMediaFile(final OrnidroidFile selectedFile) {
		this.currentMediaFile = selectedFile;
		if (this.currentMediaFile != null) {
			if (this.currentMediaFile.isCustomMediaFile()) {
				removeCustomMediaButton.setVisibility(View.VISIBLE);
			} else {
				removeCustomMediaButton.setVisibility(View.GONE);
			}
		} else {
			this.removeCustomMediaButton.setVisibility(View.GONE);
		}
	}

	/**
	 * Gets the current media file.
	 * 
	 * @return the current media file
	 */
	public OrnidroidFile getCurrentMediaFile() {
		return currentMediaFile;
	}

	/**
	 * Prints the download button and info.
	 * 
	 * @param ornidroidException
	 *            the ornidroid exception
	 * @return the error message
	 */
	public String getErrorMessage(OrnidroidException ornidroidException) {
		OrnidroidError ornidroidError = ornidroidException.getErrorType();
		String msg = null;
		switch (ornidroidError) {
		case ORNIDROID_CONNECTION_PROBLEM:
			msg = getActivity().getResources().getString(
					R.string.dialog_alert_connection_problem);
			break;
		case ORNIDROID_DOWNLOAD_ERROR_MEDIA_DOES_NOT_EXIST:
			msg = getActivity().getResources().getString(
					R.string.no_resources_online);

			break;
		case NO_ERROR:
			switch (getFileType()) {
			case AUDIO:
				msg = getActivity().getResources().getString(
						R.string.no_records);

				break;
			case PICTURE:
				msg = getActivity().getResources().getString(
						R.string.no_pictures);

				break;
			case WIKIPEDIA_PAGE:
				msg = getActivity().getResources().getString(R.string.no_wiki);

				break;
			}
			break;
		default:
			msg = getActivity().getResources()
					.getString(R.string.unknown_error);
			break;
		}
		return msg;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		EventBus.getDefault().register(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	/**
	 * Common after views.
	 * 
	 * @return true, if successful and media are available. False in case of pb
	 *         or no media to display. Download banner is automatically
	 *         displayed
	 */
	boolean commonAfterViews() {
		if (downloadInProgressType != DownloadType.NO_DOWNLOAD) {
			// show download banner
			fragmentMainContent.setVisibility(View.GONE);
			downloadBanner.setVisibility(View.VISIBLE);
			resetScreenBeforeDownload();
			manageDownloadProgressBar();

			return false;
		} else {

			boolean success = true;
			if (this.ornidroidService.getCurrentBird() == null) {
				// Github : #118
				final Intent intent = new Intent(getActivity(),
						HomeActivity_.class);
				startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			} else {
				if (Constants.getAutomaticUpdateCheckPreference()
						&& updateFilesButton != null) {
					updateFilesButton.setVisibility(View.GONE);
					checkForUpdates(false);
				}
				try {
					loadMediaFilesLocally();

					switch (getFileType()) {
					case PICTURE:
						success = (ornidroidService.getCurrentBird()
								.getNumberOfPictures() > 0);
						break;
					case AUDIO:
						success = (ornidroidService.getCurrentBird()
								.getNumberOfSounds() > 0);
						break;
					case WIKIPEDIA_PAGE:
						success = (ornidroidService.getCurrentBird()
								.getWikipediaPage() != null);
						break;
					}

					if (!success) {
						fragmentMainContent.setVisibility(View.GONE);
						downloadBanner.setVisibility(View.VISIBLE);
					} else {
						fragmentMainContent.setVisibility(View.VISIBLE);
						downloadBanner.setVisibility(View.GONE);

					}
				} catch (final OrnidroidException e) {
					success = false;
					Toast.makeText(
							getActivity(),
							"Error reading media files of bird "
									+ this.ornidroidService.getCurrentBird()
											.getTaxon() + " e",
							Toast.LENGTH_LONG).show();
				}
			}
			return success;

		}
	}

	/**
	 * The Enum DownloadType.
	 */
	enum DownloadType {

		/** The no download. */
		NO_DOWNLOAD,
		/** The download one. */
		DOWNLOAD_ONE,
		/** The download all. */
		DOWNLOAD_ALL;
	}
}
