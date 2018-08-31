package admin;

import java.util.ArrayList;
 
import model.User;
import sql.DbManager;

public class UserAdmin {
	
	static void prompt(){
		while(true){
			System.out.println("Wybierz jedną z opcji list (lista użytkowników), add (dodanie użytkownika), edit (edycja użytkownika), delete (edycja użytkownika), quit (zakończenie programu):");
			String action = Application.scaner.nextLine();
			if(action.equals("add")){
				addNewUserAction();
			}else if(action.equals("list")){
				listUsersAction();
			}else if(action.equals("edit")){
				editUserAction();
			}else if(action.equals("delete")){
				removeUserAction();
			}else if(action.equals("quit")){
				System.out.println("Koniec");
				closeApplication();
			}else{
				System.out.println("Podałeś nieprawidłową komendę spróbuj ponownie.");
			}
		}
	}
	static void listUsersAction(){
		ArrayList<User> users = User.loadAll();
		for(User _user: users){
			System.out.println(_user);
		}
	}
	static void addNewUserAction(){
		System.out.println("Dodanie użytkownika");

		String username = promptString(Type.USERNAME);
		String email = promptString(Type.EMAIL);
		String password = promptString(Type.PASSWORD);
		int group_id = promptInt(Type.GROUP_ID);
		Application.scaner.nextLine();
		User user = new User(username,email,password,group_id);
		user.saveToDB();

		System.out.println("Dodano użytkownika");
	}
	static void editUserAction(){
		System.out.println("Edycja");
		int id = promptInt(Type.USER_ID);
		Application.scaner.nextLine();
		String username = promptString(Type.USERNAME);
		String email = promptString(Type.EMAIL);
		String password = promptString(Type.PASSWORD);
		int group_id = promptInt(Type.GROUP_ID);
		Application.scaner.nextLine();
		User user = User.loadById(id);
		if(!email.isEmpty()) user.setEmail(email);
		if(!password.isEmpty()) user.setPassword(password);
		if(group_id!=0) user.setPersonGroupId(group_id);
		if(!username.isEmpty()) user.setUsername(username);
		user.saveToDB();
		System.out.println("Edycja zakończona");
	}
	static void removeUserAction(){
		System.out.println("Usuń");
		int id = promptInt(Type.USER_ID);
		Application.scaner.nextLine();
		if(id!=0  && Application.areYouSurePrompt()){
			User user = User.loadById(id);
			user.delete();
			System.out.println("Usunięto użytkownika");
		}
	}
	public static String promptString(Type type){
		switch (type) {
			case USERNAME:
				System.out.println("Podaj nazwę użytkownika:");
				break;
			case EMAIL:
				System.out.println("Podaj email użytkownika:");
				break;
			case PASSWORD:
				System.out.println("Podaj hasło użytkownik:");
				break;
			default:
				break; 
		}
		return Application.scaner.nextLine();
	} 
	public static int promptInt(Type type){
		switch (type) {
			case GROUP_ID:
				System.out.println("Podaj id grupy:");
				break;
			case USER_ID:
				System.out.println("Podaj id użytkownika:");
				break;
			default:
				break; 
		}
		while(!Application.scaner.hasNextInt()){
			Application.scaner.next();
			System.out.println("Podałeś nieprawidłowe dane. Spróbuj ponownie");
		} 
		return Application.scaner.nextInt();
	} 
	public static boolean areYouSurePrompt(){
		boolean areYouSure = false;
		boolean isCorrect = false;
		while(!isCorrect){
			System.out.println("Czy na pewno chcesz usunąć rekord (T/n)?");
			if(Application.scaner.hasNextLine()){
				String action = Application.scaner.nextLine();
				if(action.equals("T")){
					isCorrect = true;
					areYouSure = true;
				}else if(action.equals("n")){
					isCorrect = true;
					areYouSure = false;
				}else{
					System.out.println("Podałeś nieprawidłową akcję. Spróbuj ponownie.");
				}
			}
		}
		return areYouSure;
	}
	static void closeApplication(){
		DbManager.close();
		System.exit(0);
	}
	private enum Type{
		USERNAME,
		EMAIL,
		PASSWORD,
		GROUP_ID,
		USER_ID
		
	}
}
