package barbeiroDorminhoco;

public class Cliente implements Runnable{
    private BarberShop shop;
    private final int id;

    public Cliente(BarberShop shop, int id) {
        this.shop = shop;
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("Clinte " + id + " chegou");
        try {
            boolean cortar = shop.EntrarEsperar();
            if(!cortar) {
                System.out.println("Cliente " +id+ " foi embora (sem cadeira).");
            }else {
                System.out.println("Cliente "+id+" saiu com o corte pronto.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Cliente "+id+" interrompido.");
        }
    }
}
