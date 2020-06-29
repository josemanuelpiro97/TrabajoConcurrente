package Monitor.rdp;

import Monitor.Logger.Log;

/**
 * @TODO ver si se hardcodea la red o se arma desde un archivo.
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
    private final long[][] MatrixTime;
    /**
     * Vector de stamptime para el sensibilizado de transiciones
     */
    private long[] vectorTime; //stamptime
    /**
     * Objeto log utilizado para almacenar el disparo de las trasiciones en un txt
     */
    private Log log;

    /**
     * @brief Constructor solo utilizado para test, sin tiempo ni logger, la dejo por los TEST
     */
    public RDP() {
        info = "RdP de Test, sin tiempo";

        // Matriz de incidencia de la red para test
        this.matrixI = new int[][]{
                {-1, 0, 0, 1},
                {1, -1, 0, 0},
                {0, 1, 0, -1},
                {1, 0, -1, 0},
                {0, 0, 1, -1}};

        // Vector de marcado incial, indica 4 tokens iniciales en la plaza p0
        this.Mark = new int[]{4, 0, 0, 0, 0};

        //Matriz de P invariantes
        this.MatrixInvPlace = new int[][]{
                {1, 1, 1, 0, 0},
                {1, 0, 0, 1, 1}
        };

        // Numero de invariante de plaza
        this.VecInvPlaces = new int[]{4, 4};

        this.MatrixTime = null;
    }

    /**
     * @brief Constructor solo utilizado para test, con logger, la dejo por los TEST
     */
    public RDP(Log l) {
        info = "RdP de Test, sin tiempo";

        // Matriz de incidencia de la red para test
        this.matrixI = new int[][]{
                {-1, 0, 0, 1},
                {1, -1, 0, 0},
                {0, 1, 0, -1},
                {1, 0, -1, 0},
                {0, 0, 1, -1}};

        // Vector de marcado incial, indica 4 tokens iniciales en la plaza p0
        this.Mark = new int[]{4, 0, 0, 0, 0};

        // Matriz de P invariantes
        this.MatrixInvPlace = new int[][]{
                {1, 1, 1, 0, 0},
                {1, 0, 0, 1, 1}
        };

        // Numero de invariante de plaza
        this.VecInvPlaces = new int[]{4, 4};

        this.MatrixTime = null;

        this.log = l;

    }

    /**
     * Red de trabajo final
     *
     * @param info Nombre de la red
     * @param l    logger
     */
    public RDP(String info, Log l) {
        this.info = info;

        // Matriz de incidencia
        this.matrixI = new int[][]{
                {0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, -1, 0, 0, 0, 2, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, -1, 0, 0, 0, 2},
                {0, -1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, -1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 1, 0, -1, -1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, -1, -1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, -2, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, -2},
                {-1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, -1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0},
                {0, 0, 0, -1, -1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 1, 0, 0},
        };

        // Vector de marcado incial, indica 4 tokens iniciales en la plaza p0
        this.Mark = new int[]{0, 0, 0, 8, 8, 4, 4, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0};

        // Matriz de P invariantes
        this.MatrixInvPlace = new int[][]{
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1},
        };

        // Numero de invariante de plaza
        this.VecInvPlaces = new int[]{8, 8, 4, 4, 1, 1, 1, 1};

        // Ventana de tiempo de las trasiciones
        this.MatrixTime = new long[][]{
                //Setear los tiempos
                {500, 0, 0, 0, 0, 200, 200, 350, 350, 0, 0, 0, 0, 200, 200, 500, 500},
                {1000000, 0, 0, 0, 0, 1000000, 1000000, 1000000, 1000000, 0, 0, 0, 0, 1000000, 1000000, 1000000, 1000000}
                //ProcesarT2Px + FinalizarT2Px > FinalizarT1Px
        };

        // Vector donde se almacenan los timestamp
        this.vectorTime = new long[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

        this.log = l;

        //set initial time for initial sensitized transitions
        this.setTimeSens();

    }

    /**
     * @param trans number of transition to shoot
     * @return true in case that the transition was shoot, false in other case.
     * @brief this method tries to shoot the transition, in case that they can, the information will be updated and
     * return true, in case that they not, return false.
     */
    public boolean ShotT(int trans) throws InvariantException {
        // take time of shot
        long timestamp = java.lang.System.currentTimeMillis();

        // check if the transition exist
        if (trans < 0 || trans > this.matrixI[0].length) {
            this.log.write(trans, "La transicion no existe");
            return false;
        }

        //check if shooting is possible according to the time
        if (this.isTransTime(trans)) {
            if (!this.getSensi4temp(timestamp, trans)) {
                this.log.write(trans, "Disparo no valido debido al tiempo");
                return false;
            }
        }

        // check if shooting is possible according to the mark
        int[] nextState = nextMark(trans);// take possible next mark
        if (!validShot(nextState)) {
            //invalid shot
            this.log.write(trans, "Disparo no valido debido a la marca");
            return false;
        }

        // if time type, update the transition time
        if (isTimeExtend()) {
            boolean[] oldSensi = this.getSensi4Mark();
            this.Mark = nextState;
            boolean[] newSensi = this.getSensi4Mark();
            for (int i = 0; i < newSensi.length; i++) {
                if ((!oldSensi[i] && newSensi[i]) && this.isTransTime(i)) {
                    // update time vector
                    this.vectorTime[i] = timestamp;
                } else if ((oldSensi[i] && !newSensi[i]) && this.isTransTime(i)) {
                    // update time vector
                    this.vectorTime[i] = -1;
                }
            }
            // else, only update the mark
        } else {
            //update mark
            this.Mark = nextState;
        }

        // check invariant place
        this.CheckInvariantPlace();

        //log
        this.log.write(trans, "Disparo exitoso ");
        StringBuilder msj = new StringBuilder();
        for (int value : this.Mark) {
            msj.append(String.format("%d", value)).append(" - ");
        }
        this.log.write2("Marca actual: " + msj.toString() + "\n");

        //registro si se completo una tarea
        if(trans == 9 || trans == 10 || trans == 11 || trans == 12 )
            this.log.write2("SE COMPLETO UNA TAREA"+ "\n");

        return true;
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


    /*=================================================================================
                                          Time methods
     ==================================================================================*/

    /**
     * @brief Update the sensitized time
     */
    private void setTimeSens() {
        for (int i = 0; i < this.vectorTime.length; i++) {
            //if is sensitized and is time transition, update timeSencibilized
            if (this.validShot(this.nextMark(i)) && isTransTime(i)) {
                this.vectorTime[i] = java.lang.System.currentTimeMillis();
            }
        }
    }

    /**
     * @param time time of the question
     * @return true if the transition is within the time parameters, else in other case
     * @brief check if the transition is within the time parameters
     */
    private boolean getSensi4temp(long time, int trans) {
        boolean valid = false;
        if (isTransTime(trans) && wasSensitized(trans)) {
            valid = true;
            valid = this.MatrixTime[0][trans] < (time - this.vectorTime[trans]);
            if (valid && this.MatrixTime[1][trans] != 0) {
                valid = this.MatrixTime[1][trans] > (time - this.vectorTime[trans]);
            }
        }
        return valid;
    }

    /**
     * @return True if the Petri net is time extend, false in other case.
     * @brief check if the Petri net is time extend
     */
    private boolean isTimeExtend() {
        return (this.MatrixTime != null);
    }

    /**
     * @param trans number of transition to check.
     * @return True if is time type, else in other case.
     * @brief check if the transition is time type
     */
    private boolean isTransTime(int trans) {
        if (this.isTimeExtend()) {
            return (this.MatrixTime[0][trans] != 0);
        }
        return false;
    }

    /**
     * @param trans number of transition
     * @return true in case that was sensitized, false in other case
     * @brief check if the transition was sensitized
     */
    private boolean wasSensitized(int trans) {
        return (this.vectorTime[trans] != -1);
    }

    /**
     * @param trans Numero de transicion
     * @return tiempo restante (tipo long), -1 si no debe dormir.
     * @brief Devuelve el tiempo que resta hasta que la transicion se encuentre dentro de la ventana de tiempo
     */
    public long getWaitTime(int trans) {
        long time = java.lang.System.currentTimeMillis();

        if (this.wasSensitized(trans) && this.isTransTime(trans)) {
            //Tiempo que lleva sensi
            long timer = (time - this.vectorTime[trans]);
            if (timer < this.MatrixTime[0][trans]) {
                //Si es menor devuelvo el valor q debe esperar
                return (this.MatrixTime[0][trans] - timer) + 2; //Milisegundo necesario para dormir un alfa mayor a la ventana
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    /*=================================================================================
                                          utility methods
     ==================================================================================*/

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

    /*=================================================================================
                                        Get net information
     ==================================================================================*/

    /**
     * @return Devuelve una copia de la matriz de incidencia
     * @brief Metodo creado solo para los tests
     */
    public int[][] getMatrixI() {
        return this.matrixI.clone();
    }

    /**
     * @return Devuelve una copia del vector de marcado
     * @brief Devuelve el vector de marcado.
     */
    public int[] getMark() {
        return this.Mark.clone();
    }

    /**
     * @return entero con el numero de transiciones
     * @brief Metodo encargado de retornar el numero de transiciones que contiene la red de petri.
     */
    public int getNumTrans() {
        return this.matrixI[0].length;
    }

    /**
     * @return vector whit sensitized
     * @brief returns those sensitized only by the mark
     */
    public boolean[] getSensi4Mark() {
        //check which transitions are sensitized by mark
        boolean[] isSensi = new boolean[this.matrixI[0].length];
        for (int i = 0; i < isSensi.length; i++) {
            isSensi[i] = this.validShot(this.nextMark(i));
        }
        return isSensi;
    }

    /**
     * @return vector de tipo booleano.
     * @brief Metodo encargado de devolver el vector de sensibilizado. En cada posicion del arreglo habra un True o
     * un False indicando si se encuentra sensibilizada la transicion o no.
     */
    public boolean[] getSensiArray() {
        //check which transitions are sensitized by mark
        boolean[] isSensi = new boolean[this.matrixI[0].length];
        for (int i = 0; i < isSensi.length; i++) {
            isSensi[i] = this.validShot(this.nextMark(i));
        }

        if (this.isTimeExtend()) {
            //take time
            long time = java.lang.System.currentTimeMillis();

            //check which transitions are time sensitized
            boolean[] isSensiTime = new boolean[this.matrixI[0].length];
            for (int i = 0; i < matrixI[0].length; i++) {
                if (this.isTransTime(i)) {
                    isSensiTime[i] = this.getSensi4temp(time, i);
                } else {
                    isSensiTime[i] = true;
                }
            }

            //check which transitions are sensitized by mark and time
            boolean[] res = new boolean[this.matrixI[0].length];
            for (int i = 0; i < matrixI[0].length; i++) {
                res[i] = isSensi[i] && isSensiTime[i];
            }

            return res;
        }
        return isSensi;
    }

}


