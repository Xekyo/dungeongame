package dg.dev;

import java.awt.event.KeyEvent;

import dg.Affiliation;
import dg.Agent;
import dg.Coordinates;
import dg.Gameboard;
import dg.event.EventHandler;
import dg.gui.Action;
import dg.gui.ImageCache;
import dg.gui.Menu;
import dg.gui.animation.AnimationQueue;

/**
 * Eine spielerartige Figur, f�r Entwicklungszwecke.
 */
public class Dummy extends Agent {

	public Dummy(Coordinates spawnpoint, Gameboard board) {
		super(spawnpoint, board);
		affiliation = Affiliation.DUNGEON;
	}

	@Override
	public void kill() {
	}

	@Override
	public void takeTurn() {
		// wait for animations to end
		AnimationQueue.onAnimationEnd(new EventHandler() {
			@Override
			public void onEvent() {
				// set menu buttons
				Menu.setActions(new Action("Zug beenden") {
					public void execute() {
						finishTurn();
					}
				},
				new Action("Hello") {
					public void execute() {
						System.out.println("Hello World");
					}
				});
			}
		});
		
		
	}

	@Override
	public String getImage() {
		return ImageCache.DUMMY;
	}

	@Override
	public void onKeyPressed(KeyEvent e) {
	}

}
