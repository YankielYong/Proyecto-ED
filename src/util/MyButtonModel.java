package util;

import javax.swing.DefaultButtonModel;

public class MyButtonModel extends DefaultButtonModel{
	
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isPressed() {
		return false;
	}

	@Override
	public boolean isRollover() {
		return false;
	}

	@Override
	public void setRollover(boolean b) {
		//NOOP
	}
}
