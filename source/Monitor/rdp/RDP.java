package Monitor.rdp;

/**
 * @TODO hardcodear la red basica para para poder debaggear mas facil.
 * @TODO -Metodos a crear: -Disparar
 *                         -QuienSeSensibilizo(return vector)
 *                         -QuienQuiereDisparar
 *                         -Constructor
 *                         -ProximoEstado
 *                         -(faltan mas seguramente)
 * @TODO Implementar Test -> Seria util para dejar todo andando mientras se va avanzando. (Junit)
 *
 */
public class RDP {
    /**
     * Descripcion basica de la red
     */
    String info;
    /**
     * Matriz de incidencia de la RdP
     */
    int[][] matrixI;
    /**
     * Vector de marca inicial
     */
    int[] MarkInit;
    /**
     * Variables para tener registro de los tiempos (Proximamente)
     */

    public RDP(){
        info = "RdP de Test, sin tiempo";

        /* Matriz de incidencia de la red para test */
        this.matrixI = new int[][]{{-1, 0, 0, 1}, {1, -1, 0, 0}, {0, 1, 0, -1}, {1, 0, -1, 0}, {0, 0, 1, -1}};

        /* Vector de marcado incial, indica 4 tokens iniciales en la plaza p0 */
        this.MarkInit = new int[]{4,0,0,0,0};
    }

    /**
     * @brief: Metodo encargado de intentar disparar una transicion, hara los chequeos correspondientes y se encargara
     *         de modificar el estado de la red. Si es posible el disparo, retorna un "true", caso contrario,
     *         devolvera un "false".
     * @param trans numero de transicion que se quiere disparar.
     * @return true para un disparo exitoso, false si fallo en el disparo.
     */
    public boolean dispararT(int trans){

        /* Verifico que la transicion exista */
        if(trans < 0 || trans > this.matrixI[0].length){
            //Ver si tirar una exepcion (q se debera crear) o hacer otra cosa
            return false;
        }

        boolean exito = true;
        int[] nextState;


        return true;
    }
}
