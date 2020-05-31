/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.food.db.CoppiaCibi;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {

	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtPorzioni"
	private TextField txtPorzioni; // Value injected by FXMLLoader

	@FXML // fx:id="txtK"
	private TextField txtK; // Value injected by FXMLLoader

	@FXML // fx:id="btnAnalisi"
	private Button btnAnalisi; // Value injected by FXMLLoader

	@FXML // fx:id="btnCalorie"
	private Button btnCalorie; // Value injected by FXMLLoader

	@FXML // fx:id="btnSimula"
	private Button btnSimula; // Value injected by FXMLLoader

	@FXML // fx:id="boxFood"
	private ComboBox<Food> boxFood; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML
	void doCreaGrafo(ActionEvent event) {
		txtResult.clear();
		
		String porzioni = this.txtPorzioni.getText();
		Integer p;

		try {
			p = Integer.parseInt(porzioni);
		} catch (NumberFormatException e) {
			this.txtResult.appendText("Inserire portion_id valido!");
			return;
		}
		
		this.boxFood.getItems().clear();
		this.boxFood.getItems().addAll(model.getFoodPortion(p));
	
		if (this.txtPorzioni == null) {
			this.txtResult.appendText("Inserire porzioni!");
			return;
		}

		model.creaGrafo();
		this.txtResult
				.appendText(String.format("Grafo creato! %d archi e %d vertici", model.nArchi(), model.nVertici()));

	}

	@FXML
	void doCalorie(ActionEvent event) {
		txtResult.clear();
		
		Map<Integer, Food> idMap = model.getIdMap();
		
		List<CoppiaCibi> cibi = model.getAdiacenti(this.boxFood.getValue());
		
		if(this.boxFood.getValue() == null) {
			this.txtResult.appendText("seleziona cibo!");
			return;
		}
		
		System.out.println(cibi);
		
		for(int i = 0; i<5 && i<cibi.size();i++){
			this.txtResult.appendText(String.format("%s %f\n", idMap.get(cibi.get(i).getF1()).getDisplay_name(),cibi.get(i).getPeso()));
				}
	}

	@FXML
	void doSimula(ActionEvent event) {
		txtResult.clear();
		txtResult.appendText("Simulazione...");
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
		assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
		assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
		assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
		assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
		assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
	}

	public void setModel(Model model) {
		this.model = model;
	}
}
