package Commands;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import a2.Starter;

public class MoveRightCommand extends AbstractAction {
	
	/**
	 * Default serial UID version
	 */
	private static final long serialVersionUID = 1L;
	private Starter starter;
	
	public MoveRightCommand(Starter starter) {
		this.starter = starter;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		starter.getCamera().moveRight();
	}

}
