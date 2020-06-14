import Monitor.Monitor;
import Monitor.Task;


/**
 * @TODO Solo implementar la instancia de monitor.
 * @TODO implementar la clase Log para llevar el registro del programa
 **/

public class Main {
    public static void main (String[] arg){
        //build monitor
        Monitor monitor = new Monitor();

        //build and start, tasks and threads
        Task[] tasks = new Task[4];
        Thread[] threads = new Thread[4];
        for (int i =0 ; i< 4 ; i++){
            tasks[i] = new Task(i,monitor);
            threads[i] = new Thread(tasks[i]);
            threads[i].start();
        }
    }
}



