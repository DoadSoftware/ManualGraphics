<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>

    <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
    <title>Initialise Screen</title>

    <!-- Bootstrap & Font Awesome -->
    <script type="text/javascript" src="<c:url value='/webjars/jquery/3.6.0/jquery.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/resources/javascript/index.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js'/>"></script>
  
    <link rel="stylesheet" href="<c:url value='/webjars/bootstrap/5.1.3/css/bootstrap.min.css'/>"/>
    <link href="<c:url value='/webjars/font-awesome/6.0.0/css/all.css'/>" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">

    <style>
    /* Apply Poppins Font */
    body {
        font-family: OCR A Std, monospace;
        background: #F3F4F6; /* Soft Light Gray */
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        margin: 0;
        color: #333; /* Dark Gray */
        font-size: 30px;
    }

    /* Form Container */
    .container-box {
        background: white;
        padding: 30px;
        border-radius: 8px;
        box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
        width: 450px;
        text-align: center;
    }

    .logo {
        width: 220px; /* Increased Logo Size Even More */
        margin-bottom: 20px;
    }

    h2 {
        font-size: 24px;
        margin-bottom: 20px;
        color: #2563EB; /* Deep Blue */
    }

    .form-group {
        text-align: left;
        margin-bottom: 18px;
    }

    .form-group label {
        font-weight: 600;
        font-size: 16px;
        color: #555;
    }

    .form-control {
        width: 100%;
        padding: 12px;
        border-radius: 6px;
        border: 1px solid #ddd;
        font-size: 16px;
    }

    .btn-submit {
        width: 100%;
        background-color: #2563EB; /* Deep Blue */
        color: white;
        font-size: 17px;
        font-weight: bold;
        padding: 12px;
        border-radius: 6px;
        border: none;
        cursor: pointer;
        transition: 0.3s;
        margin-top: 10px;
        position: relative;
        overflow: hidden;
    }

    .btn-submit:hover {
        background: #1E40AF; /* Darker Blue */
    }

    .btn-submit::after {
        content: '\f072'; /* FontAwesome bird icon */
        font-family: 'Font Awesome 5 Free';
        font-weight: 900;
        position: absolute;
        left: -20px;
        top: 50%;
        transform: translateY(-50%);
        opacity: 0;
        transition: left 0.5s ease-in-out, opacity 0.5s;
    }

    .btn-submit:hover::after {
        left: calc(100% + 10px);
        opacity: 1;
    }

    .footer {
        margin-top: 15px;
        font-size: 14px;
        color: #555;
    }
</style>

</head>
<body onload="initialisePage('INITIALISE')">
<!-- Page Content -->
<div class="container-box">
    
    <!-- Logo -->
    <img src="<c:url value='/resources/Images/Idents.jpg'/>" alt="DOAD Logo" class="logo">

    <!-- Page Title -->
    <h2>Initialise Settings</h2>

    <!-- Form -->
    <form:form name="initialise_form" autocomplete="off" action="manual" method="POST" enctype="multipart/form-data">

        <div class="form-group">
            <label for="vizIPAddressEverest">IP Address Everest</label>
            <input type="text" id="vizIPAddressEverest" name="vizIPAddressEverest" class="form-control"
                value="${session_Configurations.ipAddressEverest}" required placeholder="Enter Everest IP">
        </div>

        <div class="form-group">
            <label for="vizIPAddressScenes">IP Address Scenes</label>
            <input type="text" id="vizIPAddressScenes" name="vizIPAddressScenes" class="form-control"
                value="${session_Configurations.ipAddressScenes}" required placeholder="Enter Scenes IP">
        </div>

        <div class="form-group">
            <label for="vizPortNumber">Port Number</label>
            <input type="number" id="vizPortNumber" name="vizPortNumber" class="form-control" value="1980" required>
        </div>

        <div class="form-group">
            <label for="select_sports">Select Sport</label>
            <select id="select_sports" name="select_sports" class="form-control">
                <option value="CRICKET">CRICKET</option>
                <option value="FOOTBALL">FOOTBALL</option>
                <option value="BADMINTON">BADMINTON</option>
                <option value="BASKETBALL">BASKETBALL</option>
            </select>
        </div>

        <!-- Submit Button -->
        <button type="submit" class="btn-submit">
            <i class="fas fa-check"></i> SUBMIT
        </button>

    </form:form>

    <!-- Footer -->
    <div class="footer">
        <p>Need help? <a href="<c:url value='/contact' />" style="color: #2563EB; text-decoration: underline; font-size: 16px;">Contact Support</a></p>
    </div>

</div>

</body>
</html>
