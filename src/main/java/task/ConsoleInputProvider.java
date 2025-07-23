package task;

import java.util.Scanner;

public class ConsoleInputProvider implements InputProvider {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String nextLine(){
        return scanner.nextLine();
    }

    public void close(){
        scanner.close();
    }
}
