package cp2024.solution;

import cp2024.circuit.CircuitSolver;
import cp2024.circuit.CircuitValue;
import cp2024.circuit.Circuit;
import cp2024.demo.util.Log;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelCircuitSolver implements CircuitSolver {

    private final ArrayList<ParallelCircuitValue> values;
    private final AtomicBoolean killed;

    public ParallelCircuitSolver() {
        this.values = new ArrayList<>();
        this.killed = new AtomicBoolean(false);
    }

    @Override
    public synchronized CircuitValue solve(Circuit c) {
        if (!killed.get()) {
            Worker root = Worker.createWorker(c.getRoot(), null);
            ParallelCircuitValue result = new ParallelCircuitValue(root);
            result.startCalculations();
            values.add(result);
            return result;
        } else {
            return new BrokenCircuitValue();
        }
    }

    @Override
    public synchronized void stop() {
        if (killed.get()) {
            throw new IllegalStateException("Juz zakonczono obliczenia");
        }
        killed.set(true);
        for (ParallelCircuitValue val : values) {
            val.kill();
        }
    }

    public static class ParallelCircuitValue implements CircuitValue {
        private final Worker rootWorker;
        private final AtomicBoolean finishedTooEarly;

        public ParallelCircuitValue(Worker rootWorker) {
            this.rootWorker = rootWorker;
            this.finishedTooEarly = new AtomicBoolean(false);
        }

        private synchronized void startCalculations() {
            rootWorker.start();
        }

        private synchronized void kill() {
            if (rootWorker.isFinished()) {
                finishedTooEarly.set(false);
            } else {
                finishedTooEarly.set(true);
                rootWorker.finish();
            }
        }

        @Override
        public boolean getValue() throws InterruptedException {
            Log.LOG.log("Waiting until workers finish");
            if (finishedTooEarly.get()) {
                throw new InterruptedException();
            }
            rootWorker.blockUntilFinished();
            return rootWorker.getRes();
        }
    }

    public static class BrokenCircuitValue implements CircuitValue {
        @Override
        public synchronized boolean getValue() throws InterruptedException {
            throw new InterruptedException();
        }
    }


}
