<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>ZeroBSport - ${param.pageTitle}</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/stile.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" />

    <script>
        function deleteRow(icon) {
            let row = icon.parentNode.parentNode;
            row.parentNode.removeChild(row);
        }
    </script>
</head>

<body>
<div class="wrapper">

    <div id="top">
        <img id="logo" src="${pageContext.request.contextPath}/pallone.png" alt="logo">
        <h1>ZeroBSport</h1>
        <p>Analytic Sports Data - Back Office</p>
    </div>
    <div class="wrapper">
        <div id="menubar">
            <ul id="menulist">
                <li class="menuitem ${param.activePage == 'giocatori' ? 'active' : ''}"><a href="${pageContext.request.contextPath}/secure/giocatori.html"><i class="fa fa-male"></i>Giocatori</a></li>
                <li class="menuitem ${param.activePage == 'squadre' ? 'active' : ''}"><i class="fa fa-users"></i>Squadre</li>
                <li class="menuitem ${param.activePage == 'nazioni' ? 'active' : ''}"><i class="fa fa-globe"></i>Nazioni</li>
                <li class="menuitem ${param.activePage == 'competizioni' ? 'active' : ''}"><i class="fa fa-trophy"></i>Competizioni</li>
                <li class="menuitem ${param.activePage == 'tecnici' ? 'active' : ''}"><i class="fa fa-user-secret"></i>Tecnici</li>
            </ul>
        </div>
