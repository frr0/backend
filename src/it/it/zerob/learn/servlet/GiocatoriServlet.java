package it.zerob.learn.servlet;

import com.google.gson.Gson;
import it.zerob.learn.dao.GiocatoriDAO;
import it.zerob.learn.model.Giocatore;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GiocatoriServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Giocatore> giocatori = new ArrayList<>();
        GiocatoriDAO dao = new GiocatoriDAO();

        try {
            giocatori = dao.getAllGiocatoriSortByidRecDesc();

            // Set the giocatori list as a request attribute for JSP
            req.setAttribute("giocatori", giocatori);

            // Forward to the JSP page
            req.getRequestDispatcher("/WEB-INF/jsp/giocatori.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading players");
        }
    }
}