package bloque2_3;

import java.util.concurrent.atomic.AtomicLong;

public class Actividad1 {
    public static void main(String[] args) {
        int numHebras = 4;
        double t1;
        double t2;
        double tt;
        /*double[] vector = {1.3,122.3,23.3,323.4,
                12.3,42.4,343.2,43.2,
                0.1,0.1,0.1,0.1,
                0.1,0.1,0.1,0.1,
                0.1,0.1,0.1,0.1,
                0.1,0.1,0.1,0.1,
                0.1,0.1,0.1,0.1,
                0.1,0.1,0.1,0.1};*/
        long[] vector = {
                200000033L,4L,4L,4L,4L,4L,4L,4L,
                200000039L,4L,4L,4L,4L,4L,4L,4L,
                200000051L,4L,4L,4L,4L,4L,4L,4L,
                200000069L,4L,4L,4L,4L,4L,4L,4L,
                200000081L,4L,4L,4L,4L,4L,4L,4L,
                200000083L,4L,4L,4L,4L,4L,4L,4L,
                200000089L,4L,4L,4L,4L,4L,4L,4L,
                200000093L,4L,4L,4L,4L,4L,4L,4L};
        long maximo = vector[0];

        System.out.println("Implementación secuencial.");

        t1 = System.nanoTime();

        for (long v : vector) {
            if (v > maximo)
                maximo = v;
        }
        t2 = System.nanoTime();
        tt = (t2 - t1) / 1.0e9;

        System.out.println(maximo);
        System.out.println("Tiempo secuencial (seg.):\t\t\t" + tt);
        System.out.println();
        System.out.println();

        ciclico(numHebras, vector);
        bloque(numHebras,vector);

    }

    public static void bloque(int numHebras, long[] vector) {
        MiHebraBloque[] hebras = new MiHebraBloque[numHebras];
        double t1;
        double t2;
        double tt;
        AtomicLong maximo = new AtomicLong(vector[0]);

        System.out.println("Implementación bloque.");

        t1 = System.nanoTime();

        for (int i = 0; i < numHebras; i++) {
            hebras[i] = new MiHebraBloque(maximo , i, numHebras, vector);
        }
        try {
            for (int i = 0; i < numHebras; i++) {
                hebras[i].start();
                hebras[i].join();
            }
        }catch (InterruptedException ie){
            ie.printStackTrace();
        }

        t2 = System.nanoTime();
        tt = (t2 - t1) / 1.0e9;

        System.out.println(maximo);
        System.out.println("Tiempo bloque (seg.):\t\t\t" + tt);
        System.out.println();
        System.out.println();
    }

    public static void ciclico(int numHebras, long[] vector) {
        MiHebraCiclica[] hebras = new MiHebraCiclica[numHebras];
        double t1;
        double t2;
        double tt;
        AtomicLong maximo = new AtomicLong(vector[0]);

        System.out.println("Implementación cíclica.");

        t1 = System.nanoTime();

        for (int i = 0; i < numHebras; i++) {
            hebras[i] = new MiHebraCiclica(maximo , i, numHebras, vector);
        }
        try {
            for (int i = 0; i < numHebras; i++) {
                hebras[i].start();
                hebras[i].join();
            }
        }catch (InterruptedException ie){
            ie.printStackTrace();
        }

        t2 = System.nanoTime();
        tt = (t2 - t1) / 1.0e9;

        System.out.println(maximo);
        System.out.println("Tiempo cíclico (seg.):\t\t\t" + tt);
        System.out.println();
        System.out.println();
    }
}

class MiHebraCiclica extends Thread {
    int  miId, numHebras;
    long[] vector;
    AtomicLong maximo;

    public MiHebraCiclica (AtomicLong maximo,int miId, int numHebras, long[] vector) {
        this.miId = miId;
        this.numHebras = numHebras;
        this.vector = vector;
        this.maximo = maximo;
    }

    public void run () {
        for ( int i = miId; i < vector.length; i += numHebras ) {
            if (vector[i] > maximo.get())
                maximo.set(vector[i]);
        }
    }
}

class MiHebraBloque extends Thread {
    int  miId, numHebras;
    long[] vector;
    AtomicLong maximo;

    public MiHebraBloque (AtomicLong maximo,int miId, int numHebras, long[] vector) {
        this.miId = miId;
        this.numHebras = numHebras;
        this.vector = vector;
        this.maximo = maximo;
    }

    public void run () {
        int ini = miId * numHebras;
        int tam = vector.length/numHebras;
        int fin = (miId+1)*tam;

        for ( int i = ini; i < fin; i ++) {
            if (vector[i] > maximo.get())
                maximo.set(vector[i]);
        }
    }
}


