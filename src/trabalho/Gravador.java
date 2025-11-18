
package trabalho;

import java.io.*;
import java.util.ArrayList;

public class Gravador {
    public static void gravar(String nomeArquivo, ArrayList<Reserva> lista) throws IOException {
        File arq = new File(nomeArquivo);
        arq.getParentFile().mkdirs();
        BufferedWriter bw = new BufferedWriter(new FileWriter(arq));
        for (Reserva r : lista) {
            bw.write(r.codigo + ";" + r.nome + ";" + r.voo + ";" + r.data + ";" + r.assento);
            bw.newLine();
        }
        bw.newLine();
        bw.close();
    }
}
