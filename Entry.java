package coisas_e_coisas;

public class Entry {
	public static void testCustomer() {
		System.out.println("> Teste de clientes: Pedro de Lara");
		Customer pedro_de_lara = Customer.findOne(1);
		
		System.out.println("> Checando atributos... ");
		System.out.println("Esperava CPF 111, recebi " + pedro_de_lara.CPF);
		
		System.out.println("> Mudando nome e sobrenome...");
		pedro_de_lara.name = "Lara";
		pedro_de_lara.surname = "de Pedro";
		pedro_de_lara.save();
		System.out.println("> Esperava Lara, recebi " + pedro_de_lara.name);
		System.out.println("> Esperava de Pedro, recebi " + pedro_de_lara.surname);
		
		System.out.println("> Criando um novo cliente ");
		Customer ronaldinho = new Customer("222", "Ronaldinho", "Gaúcho");
		ronaldinho.save();
		
		System.out.println("Esperava CPF 222, recebi " + Customer.findOne(2).CPF);
		System.out.println("Deletando usuário carregado...");
		ronaldinho.delete();
	}
	

	public static void main(String[] args) {
		Entry.testCustomer();
	}
}
