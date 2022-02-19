package bgu.spl.mics.application.passiveObjects;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {

    private AtomicInteger totalAttacks;
    private long HanSoloFinish;
    private long C3POFinish;
    private long R2D2Deactivate;
    private long LeiaTerminate;
    private long HanSoloTerminate;
    private long C3POTerminate;
    private long R2D2Terminate;
    private long LandoTerminate;

    /**
     * Diary will be a singleton object
     */
    private static class SingletonHolder {
        private static Diary instance = new Diary();
    }

    private Diary()
    {
        totalAttacks = new AtomicInteger(0);
        HanSoloFinish = 0;
        C3POFinish = 0;
        R2D2Deactivate = 0;
        LeiaTerminate = 0;
        HanSoloTerminate = 0;
        C3POTerminate = 0;
        R2D2Terminate = 0;
        LandoTerminate = 0;
    }

    public static Diary getInstance(){
        return Diary.SingletonHolder.instance;
    }

    /**
     * Getters for all the private fields
     */

    public int getTotalAttacks () {
        return  this.totalAttacks.intValue();
    }

    public long getHanSoloFinish () {
        return this.HanSoloFinish;
    }

    public long getC3POFinish () {
        return this.C3POFinish;
    }

    public long getR2D2Deactivate () {
        return this.R2D2Deactivate;
    }

    public long getLeiaTerminate () {
        return this.LeiaTerminate;
    }

    public long getHanSoloTerminate () {return this.HanSoloTerminate;}

    public long getC3POTerminate () {
        return this.C3POTerminate;
    }

    public long getR2D2Terminate () {
        return this.R2D2Terminate;
    }

    public long getLandoTerminate () {
        return this.LandoTerminate;
    }

    /*
    *Setters for all the private fields
     */


    public void IncrementTotalAttacks() {
        this.totalAttacks.incrementAndGet();
    }

    public void setHanSoloFinish(long hanSoloFinsh) {
        HanSoloFinish = hanSoloFinsh;
    }

    public void setC3POFinish(long c3POFinish) {
        C3POFinish = c3POFinish;
    }

    public void setR2D2Deactivate(long r2D2Deactivate) {
        R2D2Deactivate = r2D2Deactivate;
    }

    public void setLeiaTerminate(long leiaTerminate) {
        LeiaTerminate = leiaTerminate;
    }

    public void setHanSoloTerminate(long hanSoloTerminate) {
        HanSoloTerminate = hanSoloTerminate;
    }

    public void setC3POTerminate(long c3POTerminate) {
        C3POTerminate = c3POTerminate;
    }

    public void setR2D2Terminate(long r2D2Terminate) {
        R2D2Terminate = r2D2Terminate;
    }

    public void setLandoTerminate(long landoTerminate) {
        LandoTerminate = landoTerminate;
    }
}
