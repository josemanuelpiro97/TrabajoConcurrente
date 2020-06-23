import Monitor.Logger.Log;
import Monitor.Monitor;
import Monitor.Task;


/**
 * @TODO Solo implementar la instancia de monitor.
 * @TODO Sacar la creacion del log desde Main y q lo cree el Monitor
 **/

public class Main {
    public static void main(String[] arg) {
        //build monitor
        Log log = new Log();
        Monitor monitor = new Monitor(log);


        //build and start, tasks and threads
        Task[] tasks = new Task[17];
        Thread[] threads = new Thread[17];
        for (int i = 0; i < 17; i++) {
            tasks[i] = new Task(i, monitor, log);
            threads[i] = new Thread(tasks[i]);
            threads[i].start();
        }

        //   try {
        // Thread.sleep(10000);
        //  monitor.closeLog();
        //System.exit(0);
        // }catch (InterruptedException e){
        //    e.printStackTrace();
    }
}



