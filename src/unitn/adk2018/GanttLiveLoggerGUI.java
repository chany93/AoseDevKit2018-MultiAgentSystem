package unitn.adk2018;

import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import unitn.adk2018.GanttLogger.LoggedTask;

public class GanttLiveLoggerGUI extends ApplicationFrame implements Runnable {
	
	private static final long serialVersionUID = 1L;
	
	private final Map<String, TaskSeries> taskSeries = new HashMap<String, TaskSeries>();
    private final TaskSeriesCollection dataset = new TaskSeriesCollection();
	private final Map<String, Task> tasks = new HashMap<String, Task>();
	
	
	
	private TaskSeries getTaskSeries(String agent) {
		if ( !taskSeries.containsKey( agent ) ) {
			TaskSeries agentTaskSeries = new TaskSeries( agent );
			taskSeries.put( agent, agentTaskSeries );
			dataset.add( agentTaskSeries );
		}
		return taskSeries.get( agent );
	}
	
	
	
    public GanttLiveLoggerGUI(final String title) {
    	
        super(title);
        
        final JFreeChart chart = ChartFactory.createGanttChart(
        		title,  // chart title
                "Goals and Intentions",              // domain axis label
                "Time",              // range axis label
                dataset,             // data
                true,                // include legend
                true,                // tooltips
                false                // urls
            );
        
        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
        
        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
        
        Thread t = new Thread (this );
		t.start();
    }



	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (Environment.isPaused())
				continue; // keep waiting
			
			long now = Environment.getSimulationTime();

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                	
					/*
					 * Update all tasks
					 */
                	for ( Object _ganttTask : GanttLogger.get().getACopyOfTasks()) {

                		final LoggedTask ganttTask = (LoggedTask) _ganttTask;
                		final TaskSeries agentTaskSeries = getTaskSeries( ganttTask.agent.getName() );

                		//		        if ( tasks.containsKey( ganttTask.taskName ) ) {
                		//		        	Task t = tasks.get( ganttTask.taskName );
                		//		        	if ( t.getPercentComplete()!=null && t.getPercentComplete()==1 )
                		//		        		continue;
                		//		        }

                		String taskName = ganttTask.taskName;
                		long creationTime = ganttTask.creationTime;
                		long terminationTime = ganttTask.terminationTime;
                		if ( ganttTask.terminationTime == 0 )
                			terminationTime = now;
                		if ( (terminationTime - creationTime) < 100 )
                			terminationTime = creationTime + 100;

                		double previousPercentComplete = 0;
                		if (tasks.containsKey(taskName)) {
                			Task t = tasks.get(taskName);
                			previousPercentComplete = t.getPercentComplete();
                			agentTaskSeries.remove(t);
                			tasks.remove(taskName);
                		}

                		SimpleTimePeriod period = new SimpleTimePeriod (creationTime, terminationTime);
                		Task task = new Task(taskName, period);

                		if ( ganttTask.terminationTime != 0 )
                			if ( ganttTask.status.get().isHandledWithSuccess() )
                				task.setPercentComplete(1);
                			else
                				task.setPercentComplete(0);
                		else if ( previousPercentComplete < 0.8 )
                			task.setPercentComplete( previousPercentComplete + 0.2 );
                		else
                			task.setPercentComplete( 0 );

                		agentTaskSeries.add(task);
                		tasks.put(taskName, task);

                	}

                }
            });
				
	            
		}
	}

}
