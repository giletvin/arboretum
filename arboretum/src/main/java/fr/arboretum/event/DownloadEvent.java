package fr.arboretum.event;

import fr.arboretum.bo.MediaFileType;

public class DownloadEvent extends GenericEvent {

	public final MediaFileType fileType;

	public DownloadEvent(EventType eventType, Exception pException,
			MediaFileType pFileType) {
		super(eventType, pException);
		fileType = pFileType;
	}
}
