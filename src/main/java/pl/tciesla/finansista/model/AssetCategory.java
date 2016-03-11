package pl.tciesla.finansista.model;

import java.util.ResourceBundle;

public enum AssetCategory {

	CASH("cash.label"), DEPOSIT("deposit.label"),
    BOND("bond.label"), STOCK("stock.label"),
    GOODS("goods.label"), RECEIVABLE("receivable.label"),
    REAL_ESTATE("real.estate.label");

	private String name;

	AssetCategory(String name) {
        ResourceBundle bundle = ResourceBundle.getBundle("Bundle");
        String localizedName = bundle.getString(name);
        this.name = localizedName;
	}

	@Override
	public String toString() {
		return name;
	}

}
