package Monitor.rdp;


public class ShotException extends Exception{
    /**
     * Numero de transicion que genero la interrupcion
     */
    private int trans;

    ShotException(int trans){
        super();
        this.trans = trans;
    }

    /**
     * Informa el error
     */
    public void printInfo(){
        System.out.print(String.format("Anda al archivo bro, transicion %4d no existe", this.trans));
    }

}
