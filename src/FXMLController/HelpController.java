package FXMLController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import factory.BillPDF;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class HelpController implements Initializable {

	@FXML
	private Button viewManualBtn;
	
	@FXML 
	private MediaView mediaView;
	
	@FXML
	private ImageView leftIv, rightIv;
	
	@FXML
	private Label headerText, text1, email1, text2, email2;
	
	private boolean isPlay = false;
	private ArrayList<Node> nodes;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		Image rainbow = new Image(ClassLoader.getSystemResource("rainbow.jpg").toString());
		leftIv.setImage(rainbow);
		rightIv.setImage(rainbow);
		
		String labelStyle = "-fx-background-radius: 8; -fx-background-color: #ffffff; -fx-label-padding: 10px;";
		headerText.setStyle(labelStyle);
		text1.setStyle(labelStyle);
		text2.setStyle(labelStyle);
		email1.setStyle(labelStyle);
		email2.setStyle(labelStyle);
		
		nodes = new ArrayList<Node>();
		nodes.add(text1);
		nodes.add(text2);
		nodes.add(email1);
		nodes.add(email2);
		
		
		Thread buttonColorThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				int i=0;
				int angle = 0;
				while( true ) {
					if ( isPlay ) {
						for(Node node : nodes) {
							node.setRotate(angle);
						}
						
						viewManualBtn.setBackground(new Background(new BackgroundFill(Color.rgb(255-i, i, i), CornerRadii.EMPTY, Insets.EMPTY)));
						i++;
						angle++;
						if ( i > 255 ) {
							i = 0;
						}
						if ( angle > 360 ) {
							angle = 0;
						}
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		buttonColorThread.start();
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				URL cat = ClassLoader.getSystemResource("nyan_cat.mp4");
				MediaPlayer mp = new MediaPlayer(new Media(cat.toString()));
				mediaView.setFitWidth(1200);
				mediaView.setMediaPlayer(mp);
				mediaView.setPreserveRatio(true);
				mp.setAutoPlay(true);
				mp.setOnEndOfMedia(new Runnable() {
					
					@Override
					public void run() {
						mp.seek(Duration.ZERO);
					}
				});
				
				while( true ) {
					if ( isPlay ) {
						mp.play();
					}
					else {
						mp.pause();
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		t.start();
		
	}
	
	public void viewManual() {
		try {
			String path = ClassLoader.getSystemResources("manual.pdf").toString();
			BillPDF.readPDF(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isPlay() {
		return isPlay;
	}

	public void setPlay(boolean isPlay) {
		this.isPlay = isPlay;
	}
	
	

}
