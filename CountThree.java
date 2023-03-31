import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class CountThree {

    public static int NUM_RUNS = 100;

    /**
     * Promenlivata koja treba da go sodrzi brojot na pojavuvanja na elementot 3
     */
    int count = 0;

    /**
     * Semaphores for synchronizing access to the shared count variable
     */
    Semaphore mutex = new Semaphore(1);

    /**
     * TODO: definirajte gi potrebnite elementi za sinhronizacija
     */

    public void init() {
    }

    class Counter extends Thread {

        private int[] data;

        public Counter(int[] data) {
            this.data = data;
        }

        @Override
        public void run() {
            try {
                count(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void count(int[] data) throws InterruptedException {
            int localCount = 0;
            for (int i = 0; i < data.length; i++) {
                if (data[i] == 3) {
                    localCount++;
                }
            }
            // Synchronize access to the shared count variable using the mutex semaphore
            mutex.acquire();
            count += localCount;
            mutex.release();
        }
    }

    public static void main(String[] args) {
        try {
            CountThree environment = new CountThree();
            environment.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void start() throws Exception {

        init();

        HashSet<Thread> threads = new HashSet<Thread>();
        Scanner s = new Scanner(System.in);
        int total = s.nextInt();

        for (int i = 0; i < NUM_RUNS; i++) {
            int[] data = new int[total];
            for (int j = 0; j < total; j++) {
                data[j] = s.nextInt();
            }
            Counter c = new Counter(data);
            threads.add(c);
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println(count);

    }
}