package pcd.lab03.liveness;

/**
 * Deadlock example 
 * 
 * @author aricci
 *
 */
public class TestDeadlockedResource {
	public static void main(String[] args) {
		Resource res = new Resource();
		// Thread A richiede due risorse, prima la destra e poi la sinistra
		new ThreadA(res).start();
		// Thread B richiede due anche lui le stesse due risorse, prima la sinistra e poi la destra
		new ThreadB(res).start();
		// Questo provoca deadlock perchè si trovano bloccati incrociando le due risorse.
		// Uno prende la destra e l'altro la sinistra, a questo punto quando A cerca di prendere la sinistra la trova
		// bloccata e contemporaneamente quando B cerca du prende la destra la trova bloccata, si bloccano -> DEADLOCK
	}

}
