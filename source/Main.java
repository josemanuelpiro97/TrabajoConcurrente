import Monitor.Monitor;
import Monitor.Task;
import java.io.FileNotFoundException;


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                      MAIN CLASS
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Main {

    public static void main(String[] arg) throws FileNotFoundException {
        final int LAST_TRANSITION = 15;

        final int NUMBER_OF_THREADS = 15;
        final int NUMBER_OF_TASKS_TYPE = 15;
        final int NUMBER_OF_TASKS = 1000;
        final int INFINITE_TASK_NUMBER = 0;

        //------------------------------------------------
        //specials transitions
        final int FIRST_TRANSITION = 0;

        final int TRANS_DOUBLE_1_1 = 7;
        final int TRANS_DOUBLE_1_2 = 13;

        final int TRANS_DOUBLE_2_1 = 8;
        final int TRANS_DOUBLE_2_2 = 14;

        final int VACIADO_1 = 15;
        final int VACIADO_2 = 16;

        final int[] REGULAR_TRANSITIONS = {1, 2, 3, 4, 5, 6, 9, 10, 11, 12};

        //****************************************************
        //build monitor
        Monitor monitor = new Monitor();
        //build tasks array
        Task[] tasks = new Task[NUMBER_OF_TASKS_TYPE];
        //build threads array
        Thread[] threads = new Thread[NUMBER_OF_THREADS];
        //****************************************************

        //assign task and started
        tasks[FIRST_TRANSITION] = new Task(new int[]{FIRST_TRANSITION}, monitor, NUMBER_OF_TASKS);
        threads[FIRST_TRANSITION] = new Thread(tasks[FIRST_TRANSITION],"Actival-R");
        threads[FIRST_TRANSITION].start();

        //----------------------------------------------------------
        //Procesadar T2P1 y FinalizarT2P1
        tasks[1] = new Task(new int[]{TRANS_DOUBLE_1_2, TRANS_DOUBLE_1_1}, monitor, INFINITE_TASK_NUMBER);
        threads[1] = new Thread(tasks[1], "Proc-fin-21");
        threads[1].start();

        //----------------------------------------------------------
        //Procesar T2P2 y Finaliar T2P2
        tasks[2] = new Task(new int[]{TRANS_DOUBLE_2_2, TRANS_DOUBLE_2_1}, monitor, INFINITE_TASK_NUMBER);
        threads[2] = new Thread(tasks[2], "Proc-fin-22");
        threads[2].start();

        //----------------------------------------------------------
        //Vaciado 1
        tasks[3] = new Task(new int[]{VACIADO_1}, monitor, INFINITE_TASK_NUMBER);
        threads[3] = new Thread(tasks[3],"Vaciado-1");
        threads[3].start();
        //----------------------------------------------------------
        //Vaciado 2
        tasks[4] = new Task(new int[]{VACIADO_2}, monitor, INFINITE_TASK_NUMBER);
        threads[4] = new Thread(tasks[4],"Vaciado-2");
        threads[4].start();

        for(int i = 5 ; i < LAST_TRANSITION ; i++){
            tasks[i] = new Task(new int[]{REGULAR_TRANSITIONS[i-5]}, monitor, INFINITE_TASK_NUMBER);
            threads[i] = new Thread(tasks[i],"Regular-" + (REGULAR_TRANSITIONS[i-5]));
            threads[i].start();
        }

        try{
            Thread.sleep(15000);
            monitor.closeLog();
            System.exit(0);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}


