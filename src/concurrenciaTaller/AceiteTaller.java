package concurrenciaTaller;

public class AceiteTaller {
	private int totalAceite = 100;
	private int aceiteConsumido = 0;
	private int totalPuestoDeCambioAceite = 0;
	private int totalPuestoRevisionGeneral = 0;
	private int consumoAceitePorCoche = 20;
	private int prueba= 0;
	
	//tema de aceite
	public synchronized void consumoAceite() {
		aceiteConsumido +=consumoAceitePorCoche;
	}
	
	public synchronized void reponerAceite() {
		aceiteConsumido=0;
	}
	
	public synchronized boolean verificarEstadoAceite() {
		return totalAceite == aceiteConsumido;
	}
	
	//tema puesto de mando aceite
	public synchronized void addPuestoDeCambioDeAceite() {
		totalPuestoDeCambioAceite ++;
	}
	
	public synchronized void deletePuestoDeCambioDeAceite() {
		totalPuestoDeCambioAceite --;
	}
	
	public synchronized boolean verificarPuestoDeCambioDeAceite() {
		return totalPuestoDeCambioAceite == 3;
	}
	
	//tema puesto de revision general
	public synchronized void addPuestoRerivisonGeneral() {
		totalPuestoRevisionGeneral++;
	}
	
	public synchronized void deletePuestoRerivisonGeneral() {
		totalPuestoRevisionGeneral--;
	}
	
	public synchronized boolean verificarPuestoRerivisonGeneral() {
		return totalPuestoRevisionGeneral == 5;
	}
	
	public synchronized int verAceiteConsumido() {
		return aceiteConsumido;
	}
	
	public synchronized int verPuestoDeCambioDeAceite() {
		return totalPuestoDeCambioAceite;
	}
	
	//pruebas
	public synchronized void addPrueba() {
		prueba++;
	}
	
	public synchronized int verPrueba() {
		return prueba;
	}
}
