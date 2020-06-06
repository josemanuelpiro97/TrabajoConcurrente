package Monitor.Queue;

import java.util.concurrent.Semaphore;

public class QueueManagment {

    /**
     * Array of semaphores that represent all transition.
     */
    private Semaphore semaphores[];
    private boolean sleepT[];

    /**
     * @param arraySize Numbre of transitions in the Petri net
     * @brief QueueManagment constructor
     */
    public QueueManagment(int arraySize) {
        this.semaphores = new Semaphore[arraySize];
        this.sleepT = new boolean[arraySize];

        for (int i = 0; i < arraySize; i++) {
            this.semaphores[i] = new Semaphore(0);
            this.sleepT[i] = false;
        }
    }


    /**
     * @param index [in]
     * @brief put thread to sleep in its semaphore
     */
    public void sleepN(int index) {
        for (int i = 0; i < this.semaphores.length; i++) {
            if (i == index)
                try {
                    this.sleepT[i] = true;
                    this.semaphores[i].acquire();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * @param index [in]
     * @brief Wake thread of specific semaphore
     */
    public void wakeN(int index) {
        for (int i = 0; i < this.semaphores.length; i++) {
            if (i == index)
                try {
                    this.sleepT[i] = false;
                    this.semaphores[i].release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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


}