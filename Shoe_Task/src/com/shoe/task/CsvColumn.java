package com.shoe.task;
import java.util.Date;

/**
 * 
 * @author Rajendra kumar tambabattula.
 * Java bean for csv columns.
 *
 */
public class CsvColumn {

	private Date timeStamp;
	private double elapsed;
	private String threadName;
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public double getElapsed() {
		return elapsed;
	}
	public void setElapsed(double elapsed) {
		this.elapsed = elapsed;
	}
	public String getThreadName() {
		return threadName;
	}
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	@Override
	public String toString() {
		return "CsvColumn [timeStamp=" + timeStamp + ", elapsed=" + elapsed + ", threadName=" + threadName + "]";
	}
	
	
	
}
