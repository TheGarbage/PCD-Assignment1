package pcd.lab02.check_act;

public class Counter {

	private int cont;
	private int min, max;
	
	public Counter(int min, int max){
		this.cont = this.min = min;
		this.max = max;
	}
	
	public void inc() throws OverflowException {
		synchronized (this) {		// Il lock non è a livello di singolo metodo ma di oggetto
			if (cont + 1 > max) {
				throw new OverflowException();
			}
			cont++;
		}
	}

	public void dec() throws UnderflowException {
		synchronized (this) {
			if (cont - 1 < min) {
				throw new UnderflowException();
			}
			cont--;
		}
	}
	
	public synchronized int getValue(){
		return cont;
	}
}
