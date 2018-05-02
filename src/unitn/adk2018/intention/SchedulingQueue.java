package unitn.adk2018.intention;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Timer;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Observer;

public class SchedulingQueue extends TimerTask {

	long    msecs_from_creation;
	final   int PERIOD = 100; // in msecs
	boolean paused;

	ConcurrentLinkedQueue<ScheduledIntention>  intQueue;
	
	class TimedObserver {
		long       msecsToWait;
		Observer   obs;
		TimedObserver (long l, Observer o) {
			msecsToWait = l;
			obs = o;
		}
	}
    ConcurrentLinkedQueue<TimedObserver>  wakeUpQueue;	
	
	public SchedulingQueue (Timer ti) {
		ti.scheduleAtFixedRate (this, PERIOD, PERIOD);
		intQueue = new ConcurrentLinkedQueue<ScheduledIntention> ();
		wakeUpQueue = new ConcurrentLinkedQueue<TimedObserver> ();
		msecs_from_creation = 0;
		paused = false;
	}
	
	public int size() {
		return intQueue.size();
	}
	
	public long currentTime() {
		return msecs_from_creation;
	}
	
	public ConcurrentLinkedQueue<ScheduledIntention> getQueue() {
		return intQueue;
	}
	
	synchronized public boolean offer (ScheduledIntention si) {
		return intQueue.offer(si);
	}
	
	synchronized public boolean remove (ScheduledIntention si) {
		return intQueue.remove(si);
	}

	synchronized public boolean offer (Observer obs, long howLong) {
		TimedObserver to = new TimedObserver(howLong, obs);
		return wakeUpQueue.offer(to);
	}
	
	synchronized public boolean remove (Observer obs) {
		TimedObserver found = null;
		for (TimedObserver t: wakeUpQueue) {
			if (t.obs == obs)
				found = t;
		}
		return (found != null) ? wakeUpQueue.remove(found) : false;
	}

	public void pause () {
		paused = true;
	}
	
	public void resume () {
		paused = false;
	}

	public boolean isPaused() {
		return paused;
	}

	public ScheduledIntention take () {
		while (true) {
			synchronized (this) {
				try {
					ScheduledIntention found = null;
					long older = 1000;
					for (ScheduledIntention si: intQueue) {
						if (si.isExpired() && si.whenWillExpire() < older) {
							found = si;
							older = si.whenWillExpire();
						}
					}
					if (found != null) {
						intQueue.remove(found);
						return found;
					}
					wait();
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
	@Override
	public void run() {
		if (paused)
			return;   /// ignore timer
		
		synchronized (this) {
			boolean somethingExpired = false;
			msecs_from_creation += PERIOD;
			for (ScheduledIntention si: intQueue) {
				si.tickExpired (PERIOD);
				somethingExpired |= si.isExpired();
			}
			if (somethingExpired)
				notifyAll ();
			ArrayList<TimedObserver> expired = new ArrayList<TimedObserver>();
			for (TimedObserver t: wakeUpQueue) {
				t.msecsToWait -= PERIOD;
				if (t.msecsToWait <= 0)
					expired.add(t);
			}
			for (TimedObserver t: expired) {
				wakeUpQueue.remove (t);
				t.obs.update(null, null);
			}
		}
	}


}
