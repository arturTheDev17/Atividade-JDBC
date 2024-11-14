public class LimiteExcedidoException extends RuntimeException {
    public LimiteExcedidoException() {
        super( "Valor maior do que o limite permitido." );
    }
}
