package dataModel;

public class CustomerTableModel {

	private String customerName,  buyAmount,  lastestBuyDate,  totalBuy,  totalProfit, note;

	public CustomerTableModel(String customerName, String buyAmount, String lastestBuyDate, String totalBuy,
			String totalProfit, String note) {
		super();
		this.customerName = customerName;
		this.buyAmount = buyAmount;
		this.lastestBuyDate = lastestBuyDate;
		this.totalBuy = totalBuy;
		this.totalProfit = totalProfit;
		this.note = note;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getBuyAmount() {
		return buyAmount;
	}

	public String getLastestBuyDate() {
		return lastestBuyDate;
	}

	public String getTotalBuy() {
		return totalBuy;
	}

	public String getTotalProfit() {
		return totalProfit;
	}
	
	public String getNote() {
		return note;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
}
