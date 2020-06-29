package Monitor.Queue;

/*

class QueueManagmentTest {

    @Test
    void sleepN() {
        final int NUMBER_OF_TRANSITION = 3;
        final int INDEX_TEST1 = 0;
        final int INDEX_TEST2 = 2;
        final int SIZE_TEST = 1;

        QueueManagment queueManagment = new QueueManagment(NUMBER_OF_TRANSITION);

        Runnable runnable0 = new Runnable() {
            @Override public void run() {
                queueManagment.sleepN(INDEX_TEST1);
            }
        };
        Runnable runnable1 = new Runnable() {
            @Override public void run() {
                queueManagment.sleepN(INDEX_TEST2);
            }
        };

        Thread t0 = new Thread( runnable0 );
        Thread t1 = new Thread( runnable1 );

        t0.start();
        t1.start();

        assertTrue(queueManagment.whoSleepT()[INDEX_TEST1]);
        assertTrue(queueManagment.whoSleepT()[INDEX_TEST2]);

        assertEquals(SIZE_TEST,queueManagment.getSemaphores()[INDEX_TEST1].getQueueLength());
        assertEquals(SIZE_TEST,queueManagment.getSemaphores()[INDEX_TEST2].getQueueLength());
    }

    @Test
    void wakeN() {
        final int NUMBER_OF_TRANSITION = 3;
        final int INDEX_TEST1 = 0;
        final int INDEX_TEST2 = 2;
        final int SIZE_TEST = 0;

        QueueManagment queueManagment = new QueueManagment(NUMBER_OF_TRANSITION);

        Runnable runnable0 = new Runnable() {
            @Override public void run() {
                queueManagment.sleepN(INDEX_TEST1);
            }
        };
        Runnable runnable1 = new Runnable() {
            @Override public void run() {
                queueManagment.sleepN(INDEX_TEST2);
            }
        };
        Runnable runnable2 = new Runnable() {
            @Override public void run() {
                queueManagment.wakeN(INDEX_TEST1);
                queueManagment.wakeN(INDEX_TEST2);
            }
        };

        Thread t0 = new Thread( runnable0 );
        Thread t1 = new Thread( runnable1 );
        Thread t2 = new Thread( runnable2 );

        t0.start();
        t1.start();
        t2.start();

        //some delay
        for(int i=0 ; i<1000000; i++){}

        assertFalse(queueManagment.whoSleepT()[INDEX_TEST1]);
        assertFalse(queueManagment.whoSleepT()[INDEX_TEST2]);

        int teste = queueManagment.getSemaphores()[INDEX_TEST1].getQueueLength();

        assertEquals(SIZE_TEST,queueManagment.getSemaphores()[INDEX_TEST1].getQueueLength());
        assertEquals(SIZE_TEST,queueManagment.getSemaphores()[INDEX_TEST2].getQueueLength());
    }

    @Test
    void isSleep() {
        final int NUMBER_OF_TRANSITION = 3;
        final int INDEX_TEST1 = 0;
        final int INDEX_TEST2 = 2;
        final int SIZE_TEST = 1;
        final boolean BOLEEAN_ARRAY_TEST [] = {true, false, true};


        QueueManagment queueManagment = new QueueManagment(NUMBER_OF_TRANSITION);

        Runnable runnable0 = new Runnable() {
            @Override public void run() {
                queueManagment.sleepN(INDEX_TEST1);
            }
        };
        Runnable runnable1 = new Runnable() {
            @Override public void run() {
                queueManagment.sleepN(INDEX_TEST2);
            }
        };

        Thread t0 = new Thread( runnable0 );
        Thread t1 = new Thread( runnable1 );

        t0.start();
        t1.start();

        //some delay
        for(int i=0 ; i<1000000; i++){}

        //positive test
        boolean [] testResult = queueManagment.isSleep(BOLEEAN_ARRAY_TEST);

        assertTrue(testResult[INDEX_TEST1]);
        assertTrue(testResult[INDEX_TEST2]);

    }
}
*/
