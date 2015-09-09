package ch.imlee.maturarbeit.game;

/**
 * Created by Sandro on 04.06.2015.
 */
public interface Tick {
    int TICK = 30;
    int TIME_PER_TICK = 1000 / TICK;
    int LOADING_TICK = 1;
    int TIME_PER_LOADING_TICK = 1000 / LOADING_TICK;
}