package bgu.spl.mics;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class MessageBusImplTest
{
    private MessageBusImpl messageBus;

    private class stupidMicroService extends MicroService
    {
        public stupidMicroService ()
        {
            super("stupidMicroService");
            initialize();
        }

        protected void initialize()
        {
            messageBus.register(this);
        }
    }

    private class testEvent implements Event <Boolean>{}

    private class testBroadcast implements Broadcast {}

    @BeforeEach
    public void setUp ()
    {
        messageBus = MessageBusImpl.getInstance();
    }

    /*
    The methods register and unregister are not tested because they require access to private fields.
    Register is tested indirectly in the stupid microService initialize in each test
    (if register fails to function, the entire test would fail).

    The methods subscribeEvent and subscribeBroadcast are tested indirectly in the sendEvent and sendBroadcast tests
    */

    @Test
    //checks if the complete method place the result value in the future object of the event
    public void testComplete() {
        stupidMicroService m = new stupidMicroService();
        testEvent e = new testEvent();
        m.subscribeEvent(testEvent.class,(Event)->{});
        Future <Boolean> result = messageBus.sendEvent(e);
        m.complete(e,true);
        assertTrue(result.get());
        assertTrue(result.isDone());
        messageBus.unregister(m);
    }

    @Test
    /*
    checks if after we subscribe a microService to a broadcast the microService gets the broadcast.
    This test also checks the method subscribeBroadcast because if this method will fail the whole test will fail
     */
    public void testSendBroadcast() {
        stupidMicroService m = new stupidMicroService();
        testBroadcast b = new testBroadcast();
        m.subscribeBroadcast(testBroadcast.class,(Broadcast)->{});
        messageBus.sendBroadcast(b);
        try {
            Message message = messageBus.awaitMessage(m);
            assertEquals(message,b);
        }
        catch (InterruptedException E) {assertFalse(true);}
        messageBus.unregister(m);
    }

    @Test
    /*
    checks if after we subscribe a microService to an event the microService gets the event.
    This test also checks the method subscribeEvent because if this method will fail the whole test will fail
     */
    public void testSendEvent() {
        stupidMicroService m = new stupidMicroService();
        testEvent e = new testEvent();
        m.subscribeEvent(testEvent.class,(Event)->{});
        messageBus.sendEvent(e);
        try {
            Message message = messageBus.awaitMessage(m);
            assertEquals(message,e);
        }
        catch (InterruptedException E) {assertFalse(true);}
        messageBus.unregister(m);
    }

    @Test
    //checks if after we send an event to a specific microService the awaitMessage method return the same message
    public void testAwaitMessage(){
        stupidMicroService m = new stupidMicroService();
        testEvent e = new testEvent();
        m.subscribeEvent(testEvent.class,(Event)->{});
        messageBus.sendEvent(e);
        try {
            Message message = messageBus.awaitMessage(m);
            assertEquals(message,e);
        }
        catch (InterruptedException E) {assertFalse(true);}
        messageBus.unregister(m);
    }
}