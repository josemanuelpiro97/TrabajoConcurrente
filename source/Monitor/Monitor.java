package Monitor;

import Monitor.Logger.Log;
import Monitor.Queue.QueueManagment;
import Monitor.politics.Policy;
import Monitor.rdp.InvariantException;
import Monitor.rdp.RDP;

import java.util.concurrent.Semaphore;

/**
 * @TODO Hacer fucking todo bitch
 * @TODO Agregar documentacion de cada cosa q se agregue
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

    /**
     * count control
     */
    private int count = 0;

    public Monitor(Log log) {
        this.log = log;
        //this.rdp = new RDP(this.log);
        this.rdp = new RDP("Red con tiempo", this.log);
        this.queueManagment = new QueueManagment(this.rdp.getNumTrans());
        this.policy = new Policy(this.rdp.getNumTrans());
        this.mutex = new Semaphore(1);
        this.controlFlag = true;
    }

    /**
     * @brief take the Monitor
     */
    public void takeMonitor(int transN) {
        try {
            this.mutex.acquire();

            //log
            String msj = "El hilo N: " + Thread.currentThread().getName() + " ingresa al monitor";
            this.log.write2(msj);

            this.operate(transN);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    private void operate(int transN) throws InvariantException, InterruptedException {
        boolean autoWakeUp;
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

                //log
                String msj = "El hilo N: " + Thread.currentThread().getName() + " se jue a nimir" + "\n";
                this.log.write2(msj);

                this.mutex.release();
                long timeSleep = this.rdp.getWaitTime(transN);
                if (timeSleep != -1) {
                    //log
                    String msj2 = "El hilo N: " + Thread.currentThread().getName() + " se jue a nimir: " + timeSleep
                            +" [mili]" + "\n";
                    this.log.write2(msj2);

                    autoWakeUp = this.queueManagment.sleepN(transN, timeSleep, true);
                } else {
                    autoWakeUp = this.queueManagment.sleepN(transN, 0, false);
                }

                if (autoWakeUp){
                    this.mutex.acquire(); //Si se desperto solo vuelve a competir por el mutex
                    //this.controlFlag = true;
                }

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
