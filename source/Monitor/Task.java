package Monitor;

import Monitor.rdp.InvariantException;

public class Task implements Runnable {
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                      VARIABLES
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//****************************************************
//              Private Variables
//****************************************************
    private final Monitor monitor;
    private final int disparosTotales;
    private final int[] secuencia;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                     CONSTRUCTORS
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @brief class constructor
     * @param sec sequence to shot
     * @param monitor monitor object
     * @param cant number of shots
     */
    public Task(int[] sec, Monitor monitor, int cant) {
        this.secuencia = sec;
        this.monitor = monitor;
        this.disparosTotales = cant;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                    PUBLIC METHODS
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//****************************************************
//                  Runnable method
//****************************************************

    /**
     * @// TODO: 2/7/20 revisar bien el tema de que el hilo duerma eso al salir, porque si bajamos los tiempos eso es mucho
     */
    @Override
    public void run() {
        int i = 0;
        int r = 0;
        while (i < this.disparosTotales || this.disparosTotales == 0) {
            try {
                //operate
                this.monitor.operate(this.secuencia[r]);
                if (this.secuencia.length > 1) {
                    r++;
                    if (r == this.secuencia.length) {
                        r = 0;
                    }
                }
                i++;
                Thread.sleep(100);
            } catch (Exception e) {
                if (e instanceof InvariantException) {
                    ((InvariantException) e).printInfo();
                } else {
                    e.printStackTrace();
                }
            }
        }
    }
}
