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
            Assertions.assertArrayEquals(new int[]{3, 1, 0, 1, 0}, rdp1.getMarkInit(),
                    "La red no evoluciono como debia");
            Assertions.assertTrue(rdp1.ShotT(1));
            Assertions.assertArrayEquals(new int[]{3, 0, 1, 1, 0}, rdp1.getMarkInit(),
                    "La red no evoluciono como debia");
            Assertions.assertFalse(rdp1.ShotT(3), "Se disparo y no debia");

        } catch (InvariantException e) {
            e.printInfo();
        }
    }

    @Tag("Sensibilizado")
    @Test
    void getSensiArray() {
        try {
            final boolean[] ARRAY_TEST_POSITIVE = new boolean[]{true, false, false, false};
            final boolean[] ARRAY_TEST_NEXTSTATE = new boolean[]{true, true, true, false};
            RDP rdp1 = new RDP();
            //positive test
            assertArrayEquals(ARRAY_TEST_POSITIVE, rdp1.getSensiArray());
            rdp1.ShotT(0);
            Assertions.assertArrayEquals(ARRAY_TEST_NEXTSTATE, rdp1.getSensiArray());

        } catch (InvariantException e) {
            e.printInfo();
        }
    }

}