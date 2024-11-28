package cp2024.solution;

import cp2024.circuit.CircuitNode;

import java.util.concurrent.atomic.AtomicInteger;

public final class OrWorker extends Worker{

    private final AtomicInteger cntAll;

    public OrWorker(CircuitNode node, Worker parent) {
        super(node, parent);
        this.cntAll = new AtomicInteger(0);
    }

    @Override
    @SynchronizedImplementation
    protected synchronized void receiveRes(CircuitNode whichChild, boolean childRes) {
        int all = cntAll.incrementAndGet();
        if(childRes){
            finishAndSubmit(true);
        }
        else if(all == nChildren){
            finishAndSubmit(false);
        }
    }
}
