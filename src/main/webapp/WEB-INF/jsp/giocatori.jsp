<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp">
    <jsp:param name="pageTitle" value="Giocatori"/>
    <jsp:param name="activePage" value="giocatori"/>
</jsp:include>

<div id="main">
    <div id="mainHeader">
        <h1>Gestione Giocatori</h1>
        <div>
            <label for="searchFieldGiocatori">Cerca</label><input type="text" id="searchFieldGiocatori" />
            <input type="button" value="Cerca" class="btn" onclick="ricercaGiocatori()" />
            <input type="button" value="Aggiorna" class="btn" onclick="caricaGiocatori()" />
        </div>
    </div>
    <table id="tableGiocatori">
        <thead>
        <tr>
            <th data-index="ID_REC">ID_REC</th>
            <th data-index="NOME">Nome</th>
            <th data-index="COGNOME">Cognome</th>
            <th data-index="DATA_DI_NASCITA">Data Nascita</th>
            <th data-index="ALIAS">Alias</th>
            <th data-index="NUMERO_MAGLIA_ABITUALE">N° Maglia</th>
            <th data-index="RUOLO_ABITUALE">Ruolo Abituale</th>
            <th data-index="NAZIONE_NASCITA">Nazione Nascita</th>
            <th data-index="CITTA_NASCITA">Città Nascita</th>
            <th></th>
        </tr>
        </thead>
        <tbody id="data">
        <c:forEach items="${giocatori}" var="giocatoreIterato">
            <tr>
                <td data-index="ID_REC"><c:out value="${giocatoreIterato.idRec}"/></td>
                <td data-index="NOME"><c:out value="${giocatoreIterato.nome}"/></td>
                <td data-index="COGNOME"><c:out value="${giocatoreIterato.cognome}"/></td>
                <td data-index="DATA_DI_NASCITA"><c:out value="${giocatoreIterato.dataNascita}"/></td>
                <td data-index="ALIAS"><c:out value="${giocatoreIterato.alias}"/></td>
                <td data-index="NUMERO_MAGLIA_ABITUALE"><c:out value="${giocatoreIterato.numeroMagliaAbituale}"/></td>
                <td data-index="RUOLO_ABITUALE"><c:out value="${giocatoreIterato.ruoloAbituale}"/></td>
                <td data-index="NAZIONE_NASCITA"><c:out value="${giocatoreIterato.nazioneNascita}"/></td>
                <td data-index="CITTA_NASCITA"><c:out value="${giocatoreIterato.cittaNascita}"/></td>
                <td><a style="font-size:16px;color:black;" class="fa fa-trash"></a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<div id="sidebar">
    <h3>Form Giocatore</h3>
    <form action="/Backend/salvaGiocatore" method="POST" id="formGiocatori">
        <input type="hidden" name="operation" value="insertOrUpdate" />
        <label for="ID_REC">ID</label>
        <input type="text" id="ID_REC" name="ID_REC" readonly />
        <label for="NOME">Nome</label>
        <input type="text" id="NOME" name="NOME" />
        <label for="COGNOME">Cognome</label>
        <input type="text" id="COGNOME" name="COGNOME" />
        <label for="DATA_DI_NASCITA">Data Nascita:</label>
        <input type="date" id="DATA_DI_NASCITA" name="DATA_DI_NASCITA" />
        <label for="ALIAS">Alias:</label>
        <input type="text" id="ALIAS" name="ALIAS" />
        <label FOR="NUMERO_MAGLIA_ABITUALE">N° Maglia:</label>
        <input type="number" id="NUMERO_MAGLIA_ABITUALE" name="NUMERO_MAGLIA_ABITUALE" step="1">
        <label for="RUOLO_ABITUALE">Ruolo Abituale:</label>
        <input type="text" id="RUOLO_ABITUALE" name="RUOLO_ABITUALE" />
        <label for="NAZIONE_NASCITA">Nazione Nascita:</label>
        <input type="text" id="NAZIONE_NASCITA" name="NAZIONE_NASCITA" />
        <label for="CITTA_NASCITA">Città Nascita:</label>
        <input type="text" id="CITTA_NASCITA" name="CITTA_NASCITA" />

        <input type="submit" value="Salva" class="btn" />
        <input type="reset" value="Cancella" class="btn" />
    </form>
</div>

<jsp:include page="footer.jsp"/>