package com.exercicios.bancario.exercicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.exercicios.bancario.entity.Account;
import com.exercicios.bancario.entity.AccountEnum;
import com.exercicios.bancario.entity.Client;
import com.exercicios.bancario.mock.BankService;
import com.exercicios.bancario.mock.MaxAccountCountryBean;
import com.exercicios.bancario.mock.ServiceFactory;

/**
 * OBSERVAÇÕES: 
 * NÃO é permitido o uso de nenhuma estrutura de repetição (for, while, do-while).
 * Tente, ao máximo, evitar o uso das estruturas if, else if, else e switch-case
 * 
 * @author Bruno Konzen Stahl
 * @author Victor Lira
 */
public class ExerciciosResolvidos {

	private static BankService service = ServiceFactory.getService();

	/**
	 * Exemplos de chamadas para cada método da lista de exercícios
	 * Executar apenas um por vez
	 */
	public static void main(String[] args) {

		//imprimirNomesClientes();
		//imprimirMediaSaldos();
		imprimirPaisClienteMaisRico(); 
		//imprimirSaldoMedio(1);
		//imprimirClientesComPoupanca();
		//getEstadoClientes(1);
		//getNumerosContas("Brazil");
		//getMaiorSaldo("client4@bank.com");
		//sacar(1, 1, 50);
		//depositar("Brazil",50);
		//transferir(1, 1, 1, 50); 
		//getContasConjuntas();
		//getSomaContasEstado("1");
		//getEmailsClientesContasConjuntas();
		//isPrimo(40);
		//getFatorial(9);
	}

	/**
	 * 1. Imprima na tela o nome e e-mail de todos os clientes (sem repetição), usando o seguinte formato:
	 * Victor Lira - vl@cin.ufpe.br
	 */
	public static void imprimirNomesClientes() {
		service
		.listClients()
		.stream()
		.map(cliente -> cliente.getName() +" - "+ cliente.getEmail())
		.distinct()
		.forEach(System.out::println);
	}

	/**
	 * 2. Imprima na tela o nome do cliente e a média do saldo de suas contas, ex:
	 * Victor Lira - 352
	 */
	public static void imprimirMediaSaldos() {
		service.listClients()
		.stream()
		.map(c ->
		String.join(
				" - ",
				c.getName(),
				String.valueOf(c.getAccounts().stream().mapToDouble(Account::getBalance).average().orElse(0))
				)
				).forEach(System.out::println);
	}

	/**
	 * 3. Considerando que só existem os países "Brazil" e "United States", imprima
	 * na tela qual deles possui o cliente mais rico, ou seja, com o maior saldo
	 * somando todas as suas contas.
	 */
	public static void imprimirPaisClienteMaisRico() {
		Map<Object, List<Client>> grouped = service.listClients().stream()
				.collect(Collectors.groupingBy((client) -> client.getAddress().getCountry()));

		MaxAccountCountryBean maxAccountCountry = new MaxAccountCountryBean();
		List<MaxAccountCountryBean> listCountriesWithMaxBalance = new ArrayList<>();

		grouped.keySet().stream().forEach((country) -> {
			Double maxAccount = grouped.get(country).stream().mapToDouble((c) -> getSumAccounts(c.getAccounts())).max()
					.orElse(0);
			if (maxAccount > maxAccountCountry.getMaxValueAccount()) {
				maxAccountCountry.setCountry(String.valueOf(country));
				maxAccountCountry.setMaxValueAccount(maxAccount);
			}
			listCountriesWithMaxBalance.add(new MaxAccountCountryBean(String.valueOf(country), maxAccount));
		});

		listCountriesWithMaxBalance.forEach(System.out::println);

		System.out.print(maxAccountCountry);
	}

	private static Double getSumAccounts(List<Account> accounts) {
		return accounts.stream().mapToDouble(Account::getBalance).sum();
	}
	
	/**
	 * 4. Imprime na tela o saldo médio das contas da agência
	 * @param agency
	 */
	public static void imprimirSaldoMedio(int agency) {	
		double average = 
				service
				.listAccounts()
				.stream()
				.filter(conta -> conta.getAgency() == agency)
				.mapToDouble(conta -> conta.getBalance())
				.average()
				.getAsDouble();
		System.out.println(average);
	}

	/**
	 * 5. Imprime na tela o nome de todos os clientes que possuem conta poupança (tipo SAVING)
	 */
	public static void imprimirClientesComPoupanca() {
		service
		.listAccounts()
		.stream()
		.filter(conta -> conta.getType().equals(AccountEnum.SAVING))
		.map(conta -> conta.getClient())
		.distinct()
		.map(cliente -> cliente.getName())
		.forEach(System.out::println);
	}

	/**
	 * 6.
	 * @param agency
	 * @return Retorna uma lista de Strings com o "estado" de todos os clientes da agência
	 */
	public static List<String> getEstadoClientes(int agency) {
		List<String> stateOfAllClients = 
				service
				.listAccounts()
				.stream()
				.filter(conta -> conta.getAgency() == agency)
				.map(conta -> conta.getClient().getAddress().getState())
				.collect(Collectors.toList());
		return (List<String>) stateOfAllClients;
	}

	/**
	 * 7.
	 * @param country
	 * @return Retorna uma lista de inteiros com os números das contas daquele país
	 */
	public static int[] getNumerosContas(String country) {
		int [] countryNumbers =
				service
				.listAccounts()
				.stream()
				.filter(conta -> conta.getClient().getAddress().getCountry().equals(country))
				.mapToInt(conta -> conta.getNumber()).toArray();
		return (int[]) countryNumbers;
	}

	/**
	 * 8.
	 * Retorna o somatório dos saldos das contas do cliente em questão 
	 * @param clientEmail
	 * @return
	 */
	public static double getMaiorSaldo(String clientEmail) {
		double sumBalance =
				service
				.listAccounts()
				.stream()
				.filter(conta -> conta.getClient().getEmail().equals(clientEmail))
				.mapToDouble(conta -> conta.getBalance())
				.sum();
		return sumBalance;
	}

	/**
	 * 9.
	 * Realiza uma operação de saque na conta de acordo com os parâmetros recebidos
	 * @param agency
	 * @param number
	 * @param value
	 */
	public static void sacar(int agency, int number, double value) {
		service
		.listAccounts()
		.stream()
		.filter(conta -> conta.getAgency( )== agency &&
		conta.getNumber() == number)
		.map(conta -> conta.getBalance() - value);
	}

	/**
	 * 10. Realiza um deposito para todos os clientes do país em questão	
	 * @param country
	 * @param value
	 */
	public static void depositar(String country, double value) {
		service
		.listAccounts()
		.stream()
		.filter(conta -> conta.getClient().getAddress().getCountry().equals(country))
		.map(conta -> conta.getBalance() + value);
	}

	/**
	 * 11. Realiza uma transferência entre duas contas de uma agência.
	 * @param agency - agência das duas contas
	 * @param numberSource - conta a ser debitado o dinheiro
	 * @param numberTarget - conta a ser creditado o dinheiro
	 * @param value - valor da transferência
	 */
	public static void transferir(int agency, int numberSource, int numberTarget, double value) {
		service
		.listAccounts()
		.stream()
		.filter(conta -> conta.getAgency() == agency &&
		conta.getNumber() == numberSource)
		.map(conta -> conta.getBalance() - value);
		service
		.listAccounts()
		.stream()
		.filter(conta -> conta.getAgency() == agency &&
		conta.getNumber() == numberTarget)
		.map(conta -> conta.getBalance() + value);
	}

	/**
	 * 12.
	 * @param clients
	 * @return Retorna uma lista com todas as contas conjuntas (JOINT) dos clientes
	 */
	public static List<Account> getContasConjuntas(List<Client> clients) {
		List<Account> jointAccounts = new ArrayList<Account>();
		service
		.listAccounts()
		.stream()
		.filter(conta -> conta.getClient().equals(clients))
		.map(conta -> conta.getType().equals("JOINT"))
		.collect(Collectors.toList());
		return jointAccounts;
	}

	/**
	 * 13.
	 * @param state
	 * @return Retorna uma lista com o somatório dos saldos de todas as contas do estado 
	 */
	public static double getSomaContasEstado(String state) {
		double sumAccountState =
				service
				.listAccounts()
				.stream()
				.filter(conta -> conta.getClient().getAddress().getState().equals(state))
				.mapToDouble(conta -> conta.getBalance())
				.sum();
		return sumAccountState;
	}

	/**
	 * 14.
	 * @return Retorna um array com os e-mails de todos os clientes que possuem contas conjuntas
	 */
	public static String[] getEmailsClientesContasConjuntas() {
		List<String> emailsAllClientsJoinAccounts =
				service
				.listClients()
				.stream()
				.filter(cliente -> cliente.getAccounts().equals(AccountEnum.JOINT))
				.map(cliente -> cliente.getEmail())
				.collect(Collectors.toList());
		return (String []) emailsAllClientsJoinAccounts.toArray();

	}

	/**
	 * 15.
	 * @param number
	 * @return Retorna se o número é primo ou não
	 */
	public static boolean isPrimo(int number) {
		return IntStream
				.rangeClosed(2, (number/2))
				.noneMatch(i -> number % i==0);
	}

	/**
	 * 16.
	 * @param number
	 * @return Retorna o fatorial do número
	 */
	public static int getFatorial(int number) {
		int factorial =
				IntStream.rangeClosed(1, number)
				.reduce(1, (x,y) -> x*y);
		return factorial;
	}
}
