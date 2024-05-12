package jsonClasses;

import com.google.gson.annotations.SerializedName;

public class Person {
    @SerializedName("firstname")
    private String firstName;
    @SerializedName("lastname")
    private String lastName;
    @SerializedName("city")
    private String city;
    @SerializedName("country")
    private String country;
    @SerializedName("countryCode")
    private String countryCode;
    private int salary = 0;

    public Person(String firstName, String lastName, String city, String country, String countryCode, int salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.country = country;
        this.countryCode = countryCode;
        this.salary = salary;
    }

    public int getSalary() {
        return salary;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
}
