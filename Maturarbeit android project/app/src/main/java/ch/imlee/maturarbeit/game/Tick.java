package ch.imlee.maturarbeit.game;

// this interface allows quick changes at the Tick speed and als0 gives simple access to this important data
public interface Tick {
    // game tick
    int TICK = 25;
    int TIME_PER_TICK = 1000 / TICK;

    // tick of the loading screen
    int LOADING_TICK = 1;
    int TIME_PER_LOADING_TICK = 1000 / LOADING_TICK;
}