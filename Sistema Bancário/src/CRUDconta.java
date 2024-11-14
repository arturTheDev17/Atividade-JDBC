import java.sql.*;
import java.util.ArrayList;
public class CRUDconta {

//    Statement.RETURN_GENERATED_KEYS; ISSO AQUI É PRO AUTO_INCREMENT

    public static Conta buscarPeloNumero(int numeroConta) throws RuntimeException {

        try ( Connection connection = ConexaoBanco.getConnection(); ) {
            PreparedStatement statement = connection.prepareStatement
                    ( "select * from tb_conta where numero = ?" );
            statement.setInt( 1 , numeroConta );
            ResultSet resultSet = statement.executeQuery();
            //primeiro elemento vem sempre vazio;
            if (resultSet.next()) {
                int numero = resultSet.getInt( "numero" );
                int id_titular = resultSet.getInt( 2 );
                double saldo = resultSet.getDouble( 3 );
                double limite = resultSet.getDouble( "limite" );
                Cliente cliente = CRUDcliente.buscarPorId( id_titular );
                return new Conta( numero , cliente , saldo ,limite );
            }


        } catch ( SQLException e ) {
            e.printStackTrace();
            System.out.println( "Se você está lendo isso, ferrou!" );
        }

        throw new RuntimeException( "A conta de número " + numeroConta + " não foi encontrada." );

    }

    public static ArrayList<Conta> buscaTodasContas() {
        try ( Connection connection = ConexaoBanco.getConnection(); ) {
            PreparedStatement statement = connection.prepareStatement
                    ( "select * from tb_conta" );
            ResultSet resultSet = statement.executeQuery();
            //primeiro elemento vem sempre vazio;
            ArrayList<Conta> contas = new ArrayList<>();

            while (resultSet.next()) {

                int id_titular = resultSet.getInt( "id_titular" );
                Cliente titular = CRUDcliente.buscarPorId(  id_titular );
                int numero = resultSet.getInt( "numero" );
                double saldo = resultSet.getDouble( 3 );
                double limite = resultSet.getDouble( "limite" );
                contas.add( new Conta( numero , titular , saldo ,limite ));

            }
            return contas;

        } catch ( SQLException e ) {
            e.printStackTrace();
            System.out.println( "Se você está lendo isso, ferrou!" );
        }
        return null;
    }

    public static Conta buscarPeloTitular( Cliente cliente ) {
        try (Connection connection = ConexaoBanco.getConnection()) {
            PreparedStatement statement = connection.prepareStatement
                    ("SELECT * FROM tb_conta WHERE id_titular = ?");
            statement.setInt( 1 , cliente.getId() );
            ResultSet rs = statement.executeQuery();

            if ( rs.next() ) {
                return new Conta(
                        rs.getInt( "numero" ),
                        cliente,
                        rs.getInt( "saldo" ),
                        rs.getDouble( "limite" ));
            }

        } catch ( SQLException e ) {
            System.out.println( e.getMessage() );
        }

        throw new RuntimeException( "Não foi encontrada nenhuma conta para o titular de id " + cliente.getId() );
    }

    public static String mostraTodas() {
        ArrayList<Conta> contas = buscaTodasContas();
        StringBuilder retorno = new StringBuilder();

        if ( contas == null ) {
            System.out.println( "Não foi possível visualizar as contas..." );
        } else {
            if ( contas.isEmpty() ) {
                System.out.println( "Não há contas cadastradas." );
            } else {
                for (Conta conta : contas) {
                    retorno.append(conta.toStringSemCliente());
                }
            }
        }
        return retorno.toString();
    }

    public static void editarConta(Conta conta) {
        try {
            buscarPeloNumero(conta.getNumero());
        } catch (RuntimeException e) {
            e.getMessage();
        }

        try (Connection connection = ConexaoBanco.getConnection()) {
            PreparedStatement statement = connection.prepareStatement
                    ("update tb_conta set id_titular = ?, saldo = ? , limite = ? where numero = ?");

            statement.setInt(4, conta.getNumero());
            statement.setInt(1, conta.getTitular().getId());
            statement.setDouble(2, conta.getSaldo());
            statement.setDouble(3, conta.getLimite());
            statement.execute();

        } catch (SQLException e) {
            System.out.println( e.getMessage() );
        }

    }

    public static void remover( int key ) {
        try ( Connection connection = ConexaoBanco.getConnection() ) {
            PreparedStatement statement = connection.prepareStatement
                    ( "delete from tb_conta where numero = ?");


            statement.setInt( 1 , key );
            statement.execute();
        } catch ( SQLException e ) {
            e.printStackTrace();
            System.out.println( "Se você está lendo isso, ferrou!" );

        }
    }

    public static void salvar(Conta conta) {

        try {
            buscarPeloNumero(conta.getNumero());
            System.out.println("Conta já existe!!!");
        } catch ( RuntimeException e ) {

            try (Connection connection = ConexaoBanco.getConnection()) {
                //        Statement st = connection.createStatement(); ANTIGO, PODE SOFRER INJEÇÃO
                //mais novo, mais protegido para injeções;
                //        PreparedStatement statement = connection.prepareStatement
                //                ( "INSERT INTO " + "tb_conta" + "VALUES( numero , titular , saldo , limite )" +
                //                "( " + conta.getNumero() + ", '" +
                //                        conta.getTitular() + "'," +
                //                        conta.getSaldo() + "," +
                //                        conta.getLimite() + "," + ")");
                PreparedStatement statement = connection.prepareStatement
                        ("INSERT INTO " + "tb_conta" + " ( numero , id_titular , saldo , limite ) VALUES " +
                                "( ? , ? , ? , ? )");


                statement.setInt(1, conta.getNumero());
                statement.setInt(2, conta.getTitular().getId());
                statement.setDouble(3, conta.getSaldo());
                statement.setDouble(4, conta.getLimite());
                statement.execute();
            } catch (SQLException sql) {
                sql.printStackTrace();
                System.out.println("Se você está lendo isso, ferrou!");
            }
        }

//        Como o Connection é AutoCloseable, não há necessidade de fazer finally com .close(), já que o try-with-resourses chama o .close();
//        finally {
//            connection.close();
//
//        }
    }
}
