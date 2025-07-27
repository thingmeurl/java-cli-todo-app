package task;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    @Test
    public void testIsDoneInitiallyFalse() {
        Task task = new Task("テスト",false,Priority.HIGH, LocalDate.of(2025,7,20));
        assertFalse(task.isDone());
    }
    @Test
    public void testToggleDone(){
        Task task = new Task("テスト",false,Priority.MEDIUM,LocalDate.parse("2025-07-25"));
        task.toggleDone();
        assertTrue(task.isDone());
        task.toggleDone();
        assertFalse((task.isDone()));
    }
    @Test
    public void testIsDueToday(){
        Task task = new Task("今日のタスク", false, Priority.LOW, LocalDate.now());
        assertTrue(task.isDueToday());
    }
}