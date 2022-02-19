package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.MessageBusImpl;

import java.util.Iterator;
import java.util.Vector;
import java.util.List;


/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    public Vector<Ewok> ewoks;

    /**
     * Ewok will be a singleton object
     */
    private static class SingletonHolder {
        private static Ewoks instance = new Ewoks();
    }

    private Ewoks(){
        ewoks = new Vector<Ewok>();
    }

    public static Ewoks getInstance(){
        return Ewoks.SingletonHolder.instance;
    }

    /**
     * Initialize the ewoks singelton
     * @param ewoks - the number of ewoks we get.
     */

    public void initialize (int ewoks){
        this.ewoks.setSize(ewoks+1);
        for (int i = 1; i<this.ewoks.size(); i=i+1){
            this.ewoks.set(i,new Ewok(i));
        }
    }

    /**
     * checks if an ewok is available.
     * @param index - represent the ewok we want to check if is available.
     * @return true/false depends the ewok status.
     */
    public boolean available (int index){
        return ewoks.get(index).available;
    }

    /**
     * this method try to get all the ewoks the microservice need to execute the Attackevent.
     * this method is blocking. that means that if the microservice can't get all the ewoks he need,
     * he waits until they are all available
     * @param a - list of the ewoks the microservice need for the AttackEvent
     */

    public void getResources (List <Integer> a){

        Iterator <Integer> iter = a.iterator();
        synchronized (this) {
            while (iter.hasNext()) {
                int currEwok = iter.next();
                while (!available(currEwok)) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        iter = a.iterator();
                    }
                }
                ewoks.get(currEwok).acquire();
            }
        }
    }

    /**
     * This method release ewoks after the microservice finish his AttackEvent.
     * @param a - list of ewoks the microservice used and want to release.
     */

    public void releaseResources (List <Integer> a)
    {
        Iterator <Integer> iter = a.iterator();
        synchronized (this) {
            while (iter.hasNext()) {
                ewoks.get(iter.next()).release();
            }
            notifyAll();
        }
    }
}
