/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.GenericDao;
import java.io.IOException;
import java.util.Date;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.DetailPesanan;
import model.Meja;
import model.Menu;
import model.Pesanan;

/**
 *
 * @author VIKI
 */
public class DapurController extends Application {
    
    private Stage primaryStage;
    private BorderPane rootLayout;
    private GenericDao genericDao = new GenericDao();
    private PesananController pc = new PesananController();
    
    private ObservableList<Pesanan> dataPesanan = FXCollections.observableArrayList();
    private ObservableList<Menu> dataMenu = FXCollections.observableArrayList();
    
    @FXML
    private TableView<Pesanan> pesananTable; 
    
    @FXML
    private TableView<Menu> menuTable;
    
    @FXML
    private TableColumn<Pesanan, Integer> idPesananColumn;
    
    @FXML
    private TableColumn<Pesanan, String> noMejaColumn;
    
    @FXML
    private TableColumn<Pesanan, Date> waktuColumn;
    
    @FXML
    private TableColumn<Pesanan, Integer> statusColumn;
    
    
    @FXML
    private TableColumn<Menu, String> namaMenuColumn;
    
    @FXML
    private TableColumn<Menu, Integer> jumlahColumn;
    
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage=primaryStage;
        this.primaryStage.setTitle("Dapur");
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          public void handle(WindowEvent we) {
             System.exit(0);
          }
        });
        
        initRootLayout();
    }
    
    @FXML
    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PesananController.class.getResource("../view/dapur/Dapur.fxml"));
            rootLayout =  loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void initialize(){
        pesananTable.getItems().clear();
        idPesananColumn.setCellValueFactory(new PropertyValueFactory<Pesanan, Integer>("idPesanan"));
        noMejaColumn.setCellValueFactory(new PropertyValueFactory<Pesanan, String>("noMeja"));
        waktuColumn.setCellValueFactory(new PropertyValueFactory<Pesanan, Date>("waktu"));
        
        pesananTable.setItems(getAllPesananData());
    }
    
    public ObservableList getAllPesananData(){
        GenericDao genericDao = new GenericDao();
        dataPesanan = FXCollections.observableList(genericDao.getAllData("from Pesanan where status = 0"));
        return dataPesanan; 
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
