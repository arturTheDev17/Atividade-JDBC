package classes;

import classes.CRUD.ClienteCRUD;
import classes.CRUD.PlanoCRUD;
import classes.CRUD.ServicoAdicionalCRUD;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void exec(String[] args) {
//        PlanoCRUD.createPlanoComContrato(
//                new Plano( "tim" , "pre-pago" , 50 ,
//                        10 , "zap gratis" , 25.50 ) ,
//                new Contrato( "da a alma ae" , LocalDate.now() , LocalDate.now())
//        );
//        Contrato contrato = PlanoCRUD.readPlanoEcontrato( "pre-pago" );
//        System.out.println( contrato.getPlano() );

//        PlanoCRUD.createPlano(
//                new Plano( "tim" , "pre-pago" , 50 ,
//                        10 , "zap gratis" , 25.50 ) );



//        System.out.println( plano );
//
//        ClienteCRUD.createCliente(
//                new Cliente( "tagarela" , "falafala@outlook.com" , "555-5555" , plano ));
//        ClienteCRUD.createCliente(
//                new Cliente( "tagarela" , "falafala@outlook.com" , "555-5555" , plano ));
//
//        Cliente cliente = ClienteCRUD.readCliente( "tagarela" );
//        System.out.println( cliente );

    }

    private static final Scanner sc = new Scanner( System.in );

    public static void main(String[] args) throws SQLException {

        testesCliente( "Saymon" );
        testesPlanoEContrato( "TudoLiberado+" );
        testesServicoAdicional( "Raio laser" , "super" );

//        while ( true ) {
//            System.out.println("""
//                    ┌———————————————————————————————————————————————————————————————┐
//                    │ BEM VINDO AO SISTEMA DE GESTÃO DE PLANOS DE OPERADORAS MÓVEIS │
//                    │ 1. Menu Plano e contrato;                                     │
//                    │ 2. Menu cliente;                                              │
//                    │ 3. Menu serviços adicionais;                                  │
//                    │ 0. Sair;                                                      │
//                    └———————————————————————————————————————————————————————————————┘
//                    """ );
//            switch ( (byte)tryNum() ) {
//                case 1 : {
//                    while( acoesPlano() );
//
//                } break;
//                case 2 : {
//                    while (acoesCliente());
//                } break;
//
//                case 3 : {
//                    while ( acoesServicoAdicional() );
//                } break;
//
//                case 0 : {
//                    System.exit( 0 );
//                }
//
//                default : {
//                    System.out.println( "Escolha não existe..." );
//                }
//            }
//        }
    }

    public static boolean acoesPlano( ) throws SQLException {
        System.out.println( """
                               ┌————————————————————————————————————————————┐
                               │                  MENU PLANO                │
                               │ 1. Criar um plano.                         │
                               │ 2. Apagar um plano.                        │
                               │ 3. Ver todos planos.                       │
                               │ 4. Editar um plano, buscando por cliente.  │
                               │ 5. Contrato de um plano.                   │
                               │ 0. VOLTAR                                  │
                               └————————————————————————————————————————————┘
                               """ );
        byte escolha = (byte)tryNum();

        switch ( escolha ) {
            case 1 : {
                PlanoCRUD.createPlano( criaPlano() );
            } break;

            case 2 : {
                System.out.println( "TODOS PLANOS:" );
                System.out.println( toStringPlanos( PlanoCRUD.readAllPlanos() ) );
                System.out.println( "Insira o nome do plano: " );
                sc.nextLine();
                String st = sc.nextLine();
                Plano plano = null;

                try {
                    plano = PlanoCRUD.readPlano( st );
                    PlanoCRUD.deletePlano( plano.getId() );
                } catch (RuntimeException e ){
                    System.err.println( "Esse plano não está registrado!" );

                } catch ( SQLIntegrityConstraintViolationException constraint ) {
                    System.out.println( "Há ao menos um cliente e/ou um contrato para esse plano! Deseja removê-los? s/N" );
                    String sim = sc.nextLine();

                    if ( sim.equals( "s" ) || sim.equals( "S" )) {
                        try {
                            ClienteCRUD.deleteCliente( ClienteCRUD.readCliente( plano.getId()).getId() );
                            System.err.println("Removendo cliente relacionado ao plano...");
                        } catch (RuntimeException e) {
                            PlanoCRUD.deleteContrato(plano.getId());
                            System.err.println("Removendo contrato relacionado ao plano...");
                        }
                        PlanoCRUD.deletePlano(plano.getId());
                    }
                }
            } break;

            case 3 : {
                System.out.println( toStringPlanos( PlanoCRUD.readAllPlanos() ) );
            } break;

            case 4 : {
                System.out.println( "EDITAR PLANO" );
                System.out.println( "Insira o nome atual do plano: " );
                sc.nextLine();
                String st = sc.nextLine();

                try {
                    System.out.println(PlanoCRUD.readPlano( st ));
                    Plano plano = criaPlano();
                    PlanoCRUD.updatePlano( plano );
                    System.out.println( "NOVO PLANO" );
                    System.out.println( PlanoCRUD.readPlano( plano.getNome() ) );
                } catch (RuntimeException e ){
                    System.err.println( "Esse plano não está registrado!" );
                }
            } break;

            case 0 : return false;

            case 5 : {
                System.out.println( "TODOS PLANOS:" );
                System.out.println( toStringPlanos( PlanoCRUD.readAllPlanos() ) );
                System.out.println( "Insira o nome de um plano para modificar seu contrato: " );
                sc.nextLine();
                String st = sc.nextLine();
                Plano plano = null;

                try {
                    plano = PlanoCRUD.readPlano( st );
                    while( acoesContrato( plano ) );
                } catch (RuntimeException e ) {
                    System.err.println("Esse plano não está registrado!");
                }
            } break;

            default: {
                System.out.println( "Escolha inválida" );
            } break;
        }

        return true;
    }

    public static boolean acoesContrato( Plano plano ) {
        System.out.printf("""
                ┌————————————————————————————————————————————┐
                │              MENU DO CONTRATO              │
                │ 1. Criar um contrato para um plano.        │
                │ 2. Apagar o contrato.                      │
                │ 3. Ver contrato.                           │
                │ 4. Editar contrato.                        │
                │ 0. VOLTAR                                  │
                └————————————————————————————————————————————┘
                Plano: %s
                %n""", plano );
        byte escolha = (byte)tryNum();

        switch ( escolha ) {
            case 1 : {
            try {
                PlanoCRUD.createContrato( plano , criaContrato() );
            } catch ( RuntimeException e ) {
                System.err.println( e.getMessage() );
            }
            } break;

            case 2 : {
                try {
                    PlanoCRUD.readPlanoEcontrato( plano.getId() );
                    System.out.println( "CONTRATO:" );
                    System.out.println( PlanoCRUD.readPlanoEcontrato( plano.getId() ) );
                    System.out.println( "Tem certeza? " );
                    sc.nextLine();
                    String st = sc.nextLine();
                    if ( st.equals( "s" ) || st.equals( "S" )) {
                        PlanoCRUD.deleteContrato( plano.getId() );
                    }
                } catch ( RuntimeException e ) {
                    System.err.println( "Esse plano não está registrado!" );
                }
            } break;

            case 3 : {
                try {
                    System.out.println( "CONTRATO:" );
                    System.out.println( PlanoCRUD.readPlanoEcontrato( plano.getId() ) );
                } catch (RuntimeException e ) {
                    System.err.println("Esse plano não está registrado!");
                }
            } break;

            case 4 : {
                System.out.println( "EDITAR CONTRATO" );
                sc.nextLine();
                String st = sc.nextLine();
                try {
                    System.out.println( "CONTRATO:" );
                    System.out.println( PlanoCRUD.readPlanoEcontrato( plano.getId() ) );
                    System.out.println(PlanoCRUD.readPlano( st ));
                    Contrato contrato = criaContrato();
                    PlanoCRUD.updateContrato( contrato );
                    System.out.println( "NOVO CONTRATO" );
                    System.out.println( PlanoCRUD.readPlanoEcontrato( plano.getId() ) );
                } catch (RuntimeException e ){
                    System.err.println( "Esse contrato não está registrado!" );
                }
            } break;

            case 0 : return false;

            default: {
                System.out.println( "Escolha inválida" );
            } break;
        }

        return true;
    }

    public static boolean acoesCliente( ) {
        System.out.println( """
                               ┌————————————————————————————————————————————┐
                               │                MENU CLIENTE                │
                               │ 1. Cadastrar um cliente.                   │
                               │ 2. Remover um cliente.                     │
                               │ 3. Ver todos os clientes.                  │
                               │ 4. Editar um cliente, buscando por nome.   │
                               │ 0. VOLTAR                                  │
                               └————————————————————————————————————————————┘
                               """ );
        byte escolha = (byte)tryNum();

        switch ( escolha ) {
            case 1 : {
                if ( !PlanoCRUD.readAllPlanos().isEmpty() ) {
                    ClienteCRUD.createCliente(criaCliente());
                }
            } break;

            case 2 : {
                System.out.println( "TODOS CLIENTES:" );
                System.out.println( toStringCliente( ClienteCRUD.readAllClientes() ) );
                System.out.println( "Insira o nome do cliente: " );
                sc.nextLine();
                String st = sc.nextLine();
                Cliente cliente = null;
                try {
                    cliente = ClienteCRUD.readCliente( st );
                    ClienteCRUD.deleteCliente( cliente.getId() );
                } catch (NoSuchElementException e ){
                    System.err.println( "Esse cliente não está registrado!" );
                }
            } break;

            case 3 : {
                System.out.println( toStringCliente( ClienteCRUD.readAllClientes() ) );
            } break;

            case 4 : {
                System.out.println( "EDITAR CLIENTE" );
                System.out.println( "Insira o nome do cliente: " );
                sc.nextLine();
                String st = sc.nextLine();

                try {
                    System.out.println( ClienteCRUD.readCliente( st ) );
                } catch (NoSuchElementException e ){
                    System.err.println( "Esse cliente não está registrado!" );
                }
            } break;

            case 0 : return false;

            default: {
                System.out.println( "Escolha inválida" );
            } break;
        }

        return true;
    }
    
    public static boolean acoesServicoAdicional( ) {
        System.out.println( """
                               ┌———————————————————————————————————————————————————┐
                               │               MENU SERVIÇO ADICIONAL              │
                               │ 1. Adicionar um serviço adicional.                │
                               │ 2. Apagar um serviço adicional.                   │
                               │ 3. Ver todos serviços adicionais.                 │
                               │ 4. Inserir um serviço adicional em um plano.      │
                               │ 5. Remover um serviço adicional de um plano.      │
                               │ 0. VOLTAR                                         │
                               └———————————————————————————————————————————————————┘
                               """ );
        byte escolha = (byte)tryNum();

        switch ( escolha ) {
            case 1 : {
                ServicoAdicionalCRUD.createServicoAdicional( criaServicoAdicional() );
            } break;

            case 2 : {
                System.out.println( "TODOS SERVIÇOS ADICIONAIS:" );
                System.out.println( toStringServicosAdicionais( ServicoAdicionalCRUD.readAllServicosAdicionais() ) );

                if ( !ServicoAdicionalCRUD.readAllServicosAdicionais().isEmpty() ) {
                    System.out.println( "Insira o id da serviço adicional: " );

                    int id = (int)tryNum();

                    try {
                        ServicoAdicionalCRUD.deleteServicoAdicional( id );
                    } catch (NoSuchElementException e ){
                        System.err.println( "Essa serviço adicional não existe!" );
                    }
                }
            } break;

            case 3 : {
                System.out.println( toStringServicosAdicionais( ServicoAdicionalCRUD.readAllServicosAdicionais() ) );
            } break;

            case 4,5 : {
                ArrayList<Plano> plano = PlanoCRUD.readAllPlanos();
                ArrayList<ServicoAdicional> servicosAdicionais = ServicoAdicionalCRUD.readAllServicosAdicionais();

                if ( plano.isEmpty() || servicosAdicionais.isEmpty() ) {
                    System.err.printf("""
                            ATENÇÃO, Há um total de:
                            %d serviços adicionais(s) e
                            %d plano(s) cadastrado(s)...
                            
                            """, servicosAdicionais.size(), plano.size());
                } else {
                    sc.nextLine();
                    System.out.println(toStringPlanos(plano));
                    System.out.print("Insira o nome do plano: ");
                    String nome = sc.nextLine();

                    System.out.println(toStringServicosAdicionais(servicosAdicionais));
                    System.out.print("Insira a descrição do serviço adicional: ");
                    String servico = sc.nextLine();

                    try {
                        int planoId = PlanoCRUD.readPlano(nome).getId();
                        int servicoAdicionalId = ServicoAdicionalCRUD.readServicoAdicional(servico).getId();

                        if ( escolha == 4 ) {
                            ServicoAdicionalCRUD.linkServicoAdicionalPlano(planoId, servicoAdicionalId);
                        } else {
                            ServicoAdicionalCRUD.deletelinkServicoAdicionalPlano(planoId, servicoAdicionalId);
                        }

                    } catch (RuntimeException e) {
                        System.err.println("Plano ou serviço adicional não encontrado.");
                    }

                }

            } break;

            case 0 : return false;

            default: {
                System.out.println( "Escolha inválida" );
            } break;
        }

        return true;
    }

    private static Cliente criaCliente() {
        sc.nextLine();
        System.out.print( "Insira o e-mail do cliente: " );
        String email = sc.nextLine();
        System.out.print( "Insira o nome do cliente: " );
        String nome = sc.nextLine();
        System.out.print( "Insira o telefone do cliente: " );
        String telefone = sc.nextLine();
        System.out.print( "Insira o nome do plano deste cliente: " );
        String nomePlano = sc.nextLine();
        Plano plano = PlanoCRUD.readPlano( nomePlano );
        return new Cliente( nome , email , telefone , plano );
    }

    private static Plano criaPlano() {
        sc.nextLine();
        System.out.print( "Insira o nome da operadora deste plano: " );
        String operadora = sc.nextLine();
        System.out.print( "Insira o nome do plano: " );
        String nome = sc.nextLine();
        System.out.print( "Insira a quantidade de dados (GB): " );
        double quantidade_dados = tryNum();
        System.out.print( "Insira a quantidade de dados de bônus (GB): " );
        double quantidade_dados_bonus = tryNum();
        System.out.print( "Insira os benefícios do plano: " );
        String beneficios = sc.nextLine();
        System.out.print( "Insira o valor do plano em reais: R$ " );
        double valor = tryNum();

        return new Plano( operadora , nome , quantidade_dados , quantidade_dados_bonus , beneficios , valor );
    }

    private static Contrato criaContrato() {
        sc.nextLine();
        System.out.print( "Insira os termos do contrato: " );
        String termos = sc.nextLine();
        System.out.print( "Insira o local do evento: " );
        String local = sc.nextLine();

        System.out.print( "Insira o dia de início do contrato: " );
        byte diaInicio = (byte)tryNum();
        System.out.print( "Insira o mês de início do contrato: " );
        byte mesInicio = (byte)tryNum();
        System.out.print( "Insira o ano de início do contrato: " );
        int anoInicio = (int)tryNum();
        LocalDate dataInicio = LocalDate.of( anoInicio , mesInicio , diaInicio );


        System.out.print( "Insira o dia de final do contrato: " );
        byte diaFinal = (byte)tryNum();
        System.out.print( "Insira o mês de final do contrato: " );
        byte mesFinal = (byte)tryNum();
        System.out.print( "Insira o ano de final do contrato: " );
        int anoFinal = (int)tryNum();
        LocalDate dataFinal = LocalDate.of( anoInicio , mesInicio , diaInicio );

        return new Contrato( termos , dataInicio , dataFinal );
    }

    private static ServicoAdicional criaServicoAdicional() {
        sc.nextLine();
        System.out.print( "Insira uma descrição curta para o serviço: " );
        String descricao = sc.nextLine();
        System.out.print( "Insira o valor em reais do serviço adicional: R$ " );
        double valor = sc.nextDouble();

        return new ServicoAdicional( descricao , valor );
    }


    private static String toStringPlanos(ArrayList<Plano> eventos ) {
        StringBuilder string = new StringBuilder();
        for ( Plano evento : eventos ) {
            string.append(evento);
        }
        return (eventos.isEmpty()) ? "Nenhum evento cadastrado" : string.toString();
    }
    private static String toStringCliente( ArrayList<Cliente> clientes ) {
        StringBuilder string = new StringBuilder();
        for ( Cliente cliente : clientes ) {
            string.append(cliente);
        }
        return (clientes.isEmpty()) ? "Nenhum cliente cadastrado" : string.toString();
    }
    private static String toStringServicosAdicionais(ArrayList<ServicoAdicional> servicosAdicionais ) {
        StringBuilder string = new StringBuilder();
        for ( ServicoAdicional servicoAdicional : servicosAdicionais ) {
            string.append( servicoAdicional );
        }
        return (servicosAdicionais.isEmpty()) ? "Nenhuma serviço adicional realizada" : string.toString();
    }

    public static void testesPlanoEContrato( String nomePlano ) throws SQLException {
        Plano plano =
                new Plano( "aTchIM" , nomePlano , 50 , 10 , "ondas eletromagnéticas grátis" , 52.49 );

        PlanoCRUD.createPlano( plano );

        Plano planoComID =
                PlanoCRUD.readPlano( nomePlano );

        System.out.println( planoComID );

        PlanoCRUD.deletePlano( planoComID.getId() );

        plano.setOperadora( "Claro que não" );

        PlanoCRUD.createPlano( plano );

        Plano planoComID2 =
                PlanoCRUD.readPlano( nomePlano );


        System.out.println( toStringPlanos( PlanoCRUD.readPlanos( "Claro Que Não" )));

        PlanoCRUD.createContrato( planoComID2 ,
                new Contrato( "Talvez você terá sinal" , LocalDate.now() ,
                        LocalDate.now( ZoneId.of( "GMT+2" ) ) ) );


        try {
            PlanoCRUD.deletePlano(planoComID2.getId());
        } catch ( SQLIntegrityConstraintViolationException constraintViolationException ) {
            PlanoCRUD.deleteContrato(planoComID2.getId());
            System.err.println("Removendo contrato relacionado ao plano...");
            PlanoCRUD.deletePlano(planoComID2.getId());
        }
    }
    public static void testesCliente( String nomeCliente ) throws SQLException {

        String email = nomeCliente.toLowerCase() + "@gmail.com";
        Plano plano =
                new Plano( "aTchIM" , nomeCliente + "'s plan" , 50 , 10 , "ondas eletromagnéticas grátis" , 52.49 );
        Cliente cliente =
                new Cliente( nomeCliente , email , "555-5555" , plano );

        ClienteCRUD.createCliente( cliente );

        Cliente clienteComId =
                ClienteCRUD.readCliente( nomeCliente );

        System.out.println( clienteComId );

        System.out.println( ClienteCRUD.readCliente( cliente.getNome() ) );

        try {
            PlanoCRUD.deletePlano(clienteComId.getPlano().getId());
        } catch ( SQLIntegrityConstraintViolationException constraintViolationException ) {
            ClienteCRUD.deleteCliente(ClienteCRUD.readCliente(plano.getId()).getId());
            System.err.println("Removendo cliente relacionado ao plano...");
            PlanoCRUD.deletePlano(plano.getId());
        }
    }
    public static void testesServicoAdicional( String nomeServico , String nomePlano ) {

        ServicoAdicionalCRUD.createServicoAdicional(
                new ServicoAdicional( nomeServico , 550 ) );

        ServicoAdicional servicoAdicional = ServicoAdicionalCRUD.readServicoAdicional( nomeServico );

        PlanoCRUD.createPlano(
                new Plano( "aTchIM" , nomePlano , 50 , 10 , "ondas eletromagnéticas grátis" , 52.49 ));
        Plano plano = PlanoCRUD.readPlano( nomePlano );

        ServicoAdicionalCRUD.linkServicoAdicionalPlano( plano.getId(), servicoAdicional.getId() );
    }

    private static double tryNum() {
        double num = 0;
        boolean erro = true;
        do {
            try {
                num = sc.nextDouble();
                erro = false;
            } catch ( InputMismatchException e ) {
                System.out.println( "Insira um valor válido." );
                sc.next();
            }
        } while (erro);

        return num;

    }
}
