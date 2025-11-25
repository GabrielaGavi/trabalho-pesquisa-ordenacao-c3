package trabalho.io;

import trabalho.entidades.Reserva;

import java.io.*;
import java.util.ArrayList;

public class Leitor {

    // ler arquivos de reserva (5 campos)
    public static ArrayList<Reserva> ler(String nomeArquivo) throws IOException {
        ArrayList<Reserva> lista = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(nomeArquivo));
        String linha;

        while ((linha = br.readLine()) != null) {
            String[] p = linha.split(";");
            if (p.length == 5)
                lista.add(new Reserva(p[0], p[1], p[2], p[3], p[4]));
        }

        br.close();
        return lista;
    }

    // Lê o arquivo nome.txt (apenas nomes)
    public static ArrayList<Reserva> lerNomes(String nomeArquivo) throws IOException {
        ArrayList<Reserva> lista = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(nomeArquivo));
        String linha;

        while ((linha = br.readLine()) != null) {
            linha = linha.trim();
            if (!linha.isEmpty()) {

                lista.add(new Reserva("", linha, "", "", ""));
            }
        }

        br.close();
        return lista;
    }
}
