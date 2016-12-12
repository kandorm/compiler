package decaf.error;

import decaf.Location;

/**
 * example：incompatible operands: int + bool<br>
 * PA2
 */
public class IncompatTerOpError extends DecafError {

	private String middle;

	private String right;

	public IncompatTerOpError(Location location, String middle, String right) {
		super(location);
		this.middle = middle;
		this.right = right;
	}

	@Override
	protected String getErrMsg() {
		return "incompatible condition expr type: " + middle + " and " + right;
	}

}
