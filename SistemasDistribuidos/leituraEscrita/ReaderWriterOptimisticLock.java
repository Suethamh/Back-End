package leituraEscrita;

public class ReaderWriterOptimisticLock {
    public static void main(String[] args) {
        SharedData data = new SharedData();

        Thread writer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                data.write();
                try { Thread.sleep(100); } catch (InterruptedException e) {}
            }
        }, "Escritor");

        Runnable readerTask = () -> {
            for (int i = 0; i < 5; i++) {
                data.optimisticRead();
                try { Thread.sleep(80); } catch (InterruptedException e) {}
            }
        };

        Thread reader1 = new Thread(readerTask, "Leitor-1");
        Thread reader2 = new Thread(readerTask, "Leitor-2");

        writer.start();
        reader1.start();
        reader2.start();
    }
}
