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
			throw new IllegalArgumentException("La contraseña"+e.getMessage());
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
			throw new IllegalArgumentException("La línea de investigación"+e.getMessage());
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
			throw new IllegalArgumentException(" sólo puede tener letras");
	}*/
	
	private static void soloLetrasYNumerosSinEspacios(String cadena) throws IllegalArgumentException{
		for(int i=0; i<cadena.length(); i++)
			if(!Character.isDigit(cadena.charAt(i)) && !Character.isLetter(cadena.charAt(i)) &&
					!((Character)cadena.charAt(i)).equals('_') && !((Character)cadena.charAt(i)).equals('.'))
				throw new IllegalArgumentException(" sólo puede tener letras y números");
	}
	
	private static void soloLetrasNumerosSignos(String cadena) throws IllegalArgumentException{
		for(int i=0; i<cadena.length(); i++)
			if(!Character.isSpaceChar(cadena.charAt(i)) && !Character.isDigit(cadena.charAt(i)) &&
					!Character.isLetter(cadena.charAt(i)) && !((Character)cadena.charAt(i)).equals('-') &&
					!((Character)cadena.charAt(i)).equals(',') && !((Character)cadena.charAt(i)).equals(':'))
				throw new IllegalArgumentException(" sólo puede tener letras, números, signos y guiones");
	}
	
	private static void soloLetrasYNumeros(String cadena) throws IllegalArgumentException{
		for(int i=0; i<cadena.length(); i++)
			if(!Character.isSpaceChar(cadena.charAt(i)) && !Character.isDigit(cadena.charAt(i)) &&
					!Character.isLetter(cadena.charAt(i)))
				throw new IllegalArgumentException(" sólo puede tener letras y números");
	}
	
	private static void noVacio(String cadena) throws IllegalArgumentException{
		if(cadena.length()>0){
			boolean nombreVacio = true;
			for(int i=0; i<cadena.length() && nombreVacio; i++)
				if(!Character.isSpaceChar(cadena.charAt(i)))
					nombreVacio = false;
			if(nombreVacio)
				throw new IllegalArgumentException(" está vacío");
		}
		else
			throw new IllegalArgumentException(" está vacío");
	}
}
