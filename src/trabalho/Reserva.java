package trabalho;

class Reserva {
    String codigo;
    String nome;
    String voo;
    String data;
    String assento;

    public Reserva(String codigo, String nome, String voo, String data, String assento) {
        this.codigo = codigo;
        this.nome = nome;
        this.voo = voo;
        this.data = data;
        this.assento = assento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getVoo() {
        return voo;
    }

    public void setVoo(String voo) {
        this.voo = voo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAssento() {
        return assento;
    }

    public void setAssento(String assento) {
        this.assento = assento;
    }
}