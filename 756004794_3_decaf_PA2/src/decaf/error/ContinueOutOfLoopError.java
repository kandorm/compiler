package decaf.error;

import decaf.Location;

/**
 * example 'continue' is only allowed inside a loop<br>
 * PA2
 */
public class ContinueOutOfLoopError extends DecafError {

	public ContinueOutOfLoopError(Location location) {
		super(location);
	}

	@Override
	protected String getErrMsg() {
		return "'continue' is only allowed inside a loop";
	}

}
