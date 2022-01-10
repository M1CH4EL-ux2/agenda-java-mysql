import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Agenda {
	
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	
	public static LocalDate stringToDate(String dateText) {
		LocalDate date = null;
		
		try {
			date = LocalDate.parse(dateText, DateTimeFormatter.ofPattern(DATE_FORMAT));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return date;
	}
	
	public void AddContato() {
		try {
			Connection conect = DriverManager.getConnection("jdbc:mysql://localhost/agenda", "root", "senha");
			Scanner teclado = new Scanner(System.in);
			
			System.out.println("Adicione o nome:");
			String nome = teclado.nextLine();
			
			System.out.println("Adicione o email:");
			String email = teclado.nextLine();
			
			System.out.println("Adicione o celular:");
			int celular = teclado.nextInt();
			
			System.out.println("Adicione a data de nascimento");
			LocalDate nascimento = stringToDate(teclado.next());
			
			System.out.println(nascimento);
			
			PreparedStatement stmt = conect.prepareStatement("insert into contato(nome, email, celular, nascimento) values(?, ?, ?, ?)");
			stmt.setString(1, nome);
			stmt.setString(2, email);
			stmt.setInt(3, celular);
			stmt.setObject(4, nascimento);
			
			int res = stmt.executeUpdate();
			
			if(res == 1) {
				System.out.println("Contato adicionado com sucesso");
			} else {
				System.out.println("Erro ao adicionar contato, tente novamente");
				AddContato();
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		 Scanner teclado = new Scanner(System.in);
		 
		 Agenda crud = new Agenda();
		 
		 while(true) {
			 System.out.println("0. Sair do programa");
			 System.out.println("1. Adicionar contato");
			 System.out.println("2. Remover contato");
			 System.out.println("3. Atualizar celular do contato");
			 System.out.println("4. Consultar contato");
			 System.out.println("5. Listar contatos");
			 System.out.println("  ");
			 System.out.print("Escolha uma das opções acima: ");
			 int opcao = teclado.nextInt();
			 
			 if(opcao == 0) {
				 teclado.close();
				 System.out.println("Encerrando sessão");
				 break;
			 } else if(opcao == 1) {
				 crud.AddContato();
				 continue;
			 }
		 }
	}
	
	
}
