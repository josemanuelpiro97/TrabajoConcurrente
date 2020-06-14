package Monitor;//Test Class of Monitor.Task

public class Task implements Runnable {
    private int transitionN;
    private Monitor monitor;

    public Task( int transitionN, Monitor monitor) {
        this.transitionN = transitionN;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        //test variable
        final int FINAL = 10;

        for (int i = 0; i < FINAL; i++) {
            this.monitor.takeMonitor();
            try {
                System.out.println("Entra el Hilo: " + Thread.currentThread().getName() +
                        " con la transicion N: " + this.transitionN);
                this.monitor.operate(this.transitionN);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
