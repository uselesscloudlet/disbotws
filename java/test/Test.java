//package test;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//public class Test {
//    public static void main(String[] args) {
//        Task task = new Task();
//        long delay1 = 1000, delay2 = 2, delay3 = 1, period1 = 2000, period2 = 3, period3 = 1;
//        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
////        executorService.scheduleAtFixedRate(Task::taskRecruit, delay1, period1, TimeUnit.MILLISECONDS);
////        executorService.scheduleAtFixedRate(task::taskHoliday, delay2, period2, TimeUnit.SECONDS);
////        executorService.scheduleAtFixedRate(() -> System.out.println("lambda"), delay3, period3, TimeUnit.MINUTES);
//        executorService.scheduleAtFixedRate(task::taskHoliday, 0, 3, TimeUnit.SECONDS);
//    }
//}
//// Task - статик, task - динамик
//class Task{
//    void taskHoliday() {
//        System.out.println("method " + LocalDateTime.now());
//    }
//    static void taskRecruit() {
//        System.out.println("static");
//    }
//}