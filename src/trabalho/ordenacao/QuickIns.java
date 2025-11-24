package trabalho.ordenacao;

import trabalho.entidades.Reserva;
import java.util.ArrayList;

public class QuickIns {

    private static final int LIMITE = 20;

    public static void ordenar(ArrayList<Reserva> arr, int ini, int fim) {
        if (fim - ini <= LIMITE) inserir(arr, ini, fim);
        else if (ini < fim) {
            int p = dividir(arr, ini, fim);
            ordenar(arr, ini, p - 1);
            ordenar(arr, p + 1, fim);
        }
    }

    private static void inserir(ArrayList<Reserva> arr, int ini, int fim) {
        for (int i = ini + 1; i <= fim; i++) {
            Reserva chave = arr.get(i);
            int j = i - 1;
            while (j >= ini && comp(arr.get(j), chave) > 0) {
                arr.set(j + 1, arr.get(j));
                j--;
            }
            arr.set(j + 1, chave);
        }
    }

    private static int dividir(ArrayList<Reserva> arr, int ini, int fim) {
        int meio = (ini + fim) / 2;
        Reserva a = arr.get(ini), b = arr.get(meio), c = arr.get(fim), pivo;
        if (comp(a, b) < 0) {
            if (comp(b, c) < 0) pivo = b;
            else if (comp(a, c) < 0) pivo = c;
            else pivo = a;
        } else {
            if (comp(a, c) < 0) pivo = a;
            else if (comp(b, c) < 0) pivo = c;
            else pivo = b;
        }
        int idx = (pivo == a ? ini : (pivo == b ? meio : fim));
        trocar(arr, idx, fim);
        pivo = arr.get(fim);
        int i = ini - 1;
        for (int j = ini; j < fim; j++) {
            if (comp(arr.get(j), pivo) <= 0) {
                i++;
                trocar(arr, i, j);
            }
        }
        trocar(arr, i + 1, fim);
        return i + 1;
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
