package Monitor.rdp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RDPTest {

    @Tag("Disparos")
    @Test
    void ShotT() {
        try {
            RDP rdp1 = new RDP();

            //chech initial mark
            assertArrayEquals(new int[]{4, 0, 0, 0, 0}, rdp1.getMarkInit());

            //check shots
            assertTrue(rdp1.ShotT(0));
            assertFalse(rdp1.ShotT(3));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getSensiArray() {
        final boolean[] ARRAY_TEST_POSITIVE = new boolean[]{true, false, false, false};
        final boolean ARRAY_TEST_NEGATIVE[] = new boolean[]{false, false, true, false};

        RDP rdp1 = new RDP();

        //positive test
        assertArrayEquals(ARRAY_TEST_POSITIVE, rdp1.getSensiArray());
    }
}