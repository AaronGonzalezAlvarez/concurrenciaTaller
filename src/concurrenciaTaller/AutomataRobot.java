package concurrenciaTaller;

public class AutomataRobot extends Thread{
	
	private TallerSynchronized taller;
	
	public AutomataRobot(TallerSynchronized taller) {
		this.taller = taller;
	}
	
	public void run() {
			try {
				taller.robotMontacargas();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
	}

}
