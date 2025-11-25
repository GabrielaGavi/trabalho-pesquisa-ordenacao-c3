
package trabalho.io;

import trabalho.entidades.Reserva;

import java.io.*;
import java.util.ArrayList;

public class Gravador {
//grava para os métodos de ordenação
    public static void gravar(String nomeArquivo, ArrayList<Reserva> lista) throws IOException {
        File arq = new File(nomeArquivo);
        arq.getParentFile().mkdirs();
        BufferedWriter bw = new BufferedWriter(new FileWriter(arq));

        for (Reserva r : lista) {
            bw.write(r.getCodigo() + ";" +
                    r.getNome() + ";" +
                    r.getVoo() + ";" +
                    r.getData() + ";" +
                    r.getAssento());
            bw.newLine();
        }

        bw.newLine();
        bw.close();
    }

    //grava para os metodos de pesquisa

    public static void gravarPesquisa(String nomeArquivo, ArrayList<String> linhas) throws IOException {
        File arq = new File(nomeArquivo);
        arq.getParentFile().mkdirs();
        BufferedWriter bw = new BufferedWriter(new FileWriter(arq));

        for (String linha : linhas) {
            bw.write(linha);
            bw.newLine();
        }

        bw.close();
    }
}
