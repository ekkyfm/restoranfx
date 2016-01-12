/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author VIKI
 */
public class LaporanController extends Application {
    
    private Stage primaryStage;
    private Parent rootLayout;
    private BorderPane formMejaOrMenu;
    
//    Button Menu utama admin  
    @FXML
    private Button buttonMenu;    
    @FXML
    private Button buttonMeja;
       
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Admin");
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
            public void handle(WindowEvent e){
                System.exit(0);
            }
        });
    }
    
    @FXML
    public void handleButtonMenu(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        Scene scene;
        loader.setLocation(LaporanController.class.getResource("../view/admin/Menu.fxml"));
        rootLayout = loader.load();
        scene = new Scene(rootLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void handleButtonMeja(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        Scene scene;
        loader.setLocation(LaporanController.class.getResource("../view/admin/Meja.fxml"));
        rootLayout = loader.load();
        scene = new Scene(rootLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
     
    public static void main(String[] args) {
        launch(args);
    }
    
    
//    Data Menu
    
}
