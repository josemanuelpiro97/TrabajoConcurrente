package Monitor.rdp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;

import static org.junit.jupiter.api.Assertions.*;

class RDPTest {
    @BeforeAll
    @Tag("Constructor")
    static void checkConst() {
        RDP rp1 = new RDP();
        Assertions.assertArrayEquals(new int[][]{
                {-1, 0, 0, 1},
                {1, -1, 0, 0},
                {0, 1, 0, -1},
                {1, 0, -1, 0},
                {0, 0, 1, -1}}, rp1.getMatrixI(), "Red de petri mal construida");
        Assertions.assertArrayEquals(new int[]{4, 0, 0, 0, 0}, rp1.getMarkInit(), "Vector de marcado incorrecto");
    }

    @Tag("Disparos")
    @org.junit.jupiter.api.Test
    void shotT_1() {
        try {
            /*=========================================================================
                                       RDP 1: Basica
              =========================================================================*/
            RDP rdp1 = new RDP();
            Assertions.assertArrayEquals(new int[]{4, 0, 0, 0, 0}, rdp1.getMarkInit());
            Assertions.assertEquals(rdp1.ShotT(1), "La red no se disparo y debia");
            Assertions.assertArrayEquals(new int[]{3, 1, 0, 1, 0}, rdp1.getMarkInit(),
                    "La red no evoluciono como debia");
            Assertions.assertEquals(rdp1.ShotT(2), "La red no se disparo y debia");
            Assertions.assertArrayEquals(new int[]{3, 0, 1, 1, 0}, rdp1.getMarkInit(),
                    "La red no evoluciono como debia");
            Assertions.assertEquals(rdp1.ShotT(2), "Se disparo y no debia");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    void getNumTrans() {
    }

    @org.junit.jupiter.api.Test
    void getSensiArray() {
    }
}