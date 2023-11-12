package concurrenciaTaller;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TallerSynchronized {
	private AceiteTaller aceiteTaller;
	
	public TallerSynchronized(AceiteTaller aceiteTaller){
		this.aceiteTaller = aceiteTaller;
	}
	
    private Random rand = new Random();
	private Object cogerAceite = new Object();
	private Object robot = new Object();
	private Object cambioAceite = new Object();
	private Object revisionGeneral = new Object();

	public void vehiculo(int i) throws InterruptedException {

		while (aceiteTaller.verificarPuestoDeCambioDeAceite()) {
			System.err.println("Esperando el cambio de aceite del coche " + i);
			synchronized (cambioAceite) {
				cambioAceite.wait();
			}
		}

		aceiteTaller.addPuestoDeCambioDeAceite();
		
		while(aceiteTaller.verificarEstadoAceite()) {
			System.err.println("No hay aceite hay que llamar al robot lo dice el coche "+ i);
			synchronized (robot) {
				robot.notify();
			}
			synchronized (cambioAceite) {
				cambioAceite.wait();
			}
		}
		
		aceiteTaller.consumoAceite();
		System.out.println("haciendo el cambio de aceite el coche "+ i);
		Thread.sleep((1 + rand.nextInt(5)) * 1000);
		synchronized (cambioAceite) {
			aceiteTaller.deletePuestoDeCambioDeAceite();
			cambioAceite.notifyAll();
		}
		
		//fin cambio de aceite
		
		while (aceiteTaller.verificarPuestoRerivisonGeneral()) {
			System.err.println("Esperando el cambio de aceite del coche " + i);
			synchronized (revisionGeneral) {
				revisionGeneral.wait();
			}
		}
		revisionGeneral(i);
		synchronized (revisionGeneral) {
			System.out.println("Me voy del taller coche "+ i);
			revisionGeneral.notifyAll();
		}
		
	}

	public void revisionGeneral(int i) throws InterruptedException {
		aceiteTaller.addPuestoRerivisonGeneral();
		System.out.println("haciendo la revision general del coche : " +i);
		Thread.sleep((1 + rand.nextInt(5)) * 1000);
		aceiteTaller.deletePuestoRerivisonGeneral();

	}

	public void robotMontacargas() throws InterruptedException {
		synchronized (robot) {
			while (true) {
				if (aceiteTaller.verificarEstadoAceite()) {
					reponerBidones();
					System.err.println("CAmbio realizado");
					synchronized (cambioAceite) {
						cambioAceite.notifyAll();
					}
					robot.wait();
				} else {
					robot.wait();
				}
			}
		}
	}

	public void reponerBidones() {
		aceiteTaller.reponerAceite();
	}
}
