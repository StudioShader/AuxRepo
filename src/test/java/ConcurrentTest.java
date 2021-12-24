import java.util.Iterator;

public class ConcurrentTest {

    public static void main(String[] args) throws InterruptedException {

        SetImpl<Integer> lockFreeSet = new SetImpl<>();
        lockFreeSet.add(1);
        lockFreeSet.add(2);
        lockFreeSet.remove(1);
        Iterator<Integer> my_iterator = lockFreeSet.iterator();
        int i = 0;
        while(my_iterator.hasNext()){
            i += 1;
            System.out.println(my_iterator.next());
        }
        System.out.println("SnapshotLength: " + i);
//        Runnable task1 = () -> {
//            System.out.println(Thread.currentThread().getName() + " start");
//            for (int i = 0; i < 10; i++) {
//                lockFreeSet.add(i);
//            }
//
//        };
//
//        Runnable task2 = () -> {
//            System.out.println(Thread.currentThread().getName() + " start");
//            for (int i = 10; i < 20; i++) {
//                lockFreeSet.add(i);
//            }
//        };
//
//
//        Thread thread1 = new Thread(task1);
//        Thread thread2 = new Thread(task2);
//
//        thread1.start();
//
//        System.out.println("after thread 1 start");
////        lockFreeSet.print();
//
//        thread2.start();
//        System.out.println("after thread 2 start");
////        lockFreeSet.print();
//        thread1.join();
//
//        thread2.join();


//        lockFreeSet.print();
    }
}