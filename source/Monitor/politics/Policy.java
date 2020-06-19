package Monitor.politics;


import java.util.Random;

public class Policy {

    /**
     * policy that resolves conflict in t0 and t1
     */
    private final int CONFLIC1_1 = 0;
    private final int CONFLIC1_2 = 1;
    private final int[] CLONFLICT1 = {CONFLIC1_1, CONFLIC1_2};

    private int sizeT;

    private boolean stateC = false;


    public Policy(int sizeT) {
        this.sizeT = sizeT;
    }

    /**
     * @param ask sensitized transition vector
     * @return value of the transition to be shot
     * @brief find what policy to apply and apply it
     */
    public int whoWake(int[] ask) {
        int[][] matrixP;
        matrixP = buildIdentity(this.sizeT);
        //apply random form
        Random ran = new Random();
        int lenght = matrixP[0].length;
        int[] changes = new int[lenght];
        int backVal = 999;
        for (int i = 0; i < lenght; i++) {
            changes[i] = ran.nextInt(lenght);
            while (changes[i] == backVal) changes[i] = ran.nextInt(lenght);
            backVal = changes[i];
        }
        int[][] P = matrixP.clone();
        int[][] PChanged = this.changeRow(P, changes);

        //apply political changes
        if (this.stateC) {
            PChanged = this.changeRow(PChanged, this.CLONFLICT1);
        }
        //change state for P1
        if (this.stateC)
            this.stateC = false;
        else
            this.stateC = true;

        //get transition to shot
        assert PChanged != null;
        int val = this.applyPolitic(ask, PChanged);

        return val;
    }

    /**
     * @param sizeT [in] dimension of matrix
     * @return Identity matrix
     * @brief build a sizeT-dimension Identity matrix
     */
    private int[][] buildIdentity(int sizeT) {
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
    private int[][] changeRow(int[][] matrixI, int[] changeR) {
        if (changeR.length % 2 != 0)
            return null;
        else {
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

    /**
     * @param sensitized vector whit sensitized transitions
     * @param P          politic matrix
     * @return value of the transition to shoot
     * @brief apply the policy to get the transition to shoot
     */
    private int applyPolitic(int[] sensitized, int[][] P) {
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
        int [] resultIn = new int[sensitized.length];
        for (int i = 0; i < sensitized.length; i++) {
            for (int j = 0; j < sensitized.length; j++) {
                result[i] += (P[i][j] * sensitized[j]);
                resultIn[i] += (P[i][j] * index[j]);
            }
        }
        //find the first element available
        int t = 9999;
        for (int i = 0; i < result.length; i++) {
            if (result[i] == 1)
                t = i;
        }
        return resultIn[t];
    }
}
