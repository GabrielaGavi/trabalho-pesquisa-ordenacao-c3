package trabalho.ordenacao;

import trabalho.entidades.Reserva;
import java.util.ArrayList;

public class Heapsort {

    public static void ordenar(ArrayList<Reserva> arr) {
        int n = arr.size();
        for (int i = n / 2 - 1; i >= 0; i--) heapify(arr, n, i);
        for (int i = n - 1; i > 0; i--) {
            trocar(arr, 0, i);
            heapify(arr, i, 0);
        }
    }

    private static void heapify(ArrayList<Reserva> arr, int n, int i) {
        int maior = i, esq = 2 * i + 1, dir = 2 * i + 2;
        if (esq < n && comp(arr.get(esq), arr.get(maior)) > 0) maior = esq;
        if (dir < n && comp(arr.get(dir), arr.get(maior)) > 0) maior = dir;
        if (maior != i) {
            trocar(arr, i, maior);
            heapify(arr, n, maior);
        }
    }

    private static void trocar(ArrayList<Reserva> arr, int i, int j) {
        Reserva temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }

    private static int comp(Reserva a, Reserva b) {
        int c = a.getNome().compareToIgnoreCase(b.getNome());
        return c != 0 ? c : a.getCodigo().compareToIgnoreCase(b.getCodigo());
    }
}
