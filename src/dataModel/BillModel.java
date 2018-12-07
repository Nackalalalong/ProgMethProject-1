package dataModel;

public class BillModel {

	private String billId, billDate, customerName, note;

	public BillModel(String billId, String billDate, String customerName, String note) {
		super();
		this.billId = billId;
		this.billDate = billDate;
		this.customerName = customerName;
		this.note = note;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	
	
}
