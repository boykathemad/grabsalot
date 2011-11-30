package net.grabsalot.business.task;

public interface TaskListener {

	public void taskError(TaskEvent e);

	public void taskComplete(TaskEvent e);

}
