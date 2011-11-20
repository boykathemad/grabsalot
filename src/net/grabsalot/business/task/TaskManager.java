package net.grabsalot.business.task;

import net.grabsalot.business.Logger;

import net.grabsalot.business.task.Task;
import net.grabsalot.business.task.TaskEvent;
import net.grabsalot.business.task.TaskListener;
import net.grabsalot.business.task.TaskManager;

import java.util.HashMap;
import java.util.Iterator;

public class TaskManager implements TaskListener {

	private HashMap<String, Task> taskList;
	private static TaskManager instance;
	
	public static TaskManager getInstance() {
		if (instance == null) {
			instance = new TaskManager();
		}
		return instance;
	}

	public TaskManager() {
		taskList = new HashMap<String, Task>();
	}
	
	public void add(Task task) {
		if (taskList.containsKey(task.getId())) {
			taskList.get(task.getId()).stop();
		}
		taskList.put(task.getId(), task);
		task.addTaskListener(this);
	}
	
	public void start() {
		try {
			this.getNextAvailableTask().start();
		} catch (NullPointerException e) {
			Logger._().warning("TaskManager:start: There was no souitable task to start.");
		}
	}
	
	public Task getNextAvailableTask() {
		Iterator<String> i = this.taskList.keySet().iterator();
		while (i.hasNext()) {
			String key = i.next();
			Task task = this.taskList.get(key);
			if (!task.isRunning()) {
				return task;
			}
		}
		return null;
	}

	@Override
	public void taskComplete(TaskEvent e) {
		taskList.remove(e.getTaskObject().getId());
		this.start();
	}

	@Override
	public void taskError(TaskEvent e) {
		taskList.remove(e.getTaskObject().getId());
		this.start();
	}

}
