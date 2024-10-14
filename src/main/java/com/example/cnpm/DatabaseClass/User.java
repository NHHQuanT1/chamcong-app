package com.example.cnpm.DatabaseClass;

public class User {
    private String id;
    private String name;
    private String email;
    private String department;
    private String phone;
    private String role;


    public User(String id, String name, String email, String department, String phone, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.department = department;

    }
    public String getId() {
        return this.id;
    }
    public String getName() {
        return name;
    }
    public String getDepartment() {
        return department;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }
    public String getRole() {
        return role;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhone(String phone) { this.phone = phone; }
}