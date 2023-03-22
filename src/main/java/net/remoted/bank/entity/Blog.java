package net.remoted.bank.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;


@Entity
public class Blog {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 255)
    private String title;

    @Column(length = 1000)
    private String contents;

    @Column(length = 1000)
    private String url;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }


    // constructors, getters, and setters
}