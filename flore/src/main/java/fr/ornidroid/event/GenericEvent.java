package fr.ornidroid.event;

import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.ApplicationException;

public class GenericEvent {
	/** The exception. */
	private final Exception exception;
	public final EventType eventType;

	public GenericEvent(EventType eventType, Exception pException) {
		this.exception = pException;
		this.eventType = eventType;
	}

	public GenericEvent(EventType eventType) {
		this.exception = null;
		this.eventType = eventType;
	}

	public String getExceptionMessage() {
		if (null != exception) {
			if (ApplicationException.class.isInstance(exception)) {
				ApplicationException oe = (ApplicationException) exception;
				return oe.getSourceExceptionMessage();
			} else {
				return exception.toString();
			}
		} else {
			return BasicConstants.BLANK_STRING;
		}
	}

	public Exception getException() {
		return exception;
	}

}
