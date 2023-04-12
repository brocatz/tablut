package controller;



public class Controller<T> {

	// Kind of like a singelton 
	public static Object object;
	
	public Controller(T anyClass) {
		object = anyClass;
	}
	
	
	
	
}
