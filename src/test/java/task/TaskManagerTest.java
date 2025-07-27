package task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static task.Priority.*;
import java.time.LocalDate;
import java.util.List;

//import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new TaskManager(true);
    }

//    @Test
//    void testAddTask() {
//        Task task = new Task("テストタスク", false, "中", LocalDate.of(2025, 7, 22));
//        taskManager.addTask(task);
//        List<Task> tasks = taskManager.getTasks();
//        assertEquals(1, tasks.size());
//        assertEquals("テストタスク", tasks.get(0).getTitle());
//    }

    @Test
    public void testAddTask() {
        MockInputProvider mock = new MockInputProvider("テストタスク","no","中","2025-07-25");

        taskManager.addTask(mock);
        List<Task> tasks = taskManager.getTasks();

        assertEquals(1, tasks.size());
        assertFalse(tasks.get(0).isDone());
        assertEquals("テストタスク",tasks.get(0).getTitle());
        assertEquals(MEDIUM,tasks.get(0).getPriority());
        assertEquals(LocalDate.of(2025,7,25),tasks.get(0).getDueDate());

    }

    @Test
    void testAddMultipleTasks() {
        Task task1 = new Task("タスク1", false, Priority.HIGH,LocalDate.now());
        Task task2 = new Task("タスク2", true, Priority.MEDIUM,LocalDate.now().plusDays(1));
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        List<Task> tasks = taskManager.getTasks();
        assertEquals(2,tasks.size());
        assertEquals("タスク1",tasks.get(0).getTitle());
        assertEquals("タスク2",tasks.get(1).getTitle());
    }
    @Test
    void testRemoveTask() {
        Task task = new Task("削除対象", false, Priority.LOW,LocalDate.now());
        taskManager.addTask(task);
        taskManager.removeTask(0);

        List<Task> tasks = taskManager.getTasks();
        assertEquals(0,tasks.size());
    }
    @Test
    void testUpdateTask(){
        Task task = new Task("元タイトル",false,Priority.LOW,LocalDate.now());
        taskManager.addTask(task);

        Task updateTask = new Task("更新済タイトル",true,Priority.HIGH,LocalDate.now().plusDays(5));
        taskManager.updateTask(0, updateTask);

        Task result = taskManager.getTasks().get(0);
        assertEquals("更新済タイトル",result.getTitle());
        assertEquals(true,result.isDone());
        assertEquals(HIGH,result.getPriority());
    }

    @Test
    void testGetTaskWhenEmpty(){
        List<Task> tasks = taskManager.getTasks();
        assertTrue(tasks.isEmpty());
    }

    @Test
    void testRemoveTaskWithInvalidIndex(){
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskManager.removeTask(99);
        });
    }

    @Test
    void testDeleteTask(){
        taskManager.addTask(new Task("削除テスト",false,Priority.HIGH,LocalDate.now()));
        assertEquals(1, taskManager.getTasks().size());

        MockInputProvider input = new MockInputProvider("1");
        taskManager.deleteTask(input);

        assertEquals(0,taskManager.getTasks().size());
    }

    @Test
    void testDeleteTaskInvalidIndex() {
        taskManager.addTask(new Task("削除タスク",false,Priority.HIGH,LocalDate.now()));
        MockInputProvider input = new MockInputProvider("5"); // 存在しない番号
        taskManager.deleteTask(input);
        assertEquals(1,taskManager.getTasks().size()); // 削除されていないことを確認
    }
}
