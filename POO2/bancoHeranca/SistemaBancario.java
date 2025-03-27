import java.util.InputMismatchException;
import java.util.Scanner;

public class SistemaBancario {
    
    public static void menu() {
        System.out.println(
            "1- CONTA BANCARIA\n" +
            "2- CONTA CORRENTE\n" +
            "3- CONTA CORRENTE PREMIUM\n" +
            "4- CONTA CORRENTE EMPRESARIAL\n" +
            "5- CONTA POUPANÇA\n" +
            "6- CONTA POUPANÇA ESTUDANTIL\n" +
            "7- SAIR\n"
        );
    }
    
    public static void subMenuCB() {
        System.out.println(
            "1- SACAR\n" +
            "2- DEPOSITAR\n" +
            "3- EXIBIR SALDO\n" +
            "4- VOLTAR\n"
        );
    }
    
    public static void subMenuCC() {
        System.out.println(
            "1- SACAR\n" +
            "2- DEPOSITAR\n" +
            "3- EXIBIR SALDO\n" +
            "4- EXIBIR LIMITE\n" +
            "5- VOLTAR\n"
        );
    }
    
    public static void subMenuCCP() {
        System.out.println(
            "1- SACAR\n" +
            "2- DEPOSITAR\n" +
            "3- EXIBIR SALDO\n" +
            "4- EXIBIR LIMITE\n" +
            "5- EXIBIR BENEFICIO PREMIUM\n" +
            "6- VOLTAR\n"
        );
    }
    
    public static void subMenuCCE() {
        System.out.println(
            "1- SACAR\n" +
            "2- DEPOSITAR\n" +
            "3- EXIBIR SALDO\n" +
            "4- EXIBIR LIMITE\n" +
            "5- SOLICITAR EMPRESTIMO\n" +
            "6- VOLTAR\n"
        );
    }
    
    public static void subMenuCP() {
        System.out.println(
            "1- SACAR\n" +
            "2- DEPOSITAR\n" +
            "3- EXIBIR SALDO\n" +
            "4- APLICAR RENDIMENTO\n" +
            "5- VOLTAR\n"
        );
    }
    
    public static void subMenuCPE() {
        System.out.println(
            "1- SACAR\n" +
            "2- DEPOSITAR\n" +
            "3- EXIBIR SALDO\n" +
            "4- APLICAR RENDIMENTO\n" +
            "5- EXIBIR LIMITE ISENÇÃO\n" +
            "6- VOLTAR\n"
        );
    }

    public static boolean sacar(ContaBancaria conta, double valorSaque, Scanner ler) {
        boolean precoValido = false;
        
        do{
            try{
                System.out.print("Digite o valor a sacar (ou 0 para voltar): ");
                valorSaque = ler.nextDouble();
                if (valorSaque == 0) break;

                if(conta.sacar(valorSaque)){
                    System.out.println("Saque de R$" + valorSaque + " realizado com sucesso.");
                    precoValido = true;
                }

            } catch(InputMismatchException e) {
                System.out.println("Valor inválido.");
                ler.nextLine();
                return false;
            } catch(MinhasExcecoes e) {
                System.out.println(e.getMessage());
                System.out.println(conta.exibeSaldo());
                return false;
            }
        }while(!precoValido);
        return true;
    }

    public static void depositar(ContaBancaria conta, double valorSaque) {
        conta.depositar(valorSaque);
    }

    public static void exibirSaldo(ContaBancaria conta) {
        System.out.println("Saldo atual: R$ " + conta.exibeSaldo());
    }

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        ContaBancaria contaBancaria = new ContaBancaria("João Silva", 1000.00);
        ContaCo contaCorrente = new ContaCo("Maria Souza", 2000.00, 500.00);
        ContaCoPremium contaPremium = new ContaCoPremium("Carlos Oliveira", 5000.00, 1000.00, 3);
        ContaCoEmpresarial contaEmpresarial = new ContaCoEmpresarial("Empresa XYZ Ltda", 10000.00, 2000.00, 2.0);
        ContaPo contaPoupanca = new ContaPo("Ana Santos", 3000.00, 0.5);
        ContaPoEstudantil contaEstudantil = new ContaPoEstudantil("Pedro Alves", 1500.00, 0.3, 100.00);

        double valor = 0;
        boolean continuar = false;
        
        boolean sairSubMenu = false;
        boolean sair = false;
        while (!sair) {
            menu();
            int opcao = ler.nextInt();
            switch (opcao) {
                case 1:
                    sairSubMenu = false;
                    while (!sairSubMenu) {
                        subMenuCB();
                        int opcaoSub = ler.nextInt();
                        switch (opcaoSub) {
                            case 1:
                                do {
                                    continuar = sacar(contaBancaria, valor, ler);
                                } while (!continuar);
                                break;
                            case 2:
                                System.out.print("Digite o valor a depositar: ");
                                valor = ler.nextDouble();
                                depositar(contaBancaria, valor);
                                break;
                            case 3:
                                exibirSaldo(contaBancaria);
                                break;
                            case 4:
                                sairSubMenu = true;
                                break;
                            default:
                                System.out.println("Opção inválida!");
                        }
                    }
                    break;
        
                case 2:
                    sairSubMenu = false;
                    while (!sairSubMenu) {
                        subMenuCC();
                        int opcaoSub = ler.nextInt();
                        switch (opcaoSub) {
                            case 1:
                                do {
                                    continuar = sacar(contaCorrente, valor, ler);
                                } while (!continuar);
                                break;
                            case 2:
                                System.out.print("Digite o valor a depositar: ");
                                valor = ler.nextDouble();
                                depositar(contaCorrente, valor);
                                break;
                            case 3:
                                exibirSaldo(contaCorrente);
                                break;
                            case 4:
                                System.out.println(contaCorrente.exibeLimiteChequeEspecial());
                                break;
                            case 5:
                                sairSubMenu = true;
                                break;
                            default:
                                System.out.println("Opção inválida!");
                        }
                    }
                    break;
        
                case 3:
                    sairSubMenu = false;
                    while (!sairSubMenu) {
                        subMenuCCP();
                        int opcaoSub = ler.nextInt();
                        switch (opcaoSub) {
                            case 1:
                                do {
                                    continuar = sacar(contaPremium, valor, ler);
                                } while (!continuar);
                                break;
                            case 2:
                                System.out.print("Digite o valor a depositar: ");
                                valor = ler.nextDouble();
                                depositar(contaPremium, valor);
                                break;
                            case 3:
                                exibirSaldo(contaPremium);
                                break;
                            case 4:
                                System.out.println(contaPremium.exibeLimiteChequeEspecial());
                                break;
                            case 5:
                                System.out.println(contaPremium.exibeBeneficioPremium());
                                break;
                            case 6:
                                sairSubMenu = true;
                                break;
                            default:
                                System.out.println("Opção inválida!");
                        }
                    }
                    break;
        
                case 4:
                    sairSubMenu = false;
                    while (!sairSubMenu) {
                        subMenuCCE();
                        int opcaoSub = ler.nextInt();
                        switch (opcaoSub) {
                            case 1:
                                do {
                                    continuar = sacar(contaEmpresarial, valor, ler);
                                } while (!continuar);
                                break;
                            case 2:
                                System.out.print("Digite o valor a depositar: ");
                                valor = ler.nextDouble();
                                depositar(contaEmpresarial, valor);
                                break;
                            case 3:
                                exibirSaldo(contaEmpresarial);
                                break;
                            case 4:
                                System.out.println(contaEmpresarial.exibeLimiteChequeEspecial());
                                break;
                            case 5:
                                boolean valorValido = false;
                                do{
                                    try {
                                        System.out.print("Digite o valor do empréstimo: ");
                                        valor = ler.nextDouble();
                                        if (contaEmpresarial.solicitaEmprestimo(valor)) {
                                            System.out.println("Empréstimo concedido!");
                                            valorValido = true;
                                        } else {
                                            System.out.println("Empréstimo recusado!");
                                        }
                                    } catch (MinhasExcecoes e) {
                                        System.out.println("Erro ao processar empréstimo: " + e.getMessage());
                                    }  catch(InputMismatchException e) {
                                        System.out.println("Valor inválido.");
                                        ler.nextLine();
                                    }

                                }while (!valorValido);
                                break;
                            case 6:
                                sairSubMenu = true;
                                break;
                            default:
                                System.out.println("Opção inválida!");
                        }
                    }
                    break;
        
                case 5:
                    sairSubMenu = false;
                    while (!sairSubMenu) {
                        subMenuCP();
                        int opcaoSub = ler.nextInt();
                        switch (opcaoSub) {
                            case 1:
                                do {
                                    continuar = sacar(contaPoupanca, valor, ler);
                                } while (!continuar);
                                break;
                            case 2:
                                System.out.print("Digite o valor a depositar: ");
                                valor = ler.nextDouble();
                                depositar(contaPoupanca, valor);
                                break;
                            case 3:
                                exibirSaldo(contaPoupanca);
                                break;
                            case 4:
                                contaPoupanca.aplicarRendimento();
                                System.out.println("Rendimento aplicado!");
                                break;
                            case 5:
                                sairSubMenu = true;
                                break;
                            default:
                                System.out.println("Opção inválida!");
                        }
                    }
                    break;
        
                case 6:
                    sairSubMenu = false;
                    while (!sairSubMenu) {
                        subMenuCPE();
                        int opcaoSub = ler.nextInt();
                        switch (opcaoSub) {
                            case 1:
                                do {
                                    continuar = sacar(contaEstudantil, valor, ler);
                                } while (!continuar);
                                break;
                            case 2:
                                System.out.print("Digite o valor a depositar: ");
                                valor = ler.nextDouble();
                                depositar(contaEstudantil, valor);
                                break;
                            case 3:
                                exibirSaldo(contaEstudantil);
                                break;
                            case 4:
                                contaEstudantil.aplicarRendimento();
                                System.out.println("Rendimento aplicado!");
                                break;
                            case 5:
                                System.out.println(contaEstudantil.exibeLimiteIsencao());
                                break;
                            case 6:
                                sairSubMenu = true;
                                break;
                            default:
                                System.out.println("Opção inválida!");
                        }
                    }
                    break;
        
                case 7:
                    System.out.println("Saindo...");
                    sair = true;
                    break;
        
                default:
                    System.out.println("Opção inválida!");
            }
        }

        ler.close();
    }
}
