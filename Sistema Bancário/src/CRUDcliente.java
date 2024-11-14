import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CRUDcliente {

    public static List<Cliente> buscarTodos() {
        try ( Connection con = ConexaoBanco.getConnection() ){

            PreparedStatement ps = con.prepareStatement( "select * from tb_cliente" );
            ResultSet rs = ps.executeQuery();
                List<Cliente> clientes = new ArrayList<>();
            while ( rs.next() ) {
                clientes.add( new Cliente
                        ( rs.getInt( "id" ) , rs.getInt( "cpf" ) ,
                                rs.getString( "nome" ) , CRUDconta.buscarPeloNumero( rs.getInt( "id" ) ) ) );
            }
            return clientes;
        } catch ( SQLException e ) {
            e.printStackTrace();
        }

        throw new RuntimeException( "Nenhum registro encontrado" );
    }

    public static Cliente salvar( Cliente cliente ) {

        try ( Connection connection = ConexaoBanco.getConnection() ) {

            PreparedStatement ps = connection.prepareStatement
                    ( "insert into tb_cliente ( cpf , nome ) values ( ? , ? )" , PreparedStatement.RETURN_GENERATED_KEYS );
            ps.setInt( 1 ,  (int) cliente.getCpf() );
            ps.setString( 2 , cliente.getNome() );
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                cliente.setId(rs.getInt( 1 ));
            }
            return cliente;
        } catch ( SQLException e ) {
            System.out.println( e.getMessage() );
        }

        throw new RuntimeException( "Não foi possível salvar" );

    }

    public static void editar( Cliente cliente ) {
        try (Connection connection = ConexaoBanco.getConnection() ) {

            PreparedStatement statement = connection.prepareStatement( "update tb_cliente set cpf = ? , nome = ? where id = ?" );

            statement.setInt( 3 , cliente.getId() );
            statement.setInt( 1 , (int)cliente.getCpf() );
            statement.setString( 2 , cliente.getNome() );
        } catch ( SQLException e ) {
            e.printStackTrace();
            System.out.println( "Se você está lendo isso, ferrou!" );
        }
    }
    public static Cliente buscarPorId(int idTitular) {

        try (Connection connection = ConexaoBanco.getConnection() ) {

            PreparedStatement statement = connection.prepareStatement
                    ( "select * from tb_cliente where id = ?" );
            statement.setInt( 1 , idTitular );
            ResultSet resultSet = statement.executeQuery();
            //primeiro elemento vem sempre vazio;
            if (resultSet.next()) {

                //Conta conta = CRUDconta.buscarPeloNumero(  );
                Cliente cliente = new Cliente(
                        resultSet.getInt( "id" ) ,
                        resultSet.getInt( "cpf" ),
                        resultSet.getString( "nome" )
                );

                cliente.setConta( CRUDconta.buscarPeloTitular( cliente ) );
                return cliente;

            }


        } catch ( SQLException e ) {
            e.printStackTrace();
            System.out.println( "Se você está lendo isso, ferrou!" );
        }

        throw new RuntimeException( "Cliente não encontrado!!!" );
    }

    public static void remover( int key ) throws SQLException {
        Connection connection = ConexaoBanco.getConnection();
            PreparedStatement statement = connection.prepareStatement
                    ( "delete from tb_cliente where id = ?");


            statement.setInt( 1 , key );
            statement.execute();

    }

}
