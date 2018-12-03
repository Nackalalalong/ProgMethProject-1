package FXMLController;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ItemOutController  {
	
	@FXML
	private TextField customerNameTf, noteTf, billDestDirTf, discountByAmountBathTf, discountByPercentTf, vatTf;
	
	@FXML
	private Label billIDLabel, billDateLabel, priceLabel, discountByPercentBathLabel, vatBathLabel, profitBathLabel, netPriceLabel;
		
	@FXML
	private TableView itemOutTv;
	
	public ItemOutController() {
		
	}

	@FXML
	private URL location;
	     
	@FXML
	private ResourceBundle resources;
	
	@FXML
	public void initialize() {
		// TODO Auto-generated method stub
		
		if ( customerNameTf == null ) {
			System.out.println("null");
		}
		
	}
}
