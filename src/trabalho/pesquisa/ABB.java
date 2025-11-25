package trabalho.pesquisa;

import trabalho.entidades.Reserva;
import trabalho.io.Gravador;
import java.io.IOException;
import java.util.ArrayList;

public class ABB {

    private No raiz;

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
        else
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

        String buscado = nome.trim();
        String atualNome = atual.dado.getNome().trim();

        int cmp = buscado.compareToIgnoreCase(atualNome);

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

    private int comparar(Reserva a, Reserva b) {
        String na = a.getNome().trim();
        String nb = b.getNome().trim();

        int c = na.compareToIgnoreCase(nb);
        return c != 0 ? c : a.getCodigo().compareToIgnoreCase(b.getCodigo());
    }
}
