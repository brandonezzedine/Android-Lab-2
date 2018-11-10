package com.example.brandonezz.androiduserinterfaceandlogin_brandon_ezzedine;


import java.io.Serializable;

public class User implements Serializable {

        private String name;
        private String username;
        private String birthdate;
        private String phone;
        private String email;
        private String password;


        public User(String name, String username, String birthdate, String phone, String email) {

            this.name = name;
            this.username = username;
            this.birthdate = birthdate;
            this.phone = phone;
            this.email  = email;
            this.password = password;
        }



        public String getName() {
            return name;
        }

        public void setName(String name) {
        this.name = name;
    }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
        this.username = username;
    }

        public String getBirthdate() {
            return birthdate;
        }

        public String getPhone() { return phone; }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
        this.email = email;
    }

        public String getPassword() {
        return password;



            }
    }
