package unitn.adk2018;

import java.awt.*;
import java.awt.event.*;

public class SimulationGui {

   private Frame mainFrame;
   
   
   
   public SimulationGui(){
	   mainFrame = new Frame("Simulation GUI");
	   mainFrame.setSize(500,100);
	   mainFrame.setLayout(new FlowLayout());
	   mainFrame.addWindowListener(new WindowAdapter() {
		   public void windowClosing(WindowEvent windowEvent){
//			   Environment.getEnvironment().pauseSimulationTime();
			   System.exit(0);
		   }
	   });
	   
	   Panel p = createTimerPanel();
	   mainFrame.add(p);
	   
	   mainFrame.setVisible(true);
   }
   
   public static String GetFormattedInterval(final long ms) {
	    long millis = ms % 1000;
	    long x = ms / 1000;
	    long seconds = x % 60;
	    x /= 60;
	    long minutes = x % 60;
	    x /= 60;
	    long hours = x % 24;
	
	    return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
	}
   
   public Panel createTimerPanel() {
	   Panel controlPanel = new Panel();
	   controlPanel.setLayout(new FlowLayout());
	   
	   //DurationFormatUtils.formatDuration(value, "HH:mm:ss.S")
	   
//	   Label timer = new Label();
//	   timer.setText("Timer: "+GetFormattedInterval(warehouse.getSimulationTime().get()));
//	   Environment.getEnvironment().getSimulationTime().getSimulationTime().registerListener(new IListener<Long>() {
//    	  public void notifyChanged(Long value) {
//    		  
//    		  timer.setText("Timer: "+GetFormattedInterval(value));
//    	  }
//	   });
	   
//	   Label state = new Label();
//	   state.setText(warehouse.getSimulationState().get().toString());
//	   warehouse.getSimulationState().registerListener(new IListener<SimulationState>() {
//		   public void notifyChanged(SimulationState value) {
//			   state.setText(value.toString());
//		   }
//	   });

	   Button play = new Button("play");
	   play.addActionListener(new ActionListener() {
		   public void actionPerformed(ActionEvent e) {
			   Environment.resumeSimulationTime();
		   }
	   });
	   
	   Button pause = new Button("pause");
	   pause.addActionListener(new ActionListener() {
		   public void actionPerformed(ActionEvent e) {
			   Environment.pauseSimulationTime();
		   }
	   });
	   
	   Button printFullState = new Button("printFullState");
	   printFullState.addActionListener(new ActionListener() {
		   public void actionPerformed(ActionEvent e) {
			   for (Agent a : Environment.getAgents().values()) {
				   a.printFullState();
			   }
		   }
	   });
	   
	   
	   
//	   controlPanel.add(timer);
//	   controlPanel.add(state);
	   controlPanel.add(play);
	   controlPanel.add(pause);
	   controlPanel.add(printFullState);
	   
	   mainFrame.setVisible(true);
      
	   return controlPanel;
	}
}