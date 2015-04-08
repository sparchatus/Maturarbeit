package com.ch.android.diploma.Client.Event;

import java.util.List;

import com.ch.android.diploma.Client.Entities.Bombs.Bomb;

public interface BombEventInterface {

	public void addBomb(BombEvent event, List<Bomb> bombList);
}
