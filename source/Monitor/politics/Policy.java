package Monitor.politics;


import java.util.Random;

/**
 * @TODO adaptar la politica al trabajo
 * Conflictos: 1. AsignarP1 y Asignar P2 (t1,t2)
 *             2. FinalizarT1P1 y ProcesarT2P1 (t5, t13)
 *             3. P1M1 y P1M2 (t9, t10)
 *             4. FinalizarT1P2 y ProcesarT2P2 (t6, t14)
 *             5. P2M1 y P2M2 (t11 y t12)
 * @TODO       SOLAMENTE CON RANDOM ESTO FUNCIONA, NO HAY Q RESOLVER CONFLICTOS, TODAS LAS TRANSICIONES TIENEN LA MISMA PRIORIDAD
 */
public class Policy {
    /**
     * number of transition in the RDP
     */
    private int sizeT;

    /**
     * @param sizeT number of transitions of the Petri net
     * @brief class constructor
     */
    public Policy(int sizeT) {
        this.sizeT = sizeT;
    }

    /**
     * @param ask sensitized transition vector
     * @return value of the transition to be shot
     * @brief find what policy to apply and apply it
     */
    public int whoWake(int[] ask) {
        int[][] P;

        //apply random policy
        P = this.randomPolicy();


        //get transition to shot
        int val = this.applyPolitic(ask, P);
        return val;
    }

//---------------------------------------------------------
//                      POLICY
//---------------------------------------------------------

    /**
     * @return matrix of random policy
     * @brief generate a random policy
     */
    public int[][] randomPolicy() {
        int[][] matrixP;
        matrixP = buildIdentity(this.sizeT);


        Random ran = new Random();
        int[] changes;
        int backVal = 9999;

        if ((matrixP[0].length % 2) != 0) {
            changes = new int[(matrixP[0].length) + 1];
        } else {
            changes = new int[(matrixP[0].length)];
        }

        for (int i = 0; i < changes.length; i++) {
            changes[i] = ran.nextInt((matrixP[0].length));
            while (changes[i] == backVal) changes[i] = ran.nextInt((matrixP[0].length));
            backVal = changes[i];
        }
        return matrixP;
    }

    //---------------------------------------------------------
    //                  TOOLS
    //---------------------------------------------------------

    /**
     * @param sensitized vector whit sensitized transitions
     * @param P          politic matrix
     * @return value of the transition to shoot
     * @brief apply the policy to get the transition to shoot
     */
    public int applyPolitic(int[] sensitized, int[][] P) {
        //check arguments
        if (sensitized.length != P[0].length)
            throw new IllegalArgumentException("Illegal Argument");

        //build index vector
        int[] index = new int[sensitized.length];
        for (int i = 0; i < sensitized.length; i++) {
            index[i] = i;
        }

        //apply Politic
        int[] result = new int[sensitized.length];
        int[] resultIn = new int[sensitized.length];
        for (int i = 0; i < sensitized.length; i++) {
            for (int j = 0; j < sensitized.length; j++) {
                result[i] += (P[i][j] * sensitized[j]);
                resultIn[i] += (P[i][j] * index[j]);
            }
        }
        //find the first element available
        int t = 9999;
        for (int i = 0; i < result.length; i++) {
            if (result[i] == 1) {
                return resultIn[i];
            }
        }
        // if not find someone return know error value
        return t;

    }

    /**
     * @param sizeT [in] dimension of matrix
     * @return Identity matrix
     * @brief build a sizeT-dimension Identity matrix
     */
    public int[][] buildIdentity(int sizeT) {
        //build identity matrix
        int matrixI[][] = new int[sizeT][sizeT];

        for (int i = 0; i < sizeT; i++) {
            for (int j = 0; j < sizeT; j++) {
                if (i == j)
                    matrixI[i][j] = 1;
                else
                    matrixI[i][j] = 0;
            }
        }
        return matrixI;
    }

    /**
     * @param matrixI [in] identity matrix to be modified
     * @param changeR the first element in this vector represents
     *                the row to be changed with the second element
     *                of the array and repeat the pattern
     * @return the matrix modified
     * @brief change the rows that specify in changeC param
     */
    public int[][] changeRow(int[][] matrixI, int[] changeR) {
        //size must be par
        if (changeR.length % 2 != 0)
            return null;
        //there cannot be 2 equal consecutive values
        int aux;
        for (int i = 0; i < changeR.length; i = i + 2) {
            aux = changeR[i];
            if (changeR[i + 1] == aux)
                return null;
        }

        int dimention = matrixI[0].length;
        for (int i = 0; i < changeR.length; i = i + 2) {
            int row1 = changeR[i];
            int row2 = changeR[i + 1];
            int auxVal = 0;

            for (int j = 0; j < dimention; j++) {
                auxVal = matrixI[row1][j];
                matrixI[row1][j] = matrixI[row2][j];
                matrixI[row2][j] = auxVal;
            }
        }
        return matrixI;

    }
}
