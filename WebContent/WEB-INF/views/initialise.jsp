<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
    <title>Login Page</title>

    <!-- Bootstrap & Font Awesome -->
    <script type="text/javascript" src="<c:url value='/webjars/jquery/3.6.0/jquery.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js'/>"></script>
    <link rel="stylesheet" href="<c:url value='/webjars/bootstrap/5.1.3/css/bootstrap.min.css'/>"/>
    <link href="<c:url value='/webjars/font-awesome/6.0.0/css/all.css'/>" rel="stylesheet">

    <style>
        body {
            font-family: 'Poppins', sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background: #f3f4f6;
        }
        .container-box {
            display: flex;
            width: 90%;
            height: auto;
            max-width: 1200px;
            background: white;
            border-radius: 8px;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            flex-wrap: wrap;
        }
        .left-section {
            flex: 1;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            background: linear-gradient(135deg, #2563EB, #1E40AF);
            color: white;
            text-align: center;
            padding: 40px;
        }
        .left-section h2 {
            font-size: 5vw;
            margin-top: 20px;
        }
        .right-section {
            flex: 1;
            padding: 40px;
            text-align: center;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .container-box_Right {
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
            width: 90%;
            max-width: 450px;
            text-align: center;
        }
        .form-group {
            text-align: left;
            margin-bottom: 18px;
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
            background-color: #2563EB;
            color: white;
            font-size: 18px;
            font-weight: bold;
            padding: 12px;
            border-radius: 6px;
            border: none;
            cursor: pointer;
            transition: 0.3s;
        }
        .btn-submit:hover {
            background: #1E40AF;
        }
        .footer {
            margin-top: 15px;
            font-size: 14px;
            color: #555;
        }
        .logo {
            max-width: 100%;
            height: auto;
        }
        @media (max-width: 768px) {
            .container-box {
                flex-direction: column;
                width: 95%;
            }
            .left-section {
                width: 100%;
                padding: 20px;
            }
            .right-section {
                width: 100%;
                padding: 20px;
            }
            .left-section h2 {
                font-size: 6vw;
            }
            .container-box_Right {
                width: 100%;
                padding: 20px;
            }
        }
    </style>
</head>
<body>
<div class="container-box">
    <div class="left-section">
        <p class="font">Welcome Back</p>
        <p>Nice to see you again!</p>
    </div>
    <div class="right-section">
        <div class="container-box_Right">
            <img src="<c:url value='/resources/Images/Idents.jpg'/>" alt="DOAD Logo" class="logo">
            <h2>Initialise Settings</h2>
            <form:form name="initialise_form" autocomplete="off" action="manual" method="POST" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="vizIPAddressEverest">IP Address Everest</label>
                    <input type="text" id="vizIPAddressEverest" name="vizIPAddressEverest" class="form-control" value="${session_Configurations.ipAddressEverest}" required placeholder="Enter Everest IP">
                </div>
                <div class="form-group">
                    <label for="vizIPAddressScenes">IP Address Scenes</label>
                    <input type="text" id="vizIPAddressScenes" name="vizIPAddressScenes" class="form-control" value="${session_Configurations.ipAddressScenes}" required placeholder="Enter Scenes IP">
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
                <button type="submit" class="btn-submit">
                    <i class="fas fa-check"></i> SUBMIT
                </button>
            </form:form>
            <div class="footer">
                <p>Need help? <a href="<c:url value='/contact' />" style="color: #2563EB; text-decoration: underline; font-size: 16px;">Contact Support</a></p>
            </div>
        </div>
    </div>
</div>
</body>
</html>
