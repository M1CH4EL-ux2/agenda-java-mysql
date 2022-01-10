import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Agenda {
	
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private static final String DATETIME_FORMAT = DATE_FORMAT + " " + "HH:mm:ss";
	
	public static LocalDate stringToDate(String dateText) {
		LocalDate date = null;
		
		try {
			date = LocalDate.parse(dateText, DateTimeFormatter.ofPattern(DATE_FORMAT));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return date;
	}
	
	public static String dateToString(LocalDate dateText) {
		return dateText.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
	}
	
	public static String dateTimeToString(LocalDateTime dateText) {
		return dateText.format(DateTimeFormatter.ofPattern(DATETIME_FORMAT));
	}
	
	public Connection Connect() {
		Connection connect = null;
		
		try {
			connect = DriverManager.getConnection("jdbc:mysql://localhost/agenda", "root", "senha");
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println("Erro ao conectar ao banco de dados, verifique se suas informações estão corretas.");
		}
		
		return connect;
	}
	
	public void AdicionarContato() {
		try {
			Connection conect = Connect();
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
				AdicionarContato();
			}
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println(" ");
			System.out.println("Ocorreu algum erro ao tentar adicionar o contato, tente novamente.");
			System.out.println(" ");
		}
	}
	
	public void DeletarContato() {
		try {
			Connection conect = Connect();
			Scanner teclado = new Scanner(System.in);
			
			System.out.println("Informe o email do usuário que vai ser deletado: ");
			String email = teclado.next();
			
			PreparedStatement stmt = conect.prepareStatement("delete from contato where email = ?");
			stmt.setString(1, email);
			
			int res = stmt.executeUpdate();
			
			System.out.println(res);
			
			if(res == 1) {
				System.out.println("Contato deletado com sucesso!");
			} else {
				System.out.println("Erro ao deletar o contato... tente novamente");
				DeletarContato();
			}

		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println(" ");
			System.out.println("Ocorreu algum erro ao tentar deletar o contato, tente novamente.");
			System.out.println(" ");
		}
	}
	
	public void AtualizarContato() {
		try {
			Connection conect = Connect();
			Scanner teclado = new Scanner(System.in);
			
			System.out.println("Informe o email do contato que deseja atualizar:");
			String email = teclado.next();
			
			System.out.println("Informe o novo número deste contato:");
			int celular = teclado.nextInt();
			
			PreparedStatement stmt = conect.prepareStatement("update contato set celular = ? where email = ?");
			stmt.setInt(1, celular);
			stmt.setString(2, email);
			
			int res = stmt.executeUpdate();
			
			if(res == 1) {
				System.out.println("Contato atualizado com sucesso!");
			} else {
				System.out.println("Erro ao atualizar conttato... tente novamente");
				AtualizarContato();
			}

		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println(" ");
			System.out.println("Ocorreu algum erro ao tentar atualizar o contato, tente novamente.");
			System.out.println(" ");
		}
	}
	
	public void ConsultarContato() {
		try {
			Connection conect = Connect();
			Scanner teclado = new Scanner(System.in);
			
			System.out.println("Informe o email do contato que deseja consultar:");
			String email = teclado.next();
			
			PreparedStatement stmt = conect.prepareStatement("select * from contato where email = ?");
			stmt.setString(1, email);
			
			ResultSet res = stmt.executeQuery();
			
			if(res.next()) {
				int id = res.getInt("id");
				String nomeContato = res.getString("nome");
				String emailContato = res.getString("email");
				int celular = res.getInt("celular");
				LocalDate nascimento = res.getObject("nascimento", LocalDate.class);
				LocalDateTime cadastro = res.getObject("cadastro", LocalDateTime.class);
				
				System.out.println(" ");
				System.out.println("id: " + id);
				System.out.println("nome: " + nomeContato);
				System.out.println("email: " + emailContato);
				System.out.println("celular: " + celular);
				System.out.println("nascimento: " + dateToString(nascimento));
				System.out.println("cadastro: " + dateTimeToString(cadastro));
				System.out.println(" ");
				
			}
			
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println(" ");
			System.out.println("Ocorreu algum erro ao tentar consultar o contato, tente novamente.");
			System.out.println(" ");
		}
	}
	
	public void ListarContatos() {
		try {
			Connection conect = Connect();
			Scanner teclado = new Scanner(System.in);
			
			PreparedStatement stmt = conect.prepareStatement("select * from contato");
			
			ResultSet res = stmt.executeQuery();
			
			while(res.next()) {
				int id = res.getInt("id");
				String nomeContato = res.getString("nome");
				String emailContato = res.getString("email");
				int celular = res.getInt("celular");
				LocalDate nascimento = res.getObject("nascimento", LocalDate.class);
				LocalDateTime cadastro = res.getObject("cadastro", LocalDateTime.class);
				
				System.out.println(" ");
				System.out.println("id: " + id);
				System.out.println("nome: " + nomeContato);
				System.out.println("email: " + emailContato);
				System.out.println("celular: " + celular);
				System.out.println("nascimento: " + dateToString(nascimento));
				System.out.println("cadastro: " + dateTimeToString(cadastro));
				System.out.println(" ");
				
			}
			
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println(" ");
			System.out.println("Ocorreu algum erro ao tentar listar os contatos, tente novamente.");
			System.out.println(" ");
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
				 crud.AdicionarContato();
				 continue;
			 } else if(opcao == 2) {
				 crud.DeletarContato();
				 continue;
			 } else if(opcao == 3) {
				 crud.AtualizarContato();
				 continue;
			 } else if(opcao == 4) {
				 crud.ConsultarContato();
				 continue;
			 } else if(opcao == 5) {
				 crud.ListarContatos();
				 continue;
			 }
		 }
	}
}
