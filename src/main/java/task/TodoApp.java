package task;
import java.util.Scanner;

// Todoアプリのエントリポイント
public class TodoApp {
    public static void main(String[] args) {
        InputProvider input = new ConsoleInputProvider();
        TaskManager taskManager = new TaskManager();

        try {
            runApp(taskManager, input);
        } finally {
            input.close();
        }
    }

//        taskManager.loadTasksFromFile();
//        System.out.println("================================");
//        System.out.println("         📋 Todo App 起動       ");
//        System.out.println("================================");
//        taskManager.notifyDueTasks();

     public static void runApp(TaskManager taskManager, InputProvider input) {
        while (true) {
            printMenu();
            System.out.println("選択してください（1～5）:");
            String choice = input.nextLine();

            switch (choice) {
                case "1":
                    taskManager.addTask(input);
                    break;
                case "2":
                    taskManager.
                            printTaskList();
                    break;
                case "3":
                    taskManager.deleteTask(input);
                    break;
                case "4":
                    taskManager.toggleTaskStatus(input);
                    break;
                case "5":
                    System.out.println("\nアプリを終了します。お疲れさまでした！");
                    input.close();
                    return;
                default:
                    System.out.println("無効な選択です。");
                    break;
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n------ メニュー ------");
        System.out.println("1. タスク追加");
        System.out.println("2. タスク一覧表示");
        System.out.println("3. タスク削除");
        System.out.println("4. 完了フラグ切り替え");
        System.out.println("5. アプリ終了");
        System.out.print("> ");
    }
}