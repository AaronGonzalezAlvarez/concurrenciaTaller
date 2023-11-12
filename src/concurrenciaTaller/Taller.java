package concurrenciaTaller;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Taller {
	private AceiteTaller aceiteTaller;
	
	public Taller(AceiteTaller aceiteTaller){
		this.aceiteTaller = aceiteTaller;
	}

    private Semaphore cambioAceite = new Semaphore(3);
    private Semaphore revisionGeneral = new Semaphore(5);
    private Semaphore cogerAceite = new Semaphore(1);
    private Semaphore robot = new Semaphore(1);
    private Random rand = new Random();
    private AtomicInteger aceite = new AtomicInteger(0);
    int consumoTotal = 2;

	public void vehiculo(int i) throws InterruptedException {
		boolean adquirido = false;
		while (!adquirido) {
			if (!cambioAceite.tryAcquire()) {
				System.out.println("Esperando el cambio de aceite del coche " + i);
				Thread.sleep((1 + rand.nextInt(2)) * 1000);
			} else {
				adquirido = true;
			}
		}
		cambiarAceite(i);
		cambioAceite.release();
		adquirido = false;
		while (!adquirido) {
			if (!revisionGeneral.tryAcquire()) {
				System.out.println("Esperando a la revision general coche " + i);
				Thread.sleep((1 + rand.nextInt(2)) * 1000);
			} else {
				adquirido = true;
			}
		}
		cambioAceite.release();
		revisionGeneral(i);
		System.out.println("ME voy del taller el coche : " + i);
		revisionGeneral.release();
	}
	
	public void cambiarAceite(int i) throws InterruptedException {
		cogerAceite.acquire();
		while(aceite.get() == consumoTotal) {
			System.err.println("no hay aceite, el coche "+ i + "llamaa la robot");
			robot.acquire();
		}
		aceite.incrementAndGet();
		cogerAceite.release();
		System.out.println("cambiando el aceite: " +i + " y el consumo total de aceite es :" + aceite.get());
		Thread.sleep((1 + rand.nextInt(2)) * 1000);

	}

	public void revisionGeneral(int i) throws InterruptedException {
		System.out.println("haciendo la revision general del coche : " +i);
		Thread.sleep((1 + rand.nextInt(2)) * 1000);

	}

	public void robotMontacargas() {
		while (true) {
			if(robot.availablePermits() ==0) {
				reponerBidones();
				System.err.println("CAmbio realizado");
				robot.release();
			}
		}
	}

	public void reponerBidones() {
		aceite.set(0);
	}
}