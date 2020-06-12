package Monitor.rdp;

/**
 * @TODO hardcodear la red basica para para poder debaggear mas facil.
 * @TODO -Metodos a crear: -Disparar
 * -QuienSeSensibilizo(return vector)
 * -QuienQuiereDisparar
 * -Constructor
 * -ProximoEstado
 * -(faltan mas seguramente)
 * @TODO Implementar Test -> Seria util para dejar todo andando mientras se va avanzando. (Junit)
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

    public RDP() {
        info = "RdP de Test, sin tiempo";

        /* Matriz de incidencia de la red para test */
        this.matrixI = new int[][]{{-1, 0, 0, 1}, {1, -1, 0, 0}, {0, 1, 0, -1}, {1, 0, -1, 0}, {0, 0, 1, -1}};

        /* Vector de marcado incial, indica 4 tokens iniciales en la plaza p0 */
        this.MarkInit = new int[]{4, 0, 0, 0, 0};
    }

    /**
     * @param trans numero de transicion que se quiere disparar.
     * @return true para un disparo exitoso, false si fallo en el disparo.
     * @brief: Metodo encargado de intentar disparar una transicion, hara los chequeos correspondientes y se encargara
     * de modificar el estado de la red. Si es posible el disparo, retorna un "true", caso contrario,
     * devolvera un "false".
     */
    public boolean ShotT(int trans) {

        /* Verifico que la transicion exista */
        if (trans < 0 || trans > this.matrixI[0].length) {
            //Ver si tirar una exepcion (q se debera crear) o hacer otra cosa
            return false;
        }

        int[] nextState = nextMark(trans);

        if (!validShot(nextState)) {
            /* tiro no valido */
            return false;
        } else {
            /* Actualizo la marca */
            this.MarkInit = nextState;
            return true;
        }
    }

    /**
     * @param trans numero de transicion a disparar.
     * @return vector de tipo entero con la nueva marca
     * @brief: Metodo encargado de calcular la proxima marca que se obtendra al disparar la transicion deseada.
     */
    private int[] nextMark(int trans) {

        /* Creo el vector de disparo */
        int[] VecShot = new int[this.matrixI[0].length];
        VecShot[trans] = 1;

        /* Multiplico el vector de disparo por la matriz de incidencia de la red */
        int[] nextMark = this.multMatrix2Vector(this.matrixI, VecShot);

        /* Sumamos el vector de marca actual */
        for (int i = 0; i < this.matrixI.length; i++) {
            nextMark[i] += this.MarkInit[i];
        }

        return nextMark;
    }

    /**
     * @param mark vector de proximo marcado
     * @return True en caso de que sea valido, false, si hay un elemento negativo.
     * @brief Metodo encargado de validar el proximo marcado.
     */
    private boolean validShot(int[] mark) {

        for (int i = 0; i < mark.length; i++) {
            if (mark[i] < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param matrix matriz de enteros
     * @param vector vector de enteros
     * @return vector de enteros resultante de la multiplicacion.
     * @brief: Metodo encargado de multiplicar una matriz con el vector, ambos pasado como parametro.
     */
    private int[] multMatrix2Vector(int[][] matrix, int[] vector) {

        int[] res = new int[matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                res[i] += matrix[i][j] * vector[j];
            }
        }
        return res;
    }

    /**
     * @return entero con el numero de transiciones
     * @brief Metodo encargado de retornar el numero de transiciones que contiene la red de petri.
     */
    public int getNumTrans() {
        return this.matrixI[0].length;
    }

    /**
     * @return vector de tipo booleano.
     * @brief Metodo encargado de devolver el vector de sensibilizado. En cada posicion del arreglo habra un True o
     * un False indicando si se encuentra sensibilizada la transicion o no.
     */
    public boolean[] getSensiArray() {
        boolean[] isSensi = new boolean[this.matrixI[0].length];
        for (int i = 0; i < isSensi.length; i++) {
            /* Simulo un disparo de la transicion con la marca actual para ver si el tiro es valido, de serlo, se seteara con true */
            isSensi[i] = this.validShot(this.nextMark(i));
        }
        return isSensi;
    }


    /*=================================================================================
                         Metodos para la optencion de informacion de la red
     ==================================================================================*/

    /**
     * @return Devuelve una copia de la matriz de incidencia
     * @brief Metodo creado solo para los tests
     */
    int[][] getMatrixI() {
        return this.matrixI.clone();
    }

    /**
     * @return Devuelve una copia del vector de marcado
     * @brief Devuelve el vector de marcado.
     */
    int[] getMarkInit() {
        return this.MarkInit.clone();
    }


}

