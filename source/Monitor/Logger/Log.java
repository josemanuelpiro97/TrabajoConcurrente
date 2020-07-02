package Monitor.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Log {
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                      VARIABLES
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//****************************************************
//              Private Variables
//****************************************************
    /**
     * file to write
     */
    private File archivo;
    /**
     * object used to write a file
     */
    private FileWriter fw;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                     CONSTRUCTORS
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @brief class constructor
     */
    public Log() {
        this.archivo = new File("log.txt");
        try {
            this.fw = new FileWriter(archivo, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                    PUBLIC METHODS
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @brief write a specific message in specific format in the log file
     * @param msj specific message
     * @TODO la hora debe ser tomada al momento que queres escribir, no al momento de escribir
     */
    public synchronized void write(String msj) {
        String buffer;
        long time = java.lang.System.currentTimeMillis();
        buffer = String.format("%014d | %3d | %-2s |%s\n", time, Thread.currentThread().getId(), Thread.currentThread().getName(), msj);
        try {
            this.fw.write(buffer);
            System.out.print(buffer);
            this.fw.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * @brief write a specific message in the log file
     * @param msj specific message
     */
    public void write2(String msj) {
        String buffer2;
        buffer2 = String.format(msj + "\n");
        System.out.print(java.lang.System.currentTimeMillis()+ " --> " +buffer2);
        try{
            this.fw.write(buffer2);
            this.fw.flush();
        }
         catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief close this log file
     */
    public void close() {
        try {
            this.fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
