package net.grabsalot.util;

import java.lang.ref.WeakReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TickTimer implements Runnable {

	private static int timerCount = 0;
	private volatile WeakReference<TickListener> listener;
	private int delay;
	private volatile Thread runner;
	private boolean running;
	private String name;

	public TickTimer(int delay, TickListener listener) {
		this.delay = delay;
		this.listener = new WeakReference<TickListener>(listener);
		name = this.getClass().getSimpleName() + "-" + (++timerCount);
	}

	public void start() {
		if (running) stop();
		runner = new Thread(this);
		runner.setName(name);
		running = true;
		runner.start();
	}

	@Override
	public void run() {
		while (running && listener.get() != null) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException ex) {
				running = false;
			}
			if (listener.get() == null) {
				return;
			}
			listener.get().tick();
		}
	}

	public void stop() {
		if (runner != null) {
			running = false;
			runner.interrupt();
			runner = null;
		}
	}
}
