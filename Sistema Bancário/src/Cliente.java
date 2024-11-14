public class Cliente {
    private int id;
    private long cpf;
    String nome;
    private Conta conta;

    public Cliente(long cpf, String nome) {
        this.cpf = cpf;
        this.nome = nome;
    }

    public Cliente(int id, long cpf, String nome) {
        this ( cpf , nome );
        this.id = id;
    }
    public Cliente(int id , long cpf, String nome , Conta conta) {
        this (  id , cpf , nome );
        this.conta = conta;
    }


    public String getNome() {
        return nome;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public Conta getConta() {
        return conta;
    }

    public int getId() {
        return id;
    }

    public long getCpf() {
        return cpf;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", cpf=" + cpf +
                ", nome='" + nome + '\'' +
                ", id da conta=" + conta.getNumero() +
                '}';
    }


}
