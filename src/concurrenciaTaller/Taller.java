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
    private Semaphore esperarColaAceiteCambio = new Semaphore(0);
    private Semaphore esperarColaRevisionCoche = new Semaphore(0);
    private Random rand = new Random();
    private AtomicInteger aceite = new AtomicInteger(0);
    int consumoTotalCuandoSeCambia = 5;

	public void vehiculo(int i) throws InterruptedException {
		while (!cambioAceite.tryAcquire()) {
				System.out.println("Esperando el cambio de aceite del coche " + i);
				//Thread.sleep((1 + rand.nextInt(2)) * 1000);
				esperarColaAceiteCambio.acquire();
		}
		cambiarAceite(i);
		cambioAceite.release();
		esperarColaAceiteCambio.release();
		while (!revisionGeneral.tryAcquire()) {
				System.out.println("Esperando a la revision general coche " + i);
				esperarColaRevisionCoche.acquire();
		}
		revisionGeneral(i);
		System.out.println("ME voy del taller el coche : " + i);
		revisionGeneral.release();
		esperarColaRevisionCoche.release();
	}
	
	public void cambiarAceite(int i) throws InterruptedException {
		cogerAceite.acquire();
		while(aceite.get() == consumoTotalCuandoSeCambia) {
			System.out.println("no hay aceite, el coche "+ i + "llamaa la robot");
			robot.acquire();
		}
		aceite.incrementAndGet();
		System.out.println("cambiando el aceite: " +i + " y el consumo total de aceite es :" + aceite.get());
		Thread.sleep((1 + rand.nextInt(2)) * 1000);
		cogerAceite.release();

	}

	public void revisionGeneral(int i) throws InterruptedException {
		System.out.println("haciendo la revision general del coche : " +i);
		Thread.sleep((1 + rand.nextInt(2)) * 1000);

	}

	public void robotMontacargas() {
		while (true) {
			if(robot.availablePermits() ==0) {
				reponerBidones();
				System.out.println("CAmbio realizado");
				robot.release();
			}
		}
	}

	public void reponerBidones() {
		aceite.set(0);
	}
}