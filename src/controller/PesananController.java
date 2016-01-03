package controller;/**
 * Created by ekky on 1/1/16.
 */

import dao.DaoPesanan;
import dao.GenericDao;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Pesanan;
import model.DetailPesanan;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Observable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import static javafx.scene.input.KeyCode.T;
import model.Menu;

public class PesananController extends Application {

    private ObservableList<Menu> menuData = FXCollections.observableArrayList();
    private ObservableList<DetailPesanan> selectedMenu= FXCollections.observableArrayList();
    private ObservableList<Pesanan> pesananData= FXCollections.observableArrayList();
    
    private Stage primaryStage;
    private BorderPane rootLayout;
    
    
    private boolean searchClicked = false;

    @FXML
    private ListView<Menu> listMenu;
    @FXML
    private TableView<Menu> menuTableView;
    @FXML
    private TableColumn<Menu,String> idMenuColumn;
    @FXML
    private TableColumn<Menu,String> namaColumn;
    @FXML
    private TableColumn<Menu,String> stokColumn;
    @FXML
    private TableColumn<Menu,String> hargaColumn;
    @FXML
    private TableColumn<Menu,String> jenisColumn;
    
    @FXML
    private TableView<Pesanan> pesananTableView;
    @FXML
    private TableColumn<Pesanan,String> idPesananColumn;
    @FXML
    private TableColumn<Pesanan,String> noMejaColumn;
    @FXML
    private TableColumn<Pesanan,String> waktuColumn;
    @FXML
    private TableColumn<Pesanan,String> statusPesananColumn;
    
    @FXML
    private ComboBox noMejaCombo;

    @FXML
    private TableView<DetailPesanan> detailMenuTableView;
    @FXML
    private TableColumn<DetailPesanan,Menu> namaDetailMenuColumn;
    @FXML
    private TableColumn<DetailPesanan,String> jumlahDetailMenuColumn;
    @FXML
    private TableColumn<DetailPesanan,String> aksiDetailMenuColumn;
    
    
    //launch this app
    public static void main(String[] args) {
        launch(args);
    }

    //constructor
    public PesananController() {

    }

    //get list menu
    public ObservableList getAllMenuData(){
        GenericDao genericDao = new GenericDao();
        menuData = FXCollections.observableList((List<Menu>) genericDao.getAllData("from Menu"));
        return menuData; 
    }
    
    public ObservableList getAllPesananData(){
        GenericDao genericDao = new GenericDao();
        menuData = FXCollections.observableList((List<Menu>) genericDao.getAllData("from Pesanan"));
        return menuData; 
    }
    
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage=primaryStage;
        this.primaryStage.setTitle("POS Restoran");
        initRootLayout();
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PesananController.class.getResource("../view/MainPesanan.fxml"));
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
        
        menuTableView.getItems().clear();
        idMenuColumn.setCellValueFactory(new PropertyValueFactory<Menu, String>("IdMenu"));
        namaColumn.setCellValueFactory(new PropertyValueFactory<Menu, String>("NamaMenu"));
        stokColumn.setCellValueFactory(new PropertyValueFactory<Menu, String>("Stok"));
        hargaColumn.setCellValueFactory(new PropertyValueFactory<Menu, String>("harga"));
        jenisColumn.setCellValueFactory(new PropertyValueFactory<Menu, String>("jenis"));
        menuTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setSelectedMenu(newValue));
        
        
        menuTableView.setItems(getAllMenuData());
        
        pesananTableView.getItems().clear();
        idPesananColumn.setCellValueFactory(new PropertyValueFactory<Pesanan,String>("idPesanan"));
        noMejaColumn.setCellValueFactory(new PropertyValueFactory<Pesanan,String>("noMeja"));
        waktuColumn.setCellValueFactory(new PropertyValueFactory<Pesanan,String>("waktu"));
        statusPesananColumn.setCellValueFactory(new PropertyValueFactory<Pesanan,String>("status"));
        pesananTableView.setItems(getAllPesananData());
        
        detailMenuTableView.getItems().clear();
        namaDetailMenuColumn.setCellValueFactory(new PropertyValueFactory<DetailPesanan, Menu>("menu"));
        jumlahDetailMenuColumn.setCellValueFactory(new PropertyValueFactory<DetailPesanan, String>("jumlah"));
        aksiDetailMenuColumn.setCellValueFactory(new PropertyValueFactory<DetailPesanan, String>("aksiDetailPesanan"));
        jumlahDetailMenuColumn.editableProperty().set(true);
        detailMenuTableView.setItems(selectedMenu);
        
        noMejaCombo.getItems().addAll(1,2,3);
        
    }
    

    public Stage getPrimaryStage(){
        return primaryStage;
    }

    public void setSelectedMenu(Menu m){
        selectedMenu.add(new DetailPesanan(m, 1));
    }
    
    @FXML
    public void handleSimpan(){
        DaoPesanan dao = new DaoPesanan();
        try {
            dao.save(new Pesanan(Date.from(Instant.now()) , Integer.parseInt(noMejaCombo.getSelectionModel().getSelectedItem().toString()), "0"), selectedMenu);
        } catch (Exception e) {
            System.out.println("Data gagal disimpan");
            System.out.println(e);
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(this.getPrimaryStage());
            alert.setTitle("Gagal");
            alert.setHeaderText("Data gagal disimpan");
            alert.setContentText("Ulangi kembali insert data atau hubungi administrator!");

            alert.showAndWait();
        }
        
    }

}
