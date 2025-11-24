
package trabalho.io;

import trabalho.entidades.Reserva;

import java.io.*;
import java.util.ArrayList;

public class Leitor {
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
}
