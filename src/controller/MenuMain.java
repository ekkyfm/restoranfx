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
import javafx.stage.Stage;
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
    
    
    ObservableList<Menu> dataMenu = FXCollections.observableArrayList();
    
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage=primaryStage;
        this.primaryStage.setTitle("Menu");
      
        initRootLayout();
        
    }
    
    @FXML
    private void initialize(){
        
        menuTableView.getItems().clear();
        idMenuColumn.setCellValueFactory(new PropertyValueFactory<Menu, Integer>("idMenu"));
        namaMenuColumn.setCellValueFactory(new PropertyValueFactory<Menu, String>("namaMenu"));
        stokColumn.setCellValueFactory(new PropertyValueFactory<Menu, Integer>("stok"));
        hargaColumn.setCellValueFactory(new PropertyValueFactory<Menu, Double>("harga"));
        jenisColumn.setCellValueFactory(new PropertyValueFactory<Menu, String>("jenis"));
        menuTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> menuDetail(newValue)  );
    
        disable();
        buttonUbah.setDisable(true);
        buttonHapus.setDisable(true);
        textIdMenu.setVisible(false);
        menuTableView.setItems(getAllMenu());
        
    }
    
    public void menuDetail(Menu menu){
        buttonHapus.setVisible(true);
        buttonUbah.setVisible(true);
        buttonHapus.setDisable(false);
        buttonUbah.setDisable(false);
        disable();
        textIdMenu.setText(String.valueOf(menu.getIdMenu()));
        textNamaMenu.setText(menu.getNamaMenu());
        // setcombobox
        textStok.setText(String.valueOf(menu.getStok()));
        textHarga.setText(String.valueOf(menu.getHarga()));
    }
    
    @FXML
    private void handleNew(){
        if(buttonTambah.getText().equals("Tambah")){
            enable();
            clearMenu();
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
        if(textNamaMenu.getText().isEmpty() || textStok.getText().isEmpty() || textHarga.getText().isEmpty()){
            //Alert
        }else{
            Menu menu = new Menu();
            menu.setNamaMenu(textNamaMenu.getText());
            menu.setJenis("makanan");
            menu.setStok(Integer.parseInt(textStok.getText()));
            menu.setHarga(Double.parseDouble(textHarga.getText()));

            if(buttonTambah.getText().equals("Simpan")){  
                genericDao.save(menu);
                loadMenu();
            }else if(buttonTambah.getText().equals("Ubah")){
                menu.setIdMenu(Integer.parseInt(textIdMenu.getText()));
                genericDao.update(menu);
                loadMenu();
            }
            
            buttonTambah.setText("Tambah");
            disable();
        }
        
        
        
        
        
    }
    
    @FXML
    private void handleDeleteMenu() {    
        genericDao.delete((Menu) menuTableView.getSelectionModel().getSelectedItem());
        loadMenu();
        clearMenu();
        buttonHapus.setDisable(true);
        buttonUbah.setDisable(true);
    }
    
    @FXML
    private void handleUpdateMenu(){
        enable();
        buttonHapus.setVisible(false);
        buttonUbah.setVisible(false);
        buttonTambah.setText("Ubah");
        
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
        comboJenis.setItems(null);
        textStok.setText("");
        textHarga.setText("");
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
            menuTableView.setItems(getAllMenu());
            clearMenu();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    

    public static void main(String[] args) {
        launch(args);
    }
    
}
