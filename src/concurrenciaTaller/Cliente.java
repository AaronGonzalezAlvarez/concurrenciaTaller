package concurrenciaTaller;

import java.util.Random;

public class Cliente extends Thread {

	private TallerSynchronized taller;
	private int num;

	private Random rand = new Random();
	public Cliente(TallerSynchronized taller, int num) {
		this.taller = taller;
		this.num = num;
	}

	public void run() {
		try {
			//Thread.sleep((1 + rand.nextInt(2)) * 1000);
			taller.vehiculo(num);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
