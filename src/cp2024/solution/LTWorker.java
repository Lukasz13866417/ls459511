package cp2024.solution;

import cp2024.circuit.CircuitNode;
import cp2024.circuit.ThresholdNode;

import java.util.concurrent.atomic.AtomicInteger;

public final class LTWorker extends Worker{
    private final AtomicInteger cntFalse, cntAll;
    public LTWorker(CircuitNode node, Worker parent) {
        super(node, parent);
        this.cntFalse = new AtomicInteger(0);
        this.cntAll = new AtomicInteger(0);
    }

    @Override
    @SynchronizedImplementation
    protected synchronized void receiveRes(CircuitNode whichChild, boolean childRes) {
        ThresholdNode thrNode = (ThresholdNode) node;
        int threshold = thrNode.getThreshold();
        int all = cntAll.incrementAndGet();
        if(childRes){
            if(cntAll.get() - cntFalse.get() >= threshold){
                finishAndSubmit(false);
                return;
            }
        }else{
            int curr = cntFalse.incrementAndGet();
            if(nChildren - curr  < threshold){
                finishAndSubmit(true);
                return;
            }
        }
        if(all == nChildren){
            finishAndSubmit(cntFalse.intValue() < threshold);
        }
    }
}
