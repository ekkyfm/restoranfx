package controller;/**
 * Created by ekky on 1/1/16.
 */

import dao.DaoPesanan;
import dao.GenericDao;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import model.Pesanan;
import model.DetailPesanan;

import java.io.IOException;
import java.util.*;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import model.Meja;
import model.Menu;
import org.hibernate.Hibernate;

public class DapurController extends Application {

    private ObservableList<Menu> menuData = FXCollections.observableArrayList();
    private ObservableList<DetailPesanan> selectedMenu = FXCollections.observableArrayList();
    private ObservableList<Pesanan> pesananData = FXCollections.observableArrayList();
    private Double harga = 0.0;

    private Stage primaryStage;
    private BorderPane rootLayout;

    public Pesanan draf;

    @FXML
    private TableView<Pesanan> pesananTableView;
    @FXML
    private TableColumn<Pesanan, String> idPesananColumn;
    @FXML
    private TableColumn<Pesanan, String> noMejaColumn;
    @FXML
    private TableColumn<Pesanan, String> waktuColumn;
    @FXML
    private TableColumn<Pesanan, String> statusPesananColumn;

    @FXML
    private TableView<DetailPesanan> detailMenuTableView;
    @FXML
    private TableColumn<DetailPesanan, String> namaDetailMenuColumn;
    @FXML
    private TableColumn<DetailPesanan, Integer> jumlahDetailMenuColumn;

    @FXML
    private Label labelHarga;

    @FXML
    private Button btnApprove;

    public static void main(String[] args) {
        launch(args);
    }

    public DapurController() {

    }


    public ObservableList getAllPesananData() {
        GenericDao genericDao = new GenericDao();
        menuData = FXCollections.observableList((List<Menu>) genericDao.getAllData("from Pesanan order by id_pesanan desc"));
        return menuData;
    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("POS Restoran");
        initRootLayout();
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PesananController.class.getResource("../view/Dapur.fxml"));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        setTableViewPesanan();
        setTableDetailPesanan();
    }

    class ButtonCell extends TableCell<DetailPesanan, Boolean> {

        final Button cellButton = new Button("Hapus");

        ButtonCell(final TableView tblView) {

            cellButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    DetailPesanan curDetailPesanan = (DetailPesanan) ButtonCell.this.getTableView().getItems().get(getTableRow().getIndex());
                    selectedMenu.remove(curDetailPesanan);
                }
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            } else {
                setGraphic(null);
            }
        }

    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setSelectedMenu(Menu m) {
        selectedMenu.add(new DetailPesanan(m, 1));
    }


    public void setTableViewPesanan() {
        pesananTableView.getItems().clear();
        idPesananColumn.setCellValueFactory(new PropertyValueFactory<Pesanan, String>("idPesanan"));
        noMejaColumn.setCellValueFactory(new PropertyValueFactory<Pesanan, String>("noMeja"));
        waktuColumn.setCellValueFactory(new PropertyValueFactory<Pesanan, String>("waktu"));
        statusPesananColumn.setCellValueFactory(new PropertyValueFactory<Pesanan, String>("status"));
        pesananTableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Pesanan> observable, Pesanan oldValue, Pesanan newValue) -> {
            selectedMenu.clear();
            Set<DetailPesanan> dp = new HashSet<DetailPesanan>();
            dp = newValue.getDetailPesanans();
            Hibernate.initialize(newValue.getDetailPesanans());
            for (DetailPesanan d : dp) {
                Hibernate.initialize(d.getMenu());
                selectedMenu.add(d);
            }
            draf = newValue;
            System.out.println(newValue.getIdPesanan());
        });
        pesananTableView.setItems(getAllPesananData());
    }

    public void setTableDetailPesanan() {
        labelHarga.setText("0");
        detailMenuTableView.getItems().clear();

        namaDetailMenuColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DetailPesanan, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DetailPesanan, String> param) {
                SimpleStringProperty namaProperty = new SimpleStringProperty();

                namaProperty.setValue(param.getValue().getMenu().getNamaMenu());
                return namaProperty;
            }

        });

        jumlahDetailMenuColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DetailPesanan, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<DetailPesanan, Integer> param) {
                return new SimpleIntegerProperty(param.getValue().getJumlah()).asObject();
            }
        });

        jumlahDetailMenuColumn.editableProperty().set(true);


        jumlahDetailMenuColumn.setCellFactory(TextFieldTableCell.<DetailPesanan, Integer>forTableColumn(new IntegerStringConverter()));
        jumlahDetailMenuColumn.setOnEditCommit((TableColumn.CellEditEvent<DetailPesanan, Integer> event) -> {
            ((DetailPesanan) event.getTableView().getItems().get(event.getTablePosition().getRow())).setJumlah((int) event.getNewValue());
        });

        detailMenuTableView.setItems(selectedMenu);

        detailMenuTableView.setEditable(true);
    }


    @FXML
    public void handleApprove() {
        GenericDao dao = new GenericDao();
        if(draf.getStatus().equals("1")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(this.getPrimaryStage());
            alert.setTitle("Berhasil");
            alert.setHeaderText("Pesanan meja " + draf.getNoMeja() + " sudah dikomfirmasi!");
            alert.showAndWait();
        }else{
            draf.setStatus("1");
            dao.update(draf);
            setTableViewPesanan();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(this.getPrimaryStage());
            alert.setTitle("Berhasil");
            alert.setHeaderText("Pesanan meja " + draf.getNoMeja() + " dikomfirmasi!");
            alert.showAndWait();
        }


    }
}
