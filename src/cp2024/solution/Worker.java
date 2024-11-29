package cp2024.solution;

import cp2024.circuit.CircuitNode;
import cp2024.circuit.LeafNode;
import cp2024.circuit.NodeType;
import cp2024.demo.util.CircuitPrinter;
import cp2024.demo.util.Log;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public sealed abstract class Worker extends Thread permits AndWorker, OrWorker, GTWorker, LTWorker, LeafWorker, NotWorker, IfWorker {

    private final Worker parent;
    private Worker[] children;
    protected int nChildren;

    protected final CircuitNode node;
    protected CircuitNode[] childNodes;

    private final AtomicBoolean res, isFinished, childrenFound;

    private final Semaphore finishSemaphore;

    String str;

    public Worker(CircuitNode node, Worker parent) {
        this.node = node;
        this.parent = parent;
        this.res = new AtomicBoolean(false);
        this.isFinished = new AtomicBoolean(false);
        this.childrenFound = new AtomicBoolean(false);
        str = CircuitPrinter.printCircuit(node);
        finishSemaphore = new Semaphore(0,true);
    }

    @SynchronizedImplementation
    protected abstract void receiveRes(CircuitNode whichChild, boolean childRes);

    protected synchronized void finishAndSubmit(boolean res) {
        this.res.set(res);
        finish();
        //Log.LOG.log("Worker " + this + " has res " + res + ". Sending res to parent: " + parent);
        if (parent != null && !parent.isFinished.get()) {
            parent.receiveRes(node, res);
        }
    }

    private synchronized void findChildren() throws InterruptedException {
        if (!childrenFound.get()) {
            this.childNodes = node.getArgs();
            this.nChildren = childNodes.length;

            this.children = new Worker[nChildren];
            for (int i = 0; i < nChildren; i++) {
                children[i] = createWorker(childNodes[i], this);
                assert(children[i] != null);
            }
            childrenFound.set(true);
        }
    }

    /**
     * Konczy prace swoja i dzieci
     */
    public void finish() {
        if (!isFinished.get()) {
            isFinished.set(true);
            Log.LOG.log(toString() + " started finishing.");
            if (childrenFound.get()) {
                for (Worker child : children) {
                    if (!child.isFinished.get()) {
                        Log.LOG.log(toString() + " started finishing a child.");
                        child.finish();
                    }
                }
                interrupt();
            } // else { NIC } Jesli jeszcze nie znamy dzieci tego goscia,
            // ,to na pewno sie jeszcze nie uruchomily.
            Log.LOG.log(toString() + " finished. ");
            finishSemaphore.release();
        }

    }

    /**
     * Cale dzialanie watku-pracownika
     * Lisc jedynie przekazuje ojcu swoja wartosc
     * Inne watki najpierw zdobywaja informacje, jakie maja dzieci, potem kaza im pracowac.
     * Wkrotce do tego watku zaczna dochodzic informacje o wartosciach logicznych w dzieciach.
     */
    @Override
    public void run() {
        try {
            if (node.getType() == NodeType.LEAF) {
                LeafNode leaf = (LeafNode) node;
                boolean val = leaf.getValue();
                finishAndSubmit(val);
            } else {
                findChildren();
                for (Worker child : children) {
                    child.start();
                }
            }
        } catch (InterruptedException ie) {
            finish();
            if (parent != null) {
                parent.interrupt();
            }
        }
    }

    static Worker createWorker(CircuitNode node, Worker parent) {
        return switch (node.getType()) {
            case LEAF -> new LeafWorker(node, parent);
            case AND -> new AndWorker(node, parent);
            case OR -> new OrWorker(node, parent);
            case IF -> new IfWorker(node, parent);
            case NOT -> new NotWorker(node, parent);
            case GT -> new GTWorker(node, parent);
            case LT -> new LTWorker(node, parent);
        };
    }

    public void blockUntilFinished() throws InterruptedException {
        if(!isFinished.get()) {
            finishSemaphore.acquire();
        }
        Log.LOG.log(toString() + " finished ");
    }

    public boolean getRes() {
        if (!isFinished.get()) {
            throw new IllegalStateException("Obliczenia jeszcze sie nie skonczyly");
        }
        return res.get();
    }

    @Override
    public String toString() {
        return super.toString() + " " + str;
    }

    public boolean isFinished() {
        return isFinished.get();
    }
}
