package Monitor.Queue;

import Monitor.Logger.Log;

import java.util.concurrent.Semaphore;

public class QueueManagment {
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                      VARIABLES
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//****************************************************
//              Private Variables
//****************************************************
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
     * Logger
     */
    private Log log;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                     CONSTRUCTORS
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @param arraySize Numbre of transitions in the Petri net
     * @brief QueueManagment constructor
     */
    public QueueManagment(int arraySize, Log l) {
        this.log = l;
        this.semaphores = new Semaphore[arraySize];
        this.sleepT = new boolean[arraySize];
        this.autoWake = new boolean[arraySize];

        for (int i = 0; i < arraySize; i++) {
            this.semaphores[i] = new Semaphore(0);
            this.sleepT[i] = false;
            this.autoWake[i] = false;
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                    PUBLIC METHODS
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @param index [in]
     * @brief put thread to sleep in its semaphore
     */
    public boolean sleepN(int index, long time, boolean transTime) {
        try {
            if (transTime) {
                this.log.write(String.format(" AutoSleep | t:%d |", time),java.lang.System.currentTimeMillis());
                this.autoWake[index] = true;
                Thread.sleep(time);
                this.log.write(String.format(" AutoWakeUp | %d |", index),java.lang.System.currentTimeMillis());
            } else {
                this.log.write(String.format(" AddQueue | c:%d |", index),java.lang.System.currentTimeMillis());
                this.sleepT[index] = true;
                this.autoWake[index] = false;
                this.semaphores[index].acquire();
                this.log.write(String.format(" WakeUp | c:%d |", index),java.lang.System.currentTimeMillis());
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
    public boolean[] whoAreSleep(boolean[] sensitizedT) {
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
    public boolean[] whoSleepVector() {
        return sleepT;
    }
}