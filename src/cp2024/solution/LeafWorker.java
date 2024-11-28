package cp2024.solution;

import cp2024.circuit.CircuitNode;

public final class LeafWorker extends Worker {
    public LeafWorker(CircuitNode node, Worker parent) {
        super(node, parent);
    }

    @Override
    @SynchronizedImplementation
    protected synchronized void receiveRes(CircuitNode whichChild, boolean childRes) {
        throw new IllegalStateException("Lisc nie moze miec dzieci");
    }
}
