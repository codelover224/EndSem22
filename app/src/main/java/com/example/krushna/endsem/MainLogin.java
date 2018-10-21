package com.example.krushna.endsem;

class MainLogin {

    String mob_no,name,veh_no,type;
    LocationCoordinates lc;

    public MainLogin(){}

    public MainLogin(String mob_no, String name, String veh_no, String type, LocationCoordinates lc) {
        this.mob_no = mob_no;
        this.name = name;
        this.veh_no = veh_no;
        this.type = type;
        this.lc = lc;
    }

    public String getMob_no() {
        return mob_no;
    }

    public void setMob_no(String mob_no) {
        this.mob_no = mob_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVeh_no() {
        return veh_no;
    }

    public void setVeh_no(String veh_no) {
        this.veh_no = veh_no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocationCoordinates getLc() {
        return lc;
    }

    public void setLc(LocationCoordinates lc) {
        this.lc = lc;
    }
}
