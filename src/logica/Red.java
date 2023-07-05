package logica;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import util.Convert;
import cu.edu.cujae.ceis.graph.LinkedGraph;
import cu.edu.cujae.ceis.graph.edge.Edge;
import cu.edu.cujae.ceis.graph.edge.WeightedEdge;
import cu.edu.cujae.ceis.graph.interfaces.ILinkedWeightedEdgeNotDirectedGraph;
import cu.edu.cujae.ceis.graph.vertex.Vertex;
import cu.edu.cujae.ceis.tree.TreeNode;
import cu.edu.cujae.ceis.tree.binary.BinaryTreeNode;
import cu.edu.cujae.ceis.tree.general.GeneralTree;
import cu.edu.cujae.ceis.tree.iterators.general.BreadthNode;
import cu.edu.cujae.ceis.tree.iterators.general.InBreadthIteratorWithLevels;
import cu.edu.cujae.ceis.tree.iterators.general.InDepthIterator;

public final class Red{

	private static Red instance;
	private File usuarios;
	private File arcos;
	private File trab;
	private ILinkedWeightedEdgeNotDirectedGraph grafo = new LinkedGraph();
	private LinkedList<Trabajo> trabajos;
	private File arbolAmigos;
	private File arbolConexiones;
	private File lidereInvestigacion;
	private File islas;
	private File comunidades;

	private Red() throws IOException, ClassNotFoundException{
		usuarios = new File("data/USUARIOS.DAT");
		arcos = new File("data/ARCOS.DAT");
		trab = new File("data/TRABAJOS.DAT");
		arbolAmigos = new File("output/Relación jerárquica de amigos.txt");
		arbolConexiones = new File("output/Relación jerárquica de conexiones.txt");
		lidereInvestigacion = new File("output/Líderes de investigación.txt");
		islas = new File("output/Islas.txt");
		comunidades = new File("output/Comunidades.txt");
		grafo = new LinkedGraph();
		trabajos = new LinkedList<Trabajo>();

		/*crearFicheroUsuarios();
		crearFicheroTrabajos();
		crearFicheroArcos();*/

		inicializar();
	}

	public static Red getInstance() throws ClassNotFoundException, IOException{
		if(instance == null)
			instance = new Red();
		return instance;
	}

	public ILinkedWeightedEdgeNotDirectedGraph getGrafo(){
		return grafo;
	}

	public LinkedList<Trabajo> getTrabajos() {
		return trabajos;
	}

	public Vertex buscarUsuario(String n){
		Vertex aux = null;
		Iterator<Vertex> iter = grafo.getVerticesList().iterator();
		while(iter.hasNext() && aux == null){
			Vertex v = iter.next();
			if(((Usuario)v.getInfo()).getNick().equals(n))
				aux = v;
		}
		return aux;
	}

	public void eliminarTrabajo(String linea, String tema){
		Iterator<Trabajo> iter = trabajos.iterator();
		boolean eliminado = false;
		while(iter.hasNext() && !eliminado){
			Trabajo t = iter.next();
			if(t.getLineaInvestigacion().equals(linea) && t.getTema().equals(tema))
				iter.remove();
		}
	}

	public boolean yaExiste(String n){
		boolean yaExiste = false;
		Iterator<Vertex> iter = grafo.getVerticesList().iterator();
		while(iter.hasNext() && !yaExiste){
			Vertex v = iter.next();
			if(((Usuario)v.getInfo()).getNick().equals(n))
				yaExiste = true;
		}
		return yaExiste;
	}

	public boolean existeTrabajo(String linea, String tema){
		boolean existe = false;
		Iterator<Trabajo> iter = trabajos.iterator();
		while(iter.hasNext() && !existe){
			Trabajo t = iter.next();
			if(t.getLineaInvestigacion().equals(linea) && t.getTema().equals(tema))
				existe = true;
		}
		return existe;
	}

	public boolean sonAmigos(Vertex v1, Vertex v2){
		boolean amigos = false;
		LinkedList<Vertex> amigosV1 = v1.getAdjacents();
		if(amigosV1.contains(v2))
			amigos = true;
		return amigos;
	}

	public int calcularPeso(int pos1, int pos2){
		int peso = 0;
		Usuario u1 = (Usuario)grafo.getVerticesList().get(pos1).getInfo();
		Usuario u2 = (Usuario)grafo.getVerticesList().get(pos2).getInfo();
		Iterator<Trabajo> iter = trabajos.iterator();
		while(iter.hasNext()){
			Trabajo t = iter.next();
			boolean contU1 = false, contU2 = false;
			for(int i=0; i<t.getAutores().size() && (!contU1 || !contU2); i++){
				if(t.getAutores().get(i).getNick().equals(u1.getNick()))
					contU1 = true;
				else if(t.getAutores().get(i).getNick().equals(u2.getNick()))
					contU2 = true;
			}
			if(contU1 && contU2)
				peso++;
		}
		return peso;
	}

	public GeneralTree<Usuario> obtenerRelacionJerDeAmigos(Vertex v){
		GeneralTree<Usuario> arbol = new GeneralTree<Usuario>();
		Usuario raiz = (Usuario)v.getInfo();
		arbol.setRoot(new BinaryTreeNode<Usuario>(raiz));
		LinkedList<Edge> arcos = v.getEdgeList();
		agregarNodosAmigos(arbol, arcos, null);
		try {
			actualizarFicheroArbolAmigos(arbol);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arbol;
	}
	private void agregarNodosAmigos(GeneralTree<Usuario> arbol, LinkedList<Edge> arcos, Integer superior){
		int mayor = -1;
		ArrayList<Usuario> agregar = new ArrayList<Usuario>();
		Iterator<Edge> iter = arcos.iterator();
		if(superior == null){
			while(iter.hasNext()){
				WeightedEdge edge = (WeightedEdge)iter.next();
				int peso = (int)edge.getWeight();
				if(peso>mayor){
					mayor = peso;
					agregar.clear();
					agregar.add((Usuario)edge.getVertex().getInfo());
				}
				else if(peso==mayor)
					agregar.add((Usuario)edge.getVertex().getInfo());
			}
		}
		else{
			while(iter.hasNext()){
				WeightedEdge edge = (WeightedEdge)iter.next();
				int peso = (int)edge.getWeight();
				if(peso>mayor && peso<superior){
					mayor = peso;
					agregar.clear();
					agregar.add((Usuario)edge.getVertex().getInfo());
				}
				else if(peso==mayor)
					agregar.add((Usuario)edge.getVertex().getInfo());
			}
		}
		if(agregar.size() > 0){
			if(superior == null){
				for(int i=0; i<agregar.size(); i++)
					arbol.insertNode(new BinaryTreeNode<Usuario>(agregar.get(i)), (BinaryTreeNode<Usuario>)arbol.getRoot());
			}
			else{
				ArrayList<BinaryTreeNode<Usuario>> hojas = obtenerHojas(arbol);
				for(int i=0; i<agregar.size(); i++){
					BinaryTreeNode<Usuario> padre = nodoConMenosHijos(arbol, hojas);
					arbol.insertNode(new BinaryTreeNode<Usuario>(agregar.get(i)), padre);
				}
			}
			agregarNodosAmigos(arbol, arcos, mayor);
		}
	}
	public GeneralTree<Usuario> obtenerRelacionJerDeConexiones(Vertex v){
		GeneralTree<Usuario> arbol = new GeneralTree<Usuario>();
		ArrayList<Usuario> yaEstan = new ArrayList<Usuario>();
		Usuario raiz = (Usuario)v.getInfo();
		arbol.setRoot(new BinaryTreeNode<Usuario>(raiz));
		yaEstan.add(raiz);
		LinkedList<Vertex> amigos = v.getAdjacents();
		agregarNodosConexiones(arbol, raiz, amigos, yaEstan, true, 0);
		try {
			actualizarFicheroArbolConexiones(arbol);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arbol;
	}
	private void agregarNodosConexiones(GeneralTree<Usuario> arbol, Usuario padre, 
			LinkedList<Vertex> listaAgregar, ArrayList<Usuario> yaEstan, boolean sgtNivel, int nivel){

		BinaryTreeNode<Usuario> father = obtenerNodo(arbol, padre);
		Iterator<Vertex> iter = listaAgregar.iterator();
		while(iter.hasNext()){
			Vertex v = iter.next();
			Usuario ag = (Usuario)v.getInfo();
			if(!yaSeAgrego(ag, yaEstan)){
				yaEstan.add(ag);
				arbol.insertNode(new BinaryTreeNode<Usuario>(ag), father);
			}
		}
		if(sgtNivel){
			int level = nivelArbol(arbol);
			if(level != nivel){
				ArrayList<BinaryTreeNode<Usuario>> hojas = obtenerHojas(arbol);
				boolean pasarSgtNivel = false;
				for(int i=0; i<hojas.size(); i++){
					if(i+1 == hojas.size())
						pasarSgtNivel = true;
					Usuario nuevoPadre = hojas.get(i).getInfo();
					Vertex v = buscarUsuario(nuevoPadre.getNick());
					LinkedList<Vertex> amigos = v.getAdjacents();
					agregarNodosConexiones(arbol, nuevoPadre, amigos, yaEstan, pasarSgtNivel, level);
				}
			}
		}
	}
	public ArrayList<Usuario> obtenerLideresInvestigacion(){
		ArrayList<Usuario> lista = new ArrayList<Usuario>();
		Iterator<Vertex> iter = grafo.getVerticesList().iterator();
		int mayor = -1;
		while(iter.hasNext()){
			Vertex v = iter.next();
			GeneralTree<Usuario> arbol = obtenerRelacionJerDeAmigos(v);
			int nivel = nivelArbol(arbol);
			if(nivel > mayor){
				mayor = nivel;
				lista.clear();
				lista.add((Usuario)v.getInfo());
			}
			else if(nivel == mayor)
				lista.add((Usuario)v.getInfo());
		}
		try {
			actualizarFicheroLideresInvestigacion(lista);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lista;
	}
	public int nivelArbol(GeneralTree<Usuario> arbol){
		int level = -1;
		InBreadthIteratorWithLevels<Usuario> iter = arbol.inBreadthIteratorWithLevels();
		while(iter.hasNext()){
			BreadthNode<Usuario> node = iter.nextNodeWithLevel();
			int nivel = node.getLevel();
			if(nivel > level)
				level = nivel;
		}
		return level;
	}
	public BinaryTreeNode<Usuario> obtenerNodo(GeneralTree<Usuario> arbol, Usuario padre){
		BinaryTreeNode<Usuario> node = null;
		InDepthIterator<Usuario> iter = arbol.inDepthIterator();
		while(iter.hasNext() && node == null){
			BinaryTreeNode<Usuario> aux = iter.nextNode();
			if(aux.getInfo().getNick().equals(padre.getNick()))
				node = aux;
		}
		return node;
	}
	private boolean yaSeAgrego(Usuario usuario, ArrayList<Usuario> yaEstan){
		boolean esta = false;
		for(int i=0; i<yaEstan.size() && !esta; i++){
			if(yaEstan.get(i).getNick().equals(usuario.getNick()))
				esta = true;
		}
		return esta;
	}
	
	private ArrayList<BinaryTreeNode<Usuario>> obtenerHojas(GeneralTree<Usuario> arbol){
		ArrayList<BinaryTreeNode<Usuario>> lista = new ArrayList<BinaryTreeNode<Usuario>>();
		List<TreeNode<Usuario>> hojas = arbol.getLeaves();
		int mayorNivel = -1;
		for(int i=0; i<hojas.size(); i++){
			BinaryTreeNode<Usuario> aux = (BinaryTreeNode<Usuario>)hojas.get(i);
			int level = arbol.nodeLevel(aux);
			if(level > mayorNivel){
				mayorNivel = level;
				lista.clear();
				lista.add(aux);
			}
			else if(level == mayorNivel)
				lista.add(aux);
		}
		return lista;
	}
	private BinaryTreeNode<Usuario> nodoConMenosHijos(GeneralTree<Usuario> arbol, ArrayList<BinaryTreeNode<Usuario>> hojas){
		BinaryTreeNode<Usuario> node = new BinaryTreeNode<Usuario>();
		int menor = Integer.MAX_VALUE;
		for(int i=0; i<hojas.size(); i++){
			BinaryTreeNode<Usuario> aux = hojas.get(i);
			int cant = arbol.getSons(aux).size();
			if(cant < menor){
				menor = cant;
				node = aux;
			}
		}
		return node;
	}
	public ArrayList<Usuario> obtenerIslas(){
		ArrayList<Usuario> islas = new ArrayList<Usuario>();
		LinkedList<Vertex> lista = grafo.getVerticesList();
		Iterator<Vertex> iter = lista.iterator();
		while(iter.hasNext()){
			Vertex v = iter.next();
			if(v.getAdjacents().size() == 0)
				islas.add((Usuario)v.getInfo());
		}
		try {
			actualizarFicheroIslas(islas);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return islas;
	}
	public ArrayList<Comunidad> obtenerComunidades(){
		ArrayList<Comunidad> com = new ArrayList<Comunidad>();
		Iterator<Vertex> iter = grafo.getVerticesList().iterator();
		while(iter.hasNext()){
			Vertex v = iter.next();
			ArrayList<Comunidad> comV = obtenerComunidadesDeUsuario(v);
			for(int i=0; i<comV.size(); i++){
				Comunidad c = comV.get(i);
				if(!yaExisteEstaComunidad(com, c.getUsuarios()))
					com.add(c);
			}
		}
		try {
			actualizarFicheroComunidades(com);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return com;
	}
	private ArrayList<Comunidad> obtenerComunidadesDeUsuario(Vertex v){
		ArrayList<Comunidad> com = new ArrayList<Comunidad>();
		LinkedList<Vertex> adjV = v.getAdjacents();
		Iterator<Vertex> iterAdjV = adjV.iterator();
		while(iterAdjV.hasNext()){
			Vertex ve = iterAdjV.next();
			LinkedList<Vertex> lista = new LinkedList<Vertex>();
			lista.add(v);
			agregarComunidadesDeUsuario(ve, adjV, lista, com);
		}
		ArrayList<Comunidad> aux = new ArrayList<Comunidad>();
		for(int i=0; i<com.size(); i++){
			Comunidad c = com.get(i);
			if(!yaExisteEstaComunidad(aux, c.getUsuarios()))
				aux.add(c);
		}
		return com;
	}
	private void agregarComunidadesDeUsuario(Vertex v, LinkedList<Vertex> validos, LinkedList<Vertex> lista, ArrayList<Comunidad> com){
		lista.add(v);
		LinkedList<Vertex> aux = new LinkedList<Vertex>();
		LinkedList<Vertex> adj = v.getAdjacents();
		Iterator<Vertex> iterAdj = adj.iterator();
		while(iterAdj.hasNext()){
			Vertex vA = iterAdj.next();
			boolean cond1 = false;
			boolean cond2 = true;
			Iterator<Vertex> iter = validos.iterator();
			while(iter.hasNext() && !cond1){
				if(iter.next().equals(vA))
					cond1 = true;
			}
			if(cond1){
				iter = lista.iterator();
				while(iter.hasNext() && cond2){
					if(iter.next().equals(vA))
						cond2 = false;
				}
				if(cond1 && cond2)
					aux.add(vA);
			}
		}
		if(aux.isEmpty()){
			if(lista.size() > 2)
				com.add(new Comunidad(lista));
		}
		else{
			Iterator<Vertex> iterAux = aux.iterator();
			while(iterAux.hasNext()){
				LinkedList<Vertex> listaC = new LinkedList<>();
				listaC.addAll(lista);
				Vertex ver = iterAux.next();
				agregarComunidadesDeUsuario(ver, aux, listaC, com);
			}
		}
	}	
	private boolean yaExisteEstaComunidad(ArrayList<Comunidad> comunidades, LinkedList<Vertex> nuevaComunidad){
		boolean yaExiste = false;
		for(int i=0; i<comunidades.size() && !yaExiste; i++){
			Comunidad c = comunidades.get(i);
			LinkedList<Vertex> usuariosDeComunidad = c.getUsuarios();
			if(usuariosDeComunidad.size() == nuevaComunidad.size()){
				boolean mismosUsuarios = true;
				Iterator<Vertex> iterNC = nuevaComunidad.iterator();
				while(iterNC.hasNext() && mismosUsuarios){
					Usuario uNuevaComunidad = (Usuario)iterNC.next().getInfo();
					String nickUNC = uNuevaComunidad.getNick();
					boolean encontrado = false;
					Iterator<Vertex> iterUC = usuariosDeComunidad.iterator();
					while(iterUC.hasNext() && !encontrado){
						Usuario uComunidad = (Usuario)iterUC.next().getInfo();
						String nickUC = uComunidad.getNick();
						if(nickUC.equals(nickUNC))
							encontrado = true;
					}
					if(!encontrado)
						mismosUsuarios = false;
				}
				if(mismosUsuarios)
					yaExiste = true;
			}
		}
		return yaExiste;
	}
	public LinkedList<Trabajo> trabajosDeUsuario(Vertex vertex){
		LinkedList<Trabajo> lista = new LinkedList<Trabajo>();
		Usuario usuario = (Usuario)vertex.getInfo();
		String nick = usuario.getNick();
		Iterator<Trabajo> iter = trabajos.iterator();
		while(iter.hasNext()){
			Trabajo t = iter.next();
			boolean esta = false;
			for(int i=0; i<t.getAutores().size() && !esta; i++){
				if(t.getAutores().get(i).getNick().equals(nick)){
					lista.add(t);
					esta = true;
				}
			}
		}
		return lista;
	}

	private void actualizarFicheroArbolAmigos(GeneralTree<Usuario> arbol) throws IOException{
		FileWriter fw = new FileWriter(arbolAmigos);
		fw.write("RELACIÓN JERÁRQUICA DE AMIGOS\n\n\n");
		InBreadthIteratorWithLevels<Usuario> iter = arbol.inBreadthIteratorWithLevels();
		int nivel = 1;
		while(iter.hasNext()){
			BreadthNode<Usuario> node = iter.nextNodeWithLevel();
			int level = node.getLevel();
			if(level == 0)
				fw.write("Raíz: "+node.getInfo().getNick()+"\n");
			else{
				if(level == nivel){
					fw.write("\nNivel "+nivel+":\n");
					nivel++;
				}
				fw.write(node.getInfo().getNick()+"\n");
			}
		}
		fw.close();
	}
	private void actualizarFicheroArbolConexiones(GeneralTree<Usuario> arbol) throws IOException{
		FileWriter fw = new FileWriter(arbolConexiones);
		fw.write("RELACIÓN JERÁRQUICA DE CONEXIONES\n\n\n");
		InBreadthIteratorWithLevels<Usuario> iter = arbol.inBreadthIteratorWithLevels();
		int nivel = 1;
		String padre = "";
		while(iter.hasNext()){
			BreadthNode<Usuario> node = iter.nextNodeWithLevel();
			Usuario u = node.getInfo();
			String nick = u.getNick();
			int level = node.getLevel();
			if(level == 0){
				fw.write("Raíz: "+nick+"\n");
			}
			else{
				if(level == nivel){
					fw.write("\nNIVEL "+nivel+":\n");
					nivel++;
				}
				BinaryTreeNode<Usuario> nodeB = obtenerNodo(arbol, u);
				String padr = arbol.getFather(nodeB).getInfo().getNick();
				if(!padr.equals(padre)){
					padre = padr;
					fw.write("\nHijos de "+padre+":\n");
				}
				fw.write(nick+"\n");
			}
		}
		fw.close();
	}
	private void actualizarFicheroLideresInvestigacion(ArrayList<Usuario> lista) throws IOException{
		FileWriter fw = new FileWriter(lidereInvestigacion);
		fw.write("LÍDERES DE INVESTIGACIÓN");
		for(int i=0; i<lista.size(); i++){
			fw.write("\n\n");
			Usuario u = lista.get(i);
			fw.write("Usuario "+(i+1));
			fw.write(":\nNombre de usuario: "+u.getNick());
			fw.write("\nProfesión: "+u.getProfesion());
			fw.write("\nPaís: "+u.getPais());
		}
		fw.close();
	}
	private void actualizarFicheroIslas(ArrayList<Usuario> lista) throws IOException{
		FileWriter fw = new FileWriter(islas);
		fw.write("USUARIOS ISLAS");
		for(int i=0; i<lista.size(); i++){
			fw.write("\n\n");
			Usuario u = lista.get(i);
			fw.write("Usuario "+(i+1));
			fw.write(":\nNombre de usuario: "+u.getNick());
			fw.write("\nProfesión: "+u.getProfesion());
			fw.write("\nPaís: "+u.getPais());
		}
		fw.close();
	}
	private void actualizarFicheroComunidades(ArrayList<Comunidad> lista) throws IOException{
		FileWriter fw = new FileWriter(comunidades);
		fw.write("COMUNIDADES");
		for(int i=0; i<lista.size(); i++){
			fw.write("\n\n");
			fw.write("Miembros de la Comunidad "+(i+1)+":");
			Comunidad c = lista.get(i);
			Iterator<Vertex> iter = c.getUsuarios().iterator();
			while(iter.hasNext()){
				Usuario u = (Usuario)iter.next().getInfo();
				fw.write("\n"+u.getNick());
			}
		}
		fw.close();
	}
	public void agregarArcoAFichero(int posOrigen, int posDestino) throws IOException, ClassNotFoundException{
		Arco a = new Arco(posOrigen, posDestino);
		RandomAccessFile fich = new RandomAccessFile(arcos, "rw");
		long posIni = fich.getFilePointer();
		int cantArcos = fich.readInt();
		for(int i=0; i<cantArcos; i++)
			fich.skipBytes(fich.readInt());
		byte[] arcoAB = Convert.toBytes(a);
		fich.writeInt(arcoAB.length);
		fich.write(arcoAB);
		fich.seek(posIni);
		fich.writeInt(++cantArcos);
		fich.close();
	}
	public void eliminarArcoFichero(int posOrigen, int posDestino) throws IOException, ClassNotFoundException{
		int pos = obtenerIndiceArco(posOrigen, posDestino);
		RandomAccessFile fich = new RandomAccessFile(arcos, "rw");
		long posIni = fich.getFilePointer();
		int cantArcos = fich.readInt();
		int i=0;
		while(i<pos){
			fich.skipBytes(fich.readInt());
			i++;
		}
		long posEsc;
		long posLeer = 0;
		boolean primeraIteracion = true;
		while(i<cantArcos){
			posEsc = fich.getFilePointer();
			if(cantArcos-1 != i){
				if(primeraIteracion){
					fich.skipBytes(fich.readInt());
					primeraIteracion = false;
				}
				else
					fich.seek(posLeer);
				byte[] arcoAB = new byte[fich.readInt()];
				fich.read(arcoAB);
				posLeer = fich.getFilePointer();
				fich.seek(posEsc);
				fich.writeInt(arcoAB.length);
				fich.write(arcoAB);
			}
			else
				fich.writeInt(0);
			i++;
		}
		fich.seek(posIni);
		fich.writeInt(--cantArcos);
		fich.close();
	}
	private int obtenerIndiceArco(int posOrigen, int posDestino) throws IOException, ClassNotFoundException{
		RandomAccessFile fich = new RandomAccessFile(arcos, "r");
		int index = 0;
		int cantArcos = fich.readInt();
		boolean encontrado = false;
		for(int i=0; i<cantArcos && !encontrado; i++){
			byte[] arcoAB = new byte[fich.readInt()];
			fich.read(arcoAB);
			Arco a = (Arco)Convert.toObject(arcoAB);
			if(a.getOrigen() == posOrigen && a.getDestino() == posDestino){
				index = i;
				encontrado = true;
			}
			else if(a.getOrigen() == posDestino && a.getDestino() == posOrigen){
				index = i;
				encontrado = true;
			}
		}
		fich.close();
		return index;
	}

	public void agregarUsuarioAFichero(Usuario u) throws IOException, ClassNotFoundException{
		RandomAccessFile fich = new RandomAccessFile(usuarios, "rw");
		long posIni = fich.getFilePointer();
		int cantUsuarios = fich.readInt();
		for(int i=0; i<cantUsuarios; i++)
			fich.skipBytes(fich.readInt());
		byte[] usuarioAB = Convert.toBytes(u);
		fich.writeInt(usuarioAB.length);
		fich.write(usuarioAB);
		fich.seek(posIni);
		fich.writeInt(++cantUsuarios);
		fich.close();
	}
	public void modificarUsuarioEnFichero(Usuario us) throws IOException, ClassNotFoundException{
		int pos = obtenerIndiceUsuario(us.getNick());
		RandomAccessFile fich = new RandomAccessFile(usuarios, "rw");
		int cantidadUsuarios = fich.readInt();
		int i=0;
		while(i<pos){
			fich.skipBytes(fich.readInt());
			i++;
		}
		long posModificar = fich.getFilePointer();
		int j=i;
		fich.skipBytes(fich.readInt());
		i++;
		LinkedList<Usuario> agregar = new LinkedList<Usuario>();
		agregar.add(us);
		while(i<cantidadUsuarios){
			byte[] usuarioAB = new byte[fich.readInt()];
			fich.read(usuarioAB);
			Usuario u = (Usuario)Convert.toObject(usuarioAB);
			agregar.add(u);
			i++;
		}
		fich.seek(posModificar);
		fich.writeInt(0);
		fich.seek(posModificar);
		Iterator<Usuario> iter = agregar.iterator();
		while(j<cantidadUsuarios){
			Usuario u = iter.next();
			byte[] usuarioAB = Convert.toBytes(u);
			fich.writeInt(usuarioAB.length);
			fich.write(usuarioAB);
			j++;
		}
		fich.close();
	}
	public void eliminarUsuarioDeFichero(int pos) throws IOException, ClassNotFoundException{
		RandomAccessFile fich = new RandomAccessFile(usuarios, "rw");
		long posIni = fich.getFilePointer();
		int cantUsuarios = fich.readInt();
		int i=0;
		while(i<pos){
			fich.skipBytes(fich.readInt());
			i++;
		}
		long posEsc;
		long posLeer = 0;
		boolean primeraIteracion = true;
		while(i<cantUsuarios){
			posEsc = fich.getFilePointer();
			if(cantUsuarios-1 != i){
				if(primeraIteracion){
					fich.skipBytes(fich.readInt());
					primeraIteracion = false;
				}
				else
					fich.seek(posLeer);
				byte[] usuarioAB = new byte[fich.readInt()];
				fich.read(usuarioAB);
				posLeer = fich.getFilePointer();
				fich.seek(posEsc);
				fich.writeInt(usuarioAB.length);
				fich.write(usuarioAB);
			}
			else
				fich.writeInt(0);
			i++;
		}
		fich.seek(posIni);
		fich.writeInt(--cantUsuarios);
		fich.close();
		eliminarArcosDeUsuarioEnFichero(pos);
		modificarArcosNecesarios(pos, ++cantUsuarios);
	}
	private int obtenerIndiceUsuario(String nick){
		int index = 0;
		LinkedList<Vertex> lista = grafo.getVerticesList();
		Iterator<Vertex> iter = lista.iterator();
		boolean encontrado = false;
		while(iter.hasNext() && !encontrado){
			Usuario u = (Usuario)iter.next().getInfo();
			if(u.getNick().equals(nick))
				encontrado = true;
			else
				index++;
		}
		return index;
	}

	public void agregarTrabajoAFichero(Trabajo t) throws IOException, ClassNotFoundException{
		RandomAccessFile fich = new RandomAccessFile(trab, "rw");
		long posIni = fich.getFilePointer();
		int cantTrabajos = fich.readInt();
		for(int i=0; i<cantTrabajos; i++)
			fich.skipBytes(fich.readInt());
		byte[] trabajoAB = Convert.toBytes(t);
		fich.writeInt(trabajoAB.length);
		fich.write(trabajoAB);
		fich.seek(posIni);
		fich.writeInt(++cantTrabajos);
		fich.close();
	}
	public void modificarTrabajoEnFichero(Trabajo t) throws IOException, ClassNotFoundException{
		int pos = obtenerIndiceDeTrabajo(t.getLineaInvestigacion(), t.getTema());
		RandomAccessFile fich = new RandomAccessFile(trab, "rw");
		int cantTrabajos = fich.readInt();
		int i=0;
		while(i<pos){
			fich.skipBytes(fich.readInt());
			i++;
		}
		long posModificar = fich.getFilePointer();
		int j=i;
		fich.skipBytes(fich.readInt());
		i++;
		LinkedList<Trabajo> agregar = new LinkedList<Trabajo>();
		agregar.add(t);
		while(i<cantTrabajos){
			byte[] trabajoAB = new byte[fich.readInt()];
			fich.read(trabajoAB);
			Trabajo tr = (Trabajo)Convert.toObject(trabajoAB);
			agregar.add(tr);
			i++;
		}
		fich.seek(posModificar);
		fich.writeInt(0);
		fich.seek(posModificar);
		Iterator<Trabajo> iter = agregar.iterator();
		while(j<cantTrabajos){
			Trabajo tr = iter.next();
			byte[] trabajoAB = Convert.toBytes(tr);
			fich.writeInt(trabajoAB.length);
			fich.write(trabajoAB);
			j++;
		}
		fich.close();
	}
	public void eliminarTrabajoFichero(String linea, String tema) throws IOException, ClassNotFoundException{
		int pos = obtenerIndiceDeTrabajo(linea, tema);
		RandomAccessFile fich = new RandomAccessFile(trab, "rw");
		long posIni = fich.getFilePointer();
		int cantTrabajos = fich.readInt();
		int i=0;
		while(i<pos){
			fich.skipBytes(fich.readInt());
			i++;
		}
		long posEsc;
		long posLeer = 0;
		boolean primeraIteracion = true;
		while(i<cantTrabajos){
			posEsc = fich.getFilePointer();
			if(cantTrabajos-1 != i){
				if(primeraIteracion){
					fich.skipBytes(fich.readInt());
					primeraIteracion = false;
				}
				else
					fich.seek(posLeer);
				byte[] trabajoAB = new byte[fich.readInt()];
				fich.read(trabajoAB);
				posLeer = fich.getFilePointer();
				fich.seek(posEsc);
				fich.writeInt(trabajoAB.length);
				fich.write(trabajoAB);
			}
			else
				fich.writeInt(0);
			i++;
		}
		fich.seek(posIni);
		fich.writeInt(--cantTrabajos);
		fich.close();
	}
	private int obtenerIndiceDeTrabajo(String linea, String tema){
		int index = 0;
		boolean encontrado = false;
		Iterator<Trabajo> iter = trabajos.iterator();
		while(iter.hasNext() && !encontrado){
			Trabajo trab = iter.next();
			if(trab.getLineaInvestigacion().equals(linea) && trab.getTema().equals(tema))
				encontrado = true;
			else
				index++;
		}
		return index;
	}

	private void eliminarArcosDeUsuarioEnFichero(int index) throws IOException, ClassNotFoundException{
		boolean parar = false;
		while(!parar){
			int pos = buscarArcoDeUsuario(index);
			if(pos != -1)
				eliminarArcoDeFicheroDeUnaPosicion(pos);
			else
				parar = true;
		}
	}
	private void eliminarArcoDeFicheroDeUnaPosicion(int pos) throws IOException, ClassNotFoundException{
		RandomAccessFile fich = new RandomAccessFile(arcos, "rw");
		long posIni = fich.getFilePointer();
		int cantArcos = fich.readInt();
		int i=0;
		while(i<pos){
			fich.skipBytes(fich.readInt());
			i++;
		}
		long posEsc;
		long posLeer = 0;
		boolean primeraIteracion = true;
		while(i<cantArcos){
			posEsc = fich.getFilePointer();
			if(cantArcos-1 != i){
				if(primeraIteracion){
					fich.skipBytes(fich.readInt());
					primeraIteracion = false;
				}
				else
					fich.seek(posLeer);
				byte[] arcoAB = new byte[fich.readInt()];
				fich.read(arcoAB);
				posLeer = fich.getFilePointer();
				fich.seek(posEsc);
				fich.writeInt(arcoAB.length);
				fich.write(arcoAB);
			}
			else
				fich.writeInt(0);
			i++;
		}
		fich.seek(posIni);
		fich.writeInt(--cantArcos);
		fich.close();
	}
	private int buscarArcoDeUsuario(int valor) throws IOException, ClassNotFoundException{
		int index = -1;
		RandomAccessFile fich = new RandomAccessFile(arcos, "r");
		int cantArcos = fich.readInt();
		boolean encontrado = false;
		for(int i=0; i<cantArcos && !encontrado; i++){
			byte[] arcoAB = new byte[fich.readInt()];
			fich.read(arcoAB);
			Arco a = (Arco)Convert.toObject(arcoAB);
			if(a.getOrigen() == valor || a.getDestino() == valor){
				encontrado = true;
				index = i;
			}
		}
		fich.close();
		return index;
	}
	private void modificarArcosNecesarios(int valorInicio, int cantUsuarios) throws IOException, ClassNotFoundException{
		int numeroModificar = valorInicio+1;
		while(numeroModificar < cantUsuarios){
			int index = buscarArcoDeUsuario(numeroModificar);
			if(index != -1)
				modificarValorDeArco(index, numeroModificar);
			else
				numeroModificar++;
		}
	}
	private void modificarValorDeArco(int pos, int valor) throws IOException, ClassNotFoundException{
		RandomAccessFile fich = new RandomAccessFile(arcos, "rw");
		int cantArcos = fich.readInt();
		int i=0;
		while(i<pos){
			fich.skipBytes(fich.readInt());
			i++;
		}
		long posModificar = fich.getFilePointer();
		int j=i;
		i++;
		byte[] arcoAB = new byte[fich.readInt()];
		fich.read(arcoAB);
		Arco a = (Arco)Convert.toObject(arcoAB);
		if(a.getOrigen() == valor)
			a.setOrigen(valor-1);
		else if(a.getDestino() == valor)
			a.setDestino(valor-1);
		LinkedList<Arco> agregar = new LinkedList<Arco>();
		agregar.add(a);
		while(i<cantArcos){
			arcoAB = new byte[fich.readInt()];
			fich.read(arcoAB);
			a = (Arco)Convert.toObject(arcoAB);
			agregar.add(a);
			i++;
		}
		fich.seek(posModificar);
		fich.writeInt(0);
		fich.seek(posModificar);
		Iterator<Arco> iter = agregar.iterator();
		while(j<cantArcos){
			a = iter.next();
			arcoAB = Convert.toBytes(a);
			fich.writeInt(arcoAB.length);
			fich.write(arcoAB);
			j++;
		}
		fich.close();
	}

	private void inicializar() throws ClassNotFoundException, IOException{
		ArrayList<Usuario> us = leerFicheroUsuarios();
		ArrayList<Arco> ar = leerFicheroArcos();
		LinkedList<Trabajo> tr = leerFicheroTrabajos();

		for(Usuario u : us)
			grafo.insertVertex(u);

		Iterator<Trabajo> iter = tr.iterator();
		while(iter.hasNext())
			trabajos.add(iter.next());

		for(Arco a : ar){
			int origen = a.getOrigen();
			int destino = a.getDestino();
			grafo.insertWEdgeNDG(origen, destino, calcularPeso(origen, destino));
		}
	}
	private ArrayList<Arco> leerFicheroArcos() throws IOException, ClassNotFoundException{
		RandomAccessFile fich = new RandomAccessFile(arcos, "r");
		int cantArcos = fich.readInt();
		ArrayList<Arco> ar = new ArrayList<Arco>(cantArcos);
		for(int i=0; i<cantArcos; i++){
			byte[] arcoAB = new byte[fich.readInt()];
			fich.read(arcoAB);
			Arco a = (Arco)Convert.toObject(arcoAB);
			ar.add(a);
		}
		fich.close();
		return ar;
	}
	private ArrayList<Usuario> leerFicheroUsuarios() throws IOException, ClassNotFoundException{
		RandomAccessFile fich = new RandomAccessFile(usuarios, "r");
		int cantUsuarios = fich.readInt();
		ArrayList<Usuario> us = new ArrayList<Usuario>(cantUsuarios);
		for(int i=0; i<cantUsuarios; i++){
			byte[] usuarioAB = new byte[fich.readInt()];
			fich.read(usuarioAB);
			Usuario u = (Usuario)Convert.toObject(usuarioAB);
			us.add(u);
		}
		fich.close();
		return us;
	}
	private LinkedList<Trabajo> leerFicheroTrabajos() throws IOException, ClassNotFoundException{
		RandomAccessFile fich = new RandomAccessFile(trab, "r");
		int cantTrabajos = fich.readInt();
		LinkedList<Trabajo> tr = new LinkedList<Trabajo>();
		for(int i=0; i<cantTrabajos; i++){
			byte[] trabajoAB = new byte[fich.readInt()];
			fich.read(trabajoAB);
			Trabajo t = (Trabajo)Convert.toObject(trabajoAB);
			tr.add(t);
		}
		fich.close();
		return tr;
	}
	@SuppressWarnings("unused")
	private void crearFicheroArcos() throws IOException{
		RandomAccessFile fich = new RandomAccessFile(arcos, "rw");
		ArrayList<Arco> ar = listaArcos();
		int cant = 0;
		long posIni = fich.getFilePointer();
		for(Arco a: ar){
			byte[] arcoAB = Convert.toBytes(a);
			fich.seek(posIni);
			fich.writeInt(++cant);
			fich.skipBytes((int)fich.length());
			fich.writeInt(arcoAB.length);
			fich.write(arcoAB);
		}
		fich.close();
	}
	@SuppressWarnings("unused")
	private void crearFicheroUsuarios() throws IOException{
		RandomAccessFile fich = new RandomAccessFile(usuarios, "rw");
		ArrayList<Usuario> us = listaUsuarios();
		int cant = 0;
		long posIni = fich.getFilePointer();
		for(Usuario u: us){
			byte[] usuarioAB = Convert.toBytes(u);
			fich.seek(posIni);
			fich.writeInt(++cant);
			fich.skipBytes((int)fich.length());
			fich.writeInt(usuarioAB.length);
			fich.write(usuarioAB);
		}
		fich.close();
	}
	@SuppressWarnings("unused")
	private void crearFicheroTrabajos() throws IOException{
		RandomAccessFile fich = new RandomAccessFile(trab, "rw");
		LinkedList<Trabajo> tr = listaTrabajos();
		int cant = 0;
		long posIni = fich.getFilePointer();
		Iterator<Trabajo> iter = tr.iterator();
		while(iter.hasNext()){
			byte[] trabajoAB = Convert.toBytes(iter.next());
			fich.seek(posIni);
			fich.writeInt(++cant);
			fich.skipBytes((int)fich.length());
			fich.writeInt(trabajoAB.length);
			fich.write(trabajoAB);
		}
		fich.close();
	}
	private ArrayList<Arco> listaArcos(){
		ArrayList<Arco> a = new ArrayList<Arco>();
		a.add(new Arco(0, 1));
		a.add(new Arco(0, 2));
		a.add(new Arco(0, 3));
		a.add(new Arco(0, 4));
		a.add(new Arco(0, 5));
		a.add(new Arco(0, 6));
		a.add(new Arco(0, 7));
		a.add(new Arco(0, 8));
		a.add(new Arco(0, 9));
		a.add(new Arco(0, 10));
		a.add(new Arco(0, 11));
		a.add(new Arco(0, 12));
		a.add(new Arco(0, 13));
		a.add(new Arco(0, 14));
		a.add(new Arco(0, 15));
		a.add(new Arco(0, 16));
		a.add(new Arco(0, 17));
		a.add(new Arco(0, 18));
		a.add(new Arco(0, 19));
		a.add(new Arco(0, 25));
		a.add(new Arco(0, 28));
		a.add(new Arco(0, 31));
		a.add(new Arco(0, 120));
		a.add(new Arco(0, 121));
		a.add(new Arco(0, 122));
		a.add(new Arco(0, 123));

		a.add(new Arco(1, 2));
		a.add(new Arco(1, 3));
		a.add(new Arco(1, 4));
		a.add(new Arco(1, 5));
		a.add(new Arco(1, 13));
		a.add(new Arco(1, 14));
		a.add(new Arco(1, 15));
		a.add(new Arco(1, 18));
		a.add(new Arco(1, 20));
		a.add(new Arco(1, 21));
		a.add(new Arco(1, 25));
		a.add(new Arco(1, 27));
		a.add(new Arco(1, 28));
		a.add(new Arco(1, 29));
		a.add(new Arco(1, 30));
		a.add(new Arco(1, 31));
		a.add(new Arco(1, 37));
		a.add(new Arco(1, 42));
		a.add(new Arco(1, 47));
		a.add(new Arco(1, 48));
		a.add(new Arco(1, 56));
		a.add(new Arco(1, 68));
		a.add(new Arco(1, 121));

		a.add(new Arco(2, 3));
		a.add(new Arco(2, 4));
		a.add(new Arco(2, 5));
		a.add(new Arco(2, 13));
		a.add(new Arco(2, 14));
		a.add(new Arco(2, 15));
		a.add(new Arco(2, 16));
		a.add(new Arco(2, 17));
		a.add(new Arco(2, 18));
		a.add(new Arco(2, 25));
		a.add(new Arco(2, 28));

		a.add(new Arco(3, 4));
		a.add(new Arco(3, 5));
		a.add(new Arco(3, 9));
		a.add(new Arco(3, 11));
		a.add(new Arco(3, 13));
		a.add(new Arco(3, 14));
		a.add(new Arco(3, 15));
		a.add(new Arco(3, 16));
		a.add(new Arco(3, 17));
		a.add(new Arco(3, 18));
		a.add(new Arco(3, 28));

		a.add(new Arco(4, 5));
		a.add(new Arco(4, 6));
		a.add(new Arco(4, 11));
		a.add(new Arco(4, 13));
		a.add(new Arco(4, 14));
		a.add(new Arco(4, 15));
		a.add(new Arco(4, 16));
		a.add(new Arco(4, 17));
		a.add(new Arco(4, 18));
		a.add(new Arco(4, 28));

		a.add(new Arco(5, 11));
		a.add(new Arco(5, 13));
		a.add(new Arco(5, 14));
		a.add(new Arco(5, 15));
		a.add(new Arco(5, 16));
		a.add(new Arco(5, 17));
		a.add(new Arco(5, 18));
		a.add(new Arco(5, 28));

		a.add(new Arco(7, 8));

		a.add(new Arco(8, 9));
		a.add(new Arco(8, 10));
		a.add(new Arco(8, 11));
		a.add(new Arco(8, 12));
		a.add(new Arco(8, 28));
		a.add(new Arco(8, 41));
		a.add(new Arco(8, 120));

		a.add(new Arco(9, 10));
		a.add(new Arco(9, 11));
		a.add(new Arco(9, 12));
		a.add(new Arco(9, 16));
		a.add(new Arco(9, 19));
		a.add(new Arco(9, 20));
		a.add(new Arco(9, 28));
		a.add(new Arco(9, 47));
		a.add(new Arco(9, 120));

		a.add(new Arco(10, 11));
		a.add(new Arco(10, 12));
		a.add(new Arco(10, 19));
		a.add(new Arco(10, 28));
		a.add(new Arco(10, 60));
		a.add(new Arco(10, 114));
		a.add(new Arco(10, 120));

		a.add(new Arco(11, 12));
		a.add(new Arco(11, 16));
		a.add(new Arco(11, 17));
		a.add(new Arco(11, 19));
		a.add(new Arco(11, 28));
		a.add(new Arco(11, 120));

		a.add(new Arco(12, 16));
		a.add(new Arco(12, 19));
		a.add(new Arco(12, 28));
		a.add(new Arco(12, 120));

		a.add(new Arco(13, 15));
		a.add(new Arco(13, 17));

		a.add(new Arco(14, 17));
		a.add(new Arco(14, 18));
		a.add(new Arco(14, 25));
		a.add(new Arco(14, 28));

		a.add(new Arco(16, 17));
		a.add(new Arco(16, 25));
		a.add(new Arco(16, 28));
		a.add(new Arco(16, 120));

		a.add(new Arco(17, 18));
		a.add(new Arco(17, 49));

		a.add(new Arco(19, 28));

		a.add(new Arco(21, 27));
		a.add(new Arco(21, 29));
		a.add(new Arco(21, 30));
		a.add(new Arco(21, 37));
		a.add(new Arco(21, 42));
		a.add(new Arco(21, 47));
		a.add(new Arco(21, 48));
		a.add(new Arco(21, 56));
		a.add(new Arco(21, 68));

		a.add(new Arco(22, 76));

		a.add(new Arco(24, 51));
		a.add(new Arco(24, 78));

		a.add(new Arco(25, 28));
		a.add(new Arco(25, 31));
		a.add(new Arco(25, 121));

		a.add(new Arco(28, 31));
		a.add(new Arco(28, 121));

		a.add(new Arco(31, 121));

		a.add(new Arco(38, 65));

		a.add(new Arco(43, 70));
		a.add(new Arco(43, 97));

		a.add(new Arco(44, 71));
		a.add(new Arco(44, 98));

		a.add(new Arco(46, 73));
		a.add(new Arco(46, 100));

		a.add(new Arco(51, 78));

		a.add(new Arco(54, 81));
		a.add(new Arco(54, 108));

		a.add(new Arco(65, 92));

		a.add(new Arco(70, 97));

		a.add(new Arco(71, 98));

		a.add(new Arco(73, 100));

		a.add(new Arco(81, 108));

		a.add(new Arco(92, 121));

		a.add(new Arco(122, 123));

		return a;
	}
	private ArrayList<Usuario> listaUsuarios(){
		ArrayList<Usuario> u = new ArrayList<Usuario>();
		u.add(new Usuario("elyanko", "yankiel123", "Cuba", "Ingeniero/a Informático/a"));                   //0
		u.add(new Usuario("patrybarrios", "patry123", "Cuba", "Ingeniero/a Informático/a"));                //1
		u.add(new Usuario("agySaulio", "saulio123", "Cuba", "Ingeniero/a Informático/a"));                  //2
		u.add(new Usuario("antonio", "antonio123", "Cuba", "Ingeniero/a Informático/a"));                   //3
		u.add(new Usuario("lebroccc", "lebroc123", "Cuba", "Ingeniero/a Informático/a"));                   //4
		u.add(new Usuario("andyNapoles", "andy1234", "Cuba", "Ingeniero/a Informático/a"));                 //5
		u.add(new Usuario("weh.rdguez", "wendy123", "Cuba", "Arquitecto/a"));                               //6
		u.add(new Usuario("latemisonga", "temis123", "Cuba", "Ingeniero/a Informático/a"));                 //7
		u.add(new Usuario("cosetinha", "cosette123", "Cuba", "Juez/a"));                                    //8
		u.add(new Usuario("emiliola64", "emilio123", "Cuba", "Abogado/a"));                                 //9
		u.add(new Usuario("_its_edu", "eduardo123", "Cuba", "Ingeniero/a Eléctrico/a"));                    //10
		u.add(new Usuario("am_mario02", "mario123", "Cuba", "Ingeniero/a Civil"));                          //11
		u.add(new Usuario("migue_blue", "miguel123", "Cuba", "Ingeniero/a Civil"));                         //12
		u.add(new Usuario("lissi_arias", "liset123", "Cuba", "Economista"));                                //13
		u.add(new Usuario("vero_rigual", "veronica123", "Cuba", "Ingeniero/a Informático/a"));              //14
		u.add(new Usuario("claugf_fg", "claudia123", "Cuba", "Ingeniero/a Informático/a"));                 //15
		u.add(new Usuario("dguezzzzzz", "adriana123", "Cuba", "Ingeniero/a Civil"));                        //16
		u.add(new Usuario("felixvelazquez19", "felix123", "Cuba", "Deportista"));                           //17
		u.add(new Usuario("dany_chdez", "daniel123", "Cuba", "Ingeniero/a Informático/a"));                 //18
		u.add(new Usuario("marlitin.02", "marlon123", "Cuba", "Ingeniero/a Eléctrico/a"));                  //19
		u.add(new Usuario("leo_nuevo", "leonardo123", "Cuba", "Abogado/a"));                                //20
		u.add(new Usuario("hairo_yamil", "hairo123", "Cuba", "Arquitecto/a"));                              //21
		u.add(new Usuario("jacostaaa", "jose1234", "Cuba", "Deportista"));                                  //22
		u.add(new Usuario("javiermanzano205", "javier123", "Cuba", "Diseñador/a"));                         //23
		u.add(new Usuario("yai_107", "yailin123", "Cuba", "Economista"));                                   //24
		u.add(new Usuario("kendry_ow02", "kendry123", "Cuba", "Ingeniero/a Informático/a"));                //25
		u.add(new Usuario("laura_ddios", "laura123", "Cuba", "Electricista"));    							//26
		u.add(new Usuario("manu_alr", "manuel123", "Cuba", "Filósofo/a"));									//27
		u.add(new Usuario("ale_grdgez", "alejandro123", "Cuba", "Ingeniero/a Informático/a"));				//28
		u.add(new Usuario("lpz_pictrs", "carlos123", "Cuba", "Electricista"));								//29
		u.add(new Usuario("alisestudio", "alina123", "Cuba", "Filósofo/a"));								//30
		u.add(new Usuario("rociofdez.7", "rocio123", "Cuba", "Ingeniero/a Informático/a"));					//31
		u.add(new Usuario("ailen.digital", "ailen123", "Cuba", "Ingeniero/a Civil"));						//32
		u.add(new Usuario("cla_udi34", "claudia123", "Cuba", "Informático/a"));								//33
		u.add(new Usuario("diego_barrueta", "diego123", "Cuba", "Ingeniero/a Geofísico/a"));				//34
		u.add(new Usuario("its_marib", "maribella123", "Cuba", "Ingeniero/a Hidráulico/a"));				//35
		u.add(new Usuario("amelia.amg", "amelia123", "Cuba", "Ingeniero/a Industrial"));					//36
		u.add(new Usuario("erick5820", "erick123", "Cuba", "Informático/a"));								//37
		u.add(new Usuario("sheymaresma03", "sheyla123", "Cuba", "Ingeniero/a Químico/a"));					//38
		u.add(new Usuario("its_ema_0506", "emadai123", "Cuba", "Ingeniero/a Mecánico/a"));					//39
		u.add(new Usuario("amandaquerol", "amanda123", "Cuba", "Ingeniero/a en Telecomunicaciones"));		//40
		u.add(new Usuario("sussel_29", "sussel123", "Cuba", "Juez/a"));										//41
		u.add(new Usuario("isabella_fv", "isabella123", "Cuba", "Matemático/a"));							//42
		u.add(new Usuario("yilbert_morales45", "yilbert123", "Cuba", "Médico"));							//43
		u.add(new Usuario("ramon.perez", "ramon123", "Cuba", "Militar"));									//44
		u.add(new Usuario("danay.gomez.70", "danay123", "Cuba", "Músico"));									//45
		u.add(new Usuario("marta.nieto", "marta123", "Cuba", "Político/a"));								//46
		u.add(new Usuario("fabo._ale", "fabio123", "Cuba", "Abogado/a"));									//47
		u.add(new Usuario("juliohelguera", "julio123", "Cuba", "Arquitecto/a"));							//48
		u.add(new Usuario("delgado_mai_56", "mailin123", "Cuba", "Deportista"));							//49
		u.add(new Usuario("jason_5", "jason123", "Cuba", "Diseñador/a"));									//50
		u.add(new Usuario("camihdez", "camila123", "Cuba", "Economista"));									//51
		u.add(new Usuario("patryrodriguez", "patricia123", "Cuba", "Editor/a"));							//52
		u.add(new Usuario("adrianrosabal.03", "adrian123", "Cuba", "Electricista"));						//53
		u.add(new Usuario("beamsl.mlm", "beatriz123", "Cuba", "Filósofo/a"));								//54
		u.add(new Usuario("savina.martin", "savina123", "Cuba", "Físico/a"));								//55
		u.add(new Usuario("keylitabella.03", "keyla123", "Cuba", "Historiador/a"));							//56
		u.add(new Usuario("nattie_vm", "nathalia123", "Cuba", "Informático/a"));							//57
		u.add(new Usuario("anyialonso", "anyeli123", "Cuba", "Ingeniero/a Automático/a"));					//58
		u.add(new Usuario("tania.pupo", "tania123", "Cuba", "Ingeniero/a Civil"));							//59
		u.add(new Usuario("jorgemarquez_65", "jorge123", "Cuba", "Ingeniero/a Eléctrico/a"));				//60
		u.add(new Usuario("zaylicia", "zayli123", "Cuba", "Ingeniero/a Geofísico/a"));						//61
		u.add(new Usuario("gaboo_ari", "gabriel123", "Cuba", "Ingeniero/a Hidráulico/a"));					//62
		u.add(new Usuario("dtrianna_", "daniela123", "Cuba", "Ingeniero/a Industrial"));					//63
		u.add(new Usuario("nayeelis.s", "nayelis123", "Cuba", "Informático/a"));							//64
		u.add(new Usuario("lismaray6", "lismaray123", "Cuba", "Ingeniero/a Químico/a"));					//65
		u.add(new Usuario("erlisfernandez", "erlis123", "Cuba", "Ingeniero/a Mecánico/a"));					//66
		u.add(new Usuario("alvareztalo", "thalia123", "Cuba", "Ingeniero/a en Telecomunicaciones"));		//67
		u.add(new Usuario("carlosalfer_06", "carlos123", "Cuba", "Juez/a"));								//68
		u.add(new Usuario("mivhaeliscue", "michaelis123", "Cuba", "Matemático/a"));							//69
		u.add(new Usuario("_shabely003", "shabely123", "Cuba", "Médico"));									//70
		u.add(new Usuario("sofiaindigo", "sofia123", "Cuba", "Militar"));									//71
		u.add(new Usuario("thaly.mr", "thalia123", "Cuba", "Músico"));										//72
		u.add(new Usuario("jossuearte", "jossue123", "Cuba", "Político/a"));								//73
		u.add(new Usuario("manolour03", "manolo123", "Cuba", "Abogado/a"));									//74
		u.add(new Usuario("liz_pizarro", "liz12345", "Cuba", "Arquitecto/a"));								//75
		u.add(new Usuario("damarisbrows", "damaris123", "Cuba", "Deportista"));								//76	
		u.add(new Usuario("allanydreams", "allany123", "Cuba", "Diseñador/a"));								//77
		u.add(new Usuario("paocastillo", "paola123", "Cuba", "Economista"));								//78
		u.add(new Usuario("igorperezmesa", "igor1234", "Cuba", "Editor/a"));								//79
		u.add(new Usuario("izqmtnez01", "isabelle123", "Cuba", "Electricista"));							//80
		u.add(new Usuario("milagrosmartin71", "milagros123", "Cuba", "Filósofo/a"));						//81
		u.add(new Usuario("adriano_perez", "adriano123", "Cuba", "Físico/a"));								//82
		u.add(new Usuario("sab03med", "sabrina123", "Cuba", "Historiador/a"));								//83
		u.add(new Usuario("liacarley", "lia12345", "Cuba", "Informático/a"));								//84
		u.add(new Usuario("ramirezroxana", "roxana123", "Cuba", "Ingeniero/a Automático/a"));				//85
		u.add(new Usuario("briancarlitos", "brian123", "Cuba", "Ingeniero/a Civil"));						//86
		u.add(new Usuario("pupolourdes", "lourdes123", "Cuba", "Ingeniero/a Eléctrico/a"));					//87
		u.add(new Usuario("lahevlogs", "naomi123", "Cuba", "Ingeniero/a Geofísico/a"));						//88
		u.add(new Usuario("loana761", "loana123", "Cuba", "Ingeniero/a Hidráulico/a"));						//89
		u.add(new Usuario("nidiacazas", "nidia123", "Cuba", "Ingeniero/a Industrial"));						//90
		u.add(new Usuario("edu_martin", "eduardo123", "Cuba", "Informático/a"));							//91
		u.add(new Usuario("esteban_03", "esteban123", "Cuba", "Ingeniero/a Químico/a"));					//92	
		u.add(new Usuario("aimita_03", "aimita123", "Cuba", "Ingeniero/a Mecánico/a"));						//93	
		u.add(new Usuario("tamaraarias", "tamara123", "Cuba", "Ingeniero/a en Telecomunicaciones"));		//94
		u.add(new Usuario("ceciliacampos25", "cecilia123", "Cuba", "Juez/a"));								//95
		u.add(new Usuario("mariaelis65", "maria123", "Cuba", "Matemático/a"));								//96
		u.add(new Usuario("luisalejandro26", "luis1234", "Cuba", "Médico"));								//97
		u.add(new Usuario("cassaalexandra", "alexandra123", "Cuba", "Militar"));							//98
		u.add(new Usuario("andy_clau", "andy1234", "Cuba", "Músico"));										//99
		u.add(new Usuario("verdecia09", "verdecia123", "Cuba", "Político/a"));								//100
		u.add(new Usuario("melanie_glez03", "melanie123", "Cuba", "Abogado/a"));							//101
		u.add(new Usuario("nely.1981", "nely1234", "Cuba", "Arquitecto/a"));								//102
		u.add(new Usuario("pedrusky", "pedro123", "Cuba", "Deportista"));									//103
		u.add(new Usuario("yisuskz", "jesus123", "Cuba", "Diseñador/a"));									//104
		u.add(new Usuario("emilyLeyra", "emily123", "Cuba", "Economista"));									//105
		u.add(new Usuario("suarezzatiez", "suarez123", "Cuba", "Editor/a"));								//106
		u.add(new Usuario("hechavarrialianet", "alianet123", "Cuba", "Electricista"));						//107
		u.add(new Usuario("maicolgnzlz", "maicol123", "Cuba", "Filósofo/a"));								//108
		u.add(new Usuario("kevinalvarez", "kevin123", "Cuba", "Físico/a"));									//109	
		u.add(new Usuario("nestyboy85", "nesty123", "Cuba", "Historiador/a"));								//110					
		u.add(new Usuario("rachelgrez", "rachel123", "Cuba", "Informático/a"));								//111
		u.add(new Usuario("lauren.rec", "lauren123", "Cuba", "Ingeniero/a Automático/a"));					//112
		u.add(new Usuario("nayla_rdrgz", "nayla123", "Cuba", "Ingeniero/a Civil"));							//113
		u.add(new Usuario("l.angel_96", "angel123", "Cuba", "Ingeniero/a Eléctrico/a"));					//114
		u.add(new Usuario("marloneduardito", "marlon123", "Cuba", "Ingeniero/a Geofísico/a"));				//115
		u.add(new Usuario("yuleidyproenza", "yuleidy123", "Cuba", "Ingeniero/a Hidráulico/a"));				//116
		u.add(new Usuario("raynaldodelgado", "reynaldo123", "Cuba", "Ingeniero/a Industrial"));				//117
		u.add(new Usuario("melodybarcelovia", "melody123", "Cuba", "Informático/a"));						//118
		u.add(new Usuario("aniuskarivera", "aniuska123", "Cuba", "Ingeniero/a Químico/a"));					//119
		u.add(new Usuario("fgaby_gomz", "gabriela123", "Cuba", "Ingeniero/a Civil"));						//120
		u.add(new Usuario("llerena777_", "daniel123", "Cuba", "Ingeniero/a Químico/a"));					//121
		u.add(new Usuario("_arlety_gj", "arlety123", "Cuba", "Arquitecto/a"));								//122
		u.add(new Usuario("thali_0401", "thalia123", "Cuba", "Arquitecto/a"));								//123

		return u;
	}
	private LinkedList<Trabajo> listaTrabajos(){
		LinkedList<Trabajo> trabajos = new LinkedList<Trabajo>();
		ArrayList<Usuario> u = listaUsuarios();
		ArrayList<Usuario> autores = new ArrayList<Usuario>();
		Trabajo t;

		t = new Trabajo("Acondicionamiento Ambiental", 
				"La actividad humana y el ambiente construido");									//0
		autores.add(u.get(6));
		autores.add(u.get(102));
		autores.add(u.get(122));
		autores.add(u.get(123));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Acondicionamiento Ambiental", 
				"Sistemas tecnológicos alternativos sostenibles");									//1
		autores.add(u.get(75));
		autores.add(u.get(102));
		autores.add(u.get(122));
		autores.add(u.get(123));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Acondicionamiento Ambiental", 
				"Zonificación de vivienda y diseño preliminar de fenestración");					//2
		autores.add(u.get(75));
		autores.add(u.get(102));
		autores.add(u.get(122));
		autores.add(u.get(123));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Cosmética", 
				"Control de producción de productos relacionados a la industria");					//3
		autores.add(u.get(38));
		autores.add(u.get(65));
		autores.add(u.get(92));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Cosmética", 
				"Reducción de gastos y aumento de la calidad del producto");						//4
		autores.add(u.get(38));
		autores.add(u.get(65));
		autores.add(u.get(92));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Defensa Nacional", "Doctrina Militar Cubana");								//5
		autores.add(u.get(44));
		autores.add(u.get(71));
		autores.add(u.get(98));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Defensa Nacional", "El trabajo del EPMI");									//6
		autores.add(u.get(44));
		autores.add(u.get(71));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Defensa Nacional", "Las BDP y la Zona de Defensa");						//7
		autores.add(u.get(44));
		autores.add(u.get(98));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Defensa Nacional", "Las MTT y las FE");									//8
		autores.add(u.get(71));
		autores.add(u.get(98));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Deporte", "El deporte y el estrés laboral");								//9
		autores.add(u.get(22));
		autores.add(u.get(76));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Deporte", 
				"La interdisciplinariedad en la práctica sistemática del deporte");					//10
		autores.add(u.get(17));
		autores.add(u.get(22));
		autores.add(u.get(76));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Deporte",
				"La ocurrencia de traumas y lesiones en la actividad laboral");						//11
		autores.add(u.get(22));
		autores.add(u.get(76));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Deporte",
				"La práctica sistemática de deportes como forma sana, culta y útil");				//12
		autores.add(u.get(17));
		autores.add(u.get(22));
		autores.add(u.get(76));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Deporte", "Valores y virtudes del deporte");								//13
		autores.add(u.get(17));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Derecho", "Ensayo: Criminología - Cosette Ramos");							//14
		autores.add(u.get(8));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Derecho", "Ensayo: Criminología - Emilio Lozada");							//15
		autores.add(u.get(9));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Derecho", "Ensayo: Derecho Romano - Cosette Ramos");						//16
		autores.add(u.get(8));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Derecho", "Ensayo: Derecho Romano - Emilio Lozada");						//17
		autores.add(u.get(9));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Derecho", "Ensayo: Documentos jurídicos - Cosette Ramos");					//18
		autores.add(u.get(8));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Derecho", "Ensayo: Documentos jurídicos - Emilio Lozada");					//19
		autores.add(u.get(9));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Derecho",
				"Ensayo: Historia del Estado y el Derecho en Cuba - Cosette Ramos");				//20
		autores.add(u.get(8));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Derecho",
				"Ensayo: Historia del Estado y el Derecho en Cuba - Emilio Lozada");				//21
		autores.add(u.get(9));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Derecho", "Tipos de Estado modernos");										//22
		autores.add(u.get(8));
		autores.add(u.get(9));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Diseño", "La forma en el espacio unidimensional");							//23
		autores.add(u.get(77));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Diseño", "La forma en el espacio bidimensional");							//24
		autores.add(u.get(77));
		autores.add(u.get(122));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Diseño", "La forma en el espacio tridimensional");							//25
		autores.add(u.get(77));
		autores.add(u.get(123));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Diseño Geométrico de Obras Viales", "Diseño geométrico de carreteras");	//26
		autores.add(u.get(11));
		autores.add(u.get(12));
		autores.add(u.get(16));
		autores.add(u.get(120));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Diseño Geométrico de Obras Viales", "Drenaje Víal");						//27
		autores.add(u.get(11));
		autores.add(u.get(12));
		autores.add(u.get(16));
		autores.add(u.get(120));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Economía", "Costos y gastos directos e indirectos");						//28
		autores.add(u.get(24));
		autores.add(u.get(51));
		autores.add(u.get(78));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Economía", "Ideas de negocios");											//29
		autores.add(u.get(13));
		autores.add(u.get(24));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Economía", "Rentabilidad del negocio");									//30
		autores.add(u.get(13));
		autores.add(u.get(105));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Filosofía",
				"Ideas de Ernesto Guevara sobre la enajenación");									//31
		autores.add(u.get(54));
		autores.add(u.get(81));
		autores.add(u.get(108));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Filosofía",
				"La teoría del conocimiento Dialéctico Materialista");								//32
		autores.add(u.get(54));
		autores.add(u.get(108));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Industria alimentaria",
				"Control de calidad en la industria alimentaria");									//33
		autores.add(u.get(38));
		autores.add(u.get(65));
		autores.add(u.get(92));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Industria alimentaria",
				"Control de la producción en la industria alimentaria");							//34
		autores.add(u.get(65));
		autores.add(u.get(92));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Industria alimentaria",
				"Control del impacto a la salud de los alimentos procesados");						//35
		autores.add(u.get(38));
		autores.add(u.get(65));
		autores.add(u.get(121));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Inteligencia artificial",
				"Aprendizaje automático para la detección de fraudes");								//36
		autores.add(u.get(2));
		autores.add(u.get(14));
		autores.add(u.get(18));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Inteligencia artificial",
				"Reconocimiento de imágenes mediante redes neuronales");							//37
		autores.add(u.get(0));
		autores.add(u.get(1));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Inteligencia artificial", "Robótica autónoma");							//38
		autores.add(u.get(0));
		autores.add(u.get(1));
		autores.add(u.get(2));
		autores.add(u.get(15));
		autores.add(u.get(18));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Inteligencia artificial",
				"Sistemas de recomendación basados en el aprendizaje automático");					//39
		autores.add(u.get(0));
		autores.add(u.get(1));
		autores.add(u.get(2));
		autores.add(u.get(15));
		autores.add(u.get(18));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Materiales", "Microscopía óptica");										//40
		autores.add(u.get(11));
		autores.add(u.get(12));
		autores.add(u.get(16));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Materiales", "Adiciones al hormigón");										//41
		autores.add(u.get(11));
		autores.add(u.get(12));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Medicina",
				"Efectos del tabaquismo y el alcoholismo al organismo");							//42
		autores.add(u.get(43));
		autores.add(u.get(70));
		autores.add(u.get(97));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Modelación Mecánica de Estructuras", 
				"Cálculos de una nave industrial de hormigón y acero");								//43
		autores.add(u.get(11));
		autores.add(u.get(12));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Morfología", "Análisis de movimientos");									//44
		autores.add(u.get(43));
		autores.add(u.get(70));
		autores.add(u.get(97));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Petroquímica", "Control de la extracción del crudo");						//45
		autores.add(u.get(38));
		autores.add(u.get(92));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Petroquímica", "Control de los derivados del petróleo");					//46
		autores.add(u.get(38));
		autores.add(u.get(92));
		autores.add(u.get(121));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Petroquímica", "Control del proceso de refinación");						//47
		autores.add(u.get(92));
		autores.add(u.get(119));
		autores.add(u.get(121));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Política", "La organización política de la sociedad");						//48
		autores.add(u.get(46));
		autores.add(u.get(73));
		autores.add(u.get(100));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Política", "Tipos de estado y formas de gobierno");						//49
		autores.add(u.get(46));
		autores.add(u.get(73));
		autores.add(u.get(100));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Procesamiento de datos masivos",
				"Optimización de almacenamiento y gestión de datos masivos");						//50
		autores.add(u.get(25));
		autores.add(u.get(28));
		autores.add(u.get(31));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Procesamiento de datos masivos",
				"Procesamiento de datos masivos en tiempo real");									//51
		autores.add(u.get(25));
		autores.add(u.get(28));
		autores.add(u.get(31));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Seguridad Informática",
				"Análisis de las vulnerabilidades de las redes Wi-Fi");								//52
		autores.add(u.get(0));
		autores.add(u.get(2));
		autores.add(u.get(7));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Seguridad Informática", "Análisis y detección de malware");				//53
		autores.add(u.get(0));
		autores.add(u.get(1));
		autores.add(u.get(2));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Seguridad Informática", "Criptografía y protocolos de seguridad");			//54
		autores.add(u.get(0));
		autores.add(u.get(3));
		autores.add(u.get(4));
		autores.add(u.get(5));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Seguridad Informática",
				"Estudio de vulnerabilidades en sitios y aplicaciones web");						//55
		autores.add(u.get(0));
		autores.add(u.get(7));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Topografía", "Estudios topográficos - Adriana Domínguez");					//56
		autores.add(u.get(16));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Topografía", "Estudios topográficos - Gabriela Gómez");					//57
		autores.add(u.get(120));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Topografía", "Estudios topográficos - Mario Melo");						//58
		autores.add(u.get(11));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Topografía", "Estudios topográficos - Miguel Álvarez");					//59
		autores.add(u.get(12));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		t = new Trabajo("Topografía", "Nuevas tecnologías de la topografía");						//60
		autores.add(u.get(11));
		autores.add(u.get(12));
		autores.add(u.get(16));
		autores.add(u.get(120));
		t.getAutores().addAll(autores);
		autores.clear();
		trabajos.add(t);

		return trabajos ;
	}

}
