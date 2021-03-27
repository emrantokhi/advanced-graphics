package Commands;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import a2.Starter;

public class MoveForwardCommand extends AbstractAction {
	
	/**
	 * Serial version UID default
	 */
	private static final long serialVersionUID = 1L;
	private Starter starter;
	
	public MoveForwardCommand(Starter starter) {
		this.starter = starter;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		starter.getCamera().moveForward();
	}

}
