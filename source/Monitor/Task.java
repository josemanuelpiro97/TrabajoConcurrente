package Monitor;//Test Class of Monitor.Task

import Monitor.rdp.InvariantException;

public class Task implements Runnable {

    private final Monitor monitor;
    private final int disparosTotales;
    private final int[] secuencia;

    public Task(int[] sec, Monitor monitor, int cant) {
        this.secuencia = sec;
        this.monitor = monitor;
        this.disparosTotales = cant;
    }

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
