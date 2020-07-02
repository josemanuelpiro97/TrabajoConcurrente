package Monitor.rdp;

import Monitor.Logger.Log;

public class RDP {
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                      VARIABLES
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//****************************************************
//              Private Variables
//****************************************************
    /**
     * net description
     */
    private final String info;
    /**
     * incidence matrix of Petri net
     */
    private final int[][] matrixI;
    /**
     * initial mark of Petri net
     */
    private int[] mark;
    /**
     * matrix of places invariant
     */
    private final int[][] matrixInvPlace;
    /**
     * vector used for check places invariant
     */
    private final int[] vecInvPlaces;
    /**
     * time matrix
     */
    private final long[][] matrixTime;
    /**
     * stamptime vector
     */
    private long[] vectorTime;
    /**
     * log object
     */
    private Log log;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                     CONSTRUCTORS
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @param matrixI        incidence matrix of Petri net
     * @param mark           initial mark of Petri net
     * @param matrixInvPlace matrix of places invariant
     * @param vecInvPlaces   vector used for check places invariant
     * @param matrixTime     time matrix
     * @param vectorTime     stamptime vector
     * @param info           info
     * @brief class constructor, only used whit json file
     */
    public RDP(int[][] matrixI, int[] mark, int[][] matrixInvPlace, int[] vecInvPlaces, long[][] matrixTime, long[] vectorTime, String info) {
        this.matrixI = matrixI;
        this.mark = mark;
        this.matrixInvPlace = matrixInvPlace;
        this.vecInvPlaces = vecInvPlaces;
        this.matrixTime = matrixTime;
        this.vectorTime = vectorTime;
        this.info = info;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                    PUBLIC METHODS
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @param trans number of transition to shoot
     * @return true in case that the transition was shoot, false in other case.
     * @brief this method tries to shoot the transition, in case that they can, the information will be updated and
     * return true, in case that they not, return false.
     */
    public boolean ShotT(int trans) throws InvariantException, ShotException {
        // take time of shot
        long timestamp = java.lang.System.currentTimeMillis();

        // check if the transition exist
        if (trans < 0 || trans > this.matrixI[0].length) {
            throw new ShotException(trans);
        }

        //check if shooting is possible according to the time
        if (this.isTransTime(trans)) {
            if (!this.getSensi4temp(timestamp, trans)) {
                //log
                this.log.write(String.format(" Shot: | %d | %b | %s",trans, false, getStringMark()),timestamp);
                return false;
            }
        }

        // check if shooting is possible according to the mark
        int[] nextState = nextMark(trans);// take possible next mark
        if (!validShot(nextState)) {
            //invalid shot
            this.log.write(String.format(" Shot: | %d | %b | %s",trans,false, getStringMark()),timestamp);
            return false;
        }

        // if time type, update the transition time
        if (isTimeExtend()) {
            boolean[] oldSensi = this.getSensi4Mark();
            this.mark = nextState;
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
            this.mark = nextState;
        }

        // check invariant place
        this.CheckInvariantPlace();

        //log
        this.log.write(String.format(" Shot: | %d | %b | %s", trans, true, getStringMark()),timestamp);
        return true;
    }

//****************************************************
//                  Getters
//****************************************************

    /**
     * @return a copy of mark Vector
     * @brief get a copy of mark vector
     */
    public int[] getMark() {
        return this.mark.clone();
    }

    /**
     * @return number of transitions
     * @brief get total number of transition in the Petri net
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

//****************************************************
//                  Setters
//****************************************************
    /**
     * @param l log to set
     * @brief setter of log
     */
    public void setLog(Log l) {
        this.log = l;
    }

//****************************************************
//                  Time methods
//****************************************************
    /**
     * @brief Update the sensitized time
     */
    public void setTimeSens() {
        long time = java.lang.System.currentTimeMillis();
        for (int i = 0; i < this.vectorTime.length; i++) {
            //if is sensitized and is time transition, update timeSencibilized
            if (this.validShot(this.nextMark(i)) && isTransTime(i)) {
                this.vectorTime[i] = time;
            }
        }
    }

    /**
     * @param trans number of transition
     * @return time left (long type), -1 if should not sleep.
     * @brief method that calculate the time left to that the transition be in the window time
     */
    public long getWaitTime(int trans) {
        long time = System.currentTimeMillis();

        if (this.wasSensitized(trans) && this.isTransTime(trans)) {
            //time that was sensitized
            long timer = (time - this.vectorTime[trans]);
            if (timer < this.matrixTime[0][trans]) {
                return (this.matrixTime[0][trans] - timer) + 2; // time necessary to get entry to the window
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                   PRIVATE METHODS
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @param trans number of transition to shot
     * @return new mark
     * @brief: calculate a new mark given a specific transition number to shot
     */
    private int[] nextMark(int trans) {
        // build shot vector
        int[] VecShot = new int[this.matrixI[0].length];
        VecShot[trans] = 1;

        // product between incidence matrix and shot vector
        int[] nextMark = this.multMatrix2Vector(this.matrixI, VecShot);

        // sum between current mark and previous result
        for (int i = 0; i < this.matrixI.length; i++) {
            nextMark[i] += this.mark[i];
        }
        return nextMark;
    }

    /**
     * @param mark next mark vector
     * @return True in case that next mark ara correct, false in case that some value are negative.
     * @brief method that calculate if the next mark are correct.
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
     * @return True in case that places invariant are correct, false in other case
     * @brief check places invariants.
     */
    private void CheckInvariantPlace() throws InvariantException {

        int[] res = new int[this.matrixInvPlace.length];
        for (int i = 0; i < this.matrixInvPlace.length; i++) {
            res[i] = this.Vector2Vector(this.matrixInvPlace[i], this.mark);
            if (this.vecInvPlaces[i] != res[i]) {
                throw new InvariantException(this.mark, this.vecInvPlaces, res);
            }
        }
    }

//****************************************************
//                  Time methods
//****************************************************
    /**
     * @param time time of the question
     * @return true if the transition is within the time parameters, else in other case
     * @brief check if the transition is within the time parameters
     */
    private boolean getSensi4temp(long time, int trans) {
        boolean valid = false;
        if (isTransTime(trans) && wasSensitized(trans)) {
            valid = this.matrixTime[0][trans] < (time - this.vectorTime[trans]);
            if (valid && this.matrixTime[1][trans] != 0) {
                valid = this.matrixTime[1][trans] > (time - this.vectorTime[trans]);
            }
        }
        return valid;
    }

    /**
     * @return True if the Petri net is time extend, false in other case.
     * @brief check if the Petri net is time extend
     */
    private boolean isTimeExtend() {
        return (this.matrixTime != null);
    }

    /**
     * @param trans number of transition to check.
     * @return True if is time type, else in other case.
     * @brief check if the transition is time type
     */
    private boolean isTransTime(int trans) {
        if (this.isTimeExtend()) {
            return (this.matrixTime[0][trans] != 0);
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


//****************************************************
//                   Tools
//****************************************************
    /**
     * @param v1 vector 1
     * @param v2 vector 2
     * @return result of vector product.
     * @brief method that calculate vector multiplication.
     */
    private int Vector2Vector(int[] v1, int[] v2) {
        if (v1.length != v2.length) {
            // wrong dimension
            return -1;
        }
        int r = 0;
        for (int i = 0; i < v1.length; i++) {
            r += v1[i] * v2[i];
        }
        return r;
    }

    /**
     * @param matrix int matrix
     * @param vector int vecor
     * @return result of product.
     * @brief: method that calculate multiplication between matrix and vector.
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
     * @return mark vector
     * @brief current mark converted to string
     */
    private String getStringMark() {
        String buffer = "[";
        for(int i : this.mark){
            buffer += i +" ";
        }
        buffer += "]";
        return buffer;
    }
}


