package bloque2_3;

import java.util.concurrent.atomic.AtomicInteger;

public class Practica5 {
    public static void main(String[] args) {
        /*if (args.length != 1){
            System.out.println("ERROR\nIntroduce el numero de hebras");
            System.exit(-1);
        }*/
        int numHebras = 4;
        try {
            numHebras = Integer.parseInt(args[0]);
        }catch (Exception e){
            System.out.println("\n-----------------------------------------------");
            System.out.println("Parametros para numero de hebras erroneo.");
            System.out.println("Numero de hebras por defecto: 1");
            System.out.println("-----------------------------------------------\n");
        }
        long[] vector = new long[41];

        for (long i = 1;i<=vector.length;i++){
            vector[(int) i-1] = i;
        }

        secuencial(vector);
        ciclico(numHebras,vector);
        bloque(numHebras,vector);
        valorAtomico(numHebras,vector);

    }

    public static void secuencial(long[] vector) {
        double t1;
        double t2;
        double tt;
        int cantMult2 = 0;
        int cantMult3 = 0;
        int cantMult5 = 0;

        System.out.println("Implementación secuencial.");

        t1 = System.nanoTime();

        for (long l : vector) {
            if (l%2 == 0) {
                cantMult2++;
            }
            if (l%3 == 0) {
                cantMult3++;
            }
            if (l%5 == 0) {
                cantMult5++;
            }
        }

        t2 = System.nanoTime();
        tt = (t2 - t1) / 1.0e9;
        System.out.println("CANTIDAD DE MULTIPLOS:" +
                "\n----------------------------" +
                "\nMultiplos de 2: "+ cantMult2 +
                "\nMultiplos de 3: "+ cantMult3 +
                "\nMultiplos de 5: "+ cantMult5);
        System.out.println("Tiempo secuencial (seg.):\t\t\t" + tt+"\n");
    }

    public static void ciclico(int numHebras, long[] vector) {
        double t1;
        double t2;
        double tt;
        MiHebraCicl[] hebras = new MiHebraCicl[numHebras];
        int cantMult2 = 0;
        int cantMult3 = 0;
        int cantMult5 = 0;

        System.out.println("Implementación ciclica.");

        t1 = System.nanoTime();

        for (int i = 0; i<numHebras;i++){
            hebras[i] = new MiHebraCicl(vector,i,numHebras);
        }
        for (int i = 0; i<numHebras;i++){
            try {
                hebras[i].start();
                hebras[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i=0;i<numHebras;i++){
            cantMult2 += hebras[i].getNumMult2();
            cantMult3 += hebras[i].getNumMult3();
            cantMult5 += hebras[i].getNumMult5();
        }
        t2 = System.nanoTime();
        tt = (t2 - t1) / 1.0e9;
        System.out.println("CANTIDAD DE MULTIPLOS:" +
                "\n----------------------------" +
                "\nMultiplos de 2: "+cantMult2+
                "\nMultiplos de 3: "+cantMult3+
                "\nMultiplos de 5: "+cantMult5);
        System.out.println("Tiempo ciclica (seg.):\t\t\t" + tt+"\n");
    }

    public static void bloque(int numHebras, long[] vector) {
        double t1;
        double t2;
        double tt;
        MiHebraBloq[] hebras = new MiHebraBloq[numHebras];
        int cantMult2 = 0;
        int cantMult3 = 0;
        int cantMult5 = 0;

        System.out.println("Implementación bloque.");

        t1 = System.nanoTime();

        for (int i = 0; i<numHebras;i++){
            hebras[i] = new MiHebraBloq(vector,i,numHebras);
        }
        for (int i = 0; i<numHebras;i++){
            try {
                hebras[i].start();
                hebras[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i=0;i<numHebras;i++){
            cantMult2 += hebras[i].getNumMult2();
            cantMult3 += hebras[i].getNumMult3();
            cantMult5 += hebras[i].getNumMult5();
        }
        t2 = System.nanoTime();
        tt = (t2 - t1) / 1.0e9;
        System.out.println("CANTIDAD DE MULTIPLOS:" +
                "\n----------------------------" +
                "\nMultiplos de 2: "+cantMult2+
                "\nMultiplos de 3: "+cantMult3+
                "\nMultiplos de 5: "+cantMult5);
        System.out.println("Tiempo bloque (seg.):\t\t\t" + tt+"\n");
    }

    public static void valorAtomico(int numHebras, long[] vector) {
        double t1;
        double t2;
        double tt;
        MiHebraCiclAtomicValue[] hebras = new MiHebraCiclAtomicValue[numHebras];
        AtomicInteger cantMult2 = new AtomicInteger(0);
        AtomicInteger cantMult3 = new AtomicInteger(0);
        AtomicInteger cantMult5 = new AtomicInteger(0);

        System.out.println("Implementación con valor atomico.");

        t1 = System.nanoTime();

        for (int i = 0; i<numHebras;i++){
            hebras[i] = new MiHebraCiclAtomicValue(vector,i,numHebras,cantMult2,cantMult3,cantMult5);
        }
        for (int i = 0; i<numHebras;i++){
            try {
                hebras[i].start();
                hebras[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        t2 = System.nanoTime();
        tt = (t2 - t1) / 1.0e9;
        System.out.println("CANTIDAD DE MULTIPLOS:" +
                "\n----------------------------" +
                "\nMultiplos de 2: "+cantMult2.get()+
                "\nMultiplos de 3: "+cantMult3.get()+
                "\nMultiplos de 5: "+cantMult5.get());
        System.out.println("Tiempo valor atomico (seg.):\t\t\t\n" + tt+"\n");
    }
}

class MiHebraBloq extends Thread {
    private final long[] vector;
    private final int id;
    private int numMult2;
    private int numMult3;
    private int numMult5;
    private final int numHebras;

    public MiHebraBloq(long[] vector, int id, int numHebras) {
        this.vector = vector;
        this.id = id;
        this.numHebras = numHebras;
    }

    public void run() {
        int tam = vector.length/numHebras ;
        int ini = id*tam;
        int fin = Math.min(vector.length,(id+1)*tam);

        for (int i=ini;i<fin;i++) {
            if (vector[i]%2 == 0)
                numMult2++;
            if (vector[i]%3 == 0)
                numMult3++;
            if (vector[i]%5 == 0)
                numMult5++;
        }
    }

    public long getNumMult2() {
        return numMult2;
    }

    public int getNumMult3() {
        return numMult3;
    }

    public int getNumMult5() {
        return numMult5;
    }
}

class MiHebraCicl extends Thread {
    private final long[] vector;
    private final int id;
    private int numMult2;
    private int numMult3;
    private int numMult5;
    private final int numHebras;

    public MiHebraCicl(long[] vector, int id, int numHebras) {
        this.vector = vector;
        this.id = id;
        this.numHebras = numHebras;
    }

    public void run() {
        for (int i=id;i<vector.length;i+=numHebras) {
            if (vector[i]%2 == 0)
                numMult2++;
            if (vector[i]%3 == 0)
                numMult3++;
            if (vector[i]%5 == 0)
                numMult5++;
        }
    }

    public long getNumMult2() {
        return numMult2;
    }

    public int getNumMult3() {
        return numMult3;
    }

    public int getNumMult5() {
        return numMult5;
    }
}

class MiHebraCiclAtomicValue extends Thread {
    private final long[] vector;
    private final int id;
    private final AtomicInteger numMult2;
    private final AtomicInteger numMult3;
    private final AtomicInteger numMult5;
    private final int numHebras;

    public MiHebraCiclAtomicValue(long[] vector, int id, int numHebras, AtomicInteger numMult2, AtomicInteger numMult3, AtomicInteger numMult5) {
        this.vector = vector;
        this.id = id;
        this.numHebras = numHebras;
        this.numMult2 = numMult2;
        this.numMult3 = numMult3;
        this.numMult5 = numMult5;
    }

    public void run() {
        for (int i=id;i<vector.length;i+=numHebras) {
            if (vector[i]%2 == 0)
                numMult2.incrementAndGet();
            if (vector[i]%3 == 0)
                numMult3.incrementAndGet();
            if (vector[i]%5 == 0)
                numMult5.incrementAndGet();
        }
    }
}
