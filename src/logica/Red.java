package logica;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import util.Convert;
import cu.edu.cujae.ceis.graph.LinkedGraph;
import cu.edu.cujae.ceis.graph.interfaces.ILinkedWeightedEdgeNotDirectedGraph;
import cu.edu.cujae.ceis.graph.vertex.Vertex;

public class Red{
	
	private File usuarios;
	private File arcos;
	private File trab;
	private ILinkedWeightedEdgeNotDirectedGraph grafo = new LinkedGraph();
	private LinkedList<Trabajo> trabajos;
	
	public Red() throws IOException, ClassNotFoundException{
		usuarios = new File("data/USUARIOS.DAT");
		arcos = new File("data/ARCOS.DAT");
		trab = new File("data/TRABAJOS.DAT");
		grafo = new LinkedGraph();
		trabajos = new LinkedList<Trabajo>();
		
		/*crearFicheroUsuarios();
		crearFicheroTrabajos();
		crearFicheroArcos();*/
		
		inicializar();
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
