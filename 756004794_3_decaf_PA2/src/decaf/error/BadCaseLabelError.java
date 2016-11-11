package decaf.error;

import decaf.Location;

/**
 * case label must be a constant int<br>
 * PA2
 */
public class BadCaseLabelError extends DecafError {

	public BadCaseLabelError(Location location) {
		super(location);
	}

	@Override
	protected String getErrMsg() {
		return "case label must be a constant int";
	}

}
