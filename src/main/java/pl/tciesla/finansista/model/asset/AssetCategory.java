package pl.tciesla.finansista.model.asset;

public enum AssetCategory {

	CASH("Cash"), DEPOSIT("Deposit"), BOND("Bond"), STOCK("Stock"), GOODS("Goods"),
		RECEIVABLE("Receivable"), REAL_ESTATE("Real estate");

	private String name;

	AssetCategory(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
