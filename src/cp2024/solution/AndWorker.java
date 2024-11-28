package cp2024.solution;

import cp2024.circuit.CircuitNode;
import cp2024.demo.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

public final class AndWorker extends Worker{

    private final AtomicInteger cntAll;

    public AndWorker(CircuitNode node, Worker parent) {
        super(node, parent);
        this.cntAll = new AtomicInteger(0);
    }

    @Override
    @SynchronizedImplementation
    protected synchronized void receiveRes(CircuitNode whichChild, boolean childRes) {
        int all = cntAll.incrementAndGet();
        Log.LOG.log(this + " received res " + childRes + ". Curr no. of finished children: "+all);
        if(!childRes){
            finishAndSubmit(false);
        }
        else if(all == nChildren){
            finishAndSubmit(true);
        }
    }
}
