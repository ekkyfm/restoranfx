
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Menu;
import model.Pesanan;

/**
 *
 * @author VIKI
 */
public class MenuMain extends Application {
    
    private Stage primaryStage;
    private BorderPane rootLayout;
    private GenericDao genericDao = new GenericDao();
    
    @FXML
    private TableView<Menu> menuTableView;
    
    
    @FXML
    private TableColumn<Menu, Integer> idMenuColumn;
    
    @FXML
    private TableColumn<Menu, String> namaMenuColumn;
    
    @FXML
    private TableColumn<Menu, String> jenisColumn;
    
    @FXML
    private TableColumn<Menu, Integer> stokColumn;
    
    @FXML
    private TableColumn<Menu, Double> hargaColumn;
    
    
    @FXML
    private TextField textIdMenu;
    
    @FXML
    private TextField textNamaMenu;
    
    @FXML
    private ComboBox comboJenis;
    
    @FXML
    private TextField textStok;
    
    @FXML
    private TextField textHarga;
    
    @FXML
    private Button buttonUbah;
    
    @FXML
    private Button buttonHapus;
    
    @FXML
    private Button buttonTambah;
    
    @FXML
    private Button buttonCancel;
    
    
    ObservableList<Menu> dataMenu = FXCollections.observableArrayList();
    
    
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage=primaryStage;
        this.primaryStage.setTitle("Menu");
      
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          public void handle(WindowEvent we) {
             System.exit(0);
          }
        });
                    
        initRootLayout();
        
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }   
    
    @FXML
    private void initialize(){
        comboJenis.getItems().addAll("Makanan","Minuman");
        loadMenu();
    }
    
    public void menuDetail(Menu menu){
        if(menu != null){
            buttonHapus.setVisible(true);
            buttonHapus.setDisable(false);

            buttonUbah.setVisible(true);
            buttonUbah.setDisable(false);

            buttonTambah.setText("Tambah");

            disable();

            textIdMenu.setText(String.valueOf(menu.getIdMenu()));
            textNamaMenu.setText(menu.getNamaMenu());
            comboJenis.setValue(menu.getJenis());
            textStok.setText(String.valueOf(menu.getStok()));
            textHarga.setText(String.valueOf(menu.getHarga()));
        }
        
    }
    
    @FXML
    private void handleNew(){
        if(buttonTambah.getText().equals("Tambah")){
            enable();
            clearMenu();
            buttonCancel.setVisible(true);
            buttonHapus.setDisable(true);
            buttonUbah.setDisable(true);
            buttonTambah.setText("Simpan");
        }else{
            handleSave();
        }
       
    }
    
    public ObservableList getAllMenu(){
        GenericDao genericDao = new GenericDao();
        dataMenu = FXCollections.observableList((List<Menu>) genericDao.getAllData("from Menu"));
        return dataMenu;        
    }
    
    @FXML
    private void handleSave(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(this.getPrimaryStage());
        String regex = "[0-9]*";
        
        if(textNamaMenu.getText().isEmpty() || textStok.getText().isEmpty() || textHarga.getText().isEmpty() || comboJenis.getValue() == null){            
            alert.setHeaderText("Form tidak boleh ada yang kosong");
            alert.showAndWait();
        }else{        
            try{
                double harga = (double) Double.parseDouble(textHarga.getText());
                System.out.println(harga);
                if(!textStok.getText().matches(regex)){
                    alert.setHeaderText("Inputan stok harus angka");
                    alert.showAndWait();
                }else{
                    Menu menu = new Menu();
                    menu.setNamaMenu(textNamaMenu.getText());
                    menu.setJenis(String.valueOf(comboJenis.getValue()));
                    menu.setStok(Integer.parseInt(textStok.getText()));
                    menu.setHarga((double) harga);

                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    if(buttonTambah.getText().equals("Simpan")){  
                        genericDao.save(menu);
                        alert2.setHeaderText("Data berhasil disimpan");
                        alert2.showAndWait();
                        loadMenu();

                    }else if(buttonTambah.getText().equals("Ubah")){
                        menu.setIdMenu(Integer.parseInt(textIdMenu.getText()));
                        genericDao.update(menu);
                        alert2.setHeaderText("Data berhasil diubah");
                        alert2.showAndWait();
                        loadMenu();
                    }

                    buttonDefault();
                    disable();
                }

            }catch(Exception e){
                alert.setHeaderText("Inputan harga harus angka");
                alert.showAndWait();
            }
        }
    }
    
    @FXML
    private void handleDeleteMenu() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Apakah anda yakin akan menghapus data ini");
        alert.showAndWait();
        
        
        if(alert.getResult().getText().equals("OK")){
            genericDao.delete((Menu) menuTableView.getSelectionModel().getSelectedItem());
            
            alert.setHeaderText("Data berhasil dihapus");
            alert.showAndWait();
            
            loadMenu();
            clearMenu();
            buttonDefault();
        }
    }
    
    @FXML
    private void handleUpdateMenu(){
        enable();
        buttonHapus.setVisible(false);
        buttonUbah.setVisible(false);
        buttonTambah.setText("Ubah");
        buttonCancel.setVisible(true);
        
        
    }
    
    private void enable(){
        textIdMenu.setDisable(false);
        textNamaMenu.setDisable(false);
        comboJenis.setDisable(false);
        textStok.setDisable(false);
        textHarga.setDisable(false);
    }
    
    private void disable(){
        textIdMenu.setDisable(true);
        textNamaMenu.setDisable(true);
        comboJenis.setDisable(true);
        textStok.setDisable(true);
        textHarga.setDisable(true);
    }
    
    private void clearMenu(){
        textIdMenu.setText("");
        textNamaMenu.setText("");
        comboJenis.setValue("-- Pilih Jenis Makanan --");
        textStok.setText("");
        textHarga.setText("");
    }
    
    @FXML
    private void handleCancelMenu(){
        clearMenu();
        disable();
        buttonDefault();
    }
    
    public void buttonDefault(){
        buttonTambah.setText("Tambah");
        buttonTambah.setVisible(true);
        buttonTambah.setDisable(false);
        
        buttonCancel.setVisible(false);
        
        buttonHapus.setVisible(true);
        buttonHapus.setDisable(true);
        
        buttonUbah.setVisible(true);
        buttonUbah.setDisable(true);
    }
    
    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PesananController.class.getResource("../view/admin/Menu.fxml"));
            rootLayout=  loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadMenu(){
        try{
            menuTableView.getItems().clear();        
            idMenuColumn.setCellValueFactory(new PropertyValueFactory<Menu, Integer>("idMenu"));
            namaMenuColumn.setCellValueFactory(new PropertyValueFactory<Menu, String>("namaMenu"));
            stokColumn.setCellValueFactory(new PropertyValueFactory<Menu, Integer>("stok"));
            hargaColumn.setCellValueFactory(new PropertyValueFactory<Menu, Double>("harga"));
            jenisColumn.setCellValueFactory(new PropertyValueFactory<Menu, String>("jenis"));
            menuTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> menuDetail(newValue)  );

            textIdMenu.setVisible(false);
            handleCancelMenu();

            menuTableView.setItems(getAllMenu());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
        
    }
    
}