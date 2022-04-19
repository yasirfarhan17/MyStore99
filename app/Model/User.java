package com.noor.mystore15.Model;

public class User {

    private String password,name,phone,add;

    public User() {
    }

    public User( String password, String name, String phone,String add) {

        this.password = password;
        this.name = name;
        this.phone = phone;
        this.add=add;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
