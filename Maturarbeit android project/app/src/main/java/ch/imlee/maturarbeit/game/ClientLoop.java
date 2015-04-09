package ch.imlee.maturarbeit.game;

/**
 * Created by Sandro on 09.04.2015.
 */
public class ClientLoop implements  Runnable{

    public final int TICK = 20;
    public final int TIME_PER_TICK = 1000/TICK;

    private boolean running;

    public static int synchronizedTick;
    public static int lagCount;

    private long tickSpareTime;
    private long lastTickTime;
    @Override
    public void run() {
        running = true;
        while (running) {
            lastTickTime = System.currentTimeMillis();

            update();
            render();

            if ((tickSpareTime = System.currentTimeMillis() - (lastTickTime + TIME_PER_TICK)) > 0) {
                try {
                    Thread.sleep(tickSpareTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                    lagCount += tickSpareTime;
                    if(lagCount >= 200){
                        //add a tickRequest Event to the Server Event List
                        lagCount = 0;
                }
            }
            synchronizedTick++;
        }
    }

    private void update(){

    }

    private void render(){

    }
}
