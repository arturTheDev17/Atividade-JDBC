
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner( System.in );

    private static void escolhas( ) throws SQLException {

        System.out.println( """
                1. Criar conta;
                2. Remover conta;
                3. Editar uma conta;
                4. Ver todas as contas;
                5. Buscar uma conta;
                """ );

        byte escolha = sc.nextByte();
        switch ( escolha ) {

            case 1 : {
                sc.nextLine();
                CRUDconta.salvar( criarConta( CRUDcliente.salvar( criarCliente() ) ) );

                System.out.println( CRUDcliente.buscarTodos() );
            } break;

            case 2 : {
                System.out.println( CRUDconta.mostraTodas() );
                System.out.println( "Insira o número do cliente a remover: " );

                int numero = sc.nextInt();
                try {
                    CRUDcliente.remover( numero );
                } catch ( SQLIntegrityConstraintViolationException e ) {
                    CRUDconta.remover(CRUDcliente.buscarPorId(numero).getConta().getNumero());
                    CRUDcliente.remover( numero );
                }
            } break;

            case 3 : {
                CRUDconta.editarConta( criarConta( criarCliente() ) );
            } break;

            case 4 : {
                System.out.println( CRUDconta.mostraTodas() );
            } break;

            case 5 : {
                System.out.println( "Insira o número da conta que deseja buscar: " );
                try {
                    System.out.println(CRUDconta.buscarPeloNumero(sc.nextInt()));
                } catch ( RuntimeException e ) {
                    System.out.println( e.getMessage() );
                }
            } break;

            case 6 : {
                System.out.println( CRUDcliente.buscarTodos() );
            }
        }
    }

    private static Cliente criarCliente() {
        System.out.print( "Nome: " );
        String titular = sc.nextLine();
        sc.nextLine();
        System.out.print( "CPF: " );
        long cpf = sc.nextLong();
        return new Cliente( cpf , titular );
    }
    private static Conta criarConta( Cliente titular ) {
        System.out.print( "Saldo: " );
        double saldo = sc.nextDouble();
        System.out.print( "Limite: " );
        double limite = sc.nextDouble();
        return new Conta( titular.getId() , titular , saldo , limite );
    }
    public static void main ( String [] args ) throws SQLException {
//        Conta conta1 = new Conta( 1 , "Saymon" , 1000 , 500  );
//        Conta conta2 = new Conta( 2 , "Igor Caça-Tora" , 3000 , 1000  );
//
//        try {
//            conta1.saque( 500 );
//            conta1.deposito( 1000 );
//            conta1.transferencia( 500 , conta2 );
//
//            conta1.transferencia( 500 , conta1 );
//        } catch (SaldoInsuficienteException | ValorInvalidoException | LimiteExcedidoException e) {
//            System.out.println( e.getMessage() );
//        } catch (ContaInvalidaException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println( conta1.getSaldo() );
        while (true) {
            escolhas();
        }
//        criarConta();
//
//        //editarConta();
//        System.out.println( "Buscar uma conta: " );
//        int numero = sc.nextInt();
//        System.out.println();
//        Conta conta = buscarPeloNumero(numero);
//
//        ArrayList<Conta> contas = buscaTodasContas();
//
//        System.out.println( conta );
//
//        //remover( numero );
//
//        System.out.println( buscarPeloNumero(numero) );
//
//        System.out.println( contas );
    }

}
