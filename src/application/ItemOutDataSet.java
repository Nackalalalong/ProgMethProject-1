package application;

import javafx.scene.image.ImageView;

public class ItemOutDataSet {
	
	private DataSet dataSet;
	private int sellAmount;
	private double totalPrice;
	
	public ItemOutDataSet(DataSet dataSet, int sellAmount) {
		this.dataSet = dataSet;
		this.sellAmount = sellAmount;
		
		totalPrice = sellAmount * Double.parseDouble(dataSet.getSellPrice());
	}
	
	public String getItemSn() {
		return dataSet.getItemSn();
	}
	
	public ImageView getImage() {
		return dataSet.getImage();
	}
	
	public String getItemId() {
		return dataSet.getItemId();
	}
	
	public String getItemName() {
		return dataSet.getItemName();
	}
	
	public String getBuyPrice() {
		return dataSet.getBuyPrice();
	}

	public String getSellPrice() {
		return dataSet.getSellPrice();
	}
	
	public String getSellAmount() {
		return sellAmount + "";
	}
	
	public String getTotalPrice() {
		return totalPrice + "";
	}
}
