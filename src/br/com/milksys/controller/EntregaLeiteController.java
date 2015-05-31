package br.com.milksys.controller;

import java.time.LocalDate;
import java.util.Calendar;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.model.CalendarioRecolha;
import br.com.milksys.model.EntregaLeite;
import br.com.milksys.model.Mes;
import br.com.milksys.service.CalendarioRecolhaService;
import br.com.milksys.service.EntregaLeiteService;
import br.com.milksys.util.DateUtil;

@Controller
public class EntregaLeiteController {

	private ObservableList<EntregaLeite> data = FXCollections.observableArrayList();
	@Autowired
	private EntregaLeiteService service;
	@FXML private TableView<EntregaLeite> table;
	@FXML private TableColumn<EntregaLeite, String> dataColumn;
	@FXML private TableColumn<EntregaLeite, String> numeroVacasOrdenhadasColumn;
	@FXML private TableColumn<EntregaLeite, String> volumeColumn;
	@FXML private TableColumn<EntregaLeite, String> observacaoColumn;
	@FXML private TableColumn<EntregaLeite, String> calendarioRecolhaColumn;
	@FXML private ComboBox<Mes> inputMesReferencia;
	@FXML private TextField inputAno;
	@FXML private Label lblPeriodo;
	@Resource(name="calendarioRecolhaService")
	private CalendarioRecolhaService calendarioRecolhaService;
	private ObservableList<Mes> optionsMesReferencia = FXCollections.observableArrayList();
	
	private LocalDate dataInicio, dataFim;
	private int selectedAnoReferencia = LocalDate.now().getYear();
	private int selectedMesReferencia = LocalDate.now().getMonthValue();
	
	{
		optionsMesReferencia.add(new Mes(1,  "JANEIRO"));
		optionsMesReferencia.add(new Mes(2,  "FEVEREIRO"));
		optionsMesReferencia.add(new Mes(3,  "MARÇO"));
		optionsMesReferencia.add(new Mes(4,  "ABRIL"));
		optionsMesReferencia.add(new Mes(5,  "MAIO"));
		optionsMesReferencia.add(new Mes(6,  "JUNHO"));
		optionsMesReferencia.add(new Mes(7,  "JULHO"));
		optionsMesReferencia.add(new Mes(8,  "AGOSTO"));
		optionsMesReferencia.add(new Mes(9,  "SETEMBRO"));
		optionsMesReferencia.add(new Mes(10, "OUTUBRO"));
		optionsMesReferencia.add(new Mes(11, "NOVEMBRO"));
		optionsMesReferencia.add(new Mes(12, "DEZEMBRO"));
	}

	@FXML
	public void initialize() {
		dataColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getData())));
		numeroVacasOrdenhadasColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumeroVacasOrdenhadas())));
		volumeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getVolume())));
		observacaoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObservacao()));

		configureDataEntregaMesAnoReferencia(selectedMesReferencia, selectedAnoReferencia);
		
		inputAno.setText(String.valueOf(selectedAnoReferencia));
		inputAno.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				selectedAnoReferencia = Integer.parseInt(newValue);
				configureDataEntregaMesAnoReferencia(selectedMesReferencia, selectedAnoReferencia);
			}
		});
				
		lblPeriodo.setText("Período de " + DateUtil.format(dataInicio) + " à " + DateUtil.format(dataFim));
		
		inputMesReferencia.setItems(optionsMesReferencia);
		inputMesReferencia.getSelectionModel().select(selectedMesReferencia-1);
		inputMesReferencia.valueProperty().addListener(new ChangeListener<Mes>() {
			@Override
			public void changed(ObservableValue<? extends Mes> observable, Mes oldValue, Mes newValue) {
				selectedMesReferencia = newValue.getMesDoAno();
				configureDataEntregaMesAnoReferencia(selectedMesReferencia, selectedAnoReferencia);
			}    
		});
		 // list of values showed in combo box drop down
		inputMesReferencia.setCellFactory(new Callback<ListView<Mes>,ListCell<Mes>>(){
            @Override
            public ListCell<Mes> call(ListView<Mes> l){
                return new ListCell<Mes>(){
                    @Override
                    protected void updateItem(Mes item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getNome());
                        }
                    }
                } ;
            }
        });
		//selected value showed in combo box
		inputMesReferencia.setConverter(new StringConverter<Mes>() {
              @Override
              public String toString(Mes mes) {
                if (mes == null){
                  return null;
                } else {
                  return mes.getNome();
                }
              }

            @Override
            public Mes fromString(String mes) {
                return null;
            }
        });
	
	}
	
	private void configureDataEntregaMesAnoReferencia(int mes, int ano){
		
		int mesAtual = mes;
		int anoAtual = ano;
		int mesInicio, anoInicio, mesFim, anoFim;
		
		CalendarioRecolha calendarioVigente = calendarioRecolhaService.getCalendarioVigente();
		
		//verifica se o calendario de recolha considera dias do mes anterior
		if ( calendarioVigente.getMesInicio().equals(CalendarioRecolha.MES_ANTERIOR) ){
			
			if ( mesAtual == 1 ){
				mesInicio = 12;
				anoInicio = anoAtual - 1;
			}else{
				mesInicio = mesAtual - 1;
				anoInicio = anoAtual;
			}
		}else{
			//mes corrente
			mesInicio = mesAtual;
			anoInicio = anoAtual;
		}
		
		dataInicio = LocalDate.of(anoInicio, mesInicio, calendarioVigente.getDiaInicio());
				
		//verifica se o calendario de recolha considera dias do mes anterior
		if ( calendarioVigente.getMesInicio().equals(CalendarioRecolha.MES_ANTERIOR) ){
			//mes corrente
			anoFim = anoAtual;
			mesFim = mesAtual;
		}else{
			if ( mesAtual == 12 ){
				mesFim = 1;
				anoFim = anoAtual + 1;
			}else{
				mesFim = mesAtual + 1;
				anoFim = anoAtual;
			}
		}
		
		dataFim = LocalDate.of(anoFim, mesFim, calendarioVigente.getDiaFim());
		
		Calendar cDataInicio = Calendar.getInstance();
		cDataInicio.setTimeInMillis(DateUtil.asDate(dataInicio).getTime());
		
		Calendar cDataFim = Calendar.getInstance();
		cDataFim.setTimeInMillis(DateUtil.asDate(dataFim).getTime());
		
		while ( cDataInicio.before(cDataFim) || cDataInicio.equals(cDataFim) ){
			EntregaLeite el = new EntregaLeite(LocalDate.of(cDataInicio.get(Calendar.YEAR), cDataInicio.get(Calendar.MONTH) + 1, cDataInicio.get(Calendar.DAY_OF_MONTH)), 0, 0, calendarioVigente);
			service.save(el);
			cDataInicio.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		data.clear();
		data.addAll(service.findAllByPeriodoAsObservableList(DateUtil.asDate(dataInicio), DateUtil.asDate(dataFim)));
		table.setItems(data);
		
	}

	protected boolean isInputValid() {
		return true;
	}

}
