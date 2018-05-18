package unitn.adk2018.utils;

import java.util.Observable;
import java.util.Observer;

import unitn.adk2018.Environment;

public class Sleep {
	
	public static final void sleep(int ms) throws InterruptedException {
		WakeUpOnTimer w = new Sleep.WakeUpOnTimer();
		System.err.println("sleeping for " + ms);
		Environment.getEnvironmentAgent().rescheduleTimer(w, ms);
		synchronized (w) {
			w.wait();
		}
		System.err.println("wakeup");
	}
	
	static class WakeUpOnTimer implements Observer {
		
		@Override
		public void  update (Observable o, Object arg) {
			synchronized (this) {
				notifyAll();
			}
		}
		
	}
	
}

