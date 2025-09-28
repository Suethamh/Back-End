package leituraEscrita;

import java.util.concurrent.locks.StampedLock;

class SharedData {
    private int value = 0;
    private final StampedLock lock = new StampedLock();

    public void write() {
        long stamp = lock.writeLock();
        try {
            value++;
            System.out.println(Thread.currentThread().getName() + " escreveu: " + value);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public void optimisticRead() {
        long stamp = lock.tryOptimisticRead();
        int currentValue = value;

        try { Thread.sleep(50); } catch (InterruptedException e) {}


        if (!lock.validate(stamp)) {

            System.out.println(Thread.currentThread().getName() + " falha na leitura otimista. Releitura...");
            stamp = lock.readLock();
            try {
                currentValue = value;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        System.out.println(Thread.currentThread().getName() + " leu: " + currentValue);
    }
}

