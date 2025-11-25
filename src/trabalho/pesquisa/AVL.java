package trabalho.pesquisa;

import trabalho.entidades.Reserva;
import trabalho.io.Gravador;
import java.io.IOException;
import java.util.ArrayList;

public class AVL {

    private static class No {
        Reserva dado;
        No esq, dir;
        int altura;

        No(Reserva d) {
            this.dado = d;
            this.altura = 1;
        }
    }

    private No raiz;

    private int altura(No n) {
        return (n == null ? 0 : n.altura);
    }

    private int fator(No n) {
        return (n == null ? 0 : altura(n.esq) - altura(n.dir));
    }

    private No rotacaoDireita(No y) {
        No x = y.esq;
        No t2 = x.dir;

        x.dir = y;
        y.esq = t2;

        y.altura = Math.max(altura(y.esq), altura(y.dir)) + 1;
        x.altura = Math.max(altura(x.esq), altura(x.dir)) + 1;

        return x;
    }

    private No rotacaoEsquerda(No y) {
        No x = y.dir;
        No t2 = x.esq;

        x.esq = y;
        y.dir = t2;

        y.altura = Math.max(altura(y.esq), altura(y.dir)) + 1;
        x.altura = Math.max(altura(x.esq), altura(x.dir)) + 1;

        return x;
    }

    private int comparar(Reserva a, Reserva b) {
        int c = a.getNome().trim().compareToIgnoreCase(b.getNome().trim());
        return c != 0 ? c : a.getCodigo().compareToIgnoreCase(b.getCodigo());
    }

    public void inserir(Reserva r) {
        raiz = inserirRec(raiz, r);
    }

    private No inserirRec(No atual, Reserva r) {
        if (atual == null) return new No(r);

        int cmp = comparar(r, atual.dado);

        if (cmp < 0)
            atual.esq = inserirRec(atual.esq, r);
        else
            atual.dir = inserirRec(atual.dir, r);

        atual.altura = 1 + Math.max(altura(atual.esq), altura(atual.dir));
        int fb = fator(atual);

        if (fb > 1 && comparar(r, atual.esq.dado) < 0)
            return rotacaoDireita(atual);

        if (fb < -1 && comparar(r, atual.dir.dado) > 0)
            return rotacaoEsquerda(atual);

        if (fb > 1 && comparar(r, atual.esq.dado) > 0) {
            atual.esq = rotacaoEsquerda(atual.esq);
            return rotacaoDireita(atual);
        }

        if (fb < -1 && comparar(r, atual.dir.dado) < 0) {
            atual.dir = rotacaoDireita(atual.dir);
            return rotacaoEsquerda(atual);
        }

        return atual;
    }

    public ArrayList<Reserva> pesquisar(String nome) {
        ArrayList<Reserva> lista = new ArrayList<>();
        pesquisarRec(raiz, nome.trim(), lista);
        return lista;
    }

    private void pesquisarRec(No atual, String nome, ArrayList<Reserva> lista) {
        if (atual == null) return;

        int cmp = nome.compareToIgnoreCase(atual.dado.getNome().trim());

        if (cmp == 0) {
            lista.add(atual.dado);
            pesquisarRec(atual.esq, nome, lista);
            pesquisarRec(atual.dir, nome, lista);
        } else if (cmp < 0) {
            pesquisarRec(atual.esq, nome, lista);
        } else {
            pesquisarRec(atual.dir, nome, lista);
        }
    }

    public void gravarResultado(String nomeArquivo, ArrayList<Reserva> nomesPesquisados) {
        ArrayList<String> linhas = new ArrayList<>();

        for (Reserva nomeBuscado : nomesPesquisados) {
            String nome = nomeBuscado.getNome().trim();
            linhas.add("NOME " + nome + ":");

            ArrayList<Reserva> achadas = pesquisar(nome);

            if (achadas.isEmpty()) {
                linhas.add("NÃO TEM RESERVA");
            } else {
                for (Reserva r : achadas) {
                    linhas.add(
                            "Reserva: " + r.getCodigo() +
                                    " Voo: " + r.getVoo() +
                                    " Data: " + r.getData() +
                                    " Assento: " + r.getAssento()
                    );
                }
                linhas.add("TOTAL: " + achadas.size() + " reservas");
            }

            linhas.add("");
        }

        try {
            Gravador.gravarPesquisa(nomeArquivo, linhas);
        } catch (IOException e) {
            System.err.println("Erro ao gravar AVL: " + e.getMessage());
        }
    }
}
