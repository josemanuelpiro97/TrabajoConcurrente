package Monitor.rdp;

import Monitor.Logger.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RDPTest {

    @Tag("Disparos")
    @Test
    void ShotT() {
        try {
            Log l = new Log();
            RDP rdp1 = new RDP(l);

            //chech initial mark
            assertArrayEquals(new int[]{4, 0, 0, 0, 0}, rdp1.getMark());

            //check shots
            assertTrue(rdp1.ShotT(0));
            Assertions.assertArrayEquals(new int[]{3, 1, 0, 1, 0}, rdp1.getMark(),
                    "La red no evoluciono como debia");
            Assertions.assertTrue(rdp1.ShotT(1));
            Assertions.assertArrayEquals(new int[]{3, 0, 1, 1, 0}, rdp1.getMark(),
                    "La red no evoluciono como debia");
            Assertions.assertFalse(rdp1.ShotT(3), "Se disparo y no debia");

        } catch (InvariantException e) {
            e.printInfo();
        }
    }

    @Tag("DisparosContiempo")
    @Test
    void ShotT_time() {
        try {
            Log l = new Log();
            RDP rdp1 = new RDP("Red de petri con tiempo", l);
            rdp1.ShotT(0);
            Assertions.assertFalse(rdp1.ShotT(1), "La red se disparo y no debia");
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Assertions.assertTrue(rdp1.ShotT(1), "La red no se disparo y debia");
            rdp1.ShotT(0);
            try {
                Thread.sleep(6000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Assertions.assertFalse(rdp1.ShotT(1), "La red se disparo y no debia");
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
            Log l = new Log();
            RDP rdp1 = new RDP(l);
            //positive test
            assertArrayEquals(ARRAY_TEST_POSITIVE, rdp1.getSensiArray());
            rdp1.ShotT(0);
            Assertions.assertArrayEquals(ARRAY_TEST_NEXTSTATE, rdp1.getSensiArray());

        } catch (InvariantException e) {
            e.printInfo();
        }
    }

    @Tag("SensibilizadoConTiempo")
    @Test
    void getSensiWithTime(){
        try{
            Log l = new Log();
            RDP rdp1 = new RDP("Con tiempo",l);

            assertArrayEquals(new boolean[]{true, false, false, false}, rdp1.getSensiArray());
            rdp1.ShotT(0);
            Assertions.assertArrayEquals(new int[]{3, 1, 0, 1, 0}, rdp1.getMark(),
                    "La red no evoluciono como debia");

            assertArrayEquals(new boolean[]{true, false, true, false}, rdp1.getSensiArray());//No puede estar sensibilizada con tiempo
            try{
                Thread.sleep(2000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            assertArrayEquals(new boolean[]{true, true, true, false}, rdp1.getSensiArray());//Deberia estar dentro de la ventana
            rdp1.ShotT(1);
            assertArrayEquals(new boolean[]{true, false, true, false}, rdp1.getSensiArray());
            rdp1.ShotT(0);
            assertArrayEquals(new boolean[]{true, false, true, false}, rdp1.getSensiArray());//No puede estar sensibilizada con tiempo
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            assertArrayEquals(new boolean[]{true, true, true, false}, rdp1.getSensiArray());//Deberia estar dentro de la ventana
            try{
                Thread.sleep(6000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            assertArrayEquals(new boolean[]{true, false, true, false}, rdp1.getSensiArray());//Deberia estar fuera de la ventana

        }catch (InvariantException e){
            e.printInfo();
        }
    }
}