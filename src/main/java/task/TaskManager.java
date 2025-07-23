package task;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TaskManager {
    private List<Task> tasks = new ArrayList<>();
    private static final String FILE_NAME = "task.txt";
    InputProvider input = new ConsoleInputProvider();

    public TaskManager() {
        tasks = new ArrayList<>();
        loadTasksFromFile();
    }

    public TaskManager(boolean skipLoading){
        tasks = new ArrayList<>();
        if (!skipLoading) {
            loadTasksFromFile();
        }
    }

    public void printTaskList() {
        System.out.println("\n あなたのタスク一覧");
        System.out.println("==================================================================");
        System.out.printf("%-4s%-8s%-20s%-10s%-12s\n", "#", "状態", "タイトル", "優先度", "締切");
        System.out.println("==================================================================");

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            String checkbox = task.isDone() ? "[✓]" : "[ ]";
            String dueDateStr = (task.getDueDate() != null) ? task.getDueDate().toString() : "なし";
            System.out.printf("%-4s%-8s%-20s%-10s%-12s\n", i + 1, checkbox, task.getTitle(), task.getPriority(), dueDateStr);
        }

        if (tasks.isEmpty()) {
            System.out.println("タスクがありません");
        }
        System.out.println("==================================================================");
    }

    public void addTask(InputProvider input) {
        System.out.print("タスクを入力してください：");
        String title = input.nextLine();

        System.out.print("このタスクは完了済みですか？（yes/no）：");
        String doneInput = input.nextLine();
        boolean isDone = doneInput.equalsIgnoreCase("yes");

        System.out.println("優先度を入力してください（例：高・中・低）：");
        String priority = input.nextLine();

        System.out.println("締切日を入力してください（例：2025-07-21、未入力でスキップ）：");
        String dueDateInput = input.nextLine();
        LocalDate dueDate = null;
        if(!dueDateInput.isBlank()){
            try {
                dueDate = LocalDate.parse(dueDateInput);
            } catch (Exception e) {
                System.out.println("日付の形式が不正です。締切なしとして登録します。");
            }
        }

        tasks.add(new Task(title, isDone, priority, dueDate));
        saveTasksToFile();
        System.out.println("タスクを追加しました！");
    }

    //テスト用メソッド
    public void addTask(Task task) {
        tasks.add(task);
    }
    public void removeTask(int index) {
        if(index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundsException("無効なインデックスです：" + index);
        }
        tasks.remove(index);
    }

    public void updateTask(int index, Task updateTask){
        if(index >= 0 && index < tasks.size()){
            tasks.set(index, updateTask);
        } else {
            System.out.println("指定されたタスク番号が無効です");
        }
    }

    public List<Task> getTasks() { return tasks; }


    public void deleteTask(InputProvider input) {
        if (tasks.isEmpty()) {
            System.out.println("削除するタスクがありません。");
            return;
        }

        System.out.println("\n=== 削除対象のタスク一覧 ===");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i).getTitle());
        }

        System.out.println("削除したタスクの番号を入力してください：");
        String numInput = input.nextLine();

        try {
            int index = Integer.parseInt(numInput) - 1;
                removeTask(index);
                saveTasksToFile();
                System.out.println("削除しました。");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("無効な番号です。");
            } catch (NumberFormatException e) {
                System.out.println("数字を入力してください。");
        }
    }

    public void toggleTaskStatus(InputProvider input) {
        if (tasks.isEmpty()){
            System.out.println("完了フラグを切り替えるタスクがありません。");
            return;
        }

        System.out.println("\n=== 完了フラグを切り替えるタスク一覧===");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            String checkbox = task.isDone() ? "[✓]" : "[ ]";
            System.out.println((i + 1) + ". " + checkbox + " " + task.getTitle());
        }

        System.out.println("切り替えたいタスクの番号を入力してください：");
        String toggleInput = input.nextLine();

        try{
            int toggleIndex = Integer.parseInt(toggleInput) - 1;
            toggleTaskStatus(toggleIndex);
                System.out.println("タスク「" + tasks.get(toggleIndex).getTitle() + "」の完了を切り替えました。");
            } catch (IndexOutOfBoundsException e){
                System.out.println("無効な番号です。");
            } catch (NumberFormatException e) {
            System.out.println("数字を入力してください。");
        }
    }

    public void toggleTaskStatus(int index) {
        if(index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundsException("無効なインデックスです: " + index);
        }
        Task task = tasks.get(index);
        task.setDone(!task.isDone());
        saveTasksToFile();
    }

    // ファイル保存
    public void saveTasksToFile() {
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(FILE_NAME), StandardCharsets.UTF_8))){
            for (Task task : tasks) {
                writer.println(task.toCSV());
            }
        } catch (IOException e) {
            System.out.println("ファイル保存中にエラーが発生しました" + e.getMessage());
        }
    }

    // ファイル読み込み
    public void loadTasksFromFile(){
        tasks.clear();
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(FILE_NAME), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task.fromCSV(line).ifPresent(tasks::add);
            }
        } catch (IOException e) {
            System.out.println("ファイル読み込み中にエラーが発生しました" + e.getMessage());
        }
    }

    public void notifyDueTasks(){
        LocalDate today = LocalDate.now();
        boolean hasDueTasks = false;

        System.out.println("\n 締切が今日または過去のタスク:");
        System.out.println("======================================");

        for (Task task : tasks) {
            if(!task.isDone() && task.getDueDate() != null) {
                if(!task.getDueDate().isAfter(today)) {
                    System.out.printf("📌 %s（締切: %s）\n", task.getTitle(), task.getDueDate());
                    hasDueTasks = true;
                }
            }
        }

        if(!hasDueTasks) {
            System.out.println("📭 締切の近いタスクはありません。");
        }

        System.out.println("======================================\n");
    }

}
