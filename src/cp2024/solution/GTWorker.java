package cp2024.solution;

import cp2024.circuit.CircuitNode;
import cp2024.circuit.ThresholdNode;

import java.util.concurrent.atomic.AtomicInteger;
import cp2024.demo.util.Log;

public final class GTWorker extends Worker{
    private final AtomicInteger cntTrue, cntAll;
    public GTWorker(CircuitNode node, Worker parent) {
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
        Log.LOG.log(toString() + " gets " + childRes  + " (thr: " + threshold+")");
        if(childRes){
            int curr = cntTrue.incrementAndGet();
            if(curr > threshold){
                finishAndSubmit(true);
                return;
            }
        }else{
            if(cntTrue.get() + nChildren - cntAll.get() <= threshold){
                finishAndSubmit(false);
                return;
            }
        }
        if(all == nChildren){
            finishAndSubmit(cntTrue.intValue() > threshold);
        }
    }
}
