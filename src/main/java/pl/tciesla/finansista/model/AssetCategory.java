package pl.tciesla.finansista.model;

public enum AssetCategory {

	CASH("Cash"), DEPOSIT("Deposit"), BOND("Bond"), STOCK("Stock"), GOODS("Goods"),
		RECEIVABLE("Receivable"), REAL_ESTATE("Real estate");

	private String name;

	private AssetCategory(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
