package it.zerob.learn.servlet;

import it.zerob.learn.model.Giocatore;
import it.zerob.learn.dao.GiocatoriDAO;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/giocatori")
public class GiocatoriRestServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Giocatore> giocatori = new ArrayList<>();
        GiocatoriDAO dao = new GiocatoriDAO();
        try {
            giocatori = dao.getAllGiocatoriSortByidRecDesc();
            String json = gson.toJson(giocatori);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(json);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            // Read JSON from request body
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = req.getReader().readLine()) != null) {
                sb.append(line);
            }
            String jsonData = sb.toString();

            // Parse JSON to Giocatore object
            Giocatore giocatore = gson.fromJson(jsonData, Giocatore.class);

            GiocatoriDAO dao = new GiocatoriDAO();
            Giocatore savedGiocatore;

            // Check if it's an update (has ID_REC) or insert (no ID_REC)
            if (giocatore.getIdRec() != null && giocatore.getIdRec() > 0) {
                // Update existing player
                savedGiocatore = dao.updateGiocatore(giocatore);
                System.out.println("DEBUG: Giocatore aggiornato - ID: " + giocatore.getIdRec());
            } else {
                // Insert new player
                savedGiocatore = dao.insertGiocatore(giocatore);
                System.out.println("DEBUG: Nuovo giocatore inserito");
            }

            // Return the saved player as JSON
            String jsonResponse = gson.toJson(savedGiocatore);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}