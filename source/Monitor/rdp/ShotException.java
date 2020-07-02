package Monitor.rdp;


public class ShotException extends Exception{
    /**
     * Number of transition that generate the error
     */
    private int trans;

    ShotException(int trans){
        super();
        this.trans = trans;
    }

    /**
     * report error info
     */
    public void printInfo(){
        System.out.print(String.format("Anda al archivo bro, transicion %4d no existe", this.trans));
    }

}
