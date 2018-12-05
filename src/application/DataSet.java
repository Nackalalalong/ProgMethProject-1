package application;

import java.io.File;

import factory.ApplicationFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DataSet {
	
	public static final int CELL_IMAGE_WIDTH = 90;

	private ImageView image;
	private String itemId, itemSn, itemName, unit, amount, buyPrice, sellPrice, category, subCategory, note;
	
	public DataSet(String imagePath, String itemId, String itemSn, String itemName, String unit, String amount, String buyPrice, String sellPrice, String category, String subCategory, String note) {
		
		File file = new File(imagePath);
		
		if ( file.exists() ) {
			image = new ImageView(new Image(file.toURI().toString()));
		}
		else {
			image = new ImageView(new Image(ClassLoader.getSystemResource("icons/box.png").toString()));
		}
		
		image.setFitHeight(CELL_IMAGE_WIDTH);
		image.setFitWidth(CELL_IMAGE_WIDTH);
		
		this.itemId = itemId;
		this.itemSn = itemSn;
		this.itemName = itemName;
		this.unit = unit;
		this.amount = amount;
		this.buyPrice = buyPrice;
		this.sellPrice = sellPrice;
		this.category = category;
		this.subCategory = subCategory;
		this.note = note;
	}

	public ImageView getImage() {
		return image;
	}

	public void setImage(ImageView image) {
		this.image = image;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemSn() {
		return itemSn;
	}

	public void setItemSn(String itemSn) {
		this.itemSn = itemSn;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public String getBuyPrice() {
		return buyPrice;
	}
	
	public void setBuyPrice(String buyPrice) {
		this.buyPrice = buyPrice;
	}

	public String getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(String price) {
		this.sellPrice = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	
	
}
