package task;

import java.util.*;

public class MockInputProvider implements InputProvider {
    private Queue<String> inputs;

    public MockInputProvider(String... inputs) {
        this.inputs = new LinkedList<>(Arrays.asList(inputs));
    }

    @Override
    public String nextLine() {
        return inputs.poll();
    }

    @Override
    public void close() {
    }
}
