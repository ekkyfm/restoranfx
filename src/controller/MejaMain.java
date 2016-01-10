
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.GenericDao;
import java.io.IOException;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Meja;

/**
 *
 * @author VIKI
 */
public class MejaMain extends Application {
    
    private Stage primaryStage;
    private BorderPane rootLayout;
    private GenericDao genericDao = new GenericDao();
    
    @FXML
    private TableView<Meja> mejaTableView;
    
    @FXML
    private TableColumn<Meja, Integer> noMejaColumn;
    
    @FXML
    private TableColumn<Meja, String> kategoriColumn;
    
    @FXML
    private TableColumn<Meja, Integer> lantaiColumn;
    
    @FXML
    private TableColumn<Meja, Integer> kapasitasColumn;
    
    @FXML
    private TableColumn<Meja, Integer> statusColumn;
    
    
    @FXML
    private TextField textNoMeja;
    
    @FXML
    private ComboBox textKategori;
    
    @FXML
    private TextField textLantai;
    
    @FXML
    private TextField textKapasitas;
    
    @FXML
    private ComboBox textStatus;
    
    @FXML
    private Button buttonUbahMeja;
    
    @FXML
    private Button buttonHapusMeja;
    
    @FXML
    private Button buttonTambahMeja;
    
    @FXML
    private Button buttonCancelMeja;
    
    ObservableList<Meja> dataMeja = FXCollections.observableArrayList();
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage=primaryStage;
        this.primaryStage.setTitle("Meja");
      
        initRootLayout();
    }
    
    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PesananController.class.getResource("../view/admin/Meja.fxml"));
            rootLayout=  loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void initialize(){
        
        mejaTableView.getItems().clear();
        textKategori.getItems().addAll("Smoking", "No Smoking");
        textStatus.getItems().addAll("Tersedia", "Tidak Tersedia");
        noMejaColumn.setCellValueFactory(new PropertyValueFactory<Meja, Integer>("noMeja"));
        kategoriColumn.setCellValueFactory(new PropertyValueFactory<Meja, String>("kategori"));
        lantaiColumn.setCellValueFactory(new PropertyValueFactory<Meja, Integer>("lantai"));
        kapasitasColumn.setCellValueFactory(new PropertyValueFactory<Meja, Integer>("kapasitas"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Meja, Integer>("status"));
        mejaTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> mejaDetail(newValue)  );
    
        textNoMeja.setVisible(false);
        handleCancelMeja();
        mejaTableView.setItems(getAllMeja());
        
    }
    
    public ObservableList getAllMeja(){
        GenericDao genericDao = new GenericDao();
        dataMeja = FXCollections.observableList((List<Meja>) genericDao.getAllData("from Meja"));
        return dataMeja;        
    }
    
    public void mejaDetail(Meja meja){
        if(meja != null){
            disable();
            buttonUbahMeja.setDisable(false);
            buttonHapusMeja.setDisable(false);
            buttonCancelMeja.setVisible(true);
            buttonTambahMeja.setText("Tambah");

            textNoMeja.setText(String.valueOf(meja.getNoMeja()));
            textKategori.setValue(meja.getKategori());
            textLantai.setText(String.valueOf(meja.getLantai()));
            textKapasitas.setText(String.valueOf(meja.getKapasitas()));

            if(meja.getStatus() == 0)
                textStatus.setValue("Tersedia");
            else
                textStatus.setValue("Tidak Tersedia");
        }
        
        
    }
    
    @FXML
    private void handleNew(){
        if(buttonTambahMeja.getText().equals("Tambah")){
            enable();
            clearMeja();
            buttonHapusMeja.setDisable(true);
            buttonUbahMeja.setDisable(true);
            buttonTambahMeja.setText("Simpan");
            buttonCancelMeja.setVisible(true);
        }else{
            handleSaveMeja();
        }
       
    }
    
    private void enable(){
        textNoMeja.setDisable(false);
        textKategori.setDisable(false);
        textLantai.setDisable(false);
        textKapasitas.setDisable(false);
        textStatus.setDisable(false);
    }
    
    private void disable(){
        textNoMeja.setDisable(true);
        textKategori.setDisable(true);
        textLantai.setDisable(true);
        textKapasitas.setDisable(true);
        textStatus.setDisable(true);
    }
    
    @FXML
    private void handleCancelMeja(){
        clearMeja();
        disable();
        buttonDefault();
    }
    
    private void buttonDefault(){
        buttonTambahMeja.setText("Tambah");
        buttonTambahMeja.setVisible(true);
        buttonTambahMeja.setDisable(false);
        
        buttonCancelMeja.setVisible(false);
        
        buttonUbahMeja.setVisible(true);
        buttonUbahMeja.setDisable(true);
        
        buttonHapusMeja.setVisible(true);
        buttonHapusMeja.setDisable(true);
    }
    
    private void clearMeja(){
        textNoMeja.setText("");
        textLantai.setText("");
        textKapasitas.setText("");
    }
    
    
    
    //CRUD
    @FXML
    private void handleSaveMeja(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
        String regex = "[0-9]*";
        if(textLantai.getText().isEmpty() || textKapasitas.getText().isEmpty() || textKategori.getValue() == null || textStatus.getValue() == null ){
           alert.setHeaderText("Form tidak boleh ada yang kosong");
           alert.showAndWait();
        }else if(!textLantai.getText().matches(regex)){
           alert.setHeaderText("Inputan lantai harus angka");
           alert.showAndWait();
        }else if(!textKapasitas.getText().matches(regex)){
           alert.setHeaderText("Inputan kapasitas harus angka");
           alert.showAndWait();
        }else{
            Meja meja = new Meja();
            meja.setKategori(String.valueOf(textKategori.getValue()));
            meja.setLantai(Integer.parseInt(textLantai.getText()));
            meja.setKapasitas(Integer.parseInt(textKapasitas.getText()));
            
            if(textStatus.getValue().equals("Tersedia"))
                meja.setStatus(0);
            else
                meja.setStatus(1);
            

            if(buttonTambahMeja.getText().equals("Simpan")){  
                genericDao.save(meja);
                alert2.setHeaderText("Data berhasil disimpan");
                alert2.showAndWait();
                loadMeja();

            }else if(buttonTambahMeja.getText().equals("Ubah")){
                meja.setNoMeja(Integer.parseInt(textNoMeja.getText()));
                genericDao.update(meja);
                alert2.setHeaderText("Data berhasil diubah");
                alert2.showAndWait();
                loadMeja();
            }

            handleCancelMeja();
        }
            
        
        
    }
    
    @FXML
    private void handleUpdateMeja(){
        enable();
        buttonHapusMeja.setVisible(false);
        buttonUbahMeja.setVisible(false);
        buttonTambahMeja.setDisable(false);
        buttonTambahMeja.setText("Ubah");
        
    }
    
    
    @FXML
    private void handleDeleteMeja() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Apakah anda yakin akan menghapus data ini");
        alert.showAndWait();
        
        
        if(alert.getResult().getText().equals("OK")){
            genericDao.delete((Meja) mejaTableView.getSelectionModel().getSelectedItem());
            
            alert.setHeaderText("Data berhasil dihapus");
            alert.showAndWait();
            
            loadMeja();
            handleCancelMeja();
        }
    }
    
    private void loadMeja(){
        try{
            mejaTableView.getItems().clear();
            noMejaColumn.setCellValueFactory(new PropertyValueFactory<Meja, Integer>("noMeja"));
            kategoriColumn.setCellValueFactory(new PropertyValueFactory<Meja, String>("kategori"));
            lantaiColumn.setCellValueFactory(new PropertyValueFactory<Meja, Integer>("lantai"));
            kapasitasColumn.setCellValueFactory(new PropertyValueFactory<Meja, Integer>("kapasitas"));
            statusColumn.setCellValueFactory(new PropertyValueFactory<Meja, Integer>("status"));
                  
            mejaTableView.setItems(getAllMeja());
            clearMeja();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

  
    public static void main(String[] args) {
        launch(args);
    }
    
}

