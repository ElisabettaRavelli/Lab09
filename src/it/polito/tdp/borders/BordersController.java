/**
à * Skeleton for 'Borders.fxml' Controller Class
 */

package it.polito.tdp.borders;

import java.util.List;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.CountryAndNumber;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class BordersController {

	Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtAnno"
	private TextField txtAnno; // Value injected by FXMLLoader
	
	@FXML
    private ComboBox<String> ComboBox;

    @FXML
    private Button btnTrovaVicini;

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML
	void doCalcolaConfini(ActionEvent event) {
		
		String annoS = txtAnno.getText();
		
		try {
			int anno = Integer.parseInt(annoS);
			
			if(anno<1816 || anno>2016)
				return;
			
			model.creaGrafo(anno);
			
			List<CountryAndNumber> list = model.getCountryAndNumber();

			if (list.size() == 0) {
				txtResult.appendText("Non ci sono stati corrispondenti\n");
			} else {
				txtResult.appendText("Stati nell'anno: "+anno+"\n");
				for (CountryAndNumber c : list) {
					txtResult.appendText(c.getCountry().getNome() + " " + c.getNumber() + "\n");
				}
			}
			
			int cc = model.calcolaCC();
			txtResult.appendText("Numero di componenti connesse: " + String.valueOf(cc)+"\n");
			
			//riempio la ComboBox
			for(Country c : model.getCountries()) {
				ComboBox.getItems().add(c.getAbb());
			}

		} catch (NumberFormatException e) {
			txtResult.appendText("Errore di formattazione dell'anno\n");
			return;
		}
	}
	
	@FXML
    void doTrovaVicini(ActionEvent event) {
		
		String valore = ComboBox.getValue();
		Country valoreC =model.getCountry(valore);
		List<Country> list = model.trovaRaggiungibili(valoreC);
		
		txtResult.clear();
		txtResult.appendText("Stati raggiungibili:\n");
		for(Country tmp: list) {
			txtResult.appendText(tmp.getNome()+ "\n");
		}
		

    }


	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Borders.fxml'.";
		assert ComboBox != null : "fx:id=\"ComboBox\" was not injected: check your FXML file 'Borders.fxml'.";
        assert btnTrovaVicini != null : "fx:id=\"btnTrovaVicini\" was not injected: check your FXML file 'Borders.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Borders.fxml'.";
	}
	
	public void setModel(Model model) {
		this.model = model;
		
		
	}

}
