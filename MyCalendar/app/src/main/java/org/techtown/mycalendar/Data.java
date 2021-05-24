package org.techtown.mycalendar;

public class Data {
    String todo;
    String date;
    String location;
    String memo;

    public Data(String todo,String date, String location, String memo) {
        this.todo = todo;
        this.date = date;
        this.location = location;
        this.memo = memo;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
