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
	
	for( NavigationButtonPane navBtnPane : navBar.getNavigationButtonPanes() ) {
		if ( navBtnPane.equals(sourcePane) ) {
			navBtnPane.highlightBackground();
		}
		else {
			navBtnPane.unHighlightBackground();
		}
	}
	showContentPane();
	
	}

	public abstract void showContentPane();
	
}
