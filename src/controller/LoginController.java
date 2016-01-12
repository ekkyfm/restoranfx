/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author VIKI
 */
public class LoginController extends Application {
    
    private Stage primaryStage;
    private Parent rootLayout;
    private Pane loginForm;
    
    @FXML
    private TextField textUser;
    
    @FXML
    private PasswordField textPass;
    
    @FXML
    private Button loginButton;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Login");
        initRootLayout();
        
    }
    
    public void initRootLayout(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(LoginController.class.getResource("../view/Login.fxml"));
            loginForm = loader.load();
            Scene scene = new Scene(loginForm);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleButtonLogin(ActionEvent event){
        rootLayout = null;
        
        try{
            FXMLLoader loader = new FXMLLoader();
            Scene scene;
            
            if(textUser.getText().equals("pelayan") && textPass.getText().equals("pelayan")){
                loader.setLocation(LoginController.class.getResource("../view/MainPesanan.fxml"));
                rootLayout = loader.load();
                scene = new Scene(rootLayout);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }else if(textUser.getText().equals("admin") && textPass.getText().equals("admin")){
                loader.setLocation(LoginController.class.getResource("../view/admin/Admin.fxml"));
                rootLayout = loader.load();
                scene = new Scene(rootLayout);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Admin");
                stage.show();
                
                
            }else if(textUser.getText().equals("dapur") && textPass.getText().equals("dapur")){
                loader.setLocation(LoginController.class.getResource("../view/dapur/Dapur.fxml"));
                rootLayout = loader.load();
                scene = new Scene(rootLayout);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Dapur");
                stage.show();
                
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Username dan Password tidak cocok");
                alert.showAndWait();
            }
            
            
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
