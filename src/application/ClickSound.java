package application;

import java.net.URL;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class ClickSound extends Thread {

	public ClickSound() {
		super();
		start();
	}
	
	@Override
	public void run() {
		URL sound = ClassLoader.getSystemResource("click.wav");
		MediaPlayer player = new MediaPlayer(new Media(sound.toString()));
		player.play();
	}
	
}
