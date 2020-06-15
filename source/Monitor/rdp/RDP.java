package Monitor.rdp;

import Monitor.Logger.Log;

/**
 * @TODO ver si se hardcodea la red o se arma desde un archivo.
 * @TODO agregar que en cada disparo se imprima y guarde en el archivo la transicion disparada
 * @TODO Implementar el tiempo (Lo primero)
 */
public class RDP {
    /**
     * Descripcion basica de la red
     */
    private final String info;
    /**
     * Matriz de incidencia de la RdP
     */
    private final int[][] matrixI;
    /**
     * Vector de marca inicial
     */
    private int[] Mark;
    /**
     * Matriz para de invariantes de plaza
     */
    private final int[][] MatrixInvPlace;
    /**
     * Vector utilizado para el checkeo de invariantes de plaza
     */
    private final int[] VecInvPlaces;
    /**
     * Matriz donde se almacenan los tiempos Alfa y Beta
     */
    private final int[][] MatrixTime;
    /**
     * Vector de stamptime para el sensibilizado de transiciones
     */
    private long[] vectorTime; //stamptime
    /**
     * Objeto log utilizado para almacenar el disparo de las trasiciones en un txt
     */
    private Log log;

    /**
     * @brief Constructor solo utilizado para test, sin tiempo ni logger
     */
    public RDP() {
        info = "RdP de Test, sin tiempo";

        /* Matriz de incidencia de la red para test */
        this.matrixI = new int[][]{
                {-1, 0, 0, 1},
                {1, -1, 0, 0},
                {0, 1, 0, -1},
                {1, 0, -1, 0},
                {0, 0, 1, -1}};

        /* Vector de marcado incial, indica 4 tokens iniciales en la plaza p0 */
        this.Mark = new int[]{4, 0, 0, 0, 0};

        /* Matriz de P invariantes */
        this.MatrixInvPlace = new int[][]{
                {1, 1, 1, 0, 0},
                {1, 0, 0, 1, 1}
        };

        /* Numero de invariante de plaza */
        this.VecInvPlaces = new int[]{4, 4};

        this.MatrixTime = null;
    }

    public RDP(Log l) {
        info = "RdP de Test, sin tiempo";

        /* Matriz de incidencia de la red para test */
        this.matrixI = new int[][]{
                {-1, 0, 0, 1},
                {1, -1, 0, 0},
                {0, 1, 0, -1},
                {1, 0, -1, 0},
                {0, 0, 1, -1}};

        /* Vector de marcado incial, indica 4 tokens iniciales en la plaza p0 */
        this.Mark = new int[]{4, 0, 0, 0, 0};

        /* Matriz de P invariantes */
        this.MatrixInvPlace = new int[][]{
                {1, 1, 1, 0, 0},
                {1, 0, 0, 1, 1}
        };

        /* Numero de invariante de plaza */
        this.VecInvPlaces = new int[]{4, 4};

        this.MatrixTime = null;

        this.log = l;

    }

    public RDP(String info, Log l) {
        this.info = info;

        /* Matriz de incidencia de la red para test */
        this.matrixI = new int[][]{
                {-1, 0, 0, 1},
                {1, -1, 0, 0},
                {0, 1, 0, -1},
                {1, 0, -1, 0},
                {0, 0, 1, -1}};

        /* Vector de marcado incial, indica 4 tokens iniciales en la plaza p0 */
        this.Mark = new int[]{4, 0, 0, 0, 0};

        /* Matriz de P invariantes */
        this.MatrixInvPlace = new int[][]{
                {1, 1, 1, 0, 0},
                {1, 0, 0, 1, 1}
        };

        /* Numero de invariante de plaza */
        this.VecInvPlaces = new int[]{4, 4};

        /* Ventana de tiempo de las trasiciones */
        this.MatrixTime = new int[][]{
                {0, 2000, 0, 0},
                {0, 6000, 0, 0}
        };

        /* Vector donde se almacenan los timestamp */
        this.vectorTime = new long[]{-1,-1,-1,-1};

        this.log = l;

    }

    /**
     * @param trans numero de transicion que se quiere disparar.
     * @return true para un disparo exitoso, false si fallo en el disparo.
     * @brief Metodo encargado de intentar disparar una transicion, hara los chequeos correspondientes y se encargara
     * de modificar el estado de la red. Si es posible el disparo, retorna un "true", caso contrario,
     * devolvera un "false".
     * @TODO Posible fallo con el tiempo al actualizar los timestamp en vectorTime
     */
    public boolean ShotT(int trans) throws InvariantException {

        /* Almaceno el tiempo para las transciones temporizadas */
        long timestamp = java.lang.System.currentTimeMillis();

        /* Verifico que la transicion exista */
        if (trans < 0 || trans > this.matrixI[0].length) {
            return false;
        }

        if (this.isTransTime(trans)) {
            if (this.getSensi4temp(timestamp, trans)) {

            } else {
                this.log.write(trans, "No habilitada por tiempo");
                /* El disparo no esta habilitado por tiempo */
                return false;
            }

        }

        /* Si es posible el disparo por tiempo chekeo la marca */
        int[] nextState = nextMark(trans);

        if (!validShot(nextState)) {
            /* tiro no valido */
            this.log.write(trans, "Disparo no valido");
            return false;
        } else {
            /* Si es extendida por tiempo debo actualziar los tiempos */
            if (isTimeExtend()) {
                boolean[] oldSensi = this.getSensiArray();
                this.Mark = nextState;
                boolean[] newSensi = this.getSensiArray();
                for (int i = 0; i < newSensi.length; i++) {
                    if (!oldSensi[i] && newSensi[i]) {

                        /* actualizo el tiempo que hace q esta sensibilizada */
                        this.vectorTime[i] = timestamp;
                    }
                }
            } else {
                /* Actualizo la marca */
                this.Mark = nextState;

                //log
                this.log.write(trans, "Disparo exitoso ");
                StringBuilder msj = new StringBuilder();
                for (int value : this.Mark) {
                    msj.append(String.format("%d", value)).append(" - ");
                }
                this.log.write2( "Marca actual: "+ msj.toString() + "\n");
            }
            /* Chequeo los invariantes de plaza */
            this.CheckInvariantPlace();
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
            nextMark[i] += this.Mark[i];
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

    /**
     * @return True en caso de que se cumpla los invariantes, false, caso contrario.
     * @brief Metodo utilizado para realizar el cheackeo de invariantes de plaza.
     */
    private void CheckInvariantPlace() throws InvariantException {

        int[] res = new int[this.MatrixInvPlace.length];
        for (int i = 0; i < this.MatrixInvPlace.length; i++) {
            res[i] = this.Vector2Vector(this.MatrixInvPlace[i], this.Mark);
            if (this.VecInvPlaces[i] != res[i]) {
                throw new InvariantException(this.Mark, this.VecInvPlaces, res); //Si no se cumplen se lanza una excepcion
            }
        }
    }

    /**
     * @param v1 vector 1
     * @param v2 vector 2
     * @return Resultado del producto punto entre los vectores.
     * @brief Metodo utilizado para realizar el producto punto entre dos vectores.
     */
    private int Vector2Vector(int[] v1, int[] v2) {
        if (v1.length != v2.length) {
            /* Dimensiones incorrectas */
            return -1;
        }
        int r = 0;
        for (int i = 0; i < v1.length; i++) {
            r += v1[i] * v2[i];
        }
        return r;
    }

    /**
     * Metodo encargado de chequear que la trasicion depende del tiempo.
     *
     * @param trans numero de transicion a chequear.
     * @return True en el caso que lo sea.
     */
    private boolean isTransTime(int trans) {
        if (this.isTimeExtend()) {
            return (this.MatrixTime[0][trans] != 0);
        }
        return false;
    }

    /**
     * Metodo encargado de verificar si la red de petri es extendida en tiempo.
     *
     * @return True si lo es, false cas contrario.
     */
    private boolean isTimeExtend() {
        return (this.MatrixTime != null);
    }

    /**
     * @param time tiempo utilizado para calcular si se encuentra en al ventana de tiempo.
     * @return True si se encuentra en la ventana, false caso contrario.
     * @brief: Metodo encargado de certificar que la transicion se encuentra dentro de la ventana de tiempo para poder
     * ser disparada.
     * @TODO Agregar en el informe la explicacion de como se implementaron las transiciones con tiempo
     */
    private boolean getSensi4temp(long time, int trans) {
        boolean valid = true;
        if (this.MatrixTime[0][trans] != 0 && this.vectorTime[trans] != -1) {
            valid = this.MatrixTime[0][trans] < (time - this.vectorTime[trans]);
        }
        /* Si es cero podemos decir q el beta es infinito, por lo tanto se cumple solo alfa */
        if (valid && this.MatrixTime[1][trans] != 0) {
            /* verifico que se encuentre dentro de la ventana */
            valid = this.MatrixTime[1][trans] > (time - this.vectorTime[trans]);
            if (!valid) {
                System.out.println("Se paso el beta, verificar");
            }
        }
        return valid;
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
    int[] getMark() {
        return this.Mark.clone();
    }

    /**
     * @return entero con el numero de transiciones
     * @brief Metodo encargado de retornar el numero de transiciones que contiene la red de petri.
     */
    public int getNumTrans() {
        return this.matrixI[0].length;
    }

}

