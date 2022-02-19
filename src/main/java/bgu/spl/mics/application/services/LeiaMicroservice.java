package bgu.spl.mics.application.services;


import bgu.spl.mics.Future;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
    private Attack[] attacks;
    private Future[] attacksSolution;

    /**
     * Constructor
     * @param attacks - an arraty of the attacks that need to be done.
     */
    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
        this.attacks = attacks;
        this.attacksSolution = new Future[attacks.length];
    }

    /**
     * In this method Leia creates a list of AttackEvents from the Attacks she get, and send them to
     * HanSoloMicroservice and C3POMicroservice.
     * After she gets the results for all the AttackEvents (the method get of the object Future is blacking until the event gets a result)
     * leia sent the DeactivationEvent. After she gets the result, she send the BombDestroyerEvent.
     */

    @Override
    protected void initialize() {
        Diary diary = Diary.getInstance();
        subscribeBroadcast(TerminateBroadcast.class,(TerminateBroadcast)->{diary.setLeiaTerminate(System.currentTimeMillis());
            this.terminate();});
        for (int i = 0; i < attacks.length; i = i + 1) {
            AttackEvent a = new AttackEvent(attacks[i]);
            attacksSolution[i] = this.sendEvent(a);
        }
        for (int i = 0; i < attacksSolution.length; i = i + 1){
            attacksSolution[i].get();
        }
        Future deactivationIsDone = sendEvent(new DeactivationEvent());
        deactivationIsDone.get();
        Future bombDestroyerIsDone = sendEvent(new BombDestroyerEvent()) ;
        bombDestroyerIsDone.get();
        sendBroadcast(new TerminateBroadcast());
    }

}
