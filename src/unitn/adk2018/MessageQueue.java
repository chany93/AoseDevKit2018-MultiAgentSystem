package unitn.adk2018;

import java.util.LinkedList;
import java.util.Observable;

import unitn.adk2018.event.Message;

public class MessageQueue extends Observable {
	protected LinkedList<Message> pendingMsgs = new LinkedList<Message>();

	public void add ( Message msg ) {
		pendingMsgs.addLast(msg);
		setChanged();
		notifyObservers ( msg );
		synchronized (this) {
			notifyAll();
		}
	}
	
	public Message getIfAny () {
		clearChanged();
		return pendingMsgs.poll();
	}

	public Message waitAndGet () {
		Message m = null;
		while (m == null) {
			try {
				synchronized (this) {
					m = getIfAny();
					if (m == null)
						wait();
				}				
			} catch (Exception e) {
				System.err.println("Message.waitAndGet: unexpected " + e);
				e.printStackTrace();
			}
		}
		return m;
	}
	
	public int waitingMsgs () {
		return pendingMsgs.size();
	}
	
	public Message peekNth (int n) {
		return pendingMsgs.get(n);
	}

}
