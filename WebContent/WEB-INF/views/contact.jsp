<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
    <title>Contact Support</title>
    
    <link rel="stylesheet" href="<c:url value='/webjars/bootstrap/5.1.3/css/bootstrap.min.css'/>"/>
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background: #F3F4F6;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            color: #333;
        }

        .container-box {
            background: white;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
            width: 400px;
            text-align: center;
        }

        h2 {
            font-size: 22px;
            margin-bottom: 15px;
            color: #2563EB;
        }

        p {
            font-size: 14px;
            line-height: 1.6;
        }

        .btn-back {
            margin-top: 15px;
            display: inline-block;
            padding: 10px 15px;
            background: #2563EB;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            font-size: 14px;
        }

        .btn-back:hover {
            background: #1E40AF;
        }
    </style>
</head>
<body>

<div class="container-box">
    <h2>Contact Support</h2>
    <p>If you need assistance, please contact our support team:</p>
    <p><strong>Email:</strong> support@example.com</p>
    <p><strong>Phone:</strong> +1 (800) 123-4567</p>
    <p><strong>Working Hours:</strong> Monday - Friday, 9 AM - 6 PM</p>

    <a href="initialise" class="btn-back">Back to Initialise</a>
</div>

</body>
</html>
