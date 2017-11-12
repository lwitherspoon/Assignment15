import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.util.Observable;

/**
 * JavaFX program to enable users to calculate loan payments through a GUI interface
 * Assignment 15.5, CSC201
 * @author Laura Witherspoon
 */

public class LoanCalculator extends Application {
    private TextField annualInterestRateTf = new TextField();
    private TextField numberOfYearsTf = new TextField();
    private TextField loanAmountTf = new TextField();
    private TextField monthlyPaymentTf = new TextField();
    private TextField totalPaymentTf = new TextField();
    private Button calculateBtn = new Button("Calculate");
    private Button clearBtn = new Button("Clear");
    private HBox buttons = new HBox(2);
    BorderPane pane = new BorderPane();

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Add calculator labels and text fields to grid
        grid.add(new Label("Annual Interest Rate:"), 0, 0);
        grid.add(annualInterestRateTf, 1, 0);
        grid.add(new Label("Number of Years:"), 0, 1);
        grid.add(numberOfYearsTf, 1, 1);
        grid.add(new Label("Loan Amount"), 0, 2);
        grid.add(loanAmountTf, 1, 2);
        grid.add(new Label("Monthly Payment:"), 0, 3);
        grid.add(monthlyPaymentTf, 1, 3);
        grid.add(new Label("Total Payment:"), 0, 4);
        grid.add(totalPaymentTf, 1, 4);

        // Put buttons in HBox pane, then add to last grid cell

        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.getChildren().addAll(calculateBtn, clearBtn);
        grid.add(buttons, 1, 5);

        // Set positioning and styling of calculator
        grid.setAlignment(Pos.CENTER);
        annualInterestRateTf.setAlignment(Pos.CENTER_RIGHT);
        numberOfYearsTf.setAlignment(Pos.CENTER_RIGHT);
        loanAmountTf.setAlignment(Pos.CENTER_RIGHT);
        monthlyPaymentTf.setAlignment(Pos.CENTER_RIGHT);
        totalPaymentTf.setAlignment(Pos.CENTER_RIGHT);
        grid.setStyle("-fx-background-color: #FFFFFF");

        // Create layout and add instructions title and grid to it
        Label label = new Label("Calculate your monthly and total loan payment using the calculator below:");
        label.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        BorderPane.setMargin(label, new Insets(12,12,12,12));
        pane.setTop(label);
        pane.setCenter(grid);

        // Make monthly and total payment text fields uneditable
        monthlyPaymentTf.setEditable(false);
        totalPaymentTf.setEditable(false);

        // Attach event handlers to buttons
        calculateBtn.setOnAction(e -> calculatePayment());
        grid.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                calculatePayment();
            }
        });
        clearBtn.setOnAction(e -> clear());

        Scene scene = new Scene(pane, 400, 350);
        primaryStage.setTitle("Loan Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Reset focus on grid if key is pressed
        grid.requestFocus();

    }

    /**
     * Calculate loan payments and sets payment values in the uneditable text fields
     */
    private void calculatePayment() {
        ObservableList<TextField> textFields = FXCollections.observableArrayList();
        textFields.addAll(annualInterestRateTf, numberOfYearsTf, loanAmountTf);

        // If any fields are empty show error message and required outline fields in red
        if (annualInterestRateTf.getText().equals("") || numberOfYearsTf.getText().equals("") || loanAmountTf.getText().equals("")) {

            for (TextField field : textFields) {
                field.setStyle(null);
                if (field.getText().isEmpty()) {
                    field.setStyle("-fx-border-width: 1px; -fx-border-color: red;");
                }
            }

            Text errorMessage = new Text("You must fill in all the required fields!");
            errorMessage.setFill(Color.RED);
            errorMessage.setTextAlignment(TextAlignment.CENTER);
            BorderPane.setMargin(errorMessage, new Insets(10, 10, 10, 10));
            pane.setBottom(errorMessage);

        } else {
            pane.setBottom(null);
            for (TextField field : textFields) {
                field.setStyle("-fx-border-width: 0px;");
            }

            double annualInterestRate = Double.parseDouble(annualInterestRateTf.getText());
            int numberOfYears = Integer.parseInt(numberOfYearsTf.getText());
            double loanAmount = Double.parseDouble(loanAmountTf.getText());

            Loan loan = new Loan(annualInterestRate, numberOfYears, loanAmount);

            monthlyPaymentTf.setText(String.format("$%.2f", loan.getMonthlyPayment()));
            totalPaymentTf.setText(String.format("$%.2f", loan.getTotalPayment()));
        }

    }

    /**
     * Resets loan calculator, clears text fields, and un-sets any styles
     */
    private void clear() {
        annualInterestRateTf.setText("");
        numberOfYearsTf.setText("");
        loanAmountTf.setText("");
        monthlyPaymentTf.setText("");
        totalPaymentTf.setText("");

        pane.setBottom(null);

        annualInterestRateTf.setStyle(null);
        numberOfYearsTf.setStyle(null);
        loanAmountTf.setStyle(null);
    }
}
