package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Main extends Application {

	private String titles;
	private File file;
	private AVL<Integer, AVL<String, AVL<Integer, ElectricityRecord>>> years = new AVL<>();
	private Button back = new Button("Go Back");
	private Font btnFont = Font.font("Times New Roman", FontWeight.NORMAL, 12.5);
	int min = 0;
	int max = Integer.MAX_VALUE;

	@Override
	public void start(Stage primaryStage) {
		try {
			// set a title
			primaryStage.setTitle("Electricity in Gaza Strip");

			// set a new icon
			primaryStage.getIcons().add(new Image("icon.jpg"));

			// set the primary scene in the stage
			Scene scene = primary(primaryStage);
			primaryStage.setScene(scene);

			//
			back.setOnAction(e -> {
				primaryStage.setScene(scene);
			});

			// show the stage
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public Scene primary(Stage stage) {
		// create the root pane
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(30));
		root.setStyle("-fx-background-color: white;");

		// create scene
		Scene scene = new Scene(root, 700, 600);
		scene.setFill(Color.WHITE);

		// create imageView to hold the image
		ImageView img = new ImageView(new Image("Gaza.jpg"));
		img.fitHeightProperty().bind(root.heightProperty().divide(3));
		img.fitWidthProperty().bind(root.widthProperty().divide(2));

		// text object holds the text and text2 holds the reference(OCHA)
		String desc = "The ongoing power shortage has severely affected the availability of \r\n"
				+ "essential services, particularly health, water and sanitation services, and undermined Gaza’s fragile \r\n"
				+ "economy, particularly the manufacturing and agriculture sectors.";
		Text text = new Text(desc);
		text.setTextAlignment(TextAlignment.CENTER);
		text.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 15));
		Text text2 = new Text("-OCHA");
		// text2.setFill(Color.RED);
		text2.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 10));

		// create a VBox to hold the imageView with its contents
		VBox box = new VBox(10);
		box.getChildren().addAll(img, text);
		box.setAlignment(Pos.CENTER);

		//
		HBox tbox = new HBox();
		tbox.getChildren().add(text2);
		tbox.setAlignment(Pos.BASELINE_RIGHT);
		tbox.setPadding(new Insets(5, 25, 5, 25));

		//
		VBox bBox = new VBox();
		bBox.getChildren().addAll(box, tbox);

		// add box to the root
		root.setTop(bBox);

		// add the buttons from method loading
		root.setCenter(loading(stage));

		return scene;
	}

	public VBox loading(Stage stage) {
		// create a GridPane to hold the buttons
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setHgap(5);
		pane.setVgap(5);

		// Creating a FileChooser called chooser to select a file:
		FileChooser chooser = new FileChooser();

		// Create a button called select to select a file from the fileChooser and a
		// label called selected
		Button select = new Button("Select a file");
		select.setFont(btnFont);
		Label selected = new Label("No files have been selected");
		selected.setFont(btnFont);
		pane.add(select, 0, 0);
		pane.add(selected, 1, 0);

		// create a button called load to load data
		Button load = new Button("Load data");
		load.setFont(btnFont);
		load.setDisable(true);
		Label loaded = new Label("click the button to load data");
		loaded.setFont(btnFont);
		pane.add(load, 0, 1);
		pane.add(loaded, 1, 1);

		//
		ComboBox<String> cBox = options(stage);

		select.setOnAction(e -> {
			file = chooser.showOpenDialog(stage);
			if (file != null) {
				selected.setText(file.getAbsolutePath());
				load.setDisable(false);
			}
		});

		load.setOnAction(e -> {
			try {
				if (file.getName().endsWith(".csv") || file.getName().endsWith(".txt")) {
					load();
					loaded.setText("Loaded Successfully!");
					loaded.setTextFill(Color.GREEN);
					cBox.setDisable(false);
					// System.out.println(years.search(2023).getData().search("May").getData().search(14));
//					System.out.println("height of years tree: " + years.height());
//					System.out.println("height of months tree: " + months_Height());
//					System.out.println("height of days tree: " + DaysHeight());
//					System.out.println(years.levelOrderTraverse());
					System.out.println(traverseDaysOfMonth(2023, "April"));
				} else {
					loaded.setText("You have Selected a Wrong File!");
					loaded.setTextFill(Color.RED);
				}
			} catch (Exception o) {
				loaded.setText("Error Occured During Loading Process!");
				loaded.setTextFill(Color.RED);
			}
		});

		VBox box = new VBox(30);
		box.getChildren().addAll(pane, cBox);
		box.setPadding(new Insets(40));
		box.setAlignment(Pos.CENTER);
		return box;
	}

	public ComboBox<String> options(Stage stage) {

		// Creating a ComboBox object to select an option
		String[] options = { "Go to Mangement Screen", "Go to Statistics Screen", "Go to Save Screen" };

		ComboBox<String> userOptions = new ComboBox<>(FXCollections.observableArrayList(options));
		userOptions.setDisable(true);

		userOptions.setOnAction(e -> {
			if (userOptions.getValue() == options[0])
				stage.setScene(management());
			else if (userOptions.getValue() == options[1])
				stage.setScene(statistics());
			else if (userOptions.getValue() == options[2])
				stage.setScene(save(stage));

		});

		return userOptions;
	}

	public void load() throws Exception {
		try {
			Scanner sc = new Scanner(file);
			titles = sc.nextLine();
			int count = 0;
			while (sc.hasNext()) {
				String[] record = sc.nextLine().split(",");
				String[] date = record[0].split("/");System.out.println("12");
				int year = Integer.parseInt(date[0]);
				
				int monthInt = Integer.parseInt(date[1]);
				String month = getMonth(monthInt);
				int day = Integer.parseInt(date[2]);
				
				
				ElectricityRecord rec = new ElectricityRecord(Double.parseDouble(record[1]),
						Double.parseDouble(record[2]), Double.parseDouble(record[3]), Double.parseDouble(record[5]),
						Double.parseDouble(record[6]), Double.parseDouble(record[7]));
				// System.out.println(rec.getTotal_daily_supply());
				insertRecord(year, month, day, rec);
				count++;

			}
			sc.close();
			System.out.println("count = " + count);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new Exception();
		}
	}

	private void insertRecord(int year, String month, int day, ElectricityRecord rec) {

		TNode<Integer, AVL<String, AVL<Integer, ElectricityRecord>>> yearNode = years.search(year);
		if (yearNode == null) {
			yearNode = new TNode<>(year, new AVL<String, AVL<Integer, ElectricityRecord>>());
			years.insert(year, yearNode.getData());
		}

		AVL<String, AVL<Integer, ElectricityRecord>> months = yearNode.getData();

		TNode<String, AVL<Integer, ElectricityRecord>> monthNode = months.search(month);
		if (monthNode == null) {
			monthNode = new TNode<>(month, new AVL<Integer, ElectricityRecord>());
			months.insert(month, monthNode.getData());
		}

		AVL<Integer, ElectricityRecord> days = monthNode.getData();
		TNode<Integer, ElectricityRecord> dayNode = days.search(day);
		if (dayNode == null) {
			dayNode = new TNode<>(day, rec);
			days.insert(day, rec);
		}
	}

	private String getMonth(int monthInt) {
		String month = "";
		switch (monthInt) {
		case 1:
			month = "January";
			break;
		case 2:
			month = "February";
			break;
		case 3:
			month = "March";
			break;
		case 4:
			month = "April";
			break;
		case 5:
			month = "May";
			break;
		case 6:
			month = "June";
			break;
		case 7:
			month = "July";
			break;
		case 8:
			month = "August";
			break;
		case 9:
			month = "September";
			break;
		case 10:
			month = "October";
			break;
		case 11:
			month = "November";
			break;
		case 12:
			month = "December";
			break;
		}

		return month;
	}

	public Scene management() {
		// create a root node
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(40));
		root.setStyle("-fx-background-color: white;");

		// create a scene
		Scene scene = new Scene(root, 700, 600);

		// create a label
		Label dateLabel = new Label("Select a date");
		dateLabel.setFont(btnFont);

		// create a DatePicker
		DatePicker datePicker = new DatePicker();

		// create an HBox to hold the DatePicker with the label
		HBox dateBox = new HBox(5);
		dateBox.getChildren().addAll(dateLabel, datePicker);
		dateBox.setAlignment(Pos.CENTER);

		// create radioButtons
		Label btnLabel = new Label("What do you want to do?");
		RadioButton insert = new RadioButton("Insert Record   ");
		RadioButton delete = new RadioButton("Delete Record ");
		RadioButton update = new RadioButton("Update Record");
		RadioButton search = new RadioButton("Search Record");
		RadioButton traverse = new RadioButton("Traverse trees");
		RadioButton height = new RadioButton("Dispaly height");

		insert.setFont(btnFont);
		delete.setFont(btnFont);
		update.setFont(btnFont);
		search.setFont(btnFont);
		traverse.setFont(btnFont);
		height.setFont(btnFont);

		insert.setDisable(true);
		delete.setDisable(true);
		update.setDisable(true);
		search.setDisable(true);

		btnLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));

		ToggleGroup group = new ToggleGroup();
		insert.setToggleGroup(group);
		delete.setToggleGroup(group);
		update.setToggleGroup(group);
		search.setToggleGroup(group);
		traverse.setToggleGroup(group);
		height.setToggleGroup(group);

		// Create a label
		Label screen = new Label("Management Screen");
		screen.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 40));
		screen.setTextAlignment(TextAlignment.CENTER);
		screen.setTextFill(Color.CRIMSON);

		// set the buttons in a VBox
		VBox btnBox = new VBox(10);
		btnBox.getChildren().addAll(btnLabel, insert, delete, update, search, traverse, height);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.setPadding(new Insets(5));

		// set the dateBox and the btnBox in an HBox
		HBox hbox = new HBox(30);
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().addAll(dateBox, btnBox);

		// set the basic nodes on the root
		root.setTop(hbox);
		root.setCenter(screen);
		root.setBottom(back);

		// setOnAction
		datePicker.setOnAction(e -> {
			insert.setDisable(false);
			delete.setDisable(false);
			update.setDisable(false);
			search.setDisable(false);
			traverse.setDisable(false);
			height.setDisable(false);

			insert.setSelected(false);
			delete.setSelected(false);
			update.setSelected(false);
			search.setSelected(false);
			traverse.setSelected(false);
			height.setSelected(false);

			LocalDate date = datePicker.getValue();
			// String month = getMonth(date.getMonthValue());
			insert.setOnAction(i -> {
				if (insert.isSelected()) {
					Parent parent = insertView(date.getYear(), getMonth(date.getMonthValue()), date.getDayOfMonth());
					root.setCenter(parent);
				}
			});
			update.setOnAction(i -> {
				Parent parent = updateView(date.getYear(), getMonth(date.getMonthValue()), date.getDayOfMonth());
				root.setCenter(parent);
			});
			delete.setOnAction(i -> {
				Parent parent = deleteView(date.getYear(), getMonth(date.getMonthValue()), date.getDayOfMonth());
				root.setCenter(parent);
			});
			search.setOnAction(i -> {
				Parent parent = searchView(date.getYear(), getMonth(date.getMonthValue()), date.getDayOfMonth());
				root.setCenter(parent);
			});
		});

		traverse.setOnAction(i -> {
			Parent parent = traverseView();
			root.setCenter(parent);
		});
		height.setOnAction(i -> {
			Parent parent = heightView();
			root.setCenter(parent);
		});

		return scene;
	}

	public Parent insertView(int year, String month, int day) {
		Label exist = new Label("A record for " + month + "/" + day + "/" + year + " already exists!");
		exist.setFont(Font.font("Times New Roman", FontWeight.BOLD, 25));
		exist.setTextFill(Color.FIREBRICK);

		TNode<Integer, AVL<String, AVL<Integer, ElectricityRecord>>> yearr = years.search(year);
		TNode<String, AVL<Integer, ElectricityRecord>> monthh = searchMonthInYear(yearr, month);
		TNode<Integer, ElectricityRecord> dayy = searchDayInMonth(monthh, day);

		if (yearr != null && monthh != null && dayy != null) {
			return exist;
		}

		Label line1 = new Label("Israeli Lines: ");
		Label plant = new Label("Gaza Plant: ");
		Label line2 = new Label("Egyption Lines: ");
		Label demand = new Label("Overall Demand: ");
		Label cuts = new Label("Power Cuts: ");
		Label temp = new Label("Temperature");

		line1.setFont(btnFont);
		plant.setFont(btnFont);
		line2.setFont(btnFont);
		demand.setFont(btnFont);
		cuts.setFont(btnFont);
		temp.setFont(btnFont);

		TextField tfLine1 = new TextField();
		TextField tfPlant = new TextField();
		TextField tfLine2 = new TextField();
		TextField tfDemand = new TextField();
		TextField tfCuts = new TextField();
		TextField tfTemp = new TextField();

		tfLine1.setFont(btnFont);
		tfPlant.setFont(btnFont);
		tfLine2.setFont(btnFont);
		tfDemand.setFont(btnFont);
		tfCuts.setFont(btnFont);
		tfTemp.setFont(btnFont);

		tfLine1.setPromptText("Enter a new value");
		tfPlant.setPromptText("Enter a new value");
		tfLine2.setPromptText("Enter a new value");
		tfDemand.setPromptText("Enter a new value");
		tfCuts.setPromptText("Enter a new value");
		tfTemp.setPromptText("Enter a new value");

		Button add = new Button("Insert Record");

		// create a GridPane
		GridPane pane = new GridPane();
		pane.setHgap(5);
		pane.setVgap(5);
		pane.setPadding(new Insets(30));
		pane.setAlignment(Pos.CENTER);

		pane.add(line1, 0, 0);
		pane.add(plant, 0, 1);
		pane.add(line2, 0, 2);
		pane.add(demand, 0, 3);
		pane.add(cuts, 0, 4);
		pane.add(temp, 0, 5);

		pane.add(tfLine1, 1, 0);
		pane.add(tfPlant, 1, 1);
		pane.add(tfLine2, 1, 2);
		pane.add(tfDemand, 1, 3);
		pane.add(tfCuts, 1, 4);
		pane.add(tfTemp, 1, 5);

		pane.add(add, 1, 7);

		Label label = new Label("");
		label.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));

		VBox box = new VBox(5);
		box.getChildren().addAll(pane, label);
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(20));

		tfPlant.setDisable(true);
		tfLine2.setDisable(true);
		tfDemand.setDisable(true);
		tfCuts.setDisable(true);
		tfTemp.setDisable(true);

		add.setDisable(true);

		Text tx = new Text("");

		tfLine1.setOnAction(e -> {
			try {
				label.setText("");
				Double num = Double.parseDouble(tfLine1.getText());
				if (num < 0)
					throw new IllegalArgumentException("Occupation lines cannot be negative");
				tx.setText("" + num);
				tfLine1.setDisable(true);
				tfPlant.setDisable(false);
			} catch (IllegalArgumentException n) {
				label.setText(n.getMessage());
				label.setTextFill(Color.RED);
			} catch (Exception n) {
				label.setText("Error!");
				label.setTextFill(Color.RED);
			}
		});

		tfPlant.setOnAction(i -> {
			try {
				label.setText("");
				Double num = Double.parseDouble(tfPlant.getText());
				if (num < 0)
					throw new IllegalArgumentException("Gaze power plant cannot be negative");
				tx.setText(tx.getText() + "," + num);
				tfPlant.setDisable(true);
				tfLine2.setDisable(false);
			} catch (IllegalArgumentException n) {
				label.setText(n.getMessage());
				label.setTextFill(Color.RED);
			} catch (Exception n) {
				label.setText("Error!");
				label.setTextFill(Color.RED);
			}
		});

		tfLine2.setOnAction(e -> {
			try {
				label.setText("");
				Double num = Double.parseDouble(tfLine2.getText());
				if (num < 0)
					throw new IllegalArgumentException("Eyption lines cannot be negative");
				tx.setText(tx.getText() + "," + num);
				tfLine2.setDisable(true);
				tfDemand.setDisable(false);
			} catch (IllegalArgumentException n) {
				label.setText(n.getMessage());
				label.setTextFill(Color.RED);
			} catch (Exception n) {
				label.setText("Error!");
				label.setTextFill(Color.RED);
			}
		});

		tfDemand.setOnAction(r -> {
			try {
				label.setText("");
				Double num = Double.parseDouble(tfDemand.getText());
				if (num < 0)
					throw new IllegalArgumentException("Overall Demand cannot be negative");
				tx.setText(tx.getText() + "," + num);
				tfDemand.setDisable(true);
				tfCuts.setDisable(false);
			} catch (IllegalArgumentException n) {
				label.setText(n.getMessage());
				label.setTextFill(Color.RED);
			} catch (Exception n) {
				label.setText("Error!");
				label.setTextFill(Color.RED);
			}
		});

		tfCuts.setOnAction(v -> {
			try {
				label.setText("");
				Double num = Double.parseDouble(tfCuts.getText());
				if (num < 0 || num > 24)
					throw new IllegalArgumentException("Power cuts in a day cannot be negative or more the 24 hours");
				tx.setText(tx.getText() + "," + num);
				tfCuts.setDisable(true);
				tfTemp.setDisable(false);
			} catch (IllegalArgumentException n) {
				label.setText(n.getMessage());
				label.setTextFill(Color.RED);
			} catch (Exception n) {
				label.setText("Error!");
				label.setTextFill(Color.RED);
			}
		});

		tfTemp.setOnAction(h -> {
			try {
				label.setText("");
				Double num = Double.parseDouble(tfTemp.getText());
				tx.setText(tx.getText() + "," + num);
				tfTemp.setDisable(true);
				add.setDisable(false);
				label.setText("");
			} catch (Exception n) {
				label.setText("Error!");
				label.setTextFill(Color.RED);
			}
		});

		add.setOnAction(g -> {
			try {
				String[] numbers = tx.getText().split(",");
				ElectricityRecord rec = new ElectricityRecord(Double.parseDouble(numbers[0]),
						Double.parseDouble(numbers[1]), Double.parseDouble(numbers[2]), Double.parseDouble(numbers[3]),
						Double.parseDouble(numbers[4]), Double.parseDouble(numbers[5]));
				insertRecord(year, month, day, rec);
				label.setText("The record has been added successfully!");
				label.setTextFill(Color.GREEN);
				add.setDisable(true);
			} catch (Exception n) {
				label.setText("Error!");
				label.setTextFill(Color.RED);
			}
		});

		return box;
	}

	// this method takes a year as a node(which is a CLinkedList) and returns null
	// if the year is null or the month is not in the data of the year
	public TNode<String, AVL<Integer, ElectricityRecord>> searchMonthInYear(
			TNode<Integer, AVL<String, AVL<Integer, ElectricityRecord>>> year, String month) {

		if (year == null) {
			return null;
		}

		// if year.getData is null, construct a new empty CLinkedList of months as the
		// data of that year
		if (year.getData() == null) {
			year.setData(new AVL<String, AVL<Integer, ElectricityRecord>>());
			return null;
		}

		// if year node and the year.getData linkedList are not null, search for the
		// month and return its reference
		TNode<String, AVL<Integer, ElectricityRecord>> monthh = year.getData().search(month);
		return monthh;
	}

	public TNode<Integer, ElectricityRecord> searchDayInMonth(TNode<String, AVL<Integer, ElectricityRecord>> month,
			int day) {

		if (month == null) {
			return null;
		}

		// if month.getData is null, construct a new empty CLinkedList of months as the
		// data of that month
		if (month.getData() == null) {
			month.setData(new AVL<Integer, ElectricityRecord>());
			return null;
		}

		// if month node and the month.getData linkedList are not null, search for the
		// day and return its reference
		TNode<Integer, ElectricityRecord> dayy = month.getData().search(day);
		return dayy;
	}

	public Parent updateView(int year, String month, int day) {
		Label exist = new Label("There is no available record for " + month + "/" + day + "/" + year);
		exist.setFont(Font.font("Times New Roman", FontWeight.BOLD, 25));
		exist.setTextFill(Color.FIREBRICK);
		TNode<Integer, ElectricityRecord> dayy = search(year, month, day);
		if (dayy == null) {
			return exist;
		}
		exist.setText("Record for " + month + "/" + day + "/" + year + " has been updated successfully!");
		exist.setTextFill(Color.GREEN);

		Label checkLabel = new Label("Please tick each box next to the attributes you want to update");
		checkLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));

		// create checkBox
		CheckBox line1 = new CheckBox("Israeli Lines");
		CheckBox plant = new CheckBox("Power Plants");
		CheckBox line2 = new CheckBox("Egyption Lines");
		CheckBox demand = new CheckBox("Overall Demand");
		CheckBox cuts = new CheckBox("Power Cuts");
		CheckBox temp = new CheckBox("Temperature");

		line1.setFont(btnFont);
		plant.setFont(btnFont);
		line2.setFont(btnFont);
		demand.setFont(btnFont);
		cuts.setFont(btnFont);
		temp.setFont(btnFont);

		// create a GridPane
		GridPane pane = new GridPane();
		pane.setHgap(5);
		pane.setVgap(5);
		pane.setPadding(new Insets(30));
		pane.setAlignment(Pos.CENTER);
		pane.add(line1, 0, 0);
		pane.add(plant, 0, 1);
		pane.add(line2, 0, 2);
		pane.add(demand, 0, 3);
		pane.add(cuts, 0, 4);
		pane.add(temp, 0, 5);

		TextField tf1 = new TextField();
		TextField tf2 = new TextField();
		TextField tf3 = new TextField();
		TextField tf4 = new TextField();
		TextField tf5 = new TextField();
		TextField tf6 = new TextField();

		tf1.setPromptText("Enter updated value");
		tf2.setPromptText("Enter updated value");
		tf3.setPromptText("Enter updated value");
		tf4.setPromptText("Enter updated value");
		tf5.setPromptText("Enter updated value");
		tf6.setPromptText("Enter updated value");

		tf1.setDisable(true);
		tf2.setDisable(true);
		tf3.setDisable(true);
		tf4.setDisable(true);
		tf5.setDisable(true);
		tf6.setDisable(true);

		Button update1 = new Button("update");
		Button update2 = new Button("update");
		Button update3 = new Button("update");
		Button update4 = new Button("update");
		Button update5 = new Button("update");
		Button update6 = new Button("update");

		update1.setDisable(true);
		update2.setDisable(true);
		update3.setDisable(true);
		update4.setDisable(true);
		update5.setDisable(true);
		update6.setDisable(true);

		pane.add(tf1, 1, 0);
		pane.add(tf2, 1, 1);
		pane.add(tf3, 1, 2);
		pane.add(tf4, 1, 3);
		pane.add(tf5, 1, 4);
		pane.add(tf6, 1, 5);

		pane.add(update1, 2, 0);
		pane.add(update2, 2, 1);
		pane.add(update3, 2, 2);
		pane.add(update4, 2, 3);
		pane.add(update5, 2, 4);
		pane.add(update6, 2, 5);

		Label label1 = new Label("");
		Label label2 = new Label("");
		Label label3 = new Label("");
		Label label4 = new Label("");
		Label label5 = new Label("");
		Label label6 = new Label("");

		label1.setFont(btnFont);
		label2.setFont(btnFont);
		label3.setFont(btnFont);
		label4.setFont(btnFont);
		label5.setFont(btnFont);
		label6.setFont(btnFont);

		pane.add(label1, 3, 0);
		pane.add(label2, 3, 1);
		pane.add(label3, 3, 2);
		pane.add(label4, 3, 3);
		pane.add(label5, 3, 4);
		pane.add(label6, 3, 5);

		// setOnAction
		line1.setOnAction(e -> {
			if (line1.isSelected()) {
				tf1.setDisable(false);
			} else {
				tf1.setDisable(true);
				update1.setDisable(true);
			}
		});

		plant.setOnAction(e -> {
			if (plant.isSelected()) {
				tf2.setDisable(false);
			} else {
				tf2.setDisable(true);
				update2.setDisable(true);
			}
		});

		line2.setOnAction(e -> {
			if (line2.isSelected()) {
				tf3.setDisable(false);
			} else {
				tf3.setDisable(true);
				update3.setDisable(true);
			}
		});

		demand.setOnAction(e -> {
			if (demand.isSelected()) {
				tf4.setDisable(false);
			} else {
				tf4.setDisable(true);
				update4.setDisable(true);
			}
		});

		cuts.setOnAction(e -> {
			if (cuts.isSelected()) {
				tf5.setDisable(false);
			} else {
				tf5.setDisable(true);
				update5.setDisable(true);
			}
		});

		temp.setOnAction(e -> {
			if (temp.isSelected()) {
				tf6.setDisable(false);
			} else {
				tf6.setDisable(true);
				update6.setDisable(true);
			}
		});

		tf1.setOnAction(e -> {
			update1.setDisable(false);
			label1.setText("");
		});

		tf2.setOnAction(e -> {
			update2.setDisable(false);
			label2.setText("");
		});

		tf3.setOnAction(e -> {
			update3.setDisable(false);
			label3.setText("");
		});

		tf4.setOnAction(e -> {
			update4.setDisable(false);
			label4.setText("");
		});

		tf5.setOnAction(e -> {
			update5.setDisable(false);
			label5.setText("");
		});

		tf6.setOnAction(e -> {
			update6.setDisable(false);
			label6.setText("");
		});

		update1.setOnAction(e -> {
			try {
				label1.setText("Error!");
				label1.setTextFill(Color.RED);
				double value = Double.parseDouble(tf1.getText());
				if (updateIsraeliLines(year, month, day, value)) {
					label1.setText("Updated \nSuccessfully!");
					label1.setTextFill(Color.GREEN);
				}
			} catch (IllegalArgumentException n) {
				label1.setText(n.getMessage());
			} catch (Exception n) {
			}
			update1.setDisable(true);
		});

		update2.setOnAction(e -> {
			try {
				label2.setText("Error!");
				label2.setTextFill(Color.RED);
				double value = Double.parseDouble(tf2.getText());
				if (updatePowerPlant(year, month, day, value)) {
					label2.setTextFill(Color.GREEN);
					label2.setText("Updated \nSuccessfully!");
				}
			} catch (IllegalArgumentException n) {
				label2.setText(n.getMessage());
			} catch (Exception n) {
			}
			update2.setDisable(true);
		});

		update3.setOnAction(e -> {
			try {
				label3.setText("Error!");
				label3.setTextFill(Color.RED);
				double value = Double.parseDouble(tf3.getText());
				if (updateEgyptionLines(year, month, day, value)) {
					label3.setTextFill(Color.GREEN);
					label3.setText("Updated \nSuccessfully!");
				}
			} catch (IllegalArgumentException n) {
				label3.setText(n.getMessage());
			} catch (Exception n) {
			}
			update3.setDisable(true);
		});

		update4.setOnAction(e -> {
			try {
				label4.setText("Error!");
				label4.setTextFill(Color.RED);
				double value = Double.parseDouble(tf4.getText());
				if (updateDemand(year, month, day, value)) {
					label4.setTextFill(Color.GREEN);
					label4.setText("Updated \nSuccessfully!");
				}
			} catch (IllegalArgumentException n) {
				label4.setText(n.getMessage());
			} catch (Exception n) {
			}
			update4.setDisable(true);
		});

		update5.setOnAction(e -> {
			try {
				label5.setText("Error!");
				label5.setTextFill(Color.RED);
				double value = Double.parseDouble(tf5.getText());
				if (updatePowerCut(year, month, day, value)) {
					label5.setTextFill(Color.GREEN);
					label5.setText("Updated \nSuccessfully!");
				}
			} catch (IllegalArgumentException n) {
				label5.setText(n.getMessage());
			} catch (Exception n) {
			}
			update5.setDisable(true);
		});

		update6.setOnAction(e -> {
			try {
				label6.setText("Error!");
				label6.setTextFill(Color.RED);
				double value = Double.parseDouble(tf6.getText());
				if (updateTemp(year, month, day, value)) {
					label6.setTextFill(Color.GREEN);
					label6.setText("Updated \nSuccessfully!");
				}
			} catch (Exception n) {
			}
			update6.setDisable(true);
		});

		return pane;
	}

	public TNode<Integer, ElectricityRecord> search(int year, String month, int day) {
		TNode<String, AVL<Integer, ElectricityRecord>> b = searchMonth(year, month);
		if (b != null) {
			AVL<Integer, ElectricityRecord> days = b.getData();
			if (days != null) {
				TNode<Integer, ElectricityRecord> dayy = days.search(day);
				if (dayy != null) {
					return dayy;
				}
			}
		}
		return null;
	}

	// this method is only to be used in search(int year, String month, int day) and
	// delete(int year, String month, int day) methods
	private TNode<String, AVL<Integer, ElectricityRecord>> searchMonth(int year, String month) {
		TNode<Integer, AVL<String, AVL<Integer, ElectricityRecord>>> n = years.search(year);
		if (n != null) {
			AVL<String, AVL<Integer, ElectricityRecord>> months = n.getData();
			if (months != null) {
				TNode<String, AVL<Integer, ElectricityRecord>> b = months.search(month);
				if (b != null) {
					return b;
				}
			}
		}
		return null;
	}

	public boolean updateIsraeliLines(int year, String month, int day, double israeli_lines) {
		TNode<Integer, ElectricityRecord> dayy = search(year, month, day);
		if (dayy != null) {
			if (israeli_lines < 0)
				throw new IllegalArgumentException("This can't be negative");
			dayy.getData().setOccupation_lines(israeli_lines);
			dayy.getData().calculate_total_daily_supply();
			return true;
		}
		return false;
	}

	public boolean updateEgyptionLines(int year, String month, int day, double Egyption_lines) {
		TNode<Integer, ElectricityRecord> dayy = search(year, month, day);
		if (dayy != null) {
			if (Egyption_lines < 0)
				throw new IllegalArgumentException("This can't be negative");
			dayy.getData().setEgyption_lines(Egyption_lines);
			dayy.getData().calculate_total_daily_supply();
			return true;
		}
		return false;
	}

	public boolean updateDemand(int year, String month, int day, double demand) {
		TNode<Integer, ElectricityRecord> dayy = search(year, month, day);
		if (dayy != null) {
			if (demand < 0)
				throw new IllegalArgumentException("This can't be negative");
			dayy.getData().setDemand(demand);
			return true;
		}
		return false;
	}

	public boolean updatePowerCut(int year, String month, int day, double power_cut) {
		TNode<Integer, ElectricityRecord> dayy = search(year, month, day);
		if (dayy != null) {
			if (power_cut < 0)
				throw new IllegalArgumentException("This can't be negative");
			else if (power_cut > 24)
				throw new IllegalArgumentException("Upper limit: 24 hours");
			dayy.getData().setPower_cuts_hour_day(power_cut);
			return true;
		}
		return false;

	}

	public boolean updatePowerPlant(int year, String month, int day, double power_plant) {
		TNode<Integer, ElectricityRecord> dayy = search(year, month, day);
		if (dayy != null) {
			if (power_plant < 0)
				throw new IllegalArgumentException("This can't be negative");
			dayy.getData().setPower_plant(power_plant);
			dayy.getData().calculate_total_daily_supply();
			return true;
		}
		return false;
	}

	public boolean updateTemp(int year, String month, int day, double temp) {
		TNode<Integer, ElectricityRecord> dayy = search(year, month, day);
		if (dayy != null) {
			dayy.getData().setTemp(temp);
			return true;
		}
		return false;
	}

	public Parent searchView(int year, String month, int day) {
		Label exist = new Label("There is no available record for " + month + "/" + day + "/" + year);
		exist.setFont(Font.font("Times New Roman", FontWeight.BOLD, 25));
		exist.setTextFill(Color.FIREBRICK);
		TNode<Integer, ElectricityRecord> dayy = search(year, month, day);
		if (dayy != null) {
			VBox dataBox = new VBox(15);

			Label dataLabel = new Label("Electricity record for  " + month + "/" + day + "/" + year);
			dataLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
			dataLabel.setTextFill(Color.BLUEVIOLET);

			Text data = new Text("Israeli Lines: " + dayy.getData().getOccupation_lines() + " MWs \r\n\n"
					+ "Gaza Power Plant: " + dayy.getData().getPower_plant() + " MWs \r\n\n" + "Egyption Lines: "
					+ dayy.getData().getEgyption_lines() + " MWs \r\n\n" + "Total Daily Supply Available: "
					+ dayy.getData().getTotal_daily_supply() + " MWs \r\n\n" + "Overall Demand: "
					+ dayy.getData().getDemand() + " MWs \r\n\n" + "Power Cuts: "
					+ dayy.getData().getPower_cuts_hour_day() + " hours/day \r\n\n" + "Temperature: "
					+ dayy.getData().getTemp());

			data.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 15));
			data.setTextAlignment(TextAlignment.CENTER);

			dataBox.getChildren().addAll(dataLabel, data);
			dataBox.setAlignment(Pos.CENTER);
			return dataBox;
		}
		return exist;
	}

	public Parent deleteView(int year, String month, int day) {
		Label exist = new Label("There is no available record for " + month + "/" + day + "/" + year);
		exist.setFont(Font.font("Times New Roman", FontWeight.BOLD, 25));
		exist.setTextFill(Color.FIREBRICK);
		TNode<Integer, ElectricityRecord> dayy = delete(year, month, day);
		if (dayy != null) {
			exist.setText("Record for " + month + "/" + day + "/" + year + " has been deleted successfully!");
			exist.setTextFill(Color.GREEN);
		}
		return exist;

	}

	public TNode<Integer, ElectricityRecord> delete(int year, String month, int day) {
		TNode<String, AVL<Integer, ElectricityRecord>> b = searchMonth(year, month);
		if (b != null) {
			AVL<Integer, ElectricityRecord> days = b.getData();
			if (days != null) {
				return days.delete(day);
			}
		}
		return null;
	}

	public Parent heightView() {
		Label yearlbl = new Label("Height of years' tree: " + years.height());
		Label monthlbl = new Label("Maximum height of months' trees: " + months_Height());
		Label daylbl = new Label("Maximum height of days' trees: " + DaysHeight());

		yearlbl.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 20));
		monthlbl.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 20));
		daylbl.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 20));
		yearlbl.setTextFill(Color.FIREBRICK);
		monthlbl.setTextFill(Color.FIREBRICK);
		daylbl.setTextFill(Color.FIREBRICK);

		VBox box = new VBox(5);
		box.getChildren().addAll(yearlbl, monthlbl, daylbl);
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(15));
		return box;
	}

	// these two methods find the maximum height of months trees
	public int months_Height() {
		return months_Height(years.getRoot());
	}

	private int months_Height(TNode<Integer, AVL<String, AVL<Integer, ElectricityRecord>>> year) {
		if (year == null)
			return 0;

		if (year.isLeaf())
			return year.getData().height();

		int left = 0;
		int right = 0;

		if (year.hasLeft())
			left = months_Height(year.getLeft());

		if (year.hasRight())
			right = months_Height(year.getRight());

		return Math.max(Math.max(left, right), year.getData().height());
	}

	public int DaysHeight() {
		return DaysHeight(years.getRoot());
	}

	private int DaysHeight(TNode<Integer, AVL<String, AVL<Integer, ElectricityRecord>>> year) {
		if (year == null)
			return 0;

		if (year.isLeaf())
			return DaysOfYearHeight(year);

		int left = 0;
		int right = 0;

		if (year.hasLeft())
			left = DaysOfYearHeight(year.getLeft());

		if (year.hasRight())
			right = DaysOfYearHeight(year.getRight());

		return Math.max(Math.max(left, right), DaysOfYearHeight(year));
	}

	// these two methods accept a year node and return the maximum height of the
	// highest day tree
	public int DaysOfYearHeight(TNode<Integer, AVL<String, AVL<Integer, ElectricityRecord>>> yearNode) {
		if (yearNode != null && yearNode.getData() != null)
			return DaysOfYear_Height(yearNode.getData().getRoot());

		return 0;
	}

	private int DaysOfYear_Height(TNode<String, AVL<Integer, ElectricityRecord>> month) {
		if (month == null)
			return 0;

		if (month.isLeaf())
			return month.getData().height();

		int left = 0;
		int right = 0;

		if (month.hasLeft())
			left = DaysOfYear_Height(month.getLeft());

		if (month.hasRight())
			right = DaysOfYear_Height(month.getRight());

		return Math.max(Math.max(left, right), month.getData().height());
	}

	public Parent traverseView() {
		TextArea tx = new TextArea(years.toString());
		return tx;
	}

	// this method returns statistics for a given day across all years and months
	public Statistics traverseDay(int day) {
		Statistics stat = new Statistics(0, new ElectricityRecord(0, 0, 0, 0, 0, 0),
				new ElectricityRecord(max, max, max, max, max, max),
				new ElectricityRecord(min, min, min, min, min, min));
		TNode<Integer, AVL<String, AVL<Integer, ElectricityRecord>>> yearNode = years.getRoot();
		if (yearNode != null) {
			traverseDay_1(day, yearNode, stat);
		}

		stat.getAvg().setLabel("Average");
		stat.getMax().setLabel("Maximum");
		stat.getMin().setLabel("Minimun");
		stat.getTotal().setLabel("Total");

		return stat;
	}

	private void traverseDay_1(int day, TNode<Integer, AVL<String, AVL<Integer, ElectricityRecord>>> yearNode,
			Statistics stat) {
		if (yearNode == null)
			return;

		traverseDay_2(day, yearNode.getData().getRoot(), stat);

		if (yearNode.hasLeft())
			traverseDay_1(day, yearNode.getLeft(), stat);

		if (yearNode.hasRight())
			traverseDay_1(day, yearNode.getRight(), stat);
	}

	private void traverseDay_2(int day, TNode<String, AVL<Integer, ElectricityRecord>> monthNode, Statistics stat) {
		if (monthNode == null)
			return;

		TNode<Integer, ElectricityRecord> dayNode = monthNode.getData().search(day);

		if (dayNode != null) {
			ElectricityRecord rec = dayNode.getData();
			Statistics newStat = new Statistics(1, rec, rec, rec);
			updateStat(stat, newStat);
		}

		if (monthNode.hasLeft())
			traverseDay_2(day, monthNode.getLeft(), stat);

		if (monthNode.hasRight())
			traverseDay_2(day, monthNode.getRight(), stat);
	}

	// this method returns statistics for a given month across all days and years
	public Statistics traverseMonth(String month) {
		Statistics stat = new Statistics(0, new ElectricityRecord(0, 0, 0, 0, 0, 0),
				new ElectricityRecord(max, max, max, max, max, max),
				new ElectricityRecord(min, min, min, min, min, min));
		TNode<Integer, AVL<String, AVL<Integer, ElectricityRecord>>> yearNode = years.getRoot();
		traverseMonth(yearNode, month, stat);

		stat.getAvg().setLabel("Average");
		stat.getMax().setLabel("Maximum");
		stat.getMin().setLabel("Minimun");
		stat.getTotal().setLabel("Total");

		return stat;
	}

	private void traverseMonth(TNode<Integer, AVL<String, AVL<Integer, ElectricityRecord>>> yearNode, String month,
			Statistics stat) {
		if (yearNode == null)
			return;

		Statistics newStat = traverseDaysOfMonth(yearNode.getLabel(), month);
		updateStat(stat, newStat);

		if (yearNode.hasLeft())
			traverseMonth(yearNode.getLeft(), month, stat);

		if (yearNode.hasRight())
			traverseMonth(yearNode.getRight(), month, stat);
	}

	// this method returns the statistics for all data in the trees data structure
	public Statistics traverseYears() {
		Statistics stat = new Statistics(0, new ElectricityRecord(0, 0, 0, 0, 0, 0),
				new ElectricityRecord(max, max, max, max, max, max),
				new ElectricityRecord(min, min, min, min, min, min));
		TNode<Integer, AVL<String, AVL<Integer, ElectricityRecord>>> yearNode = years.getRoot();
		traverseYears(yearNode, stat);

		stat.getAvg().setLabel("Average");
		stat.getMax().setLabel("Maximum");
		stat.getMin().setLabel("Minimun");
		stat.getTotal().setLabel("Total");

		return stat;

	}

	private void traverseYears(TNode<Integer, AVL<String, AVL<Integer, ElectricityRecord>>> yearNode, Statistics stat) {
		if (yearNode == null)
			return;

		Statistics newStat = traverseMonthsOfYear(yearNode.getLabel());
		updateStat(stat, newStat);

		if (yearNode.hasLeft())
			traverseYears(yearNode.getLeft(), stat);

		if (yearNode.hasRight())
			traverseYears(yearNode.getRight(), stat);

	}

	// this method returns the statistics for a given year across all months and
	// days
	public Statistics traverseMonthsOfYear(int year) {
		Statistics stat = new Statistics(0, new ElectricityRecord(0, 0, 0, 0, 0, 0),
				new ElectricityRecord(max, max, max, max, max, max),
				new ElectricityRecord(min, min, min, min, min, min));
		TNode<Integer, AVL<String, AVL<Integer, ElectricityRecord>>> yearNode = years.search(year);
		if (yearNode != null) {
			TNode<String, AVL<Integer, ElectricityRecord>> monthNode = yearNode.getData().getRoot();
			traverseMonthsOfYear(year, monthNode, stat);
		}

		stat.getAvg().setLabel("Average");
		stat.getMax().setLabel("Maximum");
		stat.getMin().setLabel("Minimun");
		stat.getTotal().setLabel("Total");

		return stat;
	}

	private void traverseMonthsOfYear(int year, TNode<String, AVL<Integer, ElectricityRecord>> monthNode,
			Statistics stat) {
		if (monthNode == null)
			return;

		if (monthNode.hasLeft())
			traverseMonthsOfYear(year, monthNode.getLeft(), stat);

		if (monthNode.hasRight())
			traverseMonthsOfYear(year, monthNode.getRight(), stat);

		Statistics newStat = traverseDaysOfMonth(year, monthNode.getLabel());

		updateStat(stat, newStat);
		System.out.println(stat.getTotal().getPower_plant());
	}

	// this method returns statistics for a given month across all days
	private Statistics traverseDaysOfMonth(int year, String month) {
		Statistics stat = new Statistics(0, new ElectricityRecord(0, 0, 0, 0, 0, 0),
				new ElectricityRecord(max, max, max, max, max, max),
				new ElectricityRecord(min, min, min, min, min, min));
		TNode<Integer, AVL<String, AVL<Integer, ElectricityRecord>>> yearNode = years.search(year);
		if (yearNode != null) {
			TNode<String, AVL<Integer, ElectricityRecord>> monthNode = yearNode.getData().search(month);
			if (monthNode != null) {
				TNode<Integer, ElectricityRecord> dayNode = monthNode.getData().getRoot();
				traverseDaysOfMonth(dayNode, stat);
			}
		}
		return stat;
	}

	private void traverseDaysOfMonth(TNode<Integer, ElectricityRecord> dayNode, Statistics stat) {

		if (dayNode == null)
			return;

		ElectricityRecord rec = dayNode.getData();
		Statistics newStat = new Statistics(1, rec, rec, rec);
		updateStat(stat, newStat);

		if (dayNode.hasLeft())
			traverseDaysOfMonth(dayNode.getLeft(), stat);

		if (dayNode.hasRight())
			traverseDaysOfMonth(dayNode.getRight(), stat);

	}

	// this method finds minimum values between two ElectricityRecords' objects and
	// sets
	// them for the first object
	private void findMin(ElectricityRecord min, ElectricityRecord rec) {

		if (rec.getOccupation_lines() < min.getOccupation_lines())
			min.setOccupation_lines(rec.getOccupation_lines());

		if (rec.getPower_plant() < min.getPower_plant())
			min.setPower_plant(rec.getPower_plant());

		if (rec.getEgyption_lines() < min.getEgyption_lines())
			min.setEgyption_lines(rec.getEgyption_lines());

		if (rec.getDemand() < min.getDemand())
			min.setDemand(rec.getDemand());

		if (rec.getPower_cuts_hour_day() < min.getPower_cuts_hour_day())
			min.setPower_cuts_hour_day(rec.getPower_cuts_hour_day());

		if (rec.getTemp() < min.getTemp())
			min.setTemp(rec.getTemp());

		min.calculate_total_daily_supply();

	}

	// this method finds maximum values between two Statistics' objects and sets
	// them for the first object
	private void findMax(ElectricityRecord max, ElectricityRecord rec) {

		if (rec.getOccupation_lines() > max.getOccupation_lines())
			max.setOccupation_lines(rec.getOccupation_lines());

		if (rec.getPower_plant() > max.getPower_plant())
			max.setPower_plant(rec.getPower_plant());

		if (rec.getEgyption_lines() > max.getEgyption_lines())
			max.setEgyption_lines(rec.getEgyption_lines());

		if (rec.getDemand() > max.getDemand())
			max.setDemand(rec.getDemand());

		if (rec.getPower_cuts_hour_day() > max.getPower_cuts_hour_day())
			max.setPower_cuts_hour_day(rec.getPower_cuts_hour_day());

		if (rec.getTemp() > max.getTemp())
			max.setTemp(rec.getTemp());

		max.calculate_total_daily_supply();

	}

	// this method finds sum of values between two Statistics' objects and sets them
	// for the first object
	private void add(ElectricityRecord total, ElectricityRecord rec) {
		total.setDemand(total.getDemand() + rec.getDemand());
		total.setEgyption_lines(total.getEgyption_lines() + rec.getEgyption_lines());
		total.setOccupation_lines(total.getOccupation_lines() + rec.getOccupation_lines());
		total.setPower_cuts_hour_day(total.getPower_cuts_hour_day() + rec.getPower_cuts_hour_day());
		total.setPower_plant(total.getPower_plant() + rec.getPower_plant());
		total.setTemp(total.getTemp() + rec.getTemp());
		total.calculate_total_daily_supply();
	}

	// this method finds all preferable values of minimum, maximum and total between
	// two Statistics' objects and sets them for the first object and changes the
	// count value
	private void updateStat(Statistics update, Statistics temp) {
		update.setCount(update.getCount() + temp.getCount());

		findMax(update.getMax(), temp.getMax());

		findMin(update.getMin(), temp.getMin());

		add(update.getTotal(), temp.getTotal());

		update.calculateAvg();
	}

	public Scene statistics() {
		BorderPane root = new BorderPane();

		Label lblOpt = new Label("What do you want to find ?");
		lblOpt.setFont(Font.font("Times New Roman", FontWeight.BOLD, 18));

		RadioButton btnDay = new RadioButton("Statisitics for a given day across all months and years.");
		RadioButton btnMonth = new RadioButton("Statistics for a given month across all days and years.");
		RadioButton btnYear = new RadioButton("Statistics for a given year across all days and months.");
		RadioButton btnAll = new RadioButton("Statisitcs for all data.");

		ToggleGroup group = new ToggleGroup();

		btnDay.setToggleGroup(group);
		btnMonth.setToggleGroup(group);
		btnYear.setToggleGroup(group);
		btnAll.setToggleGroup(group);

		Font btnFont = Font.font("Times New Roman", FontWeight.NORMAL, 12);

		btnDay.setFont(btnFont);
		btnMonth.setFont(btnFont);
		btnYear.setFont(btnFont);
		btnAll.setFont(btnFont);

		GridPane btnPane = new GridPane();
		btnPane.setHgap(15);
		btnPane.setVgap(15);

		btnPane.add(lblOpt, 0, 0);

		btnPane.add(btnDay, 0, 1);
		btnPane.add(btnMonth, 0, 2);
		btnPane.add(btnYear, 0, 3);
		btnPane.add(btnAll, 0, 4);

		TextField tfYear = new TextField();
		tfYear.setPromptText("Type the year");
		tfYear.setDisable(true);

		ComboBox<Integer> days = daysB(root);
		btnPane.add(days, 1, 1);

		ComboBox<String> months = monthsB(root);
		btnPane.add(months, 1, 2);

		btnPane.add(tfYear, 1, 3);

		Label lblScreen = new Label("Statistics Screen");
		lblScreen.setFont(Font.font("Times New Roman", FontWeight.BOLD, 35));
		lblScreen.setTextAlignment(TextAlignment.CENTER);
		lblScreen.setTextFill(Color.CRIMSON);

		// setOnAction
		btnDay.setOnAction(e -> {
			days.setDisable(false);
			months.setDisable(true);
			tfYear.setDisable(true);
		});

		btnMonth.setOnAction(e -> {
			days.setDisable(true);
			months.setDisable(false);
			tfYear.setDisable(true);
		});

		btnYear.setOnAction(e -> {
			days.setDisable(true);
			months.setDisable(true);
			tfYear.setDisable(false);
		});

		btnAll.setOnAction(e -> {
			days.setDisable(true);
			months.setDisable(true);
			tfYear.setDisable(true);
			Statistics stat = traverseYears();
			showTable(stat, root);
		});

		tfYear.setOnAction(e -> {
			try {
				int yearInt = Integer.parseInt(tfYear.getText());
				TNode<Integer, AVL<String, AVL<Integer, ElectricityRecord>>> year = years.search(yearInt);
				if (year == null) {
					lblScreen.setText("No records for this year!");
					lblScreen.setFont(Font.font("Times New Roman", 20));
					root.setCenter(lblScreen);
				} else {
					Statistics stat = traverseMonthsOfYear(yearInt);
					showTable(stat, root);
				}
			} catch (NumberFormatException b) {
				lblScreen.setText("Error! You can only type a number as a year");
				lblScreen.setTextFill(Color.RED);
				lblScreen.setFont(Font.font("Times New Roman", 20));
				root.setCenter(lblScreen);
			} catch (Exception b) {
				lblScreen.setText("Error!");
				lblScreen.setTextFill(Color.RED);
				lblScreen.setFont(Font.font("Times New Roman", 20));
				root.setCenter(lblScreen);
			}
		});

		root.setTop(btnPane);
		root.setCenter(lblScreen);
		root.setBottom(back);

		root.setPadding(new Insets(35));
		root.setStyle("-fx-background-color: white;");

		Scene scene = new Scene(root, 700, 600);
		return scene;
	}

	// this method is only used in statistics() method
	private ComboBox<String> monthsB(BorderPane root) {

		String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };

		ComboBox<String> monthsBox = new ComboBox<>(FXCollections.observableArrayList(months));
		monthsBox.setOnAction(e -> {

			Statistics stat = null;

			String option = monthsBox.getValue();
			stat = traverseMonth(option);
			showTable(stat, root);
		});

		monthsBox.setDisable(true);
		return monthsBox;
	}

	// this method is only used in statistics() method
	private ComboBox<Integer> daysB(BorderPane root) {

		Integer[] days = new Integer[31];
		for (int i = 0; i < days.length; i++) {
			days[i] = i + 1;
		}

		ComboBox<Integer> daysBox = new ComboBox<>(FXCollections.observableArrayList(days));
		daysBox.setOnAction(e -> {
			int option = daysBox.getValue();
			Statistics stat = traverseDay(option);
			showTable(stat, root);
		});

		daysBox.setDisable(true);
		return daysBox;
	}

	public void showTable(Statistics stat, BorderPane root) {
//		if (stat != null) {
		TableView table = new TableView();

		ObservableList<ElectricityRecord> list = FXCollections.observableArrayList(stat.getTotal(), stat.getAvg(),
				stat.getMax(), stat.getMin());

		table.setItems(list);

		TableColumn labelClm = new TableColumn("Kind of Statistics");
		labelClm.setCellValueFactory(new PropertyValueFactory<ElectricityRecord, Double>("label"));
//		labelClm.setMaxWidth(80);

		TableColumn occuLinesClm = new TableColumn("Occupation Lines");
		occuLinesClm.setCellValueFactory(new PropertyValueFactory<ElectricityRecord, Double>("occupation_lines"));
//		occuLinesClm.setMaxWidth();

		TableColumn powerPlantclm = new TableColumn("Gaze Power Plant");
		powerPlantclm.setCellValueFactory(new PropertyValueFactory<ElectricityRecord, Double>("power_plant"));
//		powerPlantclm.setMaxWidth(80);

		TableColumn EgyptionLinesClm = new TableColumn("Egyption Lines");
		EgyptionLinesClm.setCellValueFactory(new PropertyValueFactory<ElectricityRecord, Double>("Egyption_lines"));
//		EgyptionLinesClm.setMaxWidth(80);

		TableColumn supplyClm = new TableColumn("Total Supply");
		supplyClm.setCellValueFactory(new PropertyValueFactory<ElectricityRecord, Double>("total_daily_supply"));
//		supplyClm.setMaxWidth(80);

		TableColumn demandClm = new TableColumn("Demand");
		demandClm.setCellValueFactory(new PropertyValueFactory<ElectricityRecord, Double>("demand"));
//		demandClm.setMaxWidth(80);

		TableColumn powerCutClm = new TableColumn("Power Cuts");
		powerCutClm.setCellValueFactory(new PropertyValueFactory<ElectricityRecord, Double>("power_cuts_hour_day"));
//		powerCutClm.setMaxWidth(80);

		TableColumn tempClm = new TableColumn("Temperature");
		tempClm.setCellValueFactory(new PropertyValueFactory<ElectricityRecord, Double>("temp"));
//		tempClm.setMaxWidth(80);

		table.getColumns().addAll(labelClm, occuLinesClm, powerPlantclm, EgyptionLinesClm, supplyClm, demandClm,
				powerCutClm, tempClm);

		table.setMaxSize(1000, 270);
		root.setCenter(table);
//		}
	}

	public Scene save(Stage stage) {
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 700, 600);

		Button save = new Button("Save file");
		save.setFont(btnFont);

		// create FileChooser object
		FileChooser saveFile = new FileChooser();

		// Set extension filter for text files
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		saveFile.getExtensionFilters().addAll(extFilter, extFilter2);

		save.setOnAction(e -> {
			File f = saveFile.showSaveDialog(stage);
			getFile(f);
			System.out.println(f);
			if (f != null) {
				Label label = new Label("Thank you!");
				label.setTextFill(Color.WHITE);
				label.setFont(Font.font("Times New Roman", 50));
				root.setCenter(label);
			}
		});

		root.setCenter(save);
		save.setPrefSize(80, 40);
		root.setTop(back);

		root.setPadding(new Insets(10));

		root.setStyle("-fx-background-color: ROSYBROWN;");
		return scene;
	}

	public void getFile(File file) {
		try {
			PrintWriter writer = new PrintWriter(file);
			writer.println(titles);
			printList(writer);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void printList(PrintWriter writer) {
		printList_1(years.getRoot(), writer);
	}

	private void printList_1(TNode<Integer, AVL<String, AVL<Integer, ElectricityRecord>>> year, PrintWriter writer) {

		if (year == null)
			return;

		if (year.hasLeft())
			printList_1(year.getLeft(), writer);

		printList_2(year, writer);

		if (year.hasRight())
			printList_1(year.getRight(), writer);
	}

	private void printList_2(TNode<Integer, AVL<String, AVL<Integer, ElectricityRecord>>> year, PrintWriter writer) {

		for (int i = 1; i <= 12; i++) {
			String month = getMonth(i);
			TNode<String, AVL<Integer, ElectricityRecord>> monthNode = year.getData().search(month);
			if (monthNode != null)
				printList_3(monthNode.getData().getRoot(), year.getLabel(), i, writer);
		}
	}

	private void printList_3(TNode<Integer, ElectricityRecord> day, int year, int month, PrintWriter writer) {

		if (day == null)
			return;

		if (day.hasLeft())
			printList_3(day.getLeft(), year, month, writer);

		ElectricityRecord rec = day.getData();
		String date = year + "-" + month + "-" + day.getLabel();
		String data = date + "," + rec.getOccupation_lines() + "," + rec.getPower_plant() + ","
				+ rec.getEgyption_lines() + "," + rec.getTotal_daily_supply() + "," + rec.getDemand() + ","
				+ rec.getPower_cuts_hour_day() + "," + rec.getTemp();

		writer.println(data);

		if (day.hasRight())
			printList_3(day.getRight(), year, month, writer);
	}

	/*
	 * int count = 0; TNode<Integer, AVL<String, AVL<Integer, ElectricityRecord>>>
	 * currYear = years.getRoot(); AVL<String, AVL<Integer, ElectricityRecord>>
	 * months = currYear.getData(); TNode<String, AVL<Integer, ElectricityRecord>>
	 * currMonth = months.getRoot(); AVL<Integer, ElectricityRecord> days =
	 * currMonth.getData(); TNode<Integer, ElectricityRecord> currDay =
	 * days.getRoot(); ElectricityRecord rec = currDay.getData(); String date =
	 * currYear.getLabel() + "-" + currMonth.getLabel() + "-" + currDay.getLabel();
	 * String data = date + "," + rec.getOccupation_lines() + "," +
	 * rec.getPower_plant() + "," + rec.getEgyption_lines() + "," +
	 * rec.getTotal_daily_supply() + "," + rec.getDemand() + "," +
	 * rec.getPower_cuts_hour_day() + "," + rec.getTemp();
	 * 
	 * writer.println(data);
	 * 
	 */

	private int getIntMonth(String month) {
		int monthInt = 0;
		switch (month) {
		case "January":
			monthInt = 1;
			break;
		case "February":
			monthInt = 2;
			break;
		case "March":
			monthInt = 3;
			break;
		case "April":
			monthInt = 4;
			break;
		case "May":
			monthInt = 5;
			break;
		case "June":
			monthInt = 6;
			break;
		case "July":
			monthInt = 7;
			break;
		case "August":
			monthInt = 8;
			break;
		case "September":
			monthInt = 9;
			break;
		case "October":
			monthInt = 10;
			break;
		case "November":
			monthInt = 11;
			break;
		case "December":
			monthInt = 12;
			break;
		}

		return monthInt;
	}
}
