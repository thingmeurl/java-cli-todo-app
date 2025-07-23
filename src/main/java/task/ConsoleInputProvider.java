package task;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.locks.StampedLock;

public class ConsoleInputProvider implements InputProvider {
    private final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

    @Override
    public String nextLine(){
        return scanner.nextLine();
    }

    public void close(){
        scanner.close();
    }
}
