package Model;

import java.util.Date;

public class Employee {
    private Integer employeeId;
    private String firstName;
    private String lastName;
    private String gender;
    private String phoneNum;
    private String position;
    private String image; // For storing image file paths or URLs
    private Date date;
    private Double salary;

    // Full constructor for all fields
    public Employee(Integer employeeId, String firstName, String lastName, String gender, String phoneNum, String position, String image, Date date, Double salary) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.position = position;
        this.image = image;
        this.date = date;
        this.salary = salary;
    }

    // Simplified constructor for specific use cases
    public Employee(Integer employeeId, String firstName, String lastName, String position, Double salary) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.salary = salary;
    }

    // Constructor used in DAO
    public Employee(Integer employeeId, String firstName, String lastName, String gender, String phoneNum, String position, Date date) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.position = position;
        this.date = date;
    }

    // Getters and setters for all fields
    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPhoneNum() { return phoneNum; }
    public void setPhoneNum(String phoneNum) { this.phoneNum = phoneNum; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }
}
