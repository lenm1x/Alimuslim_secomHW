package ayaz.com.homeworkalim;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

public class HelloApplication extends Application {
    private static ArrayList<Product> products = new ArrayList<>();
    private static ArrayList<MonthSales> monthSales = new ArrayList<>();
    @Override
    public void start(Stage stage) throws IOException {
        VBox root = new VBox();
        Button button = new Button("Downland the file ");
        root.getChildren().add(button);
        button.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel файлы (*.xls, *.xlsx)", "*.xls", "*.xlsx"));
            File file = fileChooser.showOpenDialog(stage);
            try {
                readExcelFile(file);
                showNewWindow();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Hello World");
        stage.setScene(scene);
        stage.show();

    }
    public static void readExcelFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;

            int id = (int) row.getCell(0).getNumericCellValue();
            String name = row.getCell(1).getStringCellValue();
            double price = row.getCell(2).getNumericCellValue();
            int quantity = (int) row.getCell(3).getNumericCellValue();
            double totalPrice = row.getCell(4).getNumericCellValue();

            LocalDate date = row.getCell(5).getDateCellValue()
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Product product = new Product(id, name, price, quantity, totalPrice, date);
            products.add(product);


            String month = date.format(DateTimeFormatter.ofPattern("MMMM yyyy"));

            boolean found = false;
            for (MonthSales item : monthSales) {
                if (item.getName().equals(month)) {
                    item.addSales(totalPrice);
                    found = true;
                    break;
                }
            }

            if (!found) {
                monthSales.add(new MonthSales(month, totalPrice ));
            }
        }








    }
    public static void showNewWindow() throws IOException {

        Stage stage = new Stage();
        VBox root = new VBox();







        final CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("sell");
        final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);
        lineChart.setTitle("Schedule by sell in 2025 ");
        XYChart.Series series = new XYChart.Series();

        for(MonthSales item : monthSales){
            series.getData().add(new XYChart.Data(item.getName(), item.getSum()));
        }
        lineChart.getData().add(series);
        TableView<Product> tableView = new TableView<>();

        TableColumn<Product, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().id()));
        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().name()));

        TableColumn<Product, Number> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().price()));

        TableColumn<Product, Number> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().quantity()));

        TableColumn<Product, Number> totalCol = new TableColumn<>("Total Price");
        totalCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().totalPrice()));

        TableColumn<Product, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().date().toString()));

        tableView.getColumns().addAll(idCol, nameCol, priceCol, quantityCol, totalCol, dateCol);
        tableView.getItems().addAll(products);



        root.getChildren().addAll(lineChart, tableView);
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Hello World");
        stage.setScene(scene);
        stage.show();



    }


    public static void main (String[]args){
        launch();
    }
}