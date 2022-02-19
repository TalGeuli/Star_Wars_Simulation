package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.concurrent.CountDownLatch;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {

    private long duration;
    private CountDownLatch setUp;

    /**
     * Constructor
     * @param duration - the time it takes to R2D2 to execute the BombDestroyerEvent.
     * @param setUp -the CountDownLatch common parameter.
     */
    public LandoMicroservice(long duration,CountDownLatch setUp) {
        super("Lando");
        this.duration=duration;
        this.setUp=setUp;
    }

    /**
     *    Subscribe LandoMicroservice with the events and broadcasts he should get and with the callback of each message.
     */
    @Override
    protected void initialize() {
        Diary diary = Diary.getInstance();
        subscribeEvent(BombDestroyerEvent.class,(BombDestroyerEvent d)->{try{Thread.sleep(duration); this.complete(d,true);}catch (InterruptedException e){}});
        subscribeBroadcast(TerminateBroadcast.class,(TerminateBroadcast)->{diary.setLandoTerminate(System.currentTimeMillis());
                                                                            this.terminate();});
        setUp.countDown();
    }
}
