package Monitor.rdp;

/**
 * places invariant exception
 */
public class InvariantException extends Exception {
    /**
     * mark in the moment that throw exception
     */
    private int[] mark;
    /**
     * places invariant correct
     */
    private int[] invariantes;
    /**
     * result vector in the invariant check
     */
    private int[] resultado;

    /**
     * @param mark mark in the moment that throw exception
     */
    InvariantException(int[] mark, int[] invariantes, int[] res) {
        super();
        this.mark = mark;
        this.invariantes = invariantes;
        this.resultado = res;
    }

    /**
     * print the Petri net state
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
