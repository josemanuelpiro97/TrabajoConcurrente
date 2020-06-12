package Monitor;

import Monitor.Queue.QueueManagment;
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

    public Monitor() {
        this.rdp = new RDP();
        this.queueManagment = new QueueManagment(this.rdp.getNumTrans());
      //  this.policy = new Policy();
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
<<<<<<< HEAD
     * @brief operate monitor tasks
     * @param transN [in] transition to shot
     */
    public void operate(int transN) {
=======
     * @param task task whit transition to shot
     * @brief operate monitor tasks
     */
    public void operate(Task task) throws InvariantException {
>>>>>>> 84589c63d13507285ff7feb290121044cac60c64
        boolean controlFlag = true;

        while (controlFlag) {
            controlFlag = this.rdp.ShotT(transN);

            if (controlFlag) {
                //check if there is some transition for wake
                boolean[] ask = new boolean[this.rdp.getNumTrans()];
                int cant = 0;
                for (int i = 0; i < this.rdp.getNumTrans(); i++) {
                    ask[i] = this.rdp.getSensiArray()[i] && this.queueManagment.whoSleepT()[i];
                    if (ask[i])
                        cant++;
                }

                // if there's no one to wake up I'm leaving
                if (cant == 0) {
                    controlFlag = false;
                }
                //otherwise, I ask who to wake up from all possibilities and give my place.
                else {
                    int wakeThis = this.policy.whoWake(ask);
                    this.queueManagment.wakeN(wakeThis);
                    break;
                }
            } else {
                //leave the monitor
                this.mutex.release();
                //go to sleep
                this.queueManagment.sleepN(transN);
            }
        }
    }
}
