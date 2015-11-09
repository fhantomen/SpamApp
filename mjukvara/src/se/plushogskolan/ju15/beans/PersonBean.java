package se.plushogskolan.ju15.beans;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Description...
 *
 * 
 * @version 1.0 2015-11-02
 */
public class PersonBean {
	
	private StringProperty firstName = new SimpleStringProperty() ;
	private StringProperty lastName = new SimpleStringProperty() ;
	private StringProperty email = new SimpleStringProperty() ;
	private StringProperty gender = new SimpleStringProperty() ;
	private IntegerProperty age = new SimpleIntegerProperty() ;
	
	public PersonBean () {
	
	}
	
	public PersonBean(String firstName,String lastName,String email, String gender, Integer age) {
		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
		setGender(gender);
		setAge(age);
	}
	
	public final StringProperty firstNameProperty() {
        return this.firstName;
    }

    public final String getFirstName() {
        return this.firstNameProperty().get();
    }

    public final void setFirstName(final String name) {
        this.firstNameProperty().set(name);
    }

    
	
	public final StringProperty lastNameProperty() {
        return this.lastName;
    }

    public final String getLastName() {
        return this.lastNameProperty().get();
    }

    public final void setLastName(final String name) {
        this.lastNameProperty().set(name);
    }
	
	public final StringProperty emailProperty() {
        return this.email;
    }

    public final String getEmail() {
        return this.emailProperty().get();
    }

    public final void setEmail(final String email) {
        this.emailProperty().set(email);
    }
	
	public final StringProperty genderProperty() {
        return this.gender;
    }

    public final String getGender() {
        return this.genderProperty().get();
    }

    public final void setGender(final String gender) {
		this.genderProperty().set(gender);
    }
	
	public final IntegerProperty ageProperty() {
        return this.age;
    }

    public final Integer getAge() {
        return this.ageProperty().get();
    }

    public final void setAge(final Integer age) {
    	this.ageProperty().set(age);
    }

 }