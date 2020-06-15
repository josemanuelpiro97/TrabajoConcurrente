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

    public Monitor(Log log) {
        this.log = log;
        //this.rdp = new RDP(this.log);
        this.rdp = new RDP("Red con tiempo", this.log);
        this.queueManagment = new QueueManagment(this.rdp.getNumTrans());
        this.policy = new Policy(this.rdp.getNumTrans());
        this.mutex = new Semaphore(1);
    }

    /**
     * @brief take the Monitor
     */
    public void takeMonitor() {
        try {
            this.mutex.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief convert boolean vector to int vector
     * @param vec boolean vector to be convert
     * @return int vector
     */
    private int[] convertBtoI(boolean[] vec){
        int [] res = new int[vec.length];
        for (int i =0 ; i< vec.length ; i++){
            if(vec[i])
                res[i] = 1;
            else
                res[i] = 0;
        }
        return res;
    }

    /**
     * @brief operate monitor tasks
     * @param transN [in] transition to shot
     */
    public void operate(int transN)throws InvariantException {

        boolean controlFlag = true;

        while (controlFlag) {
            controlFlag = this.rdp.ShotT(transN);

            if (controlFlag) {
                //check if there is some transition for wake
                boolean[] ask = new boolean[this.rdp.getNumTrans()];
                int cant = 0;
                ask = this.rdp.getSensiArray();
                for (int i = 0; i < this.rdp.getNumTrans(); i++) {
                    ask[i] = ask[i] && this.queueManagment.whoSleepT()[i];
                    if (ask[i])
                        cant++;
                }

                // if there's no one to wake up I'm leaving
                if (cant == 0) {
                    controlFlag = false;
                }
                //otherwise, I ask who to wake up from all possibilities and give my place.
                else {
                    int wakeThis = this.policy.whoWake(this.convertBtoI(ask));

                    //log
                    String msj = "Se va a despertar el hilo:  " + wakeThis;
                    this.log.write2(msj);

                    //wake thread
                    this.queueManagment.wakeN(wakeThis);
                    break;
                }
            } else {
                //log
                String msj = "El hilo N: " + Thread.currentThread().getName() + " se jue a nimir" + "\n";
                this.log.write2(msj);

                //leave the monitor
                this.mutex.release();

                //go to sleep
                this.queueManagment.sleepN(transN);

                //log
                String msj2 = "Se desperto el hilo " + Thread.currentThread().getName();
                this.log.write2(msj2);
            }
        }
        if(!controlFlag)
            this.mutex.release();
    }

    /**
     * @brief Close log
     */
    public void closeLog(){
        this.log.close();
    }




}
