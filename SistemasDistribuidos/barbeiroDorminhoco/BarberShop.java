package barbeiroDorminhoco;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BarberShop {
    private final Lock lock = new ReentrantLock();
    private final Condition clienteChegou = lock.newCondition();
    private final Condition clienteProntoCortar = lock.newCondition();
    private final Condition cortePronto = lock.newCondition();

    private final Queue<Condition> esperandoQueue = new LinkedList<>();
    private final int chairs;
    private int esperando = 0;
    private boolean isClienteSentado = false;

    public BarberShop(int chairs){
        this.chairs = chairs;
    }

    public boolean EntrarEsperar() throws InterruptedException {
        lock.lock();
        try {
            if (esperando == chairs) {
                return false;
            }
            Condition myCondition = lock.newCondition();
            esperandoQueue.add(myCondition);
            esperando++;

            clienteChegou.signal();

            myCondition.await();

            isClienteSentado = true;
            clienteProntoCortar.signal();

            cortePronto.await();

            return true;
        }finally {
            lock.unlock();
        }
    }

    public void chamarProximoClienteCortar() throws InterruptedException{
        lock.lock();
        try {
            while (esperando == 0) {
                System.out.println("sem clientes, dormindo...");
                clienteChegou.await();
            }

            Condition proximoClienteCond = esperandoQueue.remove();
            esperando--;

            proximoClienteCond.signal();

            while(!isClienteSentado) {
                clienteProntoCortar.await();
            }

            isClienteSentado = false;

        }finally {
            lock.unlock();
        }

        fazerCorte();
        
        lock.lock();
        try {
            cortePronto.signal();

        } finally {
            lock.unlock();
        }
    }

    private void fazerCorte() {
        System.out.println("Cortando cabelo...");
        try {
            Thread.sleep(1500);
        }catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Corte concluido.");
    }
}
