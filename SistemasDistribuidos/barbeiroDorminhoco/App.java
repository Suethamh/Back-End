package barbeiroDorminhoco;

public class App {
    public static void main(String[] args) {
        BarberShop shop = new BarberShop(5);
        Barbeiro barbeiro = new Barbeiro(shop);
        Thread barberThread = new Thread(barbeiro, "Barbeiro");
        barberThread.start();

        for(int i=1; i<=12; i++){
            Thread t = new Thread(new Cliente(shop, i), "Cliente-" + i);
            t.start();
            try{
                Thread.sleep((long) (Math.random()*800));
                
            } catch (InterruptedException e) {
                System.out.println("fez merda");
            }
        }
        try{
            Thread.sleep(100000);
        }catch(InterruptedException e){
            System.out.println("fez merda");
        }
        barbeiro.fecharShop();
        barberThread.interrupt();
    }
}
