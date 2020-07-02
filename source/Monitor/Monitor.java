package Monitor;

import Monitor.Logger.Log;
import Monitor.Queue.QueueManagment;
import Monitor.politics.Policy;
import Monitor.rdp.InvariantException;
import Monitor.rdp.RDP;
import Monitor.rdp.ShotException;
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
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                      VARIABLES
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//****************************************************
//              Private Variables
//****************************************************
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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                     CONSTRUCTORS
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @brief class consructor
     * @throws FileNotFoundException exeption produced by Gson constructor
     */
    public Monitor() throws FileNotFoundException {
        this.log = new Log();

<<<<<<< HEAD
        //-------------------------------------------------------------------------
        //Gson constructor
        String path = "Parameterizer.json";
=======
    public Monitor() throws FileNotFoundException {
        this.log = new Log();

        ///////////////////////////////////////////////////////////////////////
        //String path = "PetriTest.json";
        String path = "TpFinal.json";
>>>>>>> fa8737384d54f12078ab67189dece685aed730a3
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        Gson gson = new Gson();
        this.rdp = gson.fromJson(bufferedReader, RDP.class);

        //set initial time for initial sensitized transitions
        this.rdp.setTimeSens();
        this.rdp.setLog(log);
        //-------------------------------------------------------------------------

<<<<<<< HEAD
        this.queueManagment = new QueueManagment(this.rdp.getNumTrans());
=======
        this.queueManagment = new QueueManagment(this.rdp.getNumTrans(),log);
>>>>>>> fa8737384d54f12078ab67189dece685aed730a3
        this.policy = new Policy(this.rdp.getNumTrans());
        this.mutex = new Semaphore(1, true); //Semaforo de tipo FIFO
        this.controlFlag = true;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                    PUBLIC METHODS
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @param transN [in] transition to shot
     * @brief operate monitor tasks
     * @// TODO: 2/7/20 revisar el log sobre quien se va a despertar, porque ahora no es lo mismo el hilo q la trans que dspara
     * @// TODO: 2/7/20 en la parte del tiempo que debe dormir si es una transicion de tiempo, no seria mas prolijo pregutar si es trantime?
     */
    public void operate(int transN) throws InvariantException, InterruptedException, ShotException{
        this.mutex.acquire();
        boolean autoWakeUp;
        long timeSleep;

        do {
            //try shoot
            int cant = 0;
            this.controlFlag = this.rdp.ShotT(transN);

            //if shot attempt is true
            if (this.controlFlag) {
                //check for mark sensi
                boolean[] ask = this.rdp.getSensi4Mark();
                //check for marck and queue sensi
                ask =this.queueManagment.whoAreSleep(ask);
                //check if same value is true
                for (int i = 0; i < ask.length; i++) {
                    if (ask[i]) {
                        cant++;
                    }
                }

                if (cant != 0) {

                    //ask for who wake
                    int wakeThread = this.policy.whoWake(this.convertBtoI(ask));

<<<<<<< HEAD
                    //log
                    String msj = "Se va a despertar el hilo que dispara:  " + wakeThread;
                    this.log.write2(msj);

=======
>>>>>>> fa8737384d54f12078ab67189dece685aed730a3
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

                        autoWakeUp = this.queueManagment.sleepN(transN, timeSleep, true);
                    }
                } else {

                    this.mutex.release(); //Me voy a dormir a las colas normales

                    autoWakeUp = this.queueManagment.sleepN(transN, 0, false);
                }

                if (autoWakeUp) {
                    this.mutex.acquire(); //Si se desperto solo vuelve a competir por el mutex
                }
                this.controlFlag = true; //Cuando se adquiere, se setea en true para intentar disparar


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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                   PRIVATE METHODS
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//****************************************************
//                   Tools
//****************************************************
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



}
