package concurrenciaTaller;

public class ConcurrenciaTaller {

	public static void main(String[] args) {

		AceiteTaller estados = new AceiteTaller();
		Taller taller = new Taller(estados);
		//TallerLock taller = new TallerLock(estados);
		//TallerSynchronized taller = new TallerSynchronized(estados);
		AutomataRobot robot = new AutomataRobot(taller);
		robot.start();
		
		for(int x=0; x< 10;x++) {
			Cliente cliente = new Cliente(taller,x);
			cliente.start();
		}

	}

}
