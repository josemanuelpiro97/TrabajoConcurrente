package Monitor.Queue;

import java.util.concurrent.Semaphore;

public class QueueManagment {

    /**
     * Array of semaphores that represent all transition.
     */
    private final Semaphore semaphores[];
    /**
     * Control array of semaphore array
     */
    private boolean sleepT[];
    /**
     * Control array for trans with time
     */
    private boolean autoWake[];

    /**
     * @param arraySize Numbre of transitions in the Petri net
     * @brief QueueManagment constructor
     */
    public QueueManagment(int arraySize) {
        this.semaphores = new Semaphore[arraySize];
        this.sleepT = new boolean[arraySize];
        this.autoWake = new boolean[arraySize];

        for (int i = 0; i < arraySize; i++) {
            this.semaphores[i] = new Semaphore(0);
            this.sleepT[i] = false;
            this.autoWake[i] = false;
        }
    }


    /**
     * @param index [in]
     * @brief put thread to sleep in its semaphore
     */
    public boolean sleepN(int index, long time, boolean transTime) {
        try {
            if (transTime) {
                this.autoWake[index] = true;
                Thread.sleep(time);
            } else {
                this.sleepT[index] = true;
                this.autoWake[index] = false;
                this.semaphores[index].acquire();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this.autoWake[index];
    }

    /**
     * @param index [in]
     * @brief Wake thread of specific semaphore
     */
    public void wakeN(int index) {
        try {
            this.sleepT[index] = false;
            this.semaphores[index].release();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param sensitizedT [in]
     * @return Array whit sensitive thread that are sleep
     * @brief Check which thread are sleep
     */
    public boolean[] isSleep(boolean[] sensitizedT) {
        boolean result[] = new boolean[sensitizedT.length];

        for (int i = 0; i < sensitizedT.length; i++) {
            result[i] = this.sleepT[i] && sensitizedT[i];
        }

        return result;
    }


    /*======================================================================================
                                       Getters
    ========================================================================================*/

    /**
     * @return All semaphores
     * @brief Semaphores getter
     */
    public Semaphore[] getSemaphores() {
        return semaphores;
    }

    /**
     * @param transitionN Transition number representing the tail of which we want the size
     * @return Queue length specified
     * @brief Getter a specific semaphore length
     */
    public int getQueueLength(int transitionN) {
        return this.semaphores[transitionN].getQueueLength();
    }

    /**
     * @return
     * @brief Getter sleeping transitions vector
     */
    public boolean[] whoSleepT() {
        return sleepT;
    }
}