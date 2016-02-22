package pl.tciesla.finansista.view;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import pl.tciesla.finansista.model.Income;

public class IncomesOverviewController {

	private Stage stage;
	
	private static List<Income> exampleIncomes = new LinkedList<>();
	
	static {
		exampleIncomes.add(new Income("Wypłata", new BigDecimal("2500.00")));
		exampleIncomes.add(new Income("Prezent", new BigDecimal("125.00")));
		exampleIncomes.add(new Income("Sprzedaż książki", new BigDecimal("25.50")));
	}
	
	@FXML
	private TableView<Income> incomesTableView;
	@FXML
	private TableColumn<Income, String> incomeNameColumn;
	@FXML
	private TableColumn<Income, String> incomeValueColumn;
	@FXML
	private TableColumn<Income, String> incomeDateColumn;
	
	private ObservableList<Income> incomes = FXCollections.observableArrayList(exampleIncomes);
	
	@FXML
	private void initialize() {
		incomeNameColumn.setCellValueFactory(i -> i.getValue().name());
		incomeNameColumn.setStyle("-fx-alignment: CENTER;");
		incomeValueColumn.setCellValueFactory(i -> i.getValue().value().asString());
		incomeValueColumn.setStyle("-fx-alignment: CENTER;");
		incomeDateColumn.setCellValueFactory(i -> i.getValue().date().asString());
		incomeDateColumn.setStyle("-fx-alignment: CENTER;");
		incomesTableView.getItems().addAll(incomes);
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}
