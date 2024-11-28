package cp2024.solution;

import cp2024.circuit.CircuitNode;

public final class NotWorker extends Worker{
    public NotWorker(CircuitNode node, Worker parent) {
        super(node, parent);
    }

    @Override
    @SynchronizedImplementation
    protected synchronized void receiveRes(CircuitNode whichChild, boolean childRes) {
        finishAndSubmit(!childRes);
    }
}
