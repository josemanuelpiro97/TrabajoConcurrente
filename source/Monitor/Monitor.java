package Monitor;

import Monitor.Logger.Log;
import Monitor.Queue.QueueManagment;
import Monitor.politics.Policy;
import Monitor.rdp.InvariantException;
import Monitor.rdp.RDP;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.concurrent.Semaphore;

/**
 * NOTA: El hilo 1 al despertarse no le da el tiempo para disparar debido a que es el unico q esta activo, le faltan 1 a 2 milisegundos para disparar.
 * Si le agrego 100 milisegundos al tiempo q tiene q dormir funciona correctamente, creo q cuando se agregen mas hilos y mayor complejidad de la red esto se corrige.
 */
public class Monitor {
    /**
     * Petri Net to monitorize
     */
    private final RDP rdp;
    /**
     * Queues for the Petri net
     */
    private QueueManagment queueManagment;
    /**
     * Politics for taking decisions
     */
    private Policy policy;
    /**
     * Barrier of Monitor
     */
    private Semaphore mutex;
    /**
     * Log para llevar la informacion del programa
     */
    private Log log;
    /**
     * Flag control
     */
    private boolean controlFlag;


<<<<<<< HEAD
    public Monitor(Log log) throws FileNotFoundException {
        this.log = log;

        ///////////////////////////////////////////////////////////////////////
        String path = "Parameterizer.json";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        Gson gson = new Gson();
        this.rdp = gson.fromJson(bufferedReader,RDP.class);
        //set initial time for initial sensitized transitions
        this.rdp.setTimeSens();
        this.rdp.setLog(log);
        ////////////////////////////////////////////////////////////////////////

=======
    public Monitor() {
        this.log = new Log();
        this.rdp = new RDP("TP final Concurrente 2020", this.log);
>>>>>>> 76ee292354762539c01b138ba44e934670ca86de
        this.queueManagment = new QueueManagment(this.rdp.getNumTrans());
        this.policy = new Policy(this.rdp.getNumTrans());
        this.mutex = new Semaphore(1, true); //Semaforo de tipo FIFO
        this.controlFlag = true;
    }

    /**
     * @param vec boolean vector to be convert
     * @return int vector
     * @brief convert boolean vector to int vector
     */
    private int[] convertBtoI(boolean[] vec) {
        int[] res = new int[vec.length];
        for (int i = 0; i < vec.length; i++) {
            if (vec[i])
                res[i] = 1;
            else
                res[i] = 0;
        }
        return res;
    }

    /**
     * @param transN [in] transition to shot
     * @brief operate monitor tasks
     */
    public void operate(int transN) throws InvariantException, InterruptedException {
        this.mutex.acquire();
        boolean autoWakeUp;
        long timeSleep;

        do {

            int cant = 0;
            this.controlFlag = this.rdp.ShotT(transN);

            if (this.controlFlag) {
                //if shot is true
                boolean[] ask = this.rdp.getSensi4Mark();
                for (int i = 0; i < ask.length; i++) {
                    ask[i] = ask[i] && this.queueManagment.whoSleepT()[i];
                    if (ask[i]) {
                        cant++;
                    }
                }
                if (cant != 0) {

                    //pregunto a quien levantar
                    int wakeThread = this.policy.whoWake(this.convertBtoI(ask));

                    //log
                    String msj = "Se va a despertar el hilo:  " + wakeThread;
                    this.log.write2(msj);

                    //wake
                    this.queueManagment.wakeN(wakeThread);
                    return;
                } else {
                    this.controlFlag = false;
                }
            } else {

                timeSleep = this.rdp.getWaitTime(transN);
                if (timeSleep != -1) {
                    if (timeSleep == 0) {
                        //Se sensibilizo
                        this.controlFlag = true; //Lo seteo para q intente disparar de nuevo, no se libero el semaforo

                        continue;
                    } else {

                        this.mutex.release(); //Lo libero porq me voy a dormir por un tiempo

                        //log
                        String msj2 = "El hilo N: " + Thread.currentThread().getName() + " se jue a nimir: " + timeSleep
                                + " [mili]" + "\n";
                        this.log.write2(msj2);

                        autoWakeUp = this.queueManagment.sleepN(transN, timeSleep, true);
                    }
                } else {

                    this.mutex.release(); //Me voy a dormir a las colas normales
                    //log
                    String msj = "El hilo N: " + Thread.currentThread().getName() + " se jue a nimir cola comun" + "\n";
                    this.log.write2(msj);
                    autoWakeUp = this.queueManagment.sleepN(transN, 0, false);
                }

                if (autoWakeUp) {
                    this.mutex.acquire(); //Si se desperto solo vuelve a competir por el mutex
                }
                this.controlFlag = true; //Cuando se adquiere, se setea en true para intentar disparar

                //log
                String msj2 = "Se desperto el hilo " + Thread.currentThread().getName();
                this.log.write2(msj2);

            }

        } while (this.controlFlag);

        this.mutex.release();
    }

    /**
     * @brief Close log
     */
    public void closeLog() {
        this.log.close();
    }

}
