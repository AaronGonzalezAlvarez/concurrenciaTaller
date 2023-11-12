package concurrenciaTaller;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TallerLock {
	private AceiteTaller aceiteTaller;
	
	public TallerLock(AceiteTaller aceiteTaller){
		this.aceiteTaller = aceiteTaller;
	}

	private ReentrantLock lockCliente = new ReentrantLock();
	private Condition hacerCambioAceite = lockCliente.newCondition();
	private Condition hacerRevisionGeneral = lockCliente.newCondition();
	private Condition cogerAceite = lockCliente.newCondition();
	
	private ReentrantLock lockRobot = new ReentrantLock();
	private Condition renovarAceite = lockRobot.newCondition();
	
    private Random rand = new Random();

	public void vehiculo(int i) throws InterruptedException {

		while (aceiteTaller.verificarPuestoDeCambioDeAceite()) {
			System.err.println("Esperando el cambio de aceite del coche " + i);
			lockCliente.lock();
			try {
				hacerCambioAceite.await();
			} finally {
				lockCliente.unlock();
			}
		}

		aceiteTaller.addPuestoDeCambioDeAceite();
		aceiteTaller.addPrueba();

		lockCliente.lock();
		try {
			while (aceiteTaller.verificarEstadoAceite()) {
				System.err.println("no hay aceite tiene que venir el robot lo dice el coche " + i);
				lockRobot.lock();
				renovarAceite.signal();
				lockRobot.unlock();
				hacerCambioAceite.await();
			}
			aceiteTaller.consumoAceite();
		} finally {
			lockCliente.unlock();
		}

		System.out.println("cambiando aceite coche " + i + " numero " + aceiteTaller.verAceiteConsumido());
		Thread.sleep((1 + rand.nextInt(10)) * 1000);
		aceiteTaller.deletePuestoDeCambioDeAceite();

		lockCliente.lock();
		try {
			hacerCambioAceite.signalAll();
		} finally {
			lockCliente.unlock();
		}
		
		//fin cambio de aceite
		
		while (aceiteTaller.verificarPuestoRerivisonGeneral()) {
			System.err.println("Esperando la revision general del coche " + i);
			lockCliente.lock();
			try {
				hacerRevisionGeneral.await();
			} finally {
				lockCliente.unlock();
			}
		}
		revisionGeneral(i);
		lockCliente.lock();
		try {
			hacerRevisionGeneral.signalAll();
		} finally {
			lockCliente.unlock();
		}
		System.out.println("me voy del taller coche "+ i);
		
	}
	
	/*public void cambiarAceite(int i) throws InterruptedException {
		while(aceite.get() == consumoTotal) {
			System.err.println("no hay aceite, el coche "+ i + "llamaa la robot");
			renovarAceite.await();
		}
		aceite.incrementAndGet();
		System.out.println("cambiando el aceite: " +i + " y el consumo total de aceite es :" + aceite.get());
		Thread.sleep((1 + rand.nextInt(2)) * 1000);

	}*/

	public void revisionGeneral(int i) throws InterruptedException {
		aceiteTaller.addPuestoRerivisonGeneral();
		System.out.println("haciendo la revision general del coche : " +i);
		Thread.sleep((10 + rand.nextInt(5)) * 1000);
		aceiteTaller.deletePuestoRerivisonGeneral();

	}

	public void robotMontacargas() throws InterruptedException {
		lockRobot.lock();
		try {
			while (true) {
				if (aceiteTaller.verificarEstadoAceite()) {
					reponerBidones();
					System.err.println("CAmbio realizado");
					renovarAceite.await();
				} else {
					renovarAceite.await();
				}
			}
		} finally {
			lockRobot.unlock();
		}
	}

	public void reponerBidones() {
		aceiteTaller.reponerAceite();
	}
}
