package trabalho.pesquisa;

import trabalho.entidades.Reserva;
import trabalho.io.Gravador;

import java.io.IOException;
import java.util.ArrayList;

public class ABB {

    private No raiz;

    //classe interna p/ representar no da arv
    private static class No {
        Reserva dado;
        No esquerda, direita;

        No(Reserva dado) {
            this.dado = dado;
        }
    }

    public void inserir(Reserva reserva) {
        raiz = inserirRec(raiz, reserva);
    }

    private No inserirRec(No atual, Reserva reserva) {
        if (atual == null) return new No(reserva);

        int cmp = comparar(reserva, atual.dado);
        if (cmp < 0)
            atual.esquerda = inserirRec(atual.esquerda, reserva);
        else if (cmp > 0)
            atual.direita = inserirRec(atual.direita, reserva);

        return atual;
    }

    public ArrayList<Reserva> pesquisar(String nome) {
        ArrayList<Reserva> resultados = new ArrayList<>();
        pesquisarRec(raiz, nome, resultados);
        return resultados;
    }

    private void pesquisarRec(No atual, String nome, ArrayList<Reserva> resultados) {
        if (atual == null) return;

        int cmp = nome.compareToIgnoreCase(atual.dado.getNome());

        if (cmp == 0) {
            resultados.add(atual.dado);
            pesquisarRec(atual.esquerda, nome, resultados);
            pesquisarRec(atual.direita, nome, resultados);
        } else if (cmp < 0) {
            pesquisarRec(atual.esquerda, nome, resultados);
        } else {
            pesquisarRec(atual.direita, nome, resultados);
        }
    }

    public void gravarResultado(String nomeArquivo, String nome, ArrayList<Reserva> reservas) {
        ArrayList<Reserva> saida = new ArrayList<>();

        if (reservas.isEmpty()) {
            saida.add(new Reserva("", nome, "NÃO TEM RESERVA", "", ""));
        } else {
            saida.addAll(reservas);
        }

        try {
            Gravador.gravar(nomeArquivo, saida);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void balancear() {
        ArrayList<Reserva> lista = new ArrayList<>();
        emOrdem(raiz, lista);
        raiz = construirBalanceada(lista, 0, lista.size() - 1);
    }

    private void emOrdem(No atual, ArrayList<Reserva> lista) {
        if (atual != null) {
            emOrdem(atual.esquerda, lista);
            lista.add(atual.dado);
            emOrdem(atual.direita, lista);
        }
    }

    private No construirBalanceada(ArrayList<Reserva> lista, int ini, int fim) {
        if (ini > fim) return null;

        int meio = (ini + fim) / 2;
        No novo = new No(lista.get(meio));
        novo.esquerda = construirBalanceada(lista, ini, meio - 1);
        novo.direita = construirBalanceada(lista, meio + 1, fim);
        return novo;
    }

    private int comparar(Reserva a, Reserva b) {
        int c = a.getNome().compareToIgnoreCase(b.getNome());
        return c != 0 ? c : a.getCodigo().compareToIgnoreCase(b.getCodigo());
    }
}
