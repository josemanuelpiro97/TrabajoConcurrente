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
        Monitor monitor = new Monitor();

        //build and start, tasks and threads
        Task[] tasks = new Task[15];
        Thread[] threads = new Thread[15];
        for (int i = 0; i < 13; i++) {
            if (i == 0) {
                tasks[i] = new Task(new int[]{i}, monitor, 100);
                threads[i] = new Thread(tasks[i]);
                threads[i].start();
            } else if (i == 7) {
                //Procesadar T2P1 y FinalizarT2P1
                tasks[i] = new Task(new int[]{13, i}, monitor, 0);
                threads[i] = new Thread(tasks[i], "DosTransiciones1");
                threads[i].start();
            } else if (i == 8) {
                //Procesar T2P2 y Finaliar T2P2
                tasks[i] = new Task(new int[]{14, i}, monitor, 0);
                threads[i] = new Thread(tasks[i], "DosTransiciones2");
                threads[i].start();
            } else {
                tasks[i] = new Task(new int[]{i}, monitor, 0);
                threads[i] = new Thread(tasks[i]);
                threads[i].start();
            }
        }
        //Vaciado 1
        tasks[13] = new Task(new int[]{15}, monitor, 0);
        threads[13] = new Thread(tasks[14]);
        threads[13].start();
        //Vaciado 2
        tasks[14] = new Task(new int[]{16}, monitor, 0);
        threads[14] = new Thread(tasks[14]);
        threads[14].start();
    }
}


