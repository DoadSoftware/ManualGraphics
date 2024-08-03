<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>

  <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
  <title>Initialise Screen</title>

  <%-- <script type="text/javascript" src="<c:url value="/webjars/jquery/3.6.0/jquery.min.js"/>"></script> --%>
  <script type="text/javascript" src="<c:url value="/webjars/jquery/1.9.1/jquery.min.js"/>"></script>
  <%-- <script type="text/javascript" src="<c:url value="/webjars/bootstrap/5.1.3/js/bootstrap.min.js"/>"></script> --%>
  <script type="text/javascript" src="<c:url value="/resources/javascript/index.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/webjars/bootstrap/3.3.6/js/bootstrap.min.js"/>"></script>
  
  <link rel="stylesheet" href="<c:url value="/webjars/bootstrap/3.3.6/css/bootstrap.min.css"/>"/>
  <%-- <link rel="stylesheet" href="<c:url value="/webjars/bootstrap/5.1.3/css/bootstrap.min.css"/>"/>   --%>
  <link href="<c:url value="/webjars/font-awesome/6.0.0/css/all.css"/>" rel="stylesheet">
		
</head>
<body onload="initialisePage('INITIALISE')">
<div style="display:flex;justify-content: center; align-items:center;">
<img class="pull-right img-responsive" src="<c:url value="/resources/Images/Idents.jpg"/>" alt="DOAD Logo">
</div>
<form:form name="initialise_form" autocomplete="off" action="manual" method="POST" enctype="multipart/form-data">
<div class="content py-5" style="background-color: #A7E3E8; color: #2E008B; width: 100vw; height: 100vh;">
	<div>
		<div class="container h-100 d-flex align-items-center justify-content-center">
	<div class="row">
	<!-- <div class="card-header">
      <h1 class="mb-0" style = "align-items: center; font-size: 100px">MANUAL</h1>
    </div> -->
	 <div class="col-md-8 offset-md-2" style="margin-top:250px; margin-left:150px; background-color: #CDE5E7; border-radius: 20px; box-shadow: 10px 5px 50px #9AA2A2">
       <span class="anchor"></span>
         <div class="card card-outline-secondary">
           <div class="card-header">
             <h3 class="mb-0">Initialise</h3>
           </div>
          <div class="card-body">
			    <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="vizIPAddressEverest"  style="font-size: 18px; text-shadow: 2px 5px 6px #BBA2B6" class="col-sm-4 col-form-label text-left">IP Address Everest 
			    	<i class="fas fa-asterisk fa-sm text-danger"></i></label>
			    <div class="col-sm-6 col-md-6">
		             <input type="text" id="vizIPAddressEverest" name="vizIPAddressEverest" value="${session_Configurations.ipAddressEverest}"
		             		class="form-control form-control-sm floatlabel" value="localhost"></input>
		              <label id="vizIPAddressEverest-validation" style="color:red; display: none;"></label> 
			    </div>
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="vizIPAddressScenes"  style="font-size: 18px; text-shadow: 2px 5px 6px #BBA2B6" class="col-sm-4 col-form-label text-left">IP Address Scenes
			    	<i class="fas fa-asterisk fa-sm text-danger"></i></label>
			    <div class="col-sm-6 col-md-6">
		             <input type="text" id="vizIPAddressScenes" name="vizIPAddressScenes" value="${session_Configurations.ipAddressScenes}"
		             		class="form-control form-control-sm floatlabel" value="localhost"></input>
		              <label id="vizIPAddressScenes-validation" style="color:red; display: none;"></label> 
			    </div>
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="vizPortNumber" style="font-size: 18px; text-shadow: 2px 5px 6px #BBA2B6" class="col-sm-4 col-form-label text-left">Port Number 
			    	<i class="fas fa-asterisk fa-sm text-danger"></i></label>
			    <div class="col-sm-6 col-md-6">
		             <input type="text" id="vizPortNumber" name="vizPortNumber" 
		             	class="form-control form-control-sm floatlabel" value="1980"></input>
		              <label id="vizPortNumber-validation" style="color:red; display: none;"></label> 
			    </div>
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="select_sports"  style="font-size: 18px; text-shadow: 2px 5px 6px #BBA2B6" class="col-sm-4 col-form-label text-left">Select Sports </label>
			    <div class="col-sm-6 col-md-6">
			      <select id="select_sports" name="select_sports" class="browser-default custom-select custom-select-sm"
			      		onchange="processUserSelection(this)">
			      		<option value="CRICKET">CRICKET</option>
			      		<option value="FOOTBALL">FOOTBALL</option>
			          	<option value="BADMINTON">BADMINTON</option>
			      </select>
			    </div>
			  </div>
			<!-- <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="vizScene" class="col-sm-4 col-form-label text-left">Viz Scene
			    	<i class="fas fa-asterisk fa-sm text-danger" style="font-size: 7px;"></i></label>
			    <div class="col-sm-6 col-md-6">
		             <input type="text" id="vizScene" name="vizScene" 
		             	class="form-control form-control-sm floatlabel"></input>
		              <label id="vizScene-validation" style="color:red; display: none;"></label> 
			    </div>
			  </div> -->
		    <button style="background-color:#2E008B;color:#FEFEFE; width: 100px; align-items: center; justify-content: center;" class="btn btn-sm" type="button"
		  		name="finish_btn" id="finish_btn" onclick="processUserSelection(this)">
		  		<i class="fas fa-film"></i> SUBMIT </button>
	       </div>
	    </div>
       </div>
    </div>
  </div>
	</div>
</div>
</form:form>
</body>
</html>