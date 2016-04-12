package dg;

import javax.swing.SwingUtilities;

import dg.dev.DevUtilities;
import dg.dev.Dummy;
import dg.gui.Frame;
import dg.gui.animation.AnimationQueue;
import dg.gui.animation.Repainter;
import dg.gui.input.Menu;

/**
 * Die erste Klasse. Einstiegspunkt.
 */
public class DungeonGame {

	public static void main(String... args) {
		// Spielbrett laden
		GameState.setBoard(DevUtilities.getRandomGameboard(10));
		
		// Figuren drauf stellen
		GameState.getBoard().addAgent(new Dummy(new Coordinates(1, 1), GameState.getBoard()));
		DevUtilities.addDevGuards();
		
		// GUI starten fï¿½r Anzeige des Bretts
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Menu.init();
					
					Frame.getInstance();
					
					AnimationQueue.init();
					Repainter.init();
					
					// Spielablauf anschmeißen
					GameState.startGame();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
