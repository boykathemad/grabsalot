package net.grabsalot.business.task;

import net.grabsalot.business.task.TaskEvent;

public interface TaskListener {

	public void taskError(TaskEvent e);

	public void taskComplete(TaskEvent e);

}
