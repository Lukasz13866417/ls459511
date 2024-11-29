package cp2024.demo;

import cp2024.circuit.*;
import cp2024.demo.util.CircuitParser;
import cp2024.demo.util.Log;
import cp2024.solution.ParallelCircuitSolver;

import java.time.Duration;

import static cp2024.circuit.NodeType.*;

public class MojeDemo {
    public static void main(String[] args) throws InterruptedException {

            long start = System.nanoTime();
            Log.LOG.logging = false;

            // PRZEPLOTY
        /*for(int i=0;i<100000;++i) {
            Circuit c = new Circuit(CircuitParser.parse("AND(OR(FALSE,TRUE),IF(TRUE,TRUE,FALSE))"));

            ParallelCircuitSolver solver = new ParallelCircuitSolver();

            CircuitValue val = solver.solve(c);
            assert (val.getValue() == true);
            Log.LOG.log("\n\n\n");
        }*/

            Circuit c = new Circuit(
                    CircuitNode.mk(NodeType.IF,
                            CircuitNode.mk(true),
                            CircuitNode.mk(false),
                            CircuitNode.mk(true, Duration.ofSeconds(3))));

            ParallelCircuitSolver solver = new ParallelCircuitSolver();
            SequentialSolver solver2 = new SequentialSolver();
            CircuitValue secondValue = solver.solve(c);
            System.out.println(secondValue.getValue());
            System.out.println((System.nanoTime() - start) / 1000000);
    }
}
