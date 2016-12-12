package decaf.error;

import decaf.Location;

/**
 * switch varible must be a int type<br>
 * PA2
 */
public class BadSwitchVarError extends DecafError {

	public BadSwitchVarError(Location location) {
		super(location);
	}

	@Override
	protected String getErrMsg() {
		return "switch varible must be a int";
	}

}
