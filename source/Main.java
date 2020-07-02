import Monitor.Monitor;
import Monitor.Task;
import java.io.FileNotFoundException;


//@todo se me ocurre hacer algo con la asignacion y starteado de las tareas, lo dejo pendiente para hablar con el breo
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                      MAIN CLASS
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Main {

    public static void main(String[] arg) throws FileNotFoundException {
        final int FIRST_TRANSITION = 0;
        final int LAST_TRANSITION = 16;

        final int NUMBER_OF_THREADS = 15;
        final int NUMBER_OF_TASKS_TYPE = 15;
        final int NUMBER_OF_TASKS = 10;
        final int INFINITE_TASK_NUMBER = 0;


        //****************************************************
        //build monitor
        Monitor monitor = new Monitor();
        //build tasks array
        Task[] tasks = new Task[NUMBER_OF_TASKS_TYPE];
        //build threads array
        Thread[] threads = new Thread[NUMBER_OF_THREADS];
        //****************************************************

        for (int i = FIRST_TRANSITION; i < 13; i++) {
            if (i == FIRST_TRANSITION) {
                tasks[i] = new Task(new int[]{i}, monitor, NUMBER_OF_TASKS);
                threads[i] = new Thread(tasks[i]);
                threads[i].start();
            } else if (i == 7) {
                //Procesadar T2P1 y FinalizarT2P1
                tasks[i] = new Task(new int[]{13, i}, monitor, INFINITE_TASK_NUMBER);
                threads[i] = new Thread(tasks[i], "DosTransiciones1");
                threads[i].start();
            } else if (i == 8) {
                //Procesar T2P2 y Finaliar T2P2
                tasks[i] = new Task(new int[]{14, i}, monitor, INFINITE_TASK_NUMBER);
                threads[i] = new Thread(tasks[i], "DosTransiciones2");
                threads[i].start();
            } else {
                tasks[i] = new Task(new int[]{i}, monitor, INFINITE_TASK_NUMBER);
                threads[i] = new Thread(tasks[i]);
                threads[i].start();
            }
        }
        //Vaciado 1
        tasks[13] = new Task(new int[]{15}, monitor, INFINITE_TASK_NUMBER);
        threads[13] = new Thread(tasks[14]);
        threads[13].start();
        //Vaciado 2
        tasks[14] = new Task(new int[]{16}, monitor, INFINITE_TASK_NUMBER);
        threads[14] = new Thread(tasks[14]);
        threads[14].start();


        //build and start, tasks and threads
        /*
        Task[] tasks = new Task[4];
        Thread[] threads = new Thread[4];
        for (int i =0 ; i< 4 ; i++){
            tasks[i] = new Task(new int[]{i},monitor,0);
            threads[i] = new Thread(tasks[i]);
            threads[i].start();
        }

        try{
            Thread.sleep(5000);
            monitor.closeLog();
            System.exit(0);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
         */
    }
}


