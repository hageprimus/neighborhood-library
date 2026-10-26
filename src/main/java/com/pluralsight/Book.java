package com.pluralsight;

public class Book {
    private int id;
    private String isbn;
    private String title;
    private String checkedOutTo;
    public boolean isCheckedOut;

    public Book(int id, String isbn, String title) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.isCheckedOut = false;
        this.checkedOutTo = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCheckedOutTo() {
        return checkedOutTo;
    }

    public void checkOut(String name) {
        isCheckedOut = true;
        checkedOutTo = name;
    }

    public void checkIn() {
        isCheckedOut = false;
        checkedOutTo = "";
    }

    public String retrieveInfo() {
        String format = "Title: %s\nISBN: %s\nID: %d";
        return String.format(format, title, isbn, id) + (isCheckedOut ? "\nChecked out to: " + checkedOutTo : "");
    }
}
