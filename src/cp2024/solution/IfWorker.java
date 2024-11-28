package cp2024.solution;

import cp2024.circuit.CircuitNode;

import java.util.concurrent.atomic.AtomicBoolean;

public final class IfWorker extends Worker{

    private final AtomicBoolean condition, resA, resB;
    private final AtomicBoolean readyCondition, readyA, readyB;

    public IfWorker(CircuitNode node, Worker parent) {
        super(node,parent);
        resA = new AtomicBoolean(false);
        resB = new AtomicBoolean(false);
        condition = new AtomicBoolean(false);
        readyCondition = new AtomicBoolean(false);
        readyA = new AtomicBoolean(false);
        readyB = new AtomicBoolean(false);
    }

    @Override
    @SynchronizedImplementation
    protected synchronized void receiveRes(CircuitNode whichChild, boolean childRes) {
        if(whichChild == childNodes[0]) {
            condition.set(childRes);
            readyCondition.set(true);
        }else if(whichChild == childNodes[1]) {
            resA.set(childRes);
            readyA.set(true);
        }else{
            resB.set(childRes);
            readyB.set(true);
        }
        if(readyCondition.get()){
            if(condition.get() && readyA.get()){
                finishAndSubmit(resA.get());
            }else if(!condition.get() && readyB.get()){
                finishAndSubmit(resB.get());
            }
        }
    }
}
