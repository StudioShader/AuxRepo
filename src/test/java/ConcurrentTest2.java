import java.util.Iterator;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Stepan on 14.04.17.
 */
public class ConcurrentTest2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        SetImpl<Integer> lockFreeSet = new SetImpl<Integer>();

        Future future1 = executor.submit(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " start");

            for (int i = 0; i < 10; i++) {
                lockFreeSet.add(i);

                if (i % 1 == 0){
                    System.out.println(i);
                }
            }
        });


//        Future future2 = executor.submit(() -> {
//            String threadName = Thread.currentThread().getName();
//            System.out.println(threadName + " start");
//
//            for (int i = 10000-1; i >= 0; i--) {
//                lockFreeSet.remove(i);
//                if (i % 10000 == 0){
//                    System.out.println(i);
//                }
//            }
//        });

        Future future3 = executor.submit(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " start");
            Iterator<Integer> my_iterator = lockFreeSet.iterator();
            int i = 0;
            while(my_iterator.hasNext()){
                i += 1;
                System.out.println(my_iterator.next());
            }
            System.out.println("SnapshotLength: " + i);
        });


        future1.get();
//        future2.get();
//        future3.get();
        executor.shutdown();
    }
}