package pl.akai;

public class Stat {
    private String author;
    private double rating;


    public Stat(String author, double rating) {
        this.author = author;
        this.rating = rating;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return
                "author='" + author + '\'' +
                ", rating=" + rating;

    }
}
