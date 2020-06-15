package Monitor.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ThreadInfo;
import java.io.File;
import java.io.BufferedWriter;


public class Log {

    private File archivo;
    private String name;
    private String path;
    private FileWriter fw;

    public Log() {
        this.archivo = new File("log.txt");
        try {
            this.fw = new FileWriter(archivo, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(int trans, String msj) {
        String buffer;
        long time = java.lang.System.currentTimeMillis();
        buffer = String.format("%014d | %-2s | %d |%s \n",time, Thread.currentThread().getName(), trans, msj);
        try{
            this.fw.write(buffer);
            this.fw.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void write2(String msj) {
        String buffer2;
        buffer2 = String.format(msj + "\n");
        try{
            this.fw.write(buffer2);
            this.fw.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
