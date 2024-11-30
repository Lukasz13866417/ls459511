package cp2024.test;

import cp2024.circuit.*;
import cp2024.solution.ParallelCircuitSolver;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Tests {
    @Test
    public void testBasic() throws InterruptedException {
        Message msg1 = new Message("testBasic");
        CircuitSolver solver = new ParallelCircuitSolver();
        Circuit c1 = new Circuit(CircuitNode.mk(true, Duration.ofSeconds(5)));
        Circuit c2= new Circuit(CircuitNode.mk(false, Duration.ofSeconds(2)));
        CircuitValue pcv1 = solver.solve(c1);
        CircuitValue pcv2 = solver.solve(c2);
        assertFalse(pcv2.getValue());
        msg1.sendFormattedMessage("~2000ms");
        assertTrue(pcv1.getValue());
        msg1.sendFormattedMessage("~5000ms");
    }

    @Test
    public void simpleNOTTest() throws InterruptedException {
        Message msg1 = new Message("simpleNOTTest");
        Circuit c1 = new Circuit(CircuitNode.mk(NodeType.NOT, CircuitNode.mk(true, Duration.ofSeconds(1))));
        CircuitSolver solver = new ParallelCircuitSolver();
        CircuitValue pcv1 = solver.solve(c1);
        CircuitValue pcv2 = solver.solve(c1);
        msg1.sendFormattedMessage("before");
        assertFalse(pcv1.getValue());
        assertFalse(pcv2.getValue());
        msg1.sendFormattedMessage("after");
    }

    @Test
    public void simpleANDTest() throws InterruptedException {
        Circuit c1 = new Circuit(CircuitNode.mk(NodeType.AND,
                CircuitNode.mk(true, Duration.ofSeconds(1)),
                CircuitNode.mk(true, Duration.ofSeconds(1)),
                CircuitNode.mk(true, Duration.ofSeconds(1))));
        Circuit c2 = new Circuit(CircuitNode.mk(NodeType.AND,
                CircuitNode.mk(true, Duration.ofSeconds(5)),
                CircuitNode.mk(true, Duration.ofSeconds(5)),
                CircuitNode.mk(false, Duration.ofSeconds(1))));
        CircuitSolver solver = new ParallelCircuitSolver();
        CircuitValue pcv1 = solver.solve(c1);
        Message msg1 = new Message("simpleANDTest1");
        assertTrue(pcv1.getValue());
        msg1.sendFormattedMessage("~1000");
        CircuitValue pcv2 = solver.solve(c2);
        Message msg2 = new Message("simpleANDTest2");
        assertFalse(pcv2.getValue());
        msg2.sendFormattedMessage("~1000");
    }

    @Test
    public void simpleORTest() throws InterruptedException {
        Circuit c1 = new Circuit(CircuitNode.mk(NodeType.OR,
                CircuitNode.mk(false, Duration.ofSeconds(1)),
                CircuitNode.mk(false, Duration.ofSeconds(1)),
                CircuitNode.mk(false, Duration.ofSeconds(1))));
        Circuit c2 = new Circuit(CircuitNode.mk(NodeType.OR,
                CircuitNode.mk(false, Duration.ofSeconds(5)),
                CircuitNode.mk(false, Duration.ofSeconds(5)),
                CircuitNode.mk(true, Duration.ofSeconds(1))));
        CircuitSolver solver = new ParallelCircuitSolver();
        CircuitValue pcv1 = solver.solve(c1);
        Message msg1 = new Message("simpleORTest1");
        assertFalse(pcv1.getValue());
        msg1.sendFormattedMessage("~1000ms");
        CircuitValue pcv2 = solver.solve(c2);
        Message msg2 = new Message("simpleORTest2");
        assertTrue(pcv2.getValue());
        msg2.sendFormattedMessage("~1000ms");
    }

    @Test
    public void simpleGTTest() throws InterruptedException {
        Circuit c1 = new Circuit(CircuitNode.mk(NodeType.GT, 2,
                CircuitNode.mk(false),
                CircuitNode.mk(true),
                CircuitNode.mk(true),
                CircuitNode.mk(true)));
        Circuit c2 = new Circuit(CircuitNode.mk(NodeType.GT, 2,
                CircuitNode.mk(false),
                CircuitNode.mk(true),
                CircuitNode.mk(false),
                CircuitNode.mk(true)));
        CircuitSolver solver = new ParallelCircuitSolver();
        CircuitValue pcv1 = solver.solve(c1);
        CircuitValue pcv2 = solver.solve(c2);
        assertTrue(pcv1.getValue());
        assertFalse(pcv2.getValue());

        Message msg3 = new Message("simpleGTTest1");
        Circuit c3 = new Circuit(CircuitNode.mk(NodeType.GT, 3,
                CircuitNode.mk(false, Duration.ofSeconds(1)),
                CircuitNode.mk(true, Duration.ofSeconds(5)),
                CircuitNode.mk(true, Duration.ofSeconds(5)),
                CircuitNode.mk(true, Duration.ofSeconds(5))));
        CircuitValue pcv3 = solver.solve(c3);
        assertFalse(pcv3.getValue());
        msg3.sendFormattedMessage("~1000ms");


        Message msg4 = new Message("simpleGTTest2");
        Circuit c4 = new Circuit(CircuitNode.mk(NodeType.GT, 2,
                CircuitNode.mk(false, Duration.ofSeconds(5)),
                CircuitNode.mk(true, Duration.ofSeconds(1)),
                CircuitNode.mk(true, Duration.ofSeconds(1)),
                CircuitNode.mk(true, Duration.ofSeconds(1)),
                CircuitNode.mk(true, Duration.ofSeconds(5)),
                CircuitNode.mk(true, Duration.ofSeconds(5))));
        CircuitValue pcv4 = solver.solve(c4);
        assertTrue(pcv4.getValue());
        msg4.sendFormattedMessage("~1000ms");
    }

    @Test
    public void simpleLTTest() throws InterruptedException {
        Circuit c1 = new Circuit(CircuitNode.mk(NodeType.LT, 2,
                CircuitNode.mk(false),
                CircuitNode.mk(false),
                CircuitNode.mk(true),
                CircuitNode.mk(true)));
        Circuit c2 = new Circuit(CircuitNode.mk(NodeType.LT, 2,
                CircuitNode.mk(false),
                CircuitNode.mk(false),
                CircuitNode.mk(false),
                CircuitNode.mk(true)));
        CircuitSolver solver = new ParallelCircuitSolver();
        CircuitValue pcv1 = solver.solve(c1);
        CircuitValue pcv2 = solver.solve(c2);
        assertFalse(pcv1.getValue());
        assertTrue(pcv2.getValue());

        Message msg3 = new Message("simpleLTTest1");
        Circuit c3 = new Circuit(CircuitNode.mk(NodeType.LT, 3,
                CircuitNode.mk(false, Duration.ofSeconds(1)),
                CircuitNode.mk(false, Duration.ofSeconds(1)),
                CircuitNode.mk(true, Duration.ofSeconds(5)),
                CircuitNode.mk(true, Duration.ofSeconds(5))));
        CircuitValue pcv3 = solver.solve(c3);
        assertTrue(pcv3.getValue());
        msg3.sendFormattedMessage("~1000ms");


        Message msg4 = new Message("simpleLTTest2");
        Circuit c4 = new Circuit(CircuitNode.mk(NodeType.LT, 2,
                CircuitNode.mk(false, Duration.ofSeconds(5)),
                CircuitNode.mk(true, Duration.ofSeconds(1)),
                CircuitNode.mk(true, Duration.ofSeconds(1)),
                CircuitNode.mk(true, Duration.ofSeconds(5)),
                CircuitNode.mk(true, Duration.ofSeconds(5))));
        CircuitValue pcv4 = solver.solve(c4);
        assertFalse(pcv4.getValue());
        msg4.sendFormattedMessage("~1000ms");
    }

    @Test
    public void simpleIFTest () throws InterruptedException {
        Circuit c1 = new Circuit(CircuitNode.mk(NodeType.IF,
                CircuitNode.mk(true),
                CircuitNode.mk(true),
                CircuitNode.mk(false)));
        Circuit c2 = new Circuit(CircuitNode.mk(NodeType.IF,
                CircuitNode.mk(true),
                CircuitNode.mk(false),
                CircuitNode.mk(true)));
        Circuit c3 = new Circuit(CircuitNode.mk(NodeType.IF,
                CircuitNode.mk(false),
                CircuitNode.mk(true),
                CircuitNode.mk(false)));
        Circuit c4 = new Circuit(CircuitNode.mk(NodeType.IF,
                CircuitNode.mk(false),
                CircuitNode.mk(false),
                CircuitNode.mk(true)));
        CircuitSolver solver = new ParallelCircuitSolver();

        assertTrue(solver.solve(c1).getValue());
        assertFalse(solver.solve(c2).getValue());
        assertFalse(solver.solve(c3).getValue());
        assertTrue(solver.solve(c4).getValue());

        Circuit c5 = new Circuit(CircuitNode.mk(NodeType.IF,
                CircuitNode.mk(true, Duration.ofSeconds(1)),
                CircuitNode.mk(true, Duration.ofSeconds(1)),
                CircuitNode.mk(false, Duration.ofSeconds(5))));
        Circuit c6 = new Circuit(CircuitNode.mk(NodeType.IF,
                CircuitNode.mk(false, Duration.ofSeconds(1)),
                CircuitNode.mk(true, Duration.ofSeconds(5)),
                CircuitNode.mk(false, Duration.ofSeconds(1))));
        Circuit c7 = new Circuit(CircuitNode.mk(NodeType.IF,
                CircuitNode.mk(true, Duration.ofSeconds(5)),
                CircuitNode.mk(true, Duration.ofSeconds(1)),
                CircuitNode.mk(true, Duration.ofSeconds(1))));
        Circuit c8 = new Circuit(CircuitNode.mk(NodeType.IF,
                CircuitNode.mk(false, Duration.ofSeconds(5)),
                CircuitNode.mk(true, Duration.ofSeconds(1)),
                CircuitNode.mk(true, Duration.ofSeconds(1))));

        Message msg1 = new Message("simpleIFTest1");
        assertTrue(solver.solve(c5).getValue());
        msg1.sendFormattedMessage("~1000ms");
        Message msg2 = new Message("simpleIFTest2");
        assertFalse(solver.solve(c6).getValue());
        msg2.sendFormattedMessage("~1000ms");
        Message msg3 = new Message("simpleIFTest3");
        assertTrue(solver.solve(c7).getValue());
        msg3.sendFormattedMessage("~1000ms");
        Message msg4 = new Message("simpleIFTest4");
        assertTrue(solver.solve(c8).getValue());
        msg4.sendFormattedMessage("~1000ms");
    }

    @Test
    public void testNOTLine () throws InterruptedException {
        Message msg1 = new Message("testNOTLine");
        Circuit c1 = new Circuit(CircuitNode.mk(NodeType.NOT,
                CircuitNode.mk(NodeType.NOT,
                        CircuitNode.mk(NodeType.NOT,
                                CircuitNode.mk(NodeType.NOT,
                                        CircuitNode.mk(true))))));
        CircuitSolver solver = new ParallelCircuitSolver();
        CircuitValue pcv1 = solver.solve(c1);
        msg1.sendFormattedMessage("before");
        assertTrue(pcv1.getValue());
        msg1.sendFormattedMessage("after");
    }

    @Test
    public void threadsNumberTest () throws InterruptedException {
        Circuit c1 = new Circuit(CircuitNode.mk(NodeType.AND,
                CircuitNode.mk(true, Duration.ofSeconds(5)),
                CircuitNode.mk(NodeType.AND,
                        CircuitNode.mk(true, Duration.ofSeconds(10)),
                        CircuitNode.mk(true, Duration.ofSeconds(10)),
                        CircuitNode.mk(NodeType.NOT,
                                CircuitNode.mk(NodeType.NOT,
                                        CircuitNode.mk(true, Duration.ofSeconds(10)))),
                        CircuitNode.mk(NodeType.NOT,
                                CircuitNode.mk(NodeType.OR,
                                        CircuitNode.mk(false, Duration.ofSeconds(5)),
                                        CircuitNode.mk(NodeType.GT, 3,
                                                CircuitNode.mk(true),
                                                CircuitNode.mk(true),
                                                CircuitNode.mk(true),
                                                CircuitNode.mk(false, Duration.ofSeconds(5)),
                                                CircuitNode.mk(false, Duration.ofSeconds(5)),
                                                CircuitNode.mk(false, Duration.ofSeconds(5)),
                                                CircuitNode.mk(false, Duration.ofSeconds(5)),
                                                CircuitNode.mk(NodeType.LT, 2,
                                                        CircuitNode.mk(true, Duration.ofSeconds(10)),
                                                        CircuitNode.mk(false, Duration.ofSeconds(1)),
                                                        CircuitNode.mk(false, Duration.ofSeconds(1)),
                                                        CircuitNode.mk(NodeType.IF,
                                                                CircuitNode.mk(true, Duration.ofSeconds(10)),
                                                                CircuitNode.mk(false, Duration.ofSeconds(1)),
                                                                CircuitNode.mk(false, Duration.ofSeconds(1))))))))));
        Message msg1 = new Message("simpleThreadsInterruptionTest");
        CircuitSolver solver = new ParallelCircuitSolver();
        CircuitValue pcv1 = solver.solve(c1);
        msg1.sendFormattedMessage("before = " + Thread.activeCount());
        assertFalse(pcv1.getValue());
        msg1.sendFormattedMessage("after = " + Thread.activeCount());
        sleep(1000);
        msg1.sendFormattedMessage("after2 = " + Thread.activeCount());
    }
    @Test
    public void threadsNumberTest2 () throws InterruptedException {
        Circuit c1 = new Circuit(CircuitNode.mk(NodeType.AND,
                CircuitNode.mk(true),
                CircuitNode.mk(NodeType.AND,
                        CircuitNode.mk(true),
                        CircuitNode.mk(true),
                        CircuitNode.mk(NodeType.NOT,
                                CircuitNode.mk(NodeType.NOT,
                                        CircuitNode.mk(true))),
                        CircuitNode.mk(NodeType.NOT,
                                CircuitNode.mk(NodeType.OR,
                                        CircuitNode.mk(false),
                                        CircuitNode.mk(NodeType.GT, 3,
                                                CircuitNode.mk(true),
                                                CircuitNode.mk(true),
                                                CircuitNode.mk(true),
                                                CircuitNode.mk(false),
                                                CircuitNode.mk(false),
                                                CircuitNode.mk(false),
                                                CircuitNode.mk(false),
                                                CircuitNode.mk(NodeType.LT, 2,
                                                        CircuitNode.mk(true),
                                                        CircuitNode.mk(false),
                                                        CircuitNode.mk(false),
                                                        CircuitNode.mk(NodeType.IF,
                                                                CircuitNode.mk(true),
                                                                CircuitNode.mk(false),
                                                                CircuitNode.mk(false)))))))));
        Message msg1 = new Message("simpleThreadsInterruptionTest");
        CircuitSolver solver = new ParallelCircuitSolver();
        CircuitValue pcv1 = solver.solve(c1);
        msg1.sendFormattedMessage("before = " + Thread.activeCount());
        assertFalse(pcv1.getValue());
        msg1.sendFormattedMessage("after = " + Thread.activeCount());
        sleep(1000);
        msg1.sendFormattedMessage("after2 = " + Thread.activeCount());
    }

    @Test
    public void stopTest() throws InterruptedException {
        Circuit c1 = new Circuit(CircuitNode.mk(NodeType.AND,
                CircuitNode.mk(NodeType.OR,
                        CircuitNode.mk(false, Duration.ofSeconds(5)),
                        CircuitNode.mk(false, Duration.ofSeconds(5)),
                        CircuitNode.mk(true, Duration.ofSeconds(5)),
                        CircuitNode.mk(false, Duration.ofSeconds(5))),
                CircuitNode.mk(NodeType.OR,
                        CircuitNode.mk(false, Duration.ofSeconds(5)),
                        CircuitNode.mk(false, Duration.ofSeconds(5)),
                        CircuitNode.mk(true, Duration.ofSeconds(5)),
                        CircuitNode.mk(false, Duration.ofSeconds(5))),
                CircuitNode.mk(NodeType.OR,
                        CircuitNode.mk(false, Duration.ofSeconds(5)),
                        CircuitNode.mk(false, Duration.ofSeconds(5)),
                        CircuitNode.mk(true, Duration.ofSeconds(5)),
                        CircuitNode.mk(false, Duration.ofSeconds(5)))));
        Circuit c2 = new Circuit(CircuitNode.mk(NodeType.AND,
                CircuitNode.mk(NodeType.OR,
                        CircuitNode.mk(false, Duration.ofSeconds(5)),
                        CircuitNode.mk(false, Duration.ofSeconds(5)),
                        CircuitNode.mk(true, Duration.ofSeconds(5)),
                        CircuitNode.mk(false, Duration.ofSeconds(5))),
                CircuitNode.mk(NodeType.OR,
                        CircuitNode.mk(false, Duration.ofSeconds(5)),
                        CircuitNode.mk(false, Duration.ofSeconds(5)),
                        CircuitNode.mk(true, Duration.ofSeconds(5)),
                        CircuitNode.mk(false, Duration.ofSeconds(5))),
                CircuitNode.mk(NodeType.OR,
                        CircuitNode.mk(false, Duration.ofSeconds(5)),
                        CircuitNode.mk(false, Duration.ofSeconds(5)),
                        CircuitNode.mk(true, Duration.ofSeconds(5)),
                        CircuitNode.mk(false, Duration.ofSeconds(5)))));
        CircuitSolver solver = new ParallelCircuitSolver();
        CircuitValue pcv1 = solver.solve(c1);
        CircuitValue pcv2 = solver.solve(c2);
        sleep(500);
        System.out.println(Thread.activeCount());
        solver.stop();
        sleep(1000);
        System.out.println(Thread.activeCount());

        boolean wasException = false;
        try {
            solver.solve(c1).getValue();
        } catch (InterruptedException e) {
            wasException = true;
        }
        assertTrue(wasException);
    }


//    @Test
//    public void interruptionTest () throws InterruptedException {
//        Circuit c1 = new Circuit(new BrokenLeafNode());
//        CircuitSolver solver = new ParallelCircuitSolver();
//        CircuitValue pcv1 = solver.solve(c1);
//        pcv1.getValue();
//    }
}