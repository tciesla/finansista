package pl.tciesla.finansista.model;

public enum Category {
	
	CASH("Cash"), DEPOSITS("Bank deposits"), BONDS("Bonds"),
	STOCK("Stock"), GOODS("Goods"), RECEIVABLES("Receivables");
	
	private String name;
	
	private Category(String name) {
		this.name = name;	
	}

	@Override
	public String toString() {
		return name;
	}
	
}
