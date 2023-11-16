package concurrenciaTaller;

public class AutomataRobot extends Thread{
	
	private Taller taller;
	
	public AutomataRobot(Taller taller) {
		this.taller = taller;
	}
	
	public void run() {
			taller.robotMontacargas();	
	}

}
