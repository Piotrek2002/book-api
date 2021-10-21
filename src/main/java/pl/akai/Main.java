package pl.akai;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    /*
        Twoim zadaniem jest napisanie prostego programu do pobierania i transformowania danych
        udostępnianych przez API. Dokumentacje API możesz znależć pod poniższym linkiem:
        https://akai-recruitment.herokuapp.com/documentation.html

        Całe API zawiera jeden endpoint: https://akai-recruitment.herokuapp.com/book
        Endpoint ten zwraca liste książek zawierajacch informację takie jak:
        - id
        - tytuł
        - autor
        - ocena

        Twoim zadaniem jest:
        1. Stworzenie odpowiedniej klasy do przechowywania informacji o książce
        2. Sparsowanie danych udostępnianych przez endpoint. Aby ułatwić to zadanie,
           do projektu są dołaczone 3 najpopularniejsze biblioteki do parsowania JSONów
           do obiektów Javy - Gson, Org.Json, Jackson. Możesz wykorzystać dowolną z nich
        3. Po sparsowaniu JSONu do obiektów Javy, uzupełnij program o funkcję wypisującą 3 autorów z
           najwyższą średnią ocen (wypisz także średnie ocen)

       Projekt został utworzony przy użyciu najnowszej Javy 17,
       jednakże nic nie stoi na przeszkodzie użycia innej wersji jeśli chcesz
     */

    public static void main(String[] args) {
        List<Book> books = getBooksFromJson("https://akai-recruitment.herokuapp.com/book");
        books = sortBookByAuthor(books);
        List<Stat> stats =makeStats(books);


        printThreeAuthorWithHighestRating(stats);

    }

    public static List<Book> getBooksFromJson(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://akai-recruitment.herokuapp.com/book"))
                .build();


        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        Type bookListType = new TypeToken<List<Book>>() {
        }.getType();
        Gson g = new Gson();
        List<Book> books;

        return g.fromJson(response.body(), bookListType);
    }

    public static List<Book> sortBookByAuthor(List<Book> books) {
        return books.stream()
                .sorted(Comparator.comparing(Book::getAuthor))
                .collect(Collectors.toList());
    }
    public static List<Stat> makeStats(List<Book> books){
        List<Stat> stats = new ArrayList<>();
        String author = books.get(0).getAuthor();
        double count = 0;
        double sum = 0;
        for (Book book : books) {
            if (book.getAuthor().equals(author)) {
                count++;
                sum += book.getRating();
            } else {
                stats.add(new Stat(author, sum / count));
                count = 1;
                sum = book.getRating();
                author = book.getAuthor();
            }
        }
        stats.add(new Stat(author,sum/count));
        return stats;
    }
    public static void printThreeAuthorWithHighestRating(List<Stat> stats){
        Optional<Stat> stat1 = stats.stream().max(Comparator.comparing(Stat::getRating));
        stats.remove(stat1.get());
        System.out.println(stat1.get());
        Optional<Stat> stat2 = stats.stream().max(Comparator.comparing(Stat::getRating));
        stats.remove(stat2.get());
        System.out.println(stat2.get());
        Optional<Stat> stat3 = stats.stream().max(Comparator.comparing(Stat::getRating));

        double maxRating=stat3.get().getRating();
        while (maxRating==stat3.get().getRating()){
            System.out.println(stat3.get());
            stats.remove(stat3.get());
            stat3=stats.stream().max(Comparator.comparing(Stat::getRating));

        }
    }

}
