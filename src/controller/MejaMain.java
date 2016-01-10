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
        noMejaColumn.setCellValueFactory(new PropertyValueFactory<Meja, Integer>("noMeja"));
        kategoriColumn.setCellValueFactory(new PropertyValueFactory<Meja, String>("kategori"));
        lantaiColumn.setCellValueFactory(new PropertyValueFactory<Meja, Integer>("lantai"));
        kapasitasColumn.setCellValueFactory(new PropertyValueFactory<Meja, Integer>("kapasitas"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Meja, Integer>("status"));
        mejaTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> mejaDetail(newValue)  );
    
        disable();
        buttonUbahMeja.setDisable(true);
        buttonHapusMeja.setDisable(true);
        textNoMeja.setVisible(false);
        mejaTableView.setItems(getAllMeja());
        
    }
    
    public ObservableList getAllMeja(){
        GenericDao genericDao = new GenericDao();
        dataMeja = FXCollections.observableList((List<Meja>) genericDao.getAllData("from Meja"));
        return dataMeja;        
    }
    
    public void mejaDetail(Meja meja){
        disable();
        buttonUbahMeja.setDisable(false);
        buttonHapusMeja.setDisable(false);
        buttonTambahMeja.setText("Tambah");
        buttonTambahMeja.setDisable(false);
        
        textNoMeja.setText(String.valueOf(meja.getNoMeja()));
        // setcombobox kategori
        textLantai.setText(String.valueOf(meja.getLantai()));
        textKapasitas.setText(String.valueOf(meja.getKapasitas()));
        // setcombobox status
    }
    
    @FXML
    private void handleNew(){
        if(buttonTambahMeja.getText().equals("Tambah")){
            enable();
            clearMenu();
            buttonHapusMeja.setDisable(true);
            buttonUbahMeja.setDisable(true);
            buttonTambahMeja.setText("Simpan");
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
    
    private void clearMenu(){
        textNoMeja.setText("");
        textKategori.setItems(null);
        textLantai.setText("");
        textKapasitas.setText("");
        textStatus.setItems(null);
    }
    
    
    
    //CRUD
    @FXML
    private void handleSaveMeja(){
        if(textLantai.getText().isEmpty() || textKapasitas.getText().isEmpty() ){
            //Alert
        }else{
            Meja meja = new Meja();
            meja.setKategori("Smoking");
            meja.setLantai(Integer.parseInt(textLantai.getText()));
            meja.setKapasitas(Integer.parseInt(textKapasitas.getText()));
            meja.setStatus(0);

            if(buttonTambahMeja.getText().equals("Simpan")){  
                genericDao.save(meja);
                loadMeja();

            }else if(buttonTambahMeja.getText().equals("Ubah")){
               meja.setNoMeja(Integer.parseInt(textNoMeja.getText()));
                genericDao.update(meja);
                loadMeja();
            }

            disable();
            buttonHapusMeja.setVisible(true);
            buttonUbahMeja.setVisible(true);
            buttonHapusMeja.setDisable(true);
            buttonUbahMeja.setDisable(true);
            buttonTambahMeja.setText("Tambah");
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
        genericDao.delete((Meja) mejaTableView.getSelectionModel().getSelectedItem());   
        loadMeja();  
        disable();
        buttonHapusMeja.setDisable(true);
        buttonUbahMeja.setDisable(true);
        buttonTambahMeja.setDisable(false);
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
            clearMenu();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

  
    public static void main(String[] args) {
        launch(args);
    }
    
}
