public class Conta {

    private int numero;
    private Cliente titular;
    private double saldo;
    private double limite;

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Cliente getTitular() {
        return titular;
    }

    public void setTitular(Cliente titular) {
        this.titular = titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public double getLimite() {
        return limite;
    }

    public void setLimite(double limite) {
        this.limite = limite;
    }

    public Conta(int numero, Cliente titular, double saldo, double limite) {
        this.numero = numero;
        this.titular = titular;
        this.saldo = saldo;
        this.limite = limite;
    }

    public void saque(double valorDeSaque )
            throws SaldoInsuficienteException , ValorInvalidoException , LimiteExcedidoException {

        this.validaValor( valorDeSaque );
        this.validaSaldo( valorDeSaque );
        this.validaLimite( valorDeSaque );

        this.saldo -= valorDeSaque;
    }

    public void deposito( double valorDeDeposito )
            throws ValorInvalidoException {

        this.validaValor( valorDeDeposito );
        this.saldo += valorDeDeposito;
    }

    public void transferencia( double valorDeTransferencia , Conta contaATransferir )
            throws ContaInexistenteException, ValorInvalidoException, LimiteExcedidoException, SaldoInsuficienteException , ContaInvalidaException {

        this.validaConta( contaATransferir );
        this.saque( valorDeTransferencia );
        contaATransferir.deposito( valorDeTransferencia );
    }

    private void validaValor( double valor ) {
        if ( valor <= 0 ) throw new ValorInvalidoException();
    }
    private void validaLimite( double valor ) {
        if ( this.limite < valor ) throw new LimiteExcedidoException();
    }
    private void validaSaldo( double valor ) throws SaldoInsuficienteException {
        if ( this.saldo < valor  ) throw new SaldoInsuficienteException();
    }
    private void validaConta( Conta conta ) throws ContaInvalidaException {
        if ( conta == null ) throw new ContaInexistenteException();
        if ( this.equals( conta ) ) throw new ContaPropriaException();
    }

    @Override
    public String toString() {
        return """
               Conta de número: %d
               Titular: %s
               Saldo: R$ %.2f
               Limite: R$ %.2f
               
               """.formatted(this.numero , this.titular, this.saldo, this.limite);
    }

    public String toStringSemCliente() {
        return """
               Conta de número: %d
               Titular: %s
               Saldo: R$ %.2f
               Limite: R$ %.2f
               
               """.formatted(this.numero , this.titular.getNome(), this.saldo, this.limite);
    }
}
