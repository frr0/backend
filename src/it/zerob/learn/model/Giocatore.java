package it.zerob.learn.model;

import java.sql.Date;

public class Giocatore {

    private Integer idRec;
    private Integer idPersonaFK;
    private Date dal;
    private Date al;
    private String alias;
    private Integer idRuoloAbitualeFK;
    private Integer numeroMagliaAbituale;

    private String ruoloAbituale;
    private String nome;
    private String cognome;
    private String dataNascita;
    private String nazioneNascita;
    private String cittaNascita;

    public String getRuoloAbituale() {
        return ruoloAbituale;
    }

    public void setRuoloAbituale(String ruoloAbituale) {
        this.ruoloAbituale = ruoloAbituale;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getNazioneNascita() {
        return nazioneNascita;
    }

    public void setNazioneNascita(String nazioneNascita) {
        this.nazioneNascita = nazioneNascita;
    }

    public String getCittaNascita() {
        return cittaNascita;
    }

    public void setCittaNascita(String cittaNascita) {
        this.cittaNascita = cittaNascita;
    }

    public Integer getIdRec() {
        return idRec;
    }

    public void setIdRec(Integer idRec) {
        this.idRec = idRec;
    }

    public Integer getIdPersonaFK() {
        return idPersonaFK;
    }

    public void setIdPersonaFK(Integer idPersonaFK) {
        this.idPersonaFK = idPersonaFK;
    }

    public Date getDal() {
        return dal;
    }

    public void setDal(Date dal) {
        this.dal = dal;
    }

    public Date getAl() {
        return al;
    }

    public void setAl(Date al) {
        this.al = al;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getIdRuoloAbitualeFK() {
        return idRuoloAbitualeFK;
    }

    public void setIdRuoloAbitualeFK(Integer idRuoloAbitualeFK) {
        this.idRuoloAbitualeFK = idRuoloAbitualeFK;
    }

    public Integer getNumeroMagliaAbituale() {
        return numeroMagliaAbituale;
    }

    public void setNumeroMagliaAbituale(Integer numeroMagliaAbituale) {
        this.numeroMagliaAbituale = numeroMagliaAbituale;
    }
}
