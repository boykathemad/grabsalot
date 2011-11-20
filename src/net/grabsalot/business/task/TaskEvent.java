package net.grabsalot.business.task;

import net.grabsalot.business.task.Task;

public class TaskEvent {
	private Task taskObject;
	private Exception taskException;

	public TaskEvent(Task task) {
		this.taskObject = task;
	}

	public TaskEvent(Task task, Exception exception) {
		this(task);
		this.taskException = exception;
	}

	public Task getTaskObject() {
		return taskObject;
	}

	public Exception getTaskException() {
		return taskException;
	}
}