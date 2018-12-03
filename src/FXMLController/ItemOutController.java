package FXMLController;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ItemOutController implements Initializable {
	
	@FXML
	private TextField customerNameTf, noteTf, billDestDirTf, discountByAmountBathTf, discountByPercentTf, vatTf;
	
	@FXML
	private Label billIDLabel, billDateLabel, priceLabel, discountByPercentBathLabel, vatBathLabel, profitBathLabel, netPriceLabel;
		
	@FXML
	private TableView itemOutTv;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		if ( customerNameTf == null ) {
			System.out.println("null");
		}
		
	}
}
