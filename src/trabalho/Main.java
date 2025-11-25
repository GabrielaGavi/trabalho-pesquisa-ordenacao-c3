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
            ArrayList<Reserva> nomes = Leitor.lerNomes(arquivoNomes);

            for (String arquivo : arquivosReservas) {
                String nomeCurto = arquivo.substring(arquivo.lastIndexOf("/") + 1);
                System.out.println("\nArquivo: " + nomeCurto);
                testarABB(arquivo, nomeCurto, nomes);
                testarAVL(arquivo, nomeCurto, nomes);
                testarHashEncadeado(arquivo, nomeCurto, nomes);
            }

        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo de nomes: " + e.getMessage());
        }
    }

    public static void testarHeapsort(String caminho, String nomeArquivo) {
        try {
            long soma = 0;
            ArrayList<Reserva> reservas = null;

            for (int i = 0; i < 5; i++) {
                long inicio = System.nanoTime();
                reservas = Leitor.ler(caminho);
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

    public static void testarQuicksort(String caminho, String nomeArquivo) {
        try {
            long soma = 0;
            ArrayList<Reserva> reservas = null;

            for (int i = 0; i < 5; i++) {
                long inicio = System.nanoTime();
                reservas = Leitor.ler(caminho);
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

    public static void testarQuickIns(String caminho, String nomeArquivo) {
        try {
            long soma = 0;
            ArrayList<Reserva> reservas = null;

            for (int i = 0; i < 5; i++) {
                long inicio = System.nanoTime();
                reservas = Leitor.ler(caminho);
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

    public static void testarABB(String caminho, String nomeArquivo, ArrayList<Reserva> nomes) {

        long soma = 0;

        try {
            for (int i = 0; i < 5; i++) {

                long inicio = System.nanoTime();

                ABB arvore = new ABB();
                ArrayList<Reserva> reservas = Leitor.ler(caminho);

                for (Reserva r : reservas){
                    arvore.inserir(r);
                }

                arvore.balancear();

                String saida = "saida/ABB_" + nomeArquivo;
                arvore.gravarResultado(saida, nomes);

                long fim = System.nanoTime();
                soma += (fim - inicio);
            }

            long media = soma / 5;
            System.out.println("ABB: " + media + " ns");

        } catch (StackOverflowError e) {

            System.err.println("StackOverflow em " + nomeArquivo);

            try {
                ArrayList<Reserva> erro = new ArrayList<>();
                erro.add(new Reserva("ERRO", "StackOverflow ao processar este arquivo.", "", "", ""));
                Gravador.gravar("saida/ABB_" + nomeArquivo, erro);
            } catch (IOException ex) {
                System.err.println("Erro ao criar arquivo de erro.");
            }

        } catch (Exception e) {
            System.err.println("Erro inesperado na ABB (" + nomeArquivo + "): " + e.getMessage());
        }
    }

    static void testarAVL(String caminho, String nomeArquivo, ArrayList<Reserva> nomes) {

        long somaTempos = 0;

        for (int repeticao = 0; repeticao < 5; repeticao++) {

            try {
                long inicio = System.nanoTime();

                AVL arvore = new AVL();
                ArrayList<Reserva> reservas = Leitor.ler(caminho);

                for (Reserva r : reservas) arvore.inserir(r);

                String saida = "saida/AVL_" + nomeArquivo;

                arvore.gravarResultado(saida, nomes);

                long fim = System.nanoTime();
                somaTempos += (fim - inicio);

            } catch (StackOverflowError e) {
                System.err.println("StackOverflow em " + nomeArquivo + " (AVL)");

                try {
                    ArrayList<String> erro = new ArrayList<>();
                    erro.add("ERRO: StackOverflow ao processar este arquivo.");
                    Gravador.gravarPesquisa("saida/AVL_" + nomeArquivo, erro);
                } catch (IOException ex) {
                    System.err.println("Erro ao criar arquivo de erro AVL.");
                }

                return;
            } catch (Exception e) {
                System.err.println("Erro inesperado na AVL (" + nomeArquivo + "): " + e.getMessage());
                return;
            }
        }

        long media = somaTempos / 5;
        System.out.println("AVL: " + media + " ns");
    }


    public static void testarHashEncadeado(String caminho, String nomeArquivo, ArrayList<Reserva> nomes) {

        try {
            long soma = 0;

            for (int i = 0; i < 5; i++) {

                long inicio = System.nanoTime();
                HashEncadeado hash = new HashEncadeado(10007); // tamanho primo p/ ate 50k elementos
                ArrayList<Reserva> reservas = Leitor.ler(caminho);

                // inserir todas as reservas
                for (Reserva r : reservas) {
                    hash.inserir(r);
                }


                if (i == 4) {
                    String saida = "saida/HASH_" + nomeArquivo;
                    hash.gravarResultado(saida, nomes);
                }

                long fim = System.nanoTime();
                soma += (fim - inicio);
            }

            long media = soma / 5;
            System.out.println("HASH: " + media + " ns");

        } catch (StackOverflowError e) {
            System.err.println("StackOverflow em " + nomeArquivo);

            try {
                ArrayList<Reserva> erro = new ArrayList<>();
                erro.add(new Reserva("ERRO", "StackOverflow ao processar este arquivo.", "", "", ""));
                Gravador.gravar("saida/HASH_" + nomeArquivo, erro);
            } catch (IOException ex) {
                System.err.println("Erro ao criar arquivo de erro.");
            }

        } catch (Exception e) {
            System.err.println("Erro inesperado no Hashing (" + nomeArquivo + "): " + e.getMessage());
        }
    }

}
