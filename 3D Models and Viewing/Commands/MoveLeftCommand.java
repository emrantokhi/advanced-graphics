package Commands;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import a2.Starter;

public class MoveLeftCommand extends AbstractAction {
	
	/**
	 * Serial version UID default
	 */
	private static final long serialVersionUID = 1L;
	private Starter starter;
	
	public MoveLeftCommand(Starter starter) {
		this.starter = starter;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		starter.getCamera().moveLeft();
	}

}
