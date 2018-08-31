package admin;

import java.util.ArrayList;

import model.Group; 

public class GroupAdmin {
	
	static void prompt(){
		while(true){
			System.out.println("Wybierz jedną z opcji list (lista grup), add (dodanie grupy), edit (edycja grupy), delete (usunięcie grupy), quit (zakończenie programu):");
			String action = Application.scaner.nextLine();
			if(action.equals("add")){
				addGroupAction();
			}else if(action.equals("list")){
				listGroupsAction();
			}else if(action.equals("edit")){
				editGroupAction();
			}else if(action.equals("delete")){
				removeGroupAction();
			}else if(action.equals("quit")){
				System.out.println("Koniec");
				Application.closeApplication();
			}else{
				System.out.println("Podałeś nieprawidłową komendę spróbuj ponownie.");
			}
		}
	}
	static void listGroupsAction(){
		ArrayList<Group> ex = Group.loadAll();
		for(Group _ex: ex){
			System.out.println(_ex);
		}
	}
	static void addGroupAction(){
		System.out.println("Dodanie grupy");

		String title = promptString(Type.TITLE); 
		Group ex = new Group(title);
		ex.saveToDB();

		System.out.println("Dodano użytkownika");
	}
	static void editGroupAction(){
		System.out.println("Edycja");
		int id = promptInt(Type.ID);
		Application.scaner.nextLine();
		String title = promptString(Type.TITLE); 
		Group ex = Group.loadById(id);
		if(!title.isEmpty()) ex.setName(title); 
		ex.saveToDB();
		System.out.println("Edycja zakończona");
	}
	static void removeGroupAction(){
		System.out.println("Usuń");
		int id = promptInt(Type.ID);
		Application.scaner.nextLine();
		if(id!=0 && Application.areYouSurePrompt()){
			Group ex = Group.loadById(id);
			ex.delete();
			System.out.println("Usunięto grupę");
		}
	}
	public static String promptString(Type type){
		switch (type) {
			case TITLE:
				System.out.println("Podaj nazwę grupy:");
				break;  
			 
			default:
				break; 
		}
		return Application.scaner.nextLine();
	} 
	public static int promptInt(Type type){
		switch (type) {
			case ID:
				System.out.println("Podaj id grupy:");
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
	
	private enum Type{
		ID,
		TITLE,
		DESCRIPTION
	}
}
