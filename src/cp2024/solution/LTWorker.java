package cp2024.solution;

import cp2024.circuit.CircuitNode;
import cp2024.circuit.ThresholdNode;

import java.util.concurrent.atomic.AtomicInteger;

public final class LTWorker extends Worker{
    private final AtomicInteger cntTrue, cntAll;
    public LTWorker(CircuitNode node, Worker parent) {
        super(node, parent);
        this.cntTrue = new AtomicInteger(0);
        this.cntAll = new AtomicInteger(0);
    }

    @Override
    @SynchronizedImplementation
    protected synchronized void receiveRes(CircuitNode whichChild, boolean childRes) {
        ThresholdNode thrNode = (ThresholdNode) node;
        int threshold = thrNode.getThreshold();
        int all = cntAll.incrementAndGet();
        if(childRes){
            int curr = cntTrue.incrementAndGet();
            if(curr >= threshold){
                finishAndSubmit(false);
                return;
            }
        }
        if(all == nChildren){
            finishAndSubmit(cntTrue.intValue() < threshold);
        }
    }
}
