package admin;

import java.util.ArrayList;

import model.Excercise; 

public class ExcerciseAdmin {
	
	static void prompt(){
		while(true){
			System.out.println("Wybierz jedną z opcji list (lista ćwiczeń), add (dodanie ćwiczenia), edit (edycja ćwiczenia), delete (usunięcie ćwiczenia), quit (zakończenie programu):");
			String action = Application.scaner.nextLine();
			if(action.equals("add")){
				addExcerciseAction();
			}else if(action.equals("list")){
				listExcercisesAction();
			}else if(action.equals("edit")){
				editExcerciseAction();
			}else if(action.equals("delete")){
				removeExcerciseAction();
			}else if(action.equals("quit")){
				System.out.println("Koniec");
				Application.closeApplication();
			}else{
				System.out.println("Podałeś nieprawidłową komendę spróbuj ponownie.");
			}
		}
	}
	static void listExcercisesAction(){
		ArrayList<Excercise> ex = Excercise.loadAll();
		for(Excercise _ex: ex){
			System.out.println(_ex);
		}
	}
	static void addExcerciseAction(){
		System.out.println("Dodanie ćwiczenia");

		String title = promptString(Type.TITLE);
		String description = promptString(Type.DESCRIPTION);
		Excercise ex = new Excercise(title,description);
		ex.saveToDB();

		System.out.println("Dodano użytkownika");
	}
	static void editExcerciseAction(){
		System.out.println("Edycja");
		int id = promptInt(Type.ID);
		Application.scaner.nextLine();
		String title = promptString(Type.TITLE);
		String description = promptString(Type.DESCRIPTION);
		Excercise ex = Excercise.loadById(id);
		if(!title.isEmpty()) ex.setTitle(title);
		if(!description.isEmpty()) ex.setDescription(description);
		ex.saveToDB();
		System.out.println("Edycja zakończona");
	}
	static void removeExcerciseAction(){
		System.out.println("Usuń");
		int id = promptInt(Type.ID);
		Application.scaner.nextLine();
		if(id!=0 && Application.areYouSurePrompt()){
			Excercise ex = Excercise.loadById(id);
			ex.delete();
			System.out.println("Usunięto ćwiczenie");
		}
	}
	public static String promptString(Type type){
		switch (type) {
			case TITLE:
				System.out.println("Podaj nazwę ćwiczenia:");
				break;
			case DESCRIPTION:
				System.out.println("Podaj opis ćwiczenia:");
				break;
			 
			default:
				break; 
		}
		return Application.scaner.nextLine();
	} 
	public static int promptInt(Type type){
		switch (type) {
			case ID:
				System.out.println("Podaj id ćwiczenia:");
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
