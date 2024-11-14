public class ValorInvalidoException extends RuntimeException {
    public ValorInvalidoException() {
        super( "Valor nulo ou negativo." );
    }
}
