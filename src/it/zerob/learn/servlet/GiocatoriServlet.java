package it.zerob.learn.servlet;

import it.zerob.learn.dao.GiocatoriDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/secure/giocatori.html")
public class GiocatoriServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            System.out.println("DEBUG GiocatoriServlet: Inizio caricamento giocatori");
            GiocatoriDAO giocatoriDAO = new GiocatoriDAO();

            List giocatori = giocatoriDAO.getAllGiocatoriSortByidRecDesc();
            System.out.println("DEBUG GiocatoriServlet: Caricati " + giocatori.size() + " giocatori");

            request.setAttribute("giocatori", giocatori);
        } catch (Exception e) {
            System.err.println("ERRORE GiocatoriServlet: " + e.getMessage());
            e.printStackTrace();
        }

        request.getRequestDispatcher("/WEB-INF/jsp/giocatori.jsp").forward(request, response);
    }
}