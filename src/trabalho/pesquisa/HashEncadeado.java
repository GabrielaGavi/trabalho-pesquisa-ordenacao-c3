package trabalho.pesquisa;

import trabalho.entidades.Reserva;
import trabalho.io.Gravador;

import java.io.IOException;
import java.util.ArrayList;

public class HashEncadeado {

    private static class No {
        String chave;                 // nome da pessoa
        ArrayList<Reserva> reservas;  // todas as reservas desta pessoa
        No prox;

        No(String chave, Reserva r) {
            this.chave = chave;
            this.reservas = new ArrayList<>();
            this.reservas.add(r);
        }
    }

    private No[] tabela;
    private int tamanho;

    public HashEncadeado(int tamanho) {
        this.tamanho = tamanho;
        this.tabela = new No[tamanho];
    }

    private int hash(String chave) {
        return Math.abs(chave.toLowerCase().hashCode()) % tamanho;
    }

    public void inserir(Reserva r) {
        String chave = r.getNome().trim();
        int pos = hash(chave);

        No atual = tabela[pos];

        while (atual != null) {
            if (atual.chave.equalsIgnoreCase(chave)) {
                atual.reservas.add(r);
                return;
            }
            atual = atual.prox;
        }

        No novo = new No(chave, r);
        novo.prox = tabela[pos];
        tabela[pos] = novo;
    }

    public ArrayList<Reserva> pesquisar(String nome) {
        String chave = nome.trim();
        int pos = hash(chave);

        No atual = tabela[pos];

        while (atual != null) {
            if (atual.chave.equalsIgnoreCase(chave)) {
                return atual.reservas;
            }
            atual = atual.prox;
        }

        return new ArrayList<>();
    }

    public void gravarResultado(String nomeArquivo, ArrayList<Reserva> nomesBuscados) {
        ArrayList<String> linhas = new ArrayList<>();

        for (Reserva nomeObj : nomesBuscados) {

            String nome = nomeObj.getNome().trim();
            linhas.add("NOME " + nome + ":");

            ArrayList<Reserva> achadas = pesquisar(nome);

            if (achadas.isEmpty()) {
                linhas.add("NÃO TEM RESERVA");
            } else {
                for (Reserva r : achadas) {
                    linhas.add("Reserva: " + r.getCodigo()
                            + " Voo: " + r.getVoo()
                            + " Data: " + r.getData()
                            + " Assento: " + r.getAssento());
                }
                linhas.add("TOTAL: " + achadas.size() + " reservas");
            }

            linhas.add("");
        }

        try {
            Gravador.gravarPesquisa(nomeArquivo, linhas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
