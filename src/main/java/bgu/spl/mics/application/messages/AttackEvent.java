package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;

/**
 * LeiaMicroservice creates those events and send them to HansoloMicroservice and C3POMicroservice
 * @param attack -  contains the data about the AttackEvent
 */
public class AttackEvent implements Event<Boolean> {
	public Attack attack;

	public  AttackEvent (Attack attack)
    {
        this.attack = attack;
    }
}
