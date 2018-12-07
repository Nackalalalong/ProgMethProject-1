package application;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;

public abstract class NavigationButtonEventHandler implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		
	NavigationButton source = (NavigationButton)event.getSource();
	NavigationButtonPane sourcePane = source.getNavigationButtonPane();
	NavigationBar navBar = source.getNavigationBar();
	
	for( NavigationButtonPane navBtnPane : navBar.getNavigationButtonPanes() ) {  // loop through every navigation button in navigation bar
		if ( navBtnPane.equals(sourcePane) ) {
			navBtnPane.highlightBackground();
			//navBtnPane.getNavigationButton().highlightBackground();
		}
		else {
			navBtnPane.unHighlightBackground();
			//navBtnPane.getNavigationButton().unHighlightBackground();
		}
	}
	showContentPane();
	
	}

	public abstract void showContentPane();  //set specific FXML pane to front
	
}
