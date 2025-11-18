package trabalho;

import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        String[] arquivosEntrada = {
                "src/arquivos/reserva1000alea.txt",
                "src/arquivos/reserva5000alea.txt",
                "src/arquivos/reserva10000alea.txt",
                "src/arquivos/reserva50000alea.txt",
                "src/arquivos/reserva1000inv.txt",
                "src/arquivos/reserva5000inv.txt",
                "src/arquivos/reserva10000inv.txt",
                "src/arquivos/reserva50000inv.txt",
                "src/arquivos/reserva1000ord.txt",
                "src/arquivos/reserva5000ord.txt",
                "src/arquivos/reserva10000ord.txt",
                "src/arquivos/reserva50000ord.txt"
        };

        for (String arquivo : arquivosEntrada) {
            testarHeapsort(arquivo);
            testarQuicksort(arquivo);
            testarQuickIns(arquivo);
        }
    }

    static void testarHeapsort(String nomeArquivo) {
        try {
            long soma = 0;
            ArrayList<Reserva> reservas = null;

            for (int i = 0; i < 5; i++) {
                reservas = Leitor.ler(nomeArquivo);
                long inicio = System.nanoTime();
                Heapsort.ordenar(reservas);
                long fim = System.nanoTime();
                soma += (fim - inicio);
            }

            long media = soma / 5;
            System.out.println("Heapsort - " + nomeArquivo + " média: " + media + " ns");

            String saida = "saida/heap_" + new File(nomeArquivo).getName();
            Gravador.gravar(saida, reservas);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void testarQuicksort(String nomeArquivo) {
        try {
            long soma = 0;
            ArrayList<Reserva> reservas = null;

            for (int i = 0; i < 5; i++) {
                reservas = Leitor.ler(nomeArquivo);
                long inicio = System.nanoTime();
                Quicksort.ordenar(reservas, 0, reservas.size() - 1);
                long fim = System.nanoTime();
                soma += (fim - inicio);
            }

            long media = soma / 5;
            System.out.println("Quicksort - " + nomeArquivo + " média: " + media + " ns");

            String saida = "saida/quick_" + new File(nomeArquivo).getName();
            Gravador.gravar(saida, reservas);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void testarQuickIns(String nomeArquivo) {
        try {
            long soma = 0;
            ArrayList<Reserva> reservas = null;

            for (int i = 0; i < 5; i++) {
                reservas = Leitor.ler(nomeArquivo);
                long inicio = System.nanoTime();
                QuickIns.ordenar(reservas, 0, reservas.size() - 1);
                long fim = System.nanoTime();
                soma += (fim - inicio);
            }

            long media = soma / 5;
            System.out.println("Quick+Ins - " + nomeArquivo + " média: " + media + " ns");

            String saida = "saida/quickins_" + new File(nomeArquivo).getName();
            Gravador.gravar(saida, reservas);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Heapsort {
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
        int c = a.nome.compareToIgnoreCase(b.nome);
        return c != 0 ? c : a.codigo.compareToIgnoreCase(b.codigo);
    }
}

class Quicksort {
    public static void ordenar(ArrayList<Reserva> arr, int ini, int fim) {
        if (ini < fim) {
            int p = dividir(arr, ini, fim);
            ordenar(arr, ini, p - 1);
            ordenar(arr, p + 1, fim);
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
        int c = a.nome.compareToIgnoreCase(b.nome);
        return c != 0 ? c : a.codigo.compareToIgnoreCase(b.codigo);
    }
}

class QuickIns {
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
        int c = a.nome.compareToIgnoreCase(b.nome);
        return c != 0 ? c : a.codigo.compareToIgnoreCase(b.codigo);
    }
}
