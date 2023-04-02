package pcd.lab02.lost_updates;

public class SafeCounter {

    private Object lock;
    private int cont;

    public SafeCounter(int base, Object lock){
        this.cont = base;
        this.lock = lock;
    }

    public void inc(){
        synchronized (lock) {
            cont++;
        }
    }

    public int getValue(){
        return cont;
    }
}
