package bgu.spl.mics.application.services;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {
    private CountDownLatch setUp;

    /**
     * Constructor
     * @param setUp - the CountDownLatch common parameter.
     */

    public HanSoloMicroservice(CountDownLatch setUp) {
        super("Han");
        this.setUp=setUp;
    }

    /**
     * Subscribe HanSoloMicroservice with the events and broadcasts he should get and with the callback of each message.
     */
    @Override
    protected void initialize() {
        Ewoks ewoks = Ewoks.getInstance();
        Diary diary = Diary.getInstance();
        subscribeEvent(AttackEvent.class, (AttackEvent a)->{List <Integer> e = a.attack.getSerials();
                                                            ewoks.getResources(e);
                                                            try{
                                                                Thread.sleep (a.attack.getDuration());
                                                                this.complete(a,true);
                                                                diary.setHanSoloFinish(System.currentTimeMillis());
                                                                diary.IncrementTotalAttacks();
                                                                ewoks.releaseResources(e);
                                                            }
                                                            catch (InterruptedException i){}
                                                                    });
        subscribeBroadcast(TerminateBroadcast.class,(TerminateBroadcast)->{diary.setHanSoloTerminate(System.currentTimeMillis());
                                                                            this.terminate();});
        setUp.countDown();
    }
}
