package unitn.adk2018.intention;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public abstract class MyDelayed implements Delayed {
	
	private long whenToRunNext;
	
	
	
	public MyDelayed () throws Exception {
		delay (0);
		
		throw new Exception ( "No longer supported" );
	}
	
	
	
	public void delay (long msecs) {
		whenToRunNext = System.currentTimeMillis() + msecs;
	}
	
	@Override
	public long getDelay(TimeUnit unit) {
		long remaining = whenToRunNext - System.currentTimeMillis();
		if (remaining < 0)
			remaining = 0;
		return unit.convert ( remaining, unit );
	}

	@Override
	public int compareTo(Delayed o) {
		assert (o instanceof MyDelayed);
		MyDelayed other = (MyDelayed) o;
		return (int) (whenToRunNext - other.whenToRunNext ); /// assume 32 bits are enough as difference...
	}
	
}
