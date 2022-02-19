package bgu.spl.mics.application.passiveObjects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EwokTest {

    private Ewok ewok;

    @BeforeEach
    public void setUp(){
        ewok = new Ewok(1);
    }

    @Test
    public void testAcquire(){
        assertTrue(ewok.available);
        ewok.acquire();
        assertFalse(ewok.available);

    }

    @Test
    public void testRelease ()
    {
        ewok.acquire();
        assertFalse(ewok.available);
        ewok.release();
        assertTrue(ewok.available);
    }

}