package se.plushogskolan.ju15.javafx;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import javafx.application.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.stage.*;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.layout.*;

import se.plushogskolan.ju15.beans.*;
/**
 * Description...
 *class SpamWindow skapar tabell med kolumner och läser in data.
 *när användaren stänger programmet sparar klassen datat i en text fil.
 * 
 */
public class SpamWindow extends Application {

	TableView<PersonBean> table;
	private ObservableList<PersonBean> personData = FXCollections.observableArrayList();
	SortedList<PersonBean> sortedData = null;
	FilteredList<PersonBean> filteredData = null;
	private BorderPane layout;

	public static void main(String[] args) {

		System.out.println("Launching JavaFX application.");

		// Start the JavaFX application by calling launch().
		launch(args);
	}

	// Override the init() method.
	public void init() {
		System.out.println("Inside the init() method.");
		loadData();
	}

	// Override the start() method.
	public void start(Stage myStage) {

		System.out.println("Inside the start() method.");

		table = new TableView<PersonBean>();
		table.setEditable(true);
		// Give the stage a title.
		myStage.setTitle("Person Spam App");
		filteredData = new FilteredList<>(personData, p -> true);
		sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(table.comparatorProperty());

		table.setItems(sortedData);
		table.setPrefWidth(500);
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		
		TableColumn<PersonBean, String> firstNameCol = new TableColumn<PersonBean, String>("First Name");
		firstNameCol.setCellValueFactory(new PropertyValueFactory("firstName"));
		firstNameCol.setCellFactory(TextFieldTableCell.<PersonBean> forTableColumn());
		firstNameCol.setOnEditCommit((CellEditEvent<PersonBean, String> t) -> {
			((PersonBean) t.getTableView().getItems().get(t.getTablePosition().getRow())).setFirstName(t.getNewValue());
		});

		TableColumn<PersonBean, String> lastNameCol = new TableColumn<PersonBean, String>("Last Name");
		lastNameCol.setCellValueFactory(new PropertyValueFactory("lastName"));
		lastNameCol.setCellFactory(TextFieldTableCell.<PersonBean> forTableColumn());
		lastNameCol.setOnEditCommit((CellEditEvent<PersonBean, String> t) -> {
			((PersonBean) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastName(t.getNewValue());
		});
		
		
		TableColumn<PersonBean, String> emailCol = new TableColumn<PersonBean, String>("Email");
		emailCol.setCellValueFactory(new PropertyValueFactory("email"));
		emailCol.setCellFactory(TextFieldTableCell.<PersonBean> forTableColumn());
		emailCol.setOnEditCommit((CellEditEvent<PersonBean, String> t) -> {
			((PersonBean) t.getTableView().getItems().get(t.getTablePosition().getRow())).setEmail(t.getNewValue());
		});
		TableColumn<PersonBean, String> genderCol = new TableColumn<PersonBean, String>("Pet");
		genderCol.setCellValueFactory(new PropertyValueFactory("gender"));
		genderCol.setCellFactory(TextFieldTableCell.<PersonBean> forTableColumn());
		genderCol.setOnEditCommit((CellEditEvent<PersonBean, String> t) -> {
			((PersonBean) t.getTableView().getItems().get(t.getTablePosition().getRow())).setGender(t.getNewValue());
		});
		TableColumn<PersonBean, Integer> ageCol = new TableColumn<PersonBean, Integer>("Age");
		ageCol.setCellValueFactory(new PropertyValueFactory("age"));
		ageCol.setCellFactory(TextFieldTableCell.<PersonBean, Integer> forTableColumn(new IntegerStringConverter()));
		ageCol.setOnEditCommit((CellEditEvent<PersonBean, Integer> t) -> {
			((PersonBean) t.getTableView().getItems().get(t.getTablePosition().getRow())).setAge(t.getNewValue());
		});
		table.getColumns().setAll(firstNameCol,lastNameCol, emailCol, genderCol, ageCol);

		
		Button allButton = new Button("All");
		allButton.setMinSize(100, 10);
		allButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Show all!");
				filteredData.setPredicate(showAll());
			}
		});

		Button maleButton = new Button("Male");
		maleButton.setMinSize(100, 10);
		maleButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Show male");
				//printIt();
				filteredData.setPredicate(isMale());

			}
		});
		Button femaleButton = new Button("Female");
		femaleButton.setMinSize(100, 10);
		femaleButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Show female");
				filteredData.setPredicate(isFemale());
			}
		});

		Button over18Button = new Button("Over 18");
		over18Button.setMinSize(100, 10);
		over18Button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Above 18");
				//filteredData.setPredicate(over18());
				filteredData.setPredicate(new Predicate<PersonBean>() {
				    public boolean test(PersonBean p) {
				        return p.getAge() > 17;
				    }
				});
			}
		});

		Button addButton = new Button("Add row");
		addButton.setMinSize(100, 10);
		addButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Add me!");
				filteredData.setPredicate(showAll());
				addRow();
			}
		});

		Button removeButton = new Button("Delete row");
		removeButton.setMinSize(100, 10);
		removeButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("delete me!");
				List items = new ArrayList(table.getSelectionModel().getSelectedItems());
				personData.removeAll(items);
				table.getSelectionModel().clearSelection();
			}
		});
		// Create a root node. In this case, a flow layout
		// is used, but several alternatives exist.
		
		layout = new BorderPane(); 
		
		// create Hbox for all buttons.
		HBox buttons = new HBox();


		
		buttons.getChildren().addAll( allButton, maleButton, femaleButton, over18Button, addButton,
				removeButton);
		
		layout.setCenter(table);
		layout.setBottom(buttons);
		buildtop();
		buildRight();
		buildLeft();
	
		// Create a scene.
		Scene myScene = new Scene(layout, 600, 600);

		// Set the scene on the stage.
		myStage.setScene(myScene);

		// Show the stage and its scene.
		myStage.show();
	}

	/** Description of loadData())
	 * 
	 * @param a			Ladda data från fil mydata
	 * @param b			använder class PersonBean
	 * @return			Description of c
	 */

	public void loadData() {
		try {
			System.out.println(Paths.get("mydata.txt").toAbsolutePath());
List<String> lines = Files.readAllLines(Paths.get("mydata.txt"), StandardCharsets.UTF_8);		

String data[] = new String[6];
for (String line : lines) {
	data = line.split(",");
	if(!line.isEmpty()){
		PersonBean p = new PersonBean(data[0], data[1], data[2], data[3], new Integer(data[4]));
		personData.add(p);
	}
}
				
			System.out.println("Loaded " + lines.size() + " rows of data.");
		} catch (Exception e) {
			System.out.println("There was a problem loading the file:" + e.getMessage());
			e.printStackTrace();
			
		}
		
	}
	
	/** Description of WriteIt())
	 * Skriver datat från tableview till en txt fil.  
	 * 		
	 */


	private void writeIt() {
		try {
			PrintWriter writer = new PrintWriter("mydata.txt", "UTF-8");
			Iterator<PersonBean> it = personData.iterator();
			while (it.hasNext()) {
				PersonBean p = it.next();
				writer.println(p.getFirstName() + "," + p.getLastName() + ","  + p.getEmail() + "," + p.getGender() + "," + p.getAge());
			}
			writer.close();
		} catch (Exception e) {
			System.out.println("There was a problem saving the file:" + e.getMessage());
		}
	}


	private void addRow() {
		System.out.println("new row.");
		personData.add(new PersonBean());
	}

	public static Predicate<PersonBean> showAll() {
		return p -> true;
	}
	
	
	public static Predicate<PersonBean> isMale() {
		return p -> p.getGender().equalsIgnoreCase("Male");
	}

	public static Predicate<PersonBean> isFemale() {
		return p -> p.getGender().equalsIgnoreCase("Female");
	}

	public static Predicate<PersonBean> over18() {
		return p -> p.getAge() > 17;
	}

	// Override the stop() method.
	public void stop() {
		System.out.println("Inside the stop() method.");
		writeIt();
	}
	
	
private void buildtop(){
		
		BorderPane top = new BorderPane();
		top.setStyle("-fx-background-color: red");
		
		Label divider = new Label();

		divider.setPrefHeight(75);
		top.setBottom(divider);
		layout.setTop(top);	
}
	
private void buildLeft() {
	BorderPane left = new BorderPane();
	left.setStyle("-fx-background-color: red");

	Label divider = new Label();
	divider.setPrefWidth(75);
	left.setRight(divider);

	layout.setLeft(left);

}
private void buildRight() {
	BorderPane right = new BorderPane();
	right.setStyle("-fx-background-color: red");

	Label divider = new Label();
	divider.setPrefWidth(75);
	right.setLeft(divider);
	layout.setRight(right);

}
	
	
	
}