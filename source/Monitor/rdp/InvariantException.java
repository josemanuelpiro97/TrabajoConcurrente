package Monitor.rdp;

/**
 * Excepcion que se genera al no cumplir con los invariantes de plaza (Ver si se agregan los de transiciones aca)
 */
public class InvariantException extends Exception {
    /**
     * Marca al momento de producirse la exepcion
     */
    private int[] mark;
    /**
     * Vector de invariantes de plaza, valor que se debe cumplir.
     */
    private int[] invariantes;
    /**
     * Vector de resultado en el chequeo de los invariantes.
     */
    private int[] resultado;

    /**
     * Generada con informacion de estado en el momento de generarse la excepcion
     *
     * @param mark marcado al momento de la excepcion
     */
    InvariantException(int[] mark, int[] invariantes, int[] res) {
        super();
        this.mark = mark;
        this.invariantes = invariantes;
        this.resultado = res;
    }

    /**
     * Imprime el estado de la red y el numero de transicion al momento de la excepcion
     */
    public void printInfo() {
        System.out.println("Se produjo una violacion de invarantes en el estado: ");
        for (int i : this.mark) {
            System.out.print(String.format("%4d", i));
        }
        System.out.println("\nEl numero de invariante que se debia cumplir era: ");
        for (int i : this.invariantes) {
            System.out.print(String.format("%4d", i));
        }
        System.out.println("\nY fue: ");
        for (int i : this.resultado) {
            System.out.print(String.format("%4d", i));
        }
    }
}
