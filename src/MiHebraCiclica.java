public class MiHebraCiclica extends Thread{
    int id;
    int numHebras;
    long[] vectorNumeros;

    public MiHebraCiclica(int id, int numHebras, long[] vectorNumeros) {
        this.id = id;
        this.numHebras = numHebras;
        this.vectorNumeros = vectorNumeros;
    }

    @Override
    public void run() {
        for (int i = id; i < vectorNumeros.length; i+=4){
            long num = vectorNumeros[i];
            if (CalculoPrimosVector_a.esPrimo(num)) {
                System.out.println(num);
            }
        }
    }
}