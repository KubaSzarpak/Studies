package pjwstk.tpo.DTOs;


public class BookDTO {
    private String title;
    private String authorName;
    private String authorLastname;
    private String publisherName;
    private String publisherNip;

    public BookDTO(String title, String authorName, String authorLastname, String publisherName, String publisherNip) {
        this.title = title;
        this.authorName = authorName;
        this.authorLastname = authorLastname;
        this.publisherName = publisherName;
        this.publisherNip = publisherNip;
    }

    @Override
    public String toString() {
        return title + "\t" + authorName + "\t" + authorLastname + "\t" + publisherName + "\t" + publisherNip;
    }

    public String[] getData(){
        String[] data = {title, authorName, authorLastname, publisherName, publisherNip};
        return data;
    }
}