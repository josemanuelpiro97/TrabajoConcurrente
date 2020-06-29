package Monitor.politics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolicyTest {
    /*
    @Test
    void whoWake() {
        //-------------------------------------------
        //              ODD SIZE
        final int size = 5;
        //-------------------------------------------
        //all ones
        final int[] ask0 = {1, 1, 1, 1, 1};
        //interleave
        final int[] ask1 = {0, 1, 0, 1, 0};
        //interleave
        final int[] ask2 = {1, 0, 1, 0, 1};
        //left border
        final int[] ask3 = {1, 0, 0, 0, 0};
        //right border
        final int[] ask4 = {0, 0, 0, 0, 1};
        //all zeros
        final int[] ask5 = {0, 0, 0, 0, 0};
        //container
        final int[][] container = new int[size][size];
        container[0] = ask0;
        container[1] = ask1;
        container[2] = ask2;
        container[3] = ask3;
        container[4] = ask4;

        //build policy
        Policy policy = new Policy(5);

        //loop test
        int loop = 1000000;
        for (int i = 0; i < loop; i++) {
            for (int j = 0; j < container.length; j++) {
                int val = policy.whoWake(container[j]);
                assertTrue(val < size);
            }
            assertEquals(9999, policy.whoWake(ask5));
        }

        //-------------------------------------------
        //              EVEN SIZE
        final int size2 = 4;
        //-------------------------------------------
        //all ones
        final int[] ask6 = {1, 1, 1, 1};
        //interleave
        final int[] ask7 = {0, 1, 0, 1};
        //interleave
        final int[] ask8 = {1, 0, 1, 0};
        //left border
        final int[] ask9 = {1, 0, 0, 0};
        //right border
        final int[] ask10 = {0, 0, 0, 1};
        //all zeros
        final int[] ask11 = {0, 0, 0, 0};
        //container
        final int[][] container2 = new int[size2 + 1][size2];
        container2[0] = ask6;
        container2[1] = ask7;
        container2[2] = ask8;
        container2[3] = ask9;
        container2[4] = ask10;

        //build policy
        Policy policy2 = new Policy(size2);

        //loop test
        int loop2 = 1000000;
        for (int i = 0; i < loop2; i++) {
            for (int j = 0; j < container2.length; j++) {
                int val = policy2.whoWake(container2[j]);
                assertTrue(val < size2);
            }
            assertEquals(9999, policy2.whoWake(ask11));
        }

    }

    @Test
    void testBuildIdentity() {
        //different sizes
        final int a = 4;
        final int b = 9;
        final int c = 16;

        //build policy
        Policy policy = new Policy(1);

        //identity matrix
        int[][] identa = policy.buildIdentity(a);
        int[][] identb = policy.buildIdentity(b);
        int[][] identc = policy.buildIdentity(c);

        //all sum
        assertEquals(a, this.allSum(identa));
        assertEquals(b, this.allSum(identb));
        assertEquals(c, this.allSum(identc));
    }

    @Test
    void testChangeRow() {
        final int a = 4;

        //changes
        int[] changes1 = {0, 1, 1, 0};
        int[] changes2 = {0, 1, 2, 3};
        int[] changes3 = {3, 2, 1, 0};
        int[][] resultChanges_2_3 = {{0, 1, 0, 0},
                {1, 0, 0, 0},
                {0, 0, 0, 1},
                {0, 0, 1, 0}};
        int[] changesError = {0, 0, 1, 1};
        int[] changesError2 = {1, 2, 3, 4, 5};

        //build policy
        Policy policy = new Policy(1);
        int[][] identa = policy.buildIdentity(a);

        //test different case
        //case 1
        int[][] result1 = policy.changeRow(identa, changes1);
        assertArrayEquals(identa[0], result1[0]);
        assertArrayEquals(identa[1], result1[1]);
        assertArrayEquals(identa[2], result1[2]);
        assertArrayEquals(identa[3], result1[3]);
        assertEquals(a, this.allSum(result1));


        //case 2
        identa = policy.buildIdentity(a);
        int[][] result2 = policy.changeRow(identa, changes2);
        assertArrayEquals(resultChanges_2_3[0], result2[0]);
        assertArrayEquals(resultChanges_2_3[1], result2[1]);
        assertArrayEquals(resultChanges_2_3[2], result2[2]);
        assertArrayEquals(resultChanges_2_3[3], result2[3]);
        assertEquals(a, this.allSum(result2));

        //case 3
        identa = policy.buildIdentity(a);
        int[][] result3 = policy.changeRow(identa, changes3);
        assertArrayEquals(resultChanges_2_3[0], result3[0]);
        assertArrayEquals(resultChanges_2_3[1], result3[1]);
        assertArrayEquals(resultChanges_2_3[2], result3[2]);
        assertArrayEquals(resultChanges_2_3[3], result3[3]);
        assertEquals(a, this.allSum(result3));

        //case 4
        assertNull(policy.changeRow(identa, changesError));
        //case 5
        assertNull(policy.changeRow(identa, changesError2));
    }

    @Test
    void randomPolicy() {
        int size = 5;

        Policy policy = new Policy(size);
        int[][] randomP = policy.randomPolicy();

        //check
        assertEquals(size, this.allSum(randomP));
    }

    @Test
    void conflictPolicy_1() {
        int size = 5;

        Policy policy = new Policy(size);
        int[][] randomP = policy.randomPolicy();

        //check
        assertEquals(size, this.allSum(randomP));
    }

    @Test
    void applyPolitic() {
        final int a = 4;

        //for conflicts p1
        int[] ask1 = {1, 1, 0, 0};
        //for random policy
        int[] ask2 = {0, 0, 1, 0};
        int[] ask3 = {0, 0, 0, 1};
        //for know error value
        int[] ask4 = {0, 0, 0, 0};

        //build policy
        Policy policy = new Policy(4);




        //for p1
        int [][] policy_1= policy.conflictPolicy_1();
        assertEquals(0,policy.applyPolitic(ask1,policy_1));
        policy_1= policy.conflictPolicy_1();
        assertEquals(1,policy.applyPolitic(ask1,policy_1));
        policy_1= policy.conflictPolicy_1();
        assertEquals(0,policy.applyPolitic(ask1,policy_1));

        //for random
        int [][] randomP = policy.randomPolicy();
        int val = policy.applyPolitic(ask2,randomP);
        int val2 = policy.applyPolitic(ask3,randomP);
        assertTrue( val > 1 && val < 4 );
        assertTrue( val2 > 1 && val2 < 4 );

        //for know error value
        int errorVal = policy.applyPolitic(ask4,randomP);
        assertEquals(9999, errorVal);
    }

    /**
     * @param matrix matrix to sum
     * @return all sum
     * @brief sum all ij in the matrix
     *
    private int allSum(int[][] matrix) {
        int aux = 0;
        int a = matrix.length;
        for (int i = 0; i < a; i++) {
            for (int j = 0; j < a; j++) {
                aux += matrix[i][j];
            }
        }
        return aux;
    }
*/

}