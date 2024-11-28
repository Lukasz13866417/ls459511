package cp2024.demo;

import cp2024.circuit.*;
import cp2024.demo.util.Log;
import cp2024.solution.ParallelCircuitSolver;
// import cp2024.mp249046.ParallelCircuitSolver;
import java.time.Duration;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        long start = System.nanoTime();
        // CircuitSolver solver = new ParallelCircuitSolver();
        CircuitSolver solver = new SequentialSolver();

        Log.LOG.logging = false;
        Circuit c = new Circuit(CircuitNode.mk(true));
        System.out.println("Solved: " + solver.solve(c).getValue());

        c = new Circuit(CircuitNode.mk(false, Duration.ofSeconds(3)));
        CircuitValue firstValue = solver.solve(c);

        c = new Circuit(
                CircuitNode.mk(NodeType.GT, 2,
                        CircuitNode.mk(true),
                        CircuitNode.mk(false),
                        CircuitNode.mk(true),
                        CircuitNode.mk(false),
                        CircuitNode.mk(false, Duration.ofSeconds(3))));
        CircuitValue secondValue = solver.solve(c);

        System.out.println("Second value: " + secondValue.getValue());
        System.out.println("First value: " + firstValue.getValue());

        c = new Circuit(
                CircuitNode.mk(NodeType.IF,
                        CircuitNode.mk(true),
                        CircuitNode.mk(false),
                        CircuitNode.mk(true, Duration.ofSeconds(3))));
        CircuitValue thirdValue = solver.solve(c);
        System.out.println("Third value: " + thirdValue.getValue());

        solver.stop();

        c = new Circuit(CircuitNode.mk(true));
        try {
            System.out.println("Solver stopped, but solved fourth circuit: " + solver.solve(c).getValue());
        } catch (InterruptedException e) {
            System.out.println("Solver interrupted computation of the fourth circuit.");
        }
        System.out.println("Third value should be returned with no exception:");
        System.out.println("Third value snd time: " + thirdValue.getValue());

        System.out.println("End of demo");
        System.out.println(System.nanoTime() - start);
    }
}
