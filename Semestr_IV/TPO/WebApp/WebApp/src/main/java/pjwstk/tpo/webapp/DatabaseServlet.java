package pjwstk.tpo.webapp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pjwstk.tpo.DTOs.BookDTO;
import pjwstk.tpo.DbConnection.DbConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Books", value = "/books")
public class DatabaseServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().println("""
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <title>Books</title>
                            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
                        </head>
                        <body>
                            <div class="container">
                                <div class="p-2 bg-light border">
                                    <h2>Books</h2>
                                            
                                    <form action="/WebApp_war_exploded/books">
                                      <span class="input-group-text" id="basic-addon1">Search book</span>
                                      <input type="text" id="title" name="title" class="form-control" placeholder="Book title" aria-label="Username" aria-describedby="basic-addon1">
                                      <input type="submit" class="form-control" aria-label="Username" aria-describedby="basic-addon1" value="Send"">

                                    </form>
                                </div>
                                <table class="table table-striped table-hover table-sm">
                                    <thead>
                                        <tr>
                                            <th scope="col">#</th>
                                            <th scope="col">Title</th>
                                            <th scope="col">Author name</th>
                                            <th scope="col">Author lastname</th>
                                            <th scope="col">Publisher name</th>
                                            <th scope="col">Publisher nip</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                """);

        DbConnection dbConnection = new DbConnection();
        dbConnection.connect("localhost:3306/s25511", "root", "password");


        ResultSet resultSet = dbConnection.executeQuery("SELECT BOOK.TITLE, AUTHOR.NAME, AUTHOR.LASTNAME, PUBLISHER.NAME, PUBLISHER.NIP FROM BOOK\n" +
                "INNER JOIN AUTHOR ON book.Author_IdAuthor = author.IdAuthor\n" +
                "INNER JOIN PUBLISHER ON book.Publisher_IdPublisher = publisher.IdPublisher\n");

        List<BookDTO> books = new ArrayList<>();
        try {
            String title = request.getParameter("title");
            if (title == null) {
                while (resultSet.next()) {
                    books.add(new BookDTO(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5)));
                }
            } else {
                while (resultSet.next()) {
                    String bookTitle = resultSet.getString(1);


                    if (bookTitle.toLowerCase().contains(title.toLowerCase()))
                        books.add(new BookDTO(bookTitle, resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5)));
                }
            }
            int rowCount = 1;
            for (BookDTO bookDTO : books){
                try {
                    response.getWriter().println("<tr>");
                    String[] data = bookDTO.getData();
                    response.getWriter().println("<th scope=\"row\">" + rowCount + ".</th>");
                    response.getWriter().println("<td>" + data[0] + "</th>");
                    response.getWriter().println("<td>" + data[1] + "</th>");
                    response.getWriter().println("<td>" + data[2] + "</th>");
                    response.getWriter().println("<td>" + data[3] + "</th>");
                    response.getWriter().println("<td>" + data[4] + "</th>");
                    response.getWriter().println("</tr>");
                    rowCount++;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            response.getWriter().println("""
                                    </tbody>
                                </table>
                            </div>
                        </body>
                        </html>
                    """);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
