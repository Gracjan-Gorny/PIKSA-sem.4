package com.example.demo;

public class Document {
    private int id;
    private String title;
    private int year;
    private String author;
    private String category;
    private String content;

    public Document() {
    }

    public Document(int id, String title, int year, String author, String category, String content) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.author = author;
        this.category = category;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
