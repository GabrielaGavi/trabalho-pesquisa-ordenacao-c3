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
                    linhas.add("Reserva: " + r.getCodigo()
                            + " Voo: " + r.getVoo()
                            + " Data: " + r.getData()
                            + " Assento: " + r.getAssento());
                }
                linhas.add("TOTAL: " + achadas.size() + " reservas");
            }

            linhas.add(""); // linha em branco entre pessoas
        }

        try {
            Gravador.gravarPesquisa(nomeArquivo, linhas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =============================
    // BALANCEAR A ABB (após inserir tudo carregar arquivo)
    // =============================
    public void balancear() {
        ArrayList<Reserva> lista = new ArrayList<>();
        emOrdem(raiz, lista);              // 1) pega todos os elementos ordenados
        raiz = construirBalanceada(lista, 0, lista.size() - 1); // 2) monta ABB balanceada
    }

    // Percurso em ordem para gerar lista ordenada
    private void emOrdem(No atual, ArrayList<Reserva> lista) {
        if (atual != null) {
            emOrdem(atual.esquerda, lista);
            lista.add(atual.dado);
            emOrdem(atual.direita, lista);
        }
    }

    // Constroi uma ABB balanceada a partir da lista ordenada
    private No construirBalanceada(ArrayList<Reserva> lista, int ini, int fim) {
        if (ini > fim) return null;

        int meio = (ini + fim) / 2;

        No no = new No(lista.get(meio));
        no.esquerda = construirBalanceada(lista, ini, meio - 1);
        no.direita = construirBalanceada(lista, meio + 1, fim);

        return no;
    }




    private int comparar(Reserva a, Reserva b) {
        String na = a.getNome().trim();
        String nb = b.getNome().trim();

        int c = na.compareToIgnoreCase(nb);
        return c != 0 ? c : a.getCodigo().compareToIgnoreCase(b.getCodigo());
    }
}
