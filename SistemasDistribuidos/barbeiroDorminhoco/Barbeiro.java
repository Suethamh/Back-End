package barbeiroDorminhoco;

public class Barbeiro implements Runnable{
    private final BarberShop shop;
    private volatile boolean open = true;

    public Barbeiro(BarberShop shop){
        this.shop = shop;
    }

    public void fecharShop(){
        open = false;
    }

    @Override
    public void run() {
        try {
            while(open){
                shop.chamarProximoClienteCortar();
            }
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        System.out.println("Fechou a barbearia");
    }
}
