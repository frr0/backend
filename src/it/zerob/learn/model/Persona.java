package it.zerob.learn.model;

import java.sql.Date;

public class Persona {

    private Integer idRec;
    private String nome;
    private String cognome;
    private Date dataDiNascita;
    private Integer idNazioneNascitaFK;
    private Integer idCittaNascitaFK;
    private Integer peso;
    private Integer altezza;

    public Integer getIdRec() {
        return idRec;
    }

    public void setIdRec(Integer idRec) {
        this.idRec = idRec;
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

    public Date getDataDiNascita() {
        return dataDiNascita;
    }

    public void setDataDiNascita(Date dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    public Integer getIdNazioneNascitaFK() {
        return idNazioneNascitaFK;
    }

    public void setIdNazioneNascitaFK(Integer idNazioneNascitaFK) {
        this.idNazioneNascitaFK = idNazioneNascitaFK;
    }

    public Integer getIdCittaNascitaFK() {
        return idCittaNascitaFK;
    }

    public void setIdCittaNascitaFK(Integer idCittaNascitaFK) {
        this.idCittaNascitaFK = idCittaNascitaFK;
    }

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    public Integer getAltezza() {
        return altezza;
    }

    public void setAltezza(Integer altezza) {
        this.altezza = altezza;
    }

}
