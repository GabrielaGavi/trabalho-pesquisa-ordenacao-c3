package trabalho;

import trabalho.entidades.Reserva;
import trabalho.io.Leitor;
import trabalho.io.Gravador;
import trabalho.pesquisa.*;
import trabalho.ordenacao.*;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        String[] arquivosReservas = {
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

        System.out.println("===== RESULTADOS DOS TESTES =====\n");
        System.out.println("=== ORDENAÇÃO ===");

        for (String arquivo : arquivosReservas) {
            String nomeCurto = arquivo.substring(arquivo.lastIndexOf("/") + 1);
            System.out.println("Arquivo: " + nomeCurto);
            System.out.println("--------------------------------------");
            testarHeapsort(arquivo, nomeCurto);
            testarQuicksort(arquivo, nomeCurto);
            testarQuickIns(arquivo, nomeCurto);
            System.out.println();
        }

        System.out.println("===== PESQUISA =====");

        String arquivoNomes = "src/arquivos/nome.txt";
        try {
            ArrayList<Reserva> nomes = Leitor.ler(arquivoNomes);

            for (String arquivo : arquivosReservas) {
                String nomeCurto = arquivo.substring(arquivo.lastIndexOf("/") + 1);
                System.out.println("\nArquivo: " + nomeCurto);
                testarABB(arquivo, nomeCurto, nomes);
            }

        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo de nomes: " + e.getMessage());
        }
    }

    /*
     * MÉTODOS DE ORDENAÇÃO
     */

    static void testarHeapsort(String caminho, String nomeArquivo) {
        try {
            long soma = 0;
            ArrayList<Reserva> reservas = null;
            for (int i = 0; i < 5; i++) {
                reservas = Leitor.ler(caminho);
                long inicio = System.nanoTime();
                Heapsort.ordenar(reservas);
                long fim = System.nanoTime();
                soma += (fim - inicio);
            }
            long media = soma / 5;
            System.out.printf("Heapsort: %,d ns%n", media);
            Gravador.gravar("saida/heap_" + nomeArquivo, reservas);
        } catch (Exception e) {
            System.err.println("Erro no Heapsort (" + nomeArquivo + "): " + e.getMessage());
        }
    }

    static void testarQuicksort(String caminho, String nomeArquivo) {
        try {
            long soma = 0;
            ArrayList<Reserva> reservas = null;
            for (int i = 0; i < 5; i++) {
                reservas = Leitor.ler(caminho);
                long inicio = System.nanoTime();
                Quicksort.ordenar(reservas, 0, reservas.size() - 1);
                long fim = System.nanoTime();
                soma += (fim - inicio);
            }
            long media = soma / 5;
            System.out.printf("Quicksort: %,d ns%n", media);
            Gravador.gravar("saida/quick_" + nomeArquivo, reservas);
        } catch (Exception e) {
            System.err.println("Erro no Quicksort (" + nomeArquivo + "): " + e.getMessage());
        }
    }

    static void testarQuickIns(String caminho, String nomeArquivo) {
        try {
            long soma = 0;
            ArrayList<Reserva> reservas = null;
            for (int i = 0; i < 5; i++) {
                reservas = Leitor.ler(caminho);
                long inicio = System.nanoTime();
                QuickIns.ordenar(reservas, 0, reservas.size() - 1);
                long fim = System.nanoTime();
                soma += (fim - inicio);
            }
            long media = soma / 5;
            System.out.printf("Quick+Ins: %,d ns%n", media);
            Gravador.gravar("saida/quickins_" + nomeArquivo, reservas);
        } catch (Exception e) {
            System.err.println("Erro no Quick+Ins (" + nomeArquivo + "): " + e.getMessage());
        }
    }

    /*
     * MÉTODOS DE PESQUISA
     */

    static void testarABB(String caminho, String nomeArquivo, ArrayList<Reserva> nomes) {
        try {
            ABB arvore = new ABB();
            ArrayList<Reserva> reservas = Leitor.ler(caminho);

            for (Reserva r : reservas) {
                arvore.inserir(r);
            }

            arvore.balancear();


            long inicio = System.nanoTime();

            for (Reserva nomeReserva : nomes) {
                ArrayList<Reserva> achadas = arvore.pesquisar(nomeReserva.getNome());
                String saida = "saida/ABB_" + nomeArquivo;
                arvore.gravarResultado(saida, nomeReserva.getNome(), achadas);
            }

            long fim = System.nanoTime();
            System.out.println("ABB: " + (fim - inicio) + " ns");

        } catch (StackOverflowError e) {
            System.err.println("StackOverflow em " + nomeArquivo + " > árvore desbalanceada (lista encadeada gigante).");

            try {
                ArrayList<Reserva> erro = new ArrayList<>();
                erro.add(new Reserva("ERRO", "StackOverflow ao processar este arquivo.", "", "", ""));
                Gravador.gravar("saida/ABB_" + nomeArquivo, erro);
            } catch (IOException ex) {
                System.err.println("Erro ao gravar arquivo de erro: " + ex.getMessage());
            }

        } catch (IOException e) {
            System.err.println("Erro de E/S no arquivo " + nomeArquivo + ": " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado na ABB (" + nomeArquivo + "): " + e.getMessage());
        }
    }
}
