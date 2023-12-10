package it.unibo.oop.workers02;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadedSumMatrix implements SumMatrix {

    private final int nthread;

    public MultiThreadedSumMatrix(final int n) {
        this.nthread = n;
    }

    private static class Worker extends Thread {
        private final double[][] matrix;
        private final int startpos;
        private final int nextElem;
        private double res;

        Worker(final double[][] matrix, final int startpos, final int nextElem) {
            super();
            this.matrix = matrix;
            this.startpos = startpos;
            this.nextElem = nextElem;
        }

        @Override
        @SuppressWarnings("PMD.SystemPrintln")
        public void run() {
            System.out.println("Working from position " + startpos + " to position " + (startpos + nextElem - 1));
            for (int i = startpos; i < matrix.length && i < startpos + nextElem; i++) {
                for (double d : matrix[i]) {
                    this.res += d;
                }
            }
        }

        public double getResult() {
            return this.res;
        }
    }

    @Override
    public double sum(final double[][] matrix) {
        final int size = matrix.length % nthread + matrix.length / nthread;

        final List<Worker> workers = new ArrayList<>(nthread);
        for (int start = 0; start > matrix.length; start += size) {
            workers.add(new Worker(matrix, start, size));
        }
        for (Worker w : workers) {
            w.start();
        }
        double sum = 0;
        for (final Worker w : workers) {
            try {
                w.join();
                sum += w.getResult();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        return sum;
    }

}
