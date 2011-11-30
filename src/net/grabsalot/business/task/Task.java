package net.grabsalot.business.task;

import java.util.ArrayList;
import java.util.List;


public abstract class Task {
	private String id;
	private List<TaskListener> listeners;
	private Thread thread;

	public Task(String id) {
		this.id = id;
		this.listeners = new ArrayList<TaskListener>();
	}

	public Task() {
		this("NumberedTask-" + Integer.toString((int) (Math.random() * Integer.MAX_VALUE)));
	}

	public String getId() {
		return id;
	}

	public void addTaskListener(TaskListener listener) {
		this.listeners.add(listener);
	}

	public void start() {
		thread = new Thread() {
			@Override
			public void run() {
				try {
					Task.this.run();
				} catch (Exception ex) {
					System.out.println("Task error");
					ex.printStackTrace();
					for (TaskListener listener : Task.this.listeners) {
						listener.taskError(new TaskEvent(Task.this,ex));
					}
					return;
				}
				for (TaskListener listener : Task.this.listeners) {
					listener.taskComplete(new TaskEvent(Task.this));
				}
			}
		};
		thread.start();
	}

	public void stop() {
		if (this.isRunning()) {
			this.thread.interrupt();
		}
	}

	public abstract void run();

	public boolean isRunning() {
		if (this.thread != null && this.thread.isAlive()) {
			return true;
		}
		return false;
	}

}
