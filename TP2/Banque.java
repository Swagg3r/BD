import java.sql.*;

public class Banque {
  
	static Connection conn;

	static final String CONN_URL = "jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:ufrima";
	static final String USER = "thebauda";
	static final String PASSWD = "bd2015";

	public static final String COLOR_ON = "\u001B[31m";
	public static final String COLOR_OFF = "\u001B[0m";

	private static void menu() {
		System.out.println("*** Choisir une action a effectuer : ***");
		System.out.println("0 : Quitter");
		System.out.println("1 : Afficher la liste des animaux");
		System.out.println("2 : Deplacer un animal de cage");
		System.out.println("3 : Ajouter une maladie");
	}

	private static void listeAnimaux() throws SQLException {

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM LesAnimaux");

		System.out.println("---------------------------------------------------------------------------------------------------------------------");

		//Affichage attributs de la table
		ResultSetMetaData  resultSetMetaData = rs.getMetaData();
		int  nCols = resultSetMetaData.getColumnCount ();
		for (int i=1; i<=nCols; i++){
			System.out.print(String.format("%-15s", resultSetMetaData.getColumnName(i)));
		}
		System.out.println("\n---------------------------------------------------------------------------------------------------------------------");

		//Affichage contenu de la table
		while (rs.next()) {
			System.out.println(String.format("%-15s%-15s%-15s%-15s%-15s%-15s%-15s%-15s",
									rs.getString("nomA"),
									rs.getString("sexe"),
			    					rs.getString("type_an"),		
			    					rs.getString("fonction_cage"),
			    					rs.getString("pays"),
			    					rs.getString("annais"),
			    					rs.getString("nocage"),
			    					rs.getString("nb_maladies")));
		}	

		System.out.println("---------------------------------------------------------------------------------------------------------------------\n");
	}

	private static void deplacerAnimal() throws SQLException {

		System.out.println("Quel animal voulez-vous déplacer ? (saisir \"liste\" pour afficher la liste des animaux)");
		String animal= LectureClavier.lireChaine();
		if (animal.equals("liste")) { 
			listeAnimaux();
			System.out.println("Alors ?!");
			animal= LectureClavier.lireChaine();
		}
		int numCage = LectureClavier.lireEntier("Dans quelle cage ?");
		Statement stmt = conn.createStatement();

		if (stmt.executeUpdate("UPDATE LesAnimaux SET nocage = " + numCage + " WHERE nomA = " + animal) == 0) {
			System.out.println("La mise à jour a échoué");
		}
	} 

	private static void ajouterMaladie() throws SQLException {
		// A COMPLETER
	}   


	private static void commit() throws SQLException {
		// A COMPLETER0

	}       

	private static void rollback() throws SQLException {
		// A COMPLETER
	}   

	private static void getIsolation() throws SQLException {
		// A COMPLETER
	}

	private static void setIsolation() throws SQLException {
		// A COMPLETER
	} 

	public static void main(String args[]) {

	  	try {
		    int action;
		    boolean exit = false;

		    // Enregistrement du driver Oracle
		    System.out.print("Loading Oracle driver... "); 
		    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		    System.out.println("loaded");
		    
		    // Etablissement de la connection
		    System.out.print("Connecting to the database... "); 
		    conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
		    System.out.println("connected");
		    
		    // Desactivation de l'autocommit
		    conn.setAutoCommit(false);
		    System.out.println("Autocommit disabled");

		    while(!exit) {
		      menu();
		      action = LectureClavier.lireEntier("votre choix ?");
		      switch(action) {
		        case 0 : exit = true; break;
		        case 1 : listeAnimaux(); break;
		        case 2 : deplacerAnimal(); break;
		        case 3 : ajouterMaladie(); break;
		        default : System.out.println("=> choix incorrect"); menu();
		      }
		    }       

		    // Liberation des ressources et fermeture de la connexion...
		    conn.close();
		    System.out.println("au revoir");
	    
		// traitement d'exception
		} catch (SQLException e) {
			System.err.println("failed");
			System.out.println("Affichage de la pile d'erreur");
			e.printStackTrace(System.err);
			System.out.println("Affichage du message d'erreur");
			System.out.println(e.getMessage());
			System.out.println("Affichage du code d'erreur");
			System.out.println(e.getErrorCode());     
		}
	}


}