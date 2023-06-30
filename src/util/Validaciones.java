package util;

public class Validaciones {
	
	public static void nick(String nick) throws IllegalArgumentException{
		try{
			noVacio(nick);
			soloLetrasYNumerosSinEspacios(nick);
		}
		catch(IllegalArgumentException e){
			throw new IllegalArgumentException("El nick"+e.getMessage());
		}
	}
	
	public static void password(String password) throws IllegalArgumentException{
		try{
			noVacio(password);
			longitudMayorOIgualQue(password, 8);
		}
		catch(IllegalArgumentException e){
			throw new IllegalArgumentException("La contrase�a"+e.getMessage());
		}
	}
	
	public static void tema (String tema){
		try{
			noVacio(tema);
			soloLetrasNumerosSignos(tema);
		}
		catch(IllegalArgumentException e){
			throw new IllegalArgumentException("El tema"+e.getMessage());
		}
	}
	
	public static void lineaInvestigacion (String lineaInvestigacion){
		try{
			noVacio(lineaInvestigacion);
			soloLetrasYNumeros(lineaInvestigacion);
		}
		catch(IllegalArgumentException e){
			throw new IllegalArgumentException("La l�nea de investigaci�n"+e.getMessage());
		}
	}
	
	private static void longitudMayorOIgualQue(String cadena, int longitud) throws IllegalArgumentException{
		if(cadena.length()<longitud)
			throw new IllegalArgumentException(" debe tener al menos "+longitud+" caracteres");
	}
	
	/*private static void soloLetras(String cadena) throws IllegalArgumentException{
		boolean soloLetras = true;
		for(int i=0; i<cadena.length() && soloLetras; i++)
			if(!Character.isSpaceChar(cadena.charAt(i)) && !Character.isLetter(cadena.charAt(i)))
				soloLetras = false;
		if(!soloLetras)
			throw new IllegalArgumentException(" s�lo puede tener letras");
	}*/
	
	private static void soloLetrasYNumerosSinEspacios(String cadena) throws IllegalArgumentException{
		for(int i=0; i<cadena.length(); i++)
			if(!Character.isDigit(cadena.charAt(i)) && !Character.isLetter(cadena.charAt(i)) &&
					!((Character)cadena.charAt(i)).equals('_') && !((Character)cadena.charAt(i)).equals('.'))
				throw new IllegalArgumentException(" s�lo puede tener letras y n�meros");
	}
	
	private static void soloLetrasNumerosSignos(String cadena) throws IllegalArgumentException{
		for(int i=0; i<cadena.length(); i++)
			if(!Character.isSpaceChar(cadena.charAt(i)) && !Character.isDigit(cadena.charAt(i)) &&
					!Character.isLetter(cadena.charAt(i)) && !((Character)cadena.charAt(i)).equals('-') &&
					!((Character)cadena.charAt(i)).equals(',') && !((Character)cadena.charAt(i)).equals(':'))
				throw new IllegalArgumentException(" s�lo puede tener letras, n�meros, signos y guiones");
	}
	
	private static void soloLetrasYNumeros(String cadena) throws IllegalArgumentException{
		for(int i=0; i<cadena.length(); i++)
			if(!Character.isSpaceChar(cadena.charAt(i)) && !Character.isDigit(cadena.charAt(i)) &&
					!Character.isLetter(cadena.charAt(i)))
				throw new IllegalArgumentException(" s�lo puede tener letras y n�meros");
	}
	
	private static void noVacio(String cadena) throws IllegalArgumentException{
		if(cadena.length()>0){
			boolean nombreVacio = true;
			for(int i=0; i<cadena.length() && nombreVacio; i++)
				if(!Character.isSpaceChar(cadena.charAt(i)))
					nombreVacio = false;
			if(nombreVacio)
				throw new IllegalArgumentException(" est� vac�o");
		}
		else
			throw new IllegalArgumentException(" est� vac�o");
	}
}
