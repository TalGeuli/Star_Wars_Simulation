package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.concurrent.CountDownLatch;


/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
    private long duration;
    private CountDownLatch setUp;

    /**
     * Constructor
     * @param duration - the time it takes to R2D2 to execute the DeactivationEvent.
     * @param setUp - the CountDownLatch common parameter.
     */
    public R2D2Microservice(long duration, CountDownLatch setUp) {
        super("R2D2");
        this.duration=duration;
        this.setUp = setUp;
    }

    /**
     * Subscribe R2D2Microservice with the events and broadcasts he should get and with the callback of each message.
     */
    @Override
    protected void initialize() {
        Diary diary = Diary.getInstance();
        subscribeEvent(DeactivationEvent.class,(DeactivationEvent d)->{try{Thread.sleep(duration);
                                                                            diary.setR2D2Deactivate(System.currentTimeMillis());
                                                                            this.complete(d,true);
                                                                          }
                                                                        catch (InterruptedException e){}});
        subscribeBroadcast(TerminateBroadcast.class,(TerminateBroadcast)->{diary.setR2D2Terminate(System.currentTimeMillis());
                                                                            this.terminate();});
        setUp.countDown();
    }
}
