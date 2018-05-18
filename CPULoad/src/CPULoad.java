import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jezhumble.javasysmon.CpuTimes;
import com.jezhumble.javasysmon.JavaSysMon;

public class CPULoad {
    /**
     * Starts the Load Generation
     * @param args Command line arguments, ignored
     */
    public static void main(String[] args) {
        float cpu_threshold = (float) 0.9;
        JavaSysMon monitor =   new JavaSysMon();
        CpuTimes cpuTimes1 = monitor.cpuTimes();
        int numCore = Runtime.getRuntime().availableProcessors();
        System.out.println( "cpu cores detected:"+numCore);
        ExecutorService pool = Executors.newFixedThreadPool(numCore);
        
        LoadTask task=new LoadTask(" ");;
        while (true){
 
	        try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			
				e.printStackTrace();
			}
	        CpuTimes cpuTimes2 = monitor.cpuTimes();
	        
	        float cpu_usage = cpuTimes2.getCpuUsage(cpuTimes1);
	        System.out.println( "CPU usage="+cpu_usage) ;
	        if (cpu_usage > cpu_threshold){
	        	//stop a task
	        	task.running=false;
	        }else if (cpu_usage < cpu_threshold){
	        	task=new LoadTask(" ");
	        	pool.execute(task);
	        }
	        cpuTimes1 = cpuTimes2;
        }
    }

    private static class LoadTask implements Runnable {
    	private String name;
    	private final Random rng;
    	public volatile boolean running=true;
    	
        public LoadTask(String name) {
            this.name=name;
            this.rng = new Random();
        }

        /**
         * Generates the load when run
         */
        @Override
        public void run() {
        	mathTask();
        }
        private void mathTask(){
        	while (running) {
				double r = this.rng.nextFloat();
				double v = Math.sin(Math.cos(Math.sin(Math.cos(r))));
				double t = r * v;	
			}
        }
    }
}