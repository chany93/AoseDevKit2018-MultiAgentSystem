package unitn.adk2018;

import java.util.Observable;
import java.util.Observer;

public class GenericObservable<T> extends Observable {
	
	private T t;
	
	public GenericObservable(T t) {
		this.t = t;
	}

	public T get() {
		return t;
	}
	
	public void set(T t) {
		this.t = t;
		setChanged();
		notifyObservers();
	}
	
	public void syncWith(GenericObservable<T> other) {
		set(other.get());
		other.addObserver( new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if(other.get()!=get())
					set((T) other.get());
			}
		});
	}
	
}
