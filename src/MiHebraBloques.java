public class MiHebraBloques extends Thread{
    int id;
    int numHebras;
    long[] vectorNumeros;

    public MiHebraBloques(int id, int numHebras, long[] vectorNumeros) {
        this.id = id;
        this.numHebras = numHebras;
        this.vectorNumeros = vectorNumeros;
    }

    @Override
    public void run() {
        int tamaño = vectorNumeros.length/numHebras;
        int ini = id*tamaño;
        int fin = (id+1)*tamaño;

        for (int i = ini; i < fin; i++){
            long num = vectorNumeros[i];
            if (CalculoPrimosVector_a.esPrimo(num)) {
                System.out.println(num);
            }
        }
    }
}