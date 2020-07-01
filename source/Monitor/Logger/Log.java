package Monitor.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ThreadInfo;
import java.io.File;
import java.io.BufferedWriter;


public class Log {

    private File archivo;
    private FileWriter fw;

    public Log() {
        this.archivo = new File("log.txt");
        try {
            this.fw = new FileWriter(archivo, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void write(String msj) {
        String buffer;
        long time = java.lang.System.currentTimeMillis();
        buffer = String.format("%014d | %3d | %-2s |%s\n", time, Thread.currentThread().getId(), Thread.currentThread().getName(), msj);
        try {
            this.fw.write(buffer);
            System.out.print(buffer);
            this.fw.flush();
        } catch (IOException e) {
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
