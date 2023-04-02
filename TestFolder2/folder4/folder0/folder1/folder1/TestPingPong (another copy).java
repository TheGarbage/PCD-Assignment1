package pcd.lab03.sem.ex;

import java.util.concurrent.Semaphore;

/**
 * Unsynchronized version
 * 
 * @TODO make it sync 
 * @author aricci
 *
 */
public class TestPingPong {
	public static void main(String[] args) {
		Semaphore pingDone = new Semaphore(0);
		Semaphore pongDone = new Semaphore(0);
		new Pinger(pingDone, pongDone).start();
		new Ponger(pongDone, pingDone).start();

		pongDone.release(); // Triggero uno dei eventi, faccio partire prima il ping (triggero che ho fatto il pong)
	}

}
