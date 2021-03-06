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
import java.util.*;

import static javafx.application.Application.launch;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.cell.TextFieldTableCell;

import static javafx.scene.input.KeyCode.T;

import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import model.Meja;
import model.Menu;
import org.hibernate.Hibernate;

public class PesananController extends Application {

    private ObservableList<Menu> menuData = FXCollections.observableArrayList();
    private ObservableList<DetailPesanan> selectedMenu = FXCollections.observableArrayList();
    private ObservableList<Pesanan> pesananData = FXCollections.observableArrayList();
    private List<Meja> mejaData;
    private Double harga = 0.0;

    private Stage primaryStage;
    private BorderPane rootLayout;

    public Pesanan draf;


    private boolean searchClicked = false;

    @FXML
    private ListView<Menu> listMenu;
    @FXML
    private TableView<Menu> menuTableView;
    @FXML
    private TableColumn<Menu, String> idMenuColumn;
    @FXML
    private TableColumn<Menu, String> namaColumn;
    @FXML
    private TableColumn<Menu, String> stokColumn;
    @FXML
    private TableColumn<Menu, String> hargaColumn;
    @FXML
    private TableColumn<Menu, String> jenisColumn;

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
    private ComboBox noMejaCombo;

    @FXML
    private TableView<DetailPesanan> detailMenuTableView;
    @FXML
    private TableColumn<DetailPesanan, String> namaDetailMenuColumn;
    @FXML
    private TableColumn<DetailPesanan, Integer> jumlahDetailMenuColumn;
    @FXML
    private TableColumn<DetailPesanan, Boolean> aksiDetailMenuColumn;

    @FXML
    private Label labelHarga;
    @FXML
    private Label labelMeja;

    @FXML
    private Button btnNew;
    @FXML
    private Button btnSimpan;
    @FXML
    private Button btnHapus;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnCancel;
    @FXML
    private Button reload;
    @FXML
    private TextField textCari;


    //launch this app
    public static void main(String[] args) {
        launch(args);
    }

    //constructor
    public PesananController() {

    }

    //get list menu
    public ObservableList getAllMenuData() {
        GenericDao genericDao = new GenericDao();
        menuData = FXCollections.observableList((List<Menu>) genericDao.getAllData("from Menu where stok >0"));
        return menuData;
    }

    public ObservableList getAllMenuData(String like) {
        GenericDao genericDao = new GenericDao();
        menuData = FXCollections.observableList((List<Menu>) genericDao.getAllData("from Menu where stok >0 and nama_menu like '%" + like + "%' or jenis like '%" + like + "%' or harga like '%" + like + "%'"));
        return menuData;
    }

    public ObservableList getAllPesananData() {
        GenericDao genericDao = new GenericDao();
        menuData = FXCollections.observableList((List<Menu>) genericDao.getAllData("from Pesanan order by id_pesanan desc"));
        return menuData;
    }

    public List getAllMejaData() {
        GenericDao genericDao = new GenericDao();
        mejaData = genericDao.getAllData("from Meja");
        return mejaData;
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
            loader.setLocation(PesananController.class.getResource("../view/MainPesanan.fxml"));
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

        ArrayList<Button> btnDisable = new ArrayList<>(Arrays.asList(btnCancel, btnEdit, btnHapus, btnSimpan));
        buttonService(btnDisable, true);
        ArrayList<TableView> tableDisable = new ArrayList<>(Arrays.asList(detailMenuTableView, menuTableView));
        tableService(tableDisable, true);
        noMejaCombo.setDisable(true);
        labelMeja.setVisible(false);
        textCari.setDisable(true);
        setTableViewMenu();
        setTableViewPesanan();
        setTableDetailPesanan();
        if (selectedMenu.size() >= 0) {
            setLabelHarga();
        }
        ObservableList<String> options = null;
        setComboMeja();
    }

    public void buttonService(List<Button> btn, Boolean trueOrFalse) {
        for (Button button : btn) {
            button.setDisable(trueOrFalse);
        }
    }


    public void tableService(ArrayList<TableView> table, Boolean trueOrFalse) {
        for (TableView tableView : table) {
            tableView.setDisable(trueOrFalse);
        }
    }


    class ButtonCell extends TableCell<DetailPesanan, Boolean> {

        final Button cellButton = new Button("Hapus");

        ButtonCell(final TableView tblView) {

            cellButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    DetailPesanan curDetailPesanan = (DetailPesanan) ButtonCell.this.getTableView().getItems().get(getTableRow().getIndex());
                    selectedMenu.remove(curDetailPesanan);
                    setLabelHarga();
                }
            });
        }

        //Display button if the row is not empty
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


    public void setComboMeja() {
        noMejaCombo.getItems().clear();
        ArrayList<Meja> dataMeja = getMejaData();
        for (Meja meja : dataMeja) {
            noMejaCombo.getItems().add(meja.getNoMeja());
        }

        if (dataMeja.size() == 0) {
            labelMeja.setVisible(true);
            btnNew.setDisable(true);
            noMejaCombo.setDisable(true);
            reload.setVisible(true);
        } else {
            labelMeja.setVisible(false);
            reload.setVisible(false);
        }
    }

    public void setTableViewMenu() {
        menuTableView.getItems().clear();
        idMenuColumn.setCellValueFactory(new PropertyValueFactory<Menu, String>("IdMenu"));
        namaColumn.setCellValueFactory(new PropertyValueFactory<Menu, String>("NamaMenu"));
        stokColumn.setCellValueFactory(new PropertyValueFactory<Menu, String>("Stok"));
        hargaColumn.setCellValueFactory(new PropertyValueFactory<Menu, String>("harga"));
        jenisColumn.setCellValueFactory(new PropertyValueFactory<Menu, String>("jenis"));
        menuTableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Menu> observable, Menu oldValue, Menu newValue) -> {
            if (newValue != null) {
                Double hargaFromTable = Double.parseDouble(newValue.getHarga().toString());
                harga = harga + hargaFromTable;
                labelHarga.setText(harga.toString());
                setSelectedMenu(newValue);
                System.out.println(selectedMenu.size());
            }

        });

        getAllMejaData();
        menuTableView.setItems(getAllMenuData());
    }

    public void setTableViewMenu(ObservableList data) {
        menuTableView.getItems().clear();
        idMenuColumn.setCellValueFactory(new PropertyValueFactory<Menu, String>("IdMenu"));
        namaColumn.setCellValueFactory(new PropertyValueFactory<Menu, String>("NamaMenu"));
        stokColumn.setCellValueFactory(new PropertyValueFactory<Menu, String>("Stok"));
        hargaColumn.setCellValueFactory(new PropertyValueFactory<Menu, String>("harga"));
        jenisColumn.setCellValueFactory(new PropertyValueFactory<Menu, String>("jenis"));
        menuTableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Menu> observable, Menu oldValue, Menu newValue) -> {
            if (newValue != null) {
                labelHarga.setText(harga.toString());
                setSelectedMenu(newValue);
                System.out.println(selectedMenu.size());
                setLabelHarga();
            }
        });

        menuTableView.setItems(data);
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

            setLabelHarga();

            draf = newValue;
            System.out.println(newValue.getIdPesanan());
            btnCancel.setDisable(false);
            if (newValue.getStatus().equals("1")) {
                btnEdit.setDisable(true);
                btnHapus.setDisable(true);
            } else {
                btnEdit.setDisable(false);
                btnHapus.setDisable(false);
            }
        });
        pesananTableView.setItems(getAllPesananData());
    }

    public void setTableDetailPesanan() {
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

        aksiDetailMenuColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<DetailPesanan, Boolean>, ObservableValue<Boolean>>() {

                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<DetailPesanan, Boolean> param) {
                        return new SimpleBooleanProperty(param.getValue() != null);
                    }
                });

        aksiDetailMenuColumn.setCellFactory(new Callback<TableColumn<DetailPesanan, Boolean>, TableCell<DetailPesanan, Boolean>>() {

            @Override
            public TableCell<DetailPesanan, Boolean> call(TableColumn<DetailPesanan, Boolean> param) {
                return new ButtonCell(detailMenuTableView);

            }
        });


        jumlahDetailMenuColumn.setCellFactory(TextFieldTableCell.<DetailPesanan, Integer>forTableColumn(new IntegerStringConverter()));
        jumlahDetailMenuColumn.setOnEditCommit((TableColumn.CellEditEvent<DetailPesanan, Integer> event) -> {
            ((DetailPesanan) event.getTableView().getItems().get(event.getTablePosition().getRow())).setJumlah((int) event.getNewValue());
            setLabelHarga();
        });

        detailMenuTableView.setItems(selectedMenu);

        detailMenuTableView.setEditable(true);
    }

    public ArrayList getMejaData() {
        GenericDao genericDao = new GenericDao();
        return (ArrayList) genericDao.getAllData("from Meja where status ='1' ");
    }

    @FXML
    public void handleReloadMeja() {
        if (getAllMejaData().size() >= 1) {
            btnNew.setDisable(false);
            labelMeja.setVisible(false);
            setComboMeja();
        }
        ;

    }

    @FXML
    public void handleCancel() {

        btnCancel.setDisable(true);
        btnEdit.setDisable(true);
        btnHapus.setDisable(true);
        btnSimpan.setDisable(true);
        btnNew.setDisable(false);

        pesananTableView.setDisable(false);

        menuTableView.setDisable(true);
        detailMenuTableView.setDisable(true);
        noMejaCombo.setDisable(true);
        btnSimpan.setDisable(true);
        btnHapus.setDisable(true);

        selectedMenu.clear();
        setLabelHarga();
    }

    @FXML
    public void handleCari() {
        setTableViewMenu(getAllMenuData(textCari.getText()));
    }

    @FXML
    public void handleEdit() {
        detailMenuTableView.setDisable(false);
        detailMenuTableView.setEditable(true);
        btnCancel.setDisable(false);
        btnSimpan.setDisable(false);
        menuTableView.setDisable(false);
        btnSimpan.setOnAction(event -> handleSimpanEdit());
        noMejaCombo.setDisable(false);

        btnNew.setDisable(true);
        btnHapus.setDisable(true);
    }

    public void handleSimpanEdit() {
        DaoPesanan dao = new DaoPesanan();
        if (selectedMenu.size() <= 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(this.getPrimaryStage());
            alert.setTitle("Lengkapi data");
            alert.setHeaderText("Silahkan pilih minimal 1 menu!");
            alert.showAndWait();
        } else {
            dao.update(draf, selectedMenu);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(this.getPrimaryStage());
            alert.setTitle("Gagal");
            alert.setHeaderText("Data berhasil disimpan");
            alert.showAndWait();
            detailMenuTableView.setEditable(false);
            detailMenuTableView.setDisable(true);
            menuTableView.setDisable(true);
            btnCancel.setDisable(true);
            btnSimpan.setDisable(true);
            btnNew.setDisable(false);
            btnEdit.setDisable(true);

            try {
                setTableViewMenu();
                setTableViewPesanan();
                setTableDetailPesanan();
                setComboMeja();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleHapus() {
        GenericDao dao = new GenericDao();
        btnCancel.setDisable(true);
        btnEdit.setDisable(true);
        btnHapus.setDisable(true);
        btnSimpan.setDisable(true);
        btnNew.setDisable(false);

        dao.delete(draf);
        setTableViewPesanan();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(this.getPrimaryStage());
        alert.setTitle("Berhasil");
        alert.setHeaderText("Pesanan meja " + draf.getNoMeja() + " dibatalkan!");
        alert.showAndWait();

    }

    @FXML
    public void handleNew() {
        selectedMenu.clear();
        btnSimpan.setOnAction(event -> handleSimpan());
        ArrayList<Button> btnDisable = new ArrayList<>(Arrays.asList(btnEdit, btnHapus));
        buttonService(btnDisable, true);
        ArrayList<Button> btnEnable = new ArrayList<>(Arrays.asList(btnCancel, btnSimpan));
        buttonService(btnEnable, false);
        labelMeja.setVisible(false);
        noMejaCombo.setDisable(false);
        textCari.setDisable(false);
        ArrayList<TableView> tableDisable = new ArrayList<>(Arrays.asList(pesananTableView, detailMenuTableView, menuTableView));
        tableService(tableDisable, false);

        detailMenuTableView.setItems(selectedMenu);
        detailMenuTableView.setDisable(false);
        detailMenuTableView.setEditable(true);
        menuTableView.setDisable(false);
    }


    public void handleSimpan() {
        DaoPesanan dao = new DaoPesanan();
        if (noMejaCombo.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(this.getPrimaryStage());
            alert.setTitle("Lengkapi data");
            alert.setHeaderText("Silahkan lengkapi data no meja!");
            alert.showAndWait();
        } else if (selectedMenu.size() <= 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(this.getPrimaryStage());
            alert.setTitle("Lengkapi data");
            alert.setHeaderText("Silahkan pilih minimal 1 menu!");
            alert.showAndWait();
        } else {
            dao.save(new Pesanan(Date.from(Instant.now()), Integer.parseInt(noMejaCombo.getSelectionModel().getSelectedItem().toString()), "0"), selectedMenu);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(this.getPrimaryStage());
            alert.setTitle("Gagal");
            alert.setHeaderText("Data berhasil disimpan");
            alert.showAndWait();
            detailMenuTableView.setEditable(false);
            detailMenuTableView.setDisable(true);
            menuTableView.setDisable(true);
            btnCancel.setDisable(true);
            btnSimpan.setDisable(true);
            btnNew.setDisable(false);

            try {
                setTableViewMenu();
                setTableViewPesanan();
                setTableDetailPesanan();
                setComboMeja();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void setLabelHarga() {
        Double harga = 0.0;
        for (DetailPesanan detailPesanan : selectedMenu) {
            harga = harga + (detailPesanan.getMenu().getHarga() * detailPesanan.getJumlah());
        }
        labelHarga.setText(harga.toString());
    }
}
