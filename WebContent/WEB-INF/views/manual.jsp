<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
<head>
  <sec:csrfMetaTags/>
  <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
  <title>Manual</title>
  <script type="text/javascript" src="<c:url value="/webjars/jquery/1.9.1/jquery.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/resources/javascript/index.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/webjars/select2/4.0.13/js/select2.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/webjars/bootstrap/3.3.6/js/bootstrap.min.js"/>"></script>
  
  <link rel="stylesheet" href="<c:url value="/webjars/bootstrap/3.3.6/css/bootstrap.min.css"/>"/>
  <link rel="stylesheet" href="<c:url value="/webjars/select2/4.0.13/css/select2.css"/>"/>
  <link href="<c:url value="/webjars/font-awesome/6.0.0/css/all.css"/>" rel="stylesheet">
  <script type="text/javascript">
	  
  
$(document).on("keydown", function(e){
	  
	  if($('#waiting_modal').hasClass('show')) {
		  e.cancelBubble = true;
		  e.stopImmediatePropagation();
    	  e.preventDefault();
		  return false;
	  }
	  
      var evtobj = window.event? event : e;
      
      switch(e.target.tagName.toLowerCase())
      {
      case "input": case "textarea":
    	  //uploadFormDataToSessionObjects('SAVE_FILE',null);
    	 break;
      default:
    	  e.preventDefault();
	      var whichKey = '';
		  var validKeyFound = false;
	    
	      if(evtobj.ctrlKey) {
	    	  whichKey = 'Control';
	      }
	      if(evtobj.altKey) {
	    	  if(whichKey) {
	        	  whichKey = whichKey + '_Alt';
	    	  } else {
	        	  whichKey = 'Alt';
	    	  }
	      }
	      if(evtobj.shiftKey) {
	    	  if(whichKey) {
	        	  whichKey = whichKey + '_Shift';
	    	  } else {
	        	  whichKey = 'Shift';
	    	  }
	      }
	      
		  if(evtobj.keyCode) {
	    	  if(whichKey) {
	    		  if(!whichKey.includes(evtobj.key)) {
	            	  whichKey = whichKey + '_' + evtobj.key;
	    		  }
	    	  } else {
	        	  whichKey = evtobj.key;
	    	  }
		  }
		  validKeyFound = false;
		  if (whichKey.includes('_')) {
			  whichKey.split("_").forEach(function (this_key) {
				  switch (this_key) {
				  case 'Control': case 'Shift': case 'Alt':
					break;
				  default:
					validKeyFound = true;
					break;
				  }
			  });
		   } else {
			  if(whichKey != 'Control' && whichKey != 'Alt' && whichKey != 'Shift') {
				  validKeyFound = true;
			  }
		   }
			  
		   if(validKeyFound == true) {
			   console.log('whichKey = ' + whichKey);
			   userSelectionData('LOGGER_FORM_KEYPRESS',whichKey);
		   }
	      }
	  });
  setInterval(() => {
	  document.getElementById('previous_xml_data').onchange = function() {
		  processManualProcedures('READ-DATA-AND-PREVIEW');
		}
	  document.getElementById('selectedScene').onchange = function() {
		  processManualProcedures('LOAD_SCENE');
		}
	  processManualProcedures('READ-MATCH-AND-POPULATE');
	}, 1000);
  </script>  
</head>
<body onload="reloadPage('MANUAL');">
<form:form name="manual_form" autocomplete="off" action="manual" method="POST" 
	modelAttribute="session_Data" enctype="multipart/form-data">
<div id="main_div"class="content py-1" style="background-color: #A7E3E8; color: #2E008B; width: 100vw; height: 100vh;">
<div>
<div class="container h-100 d-flex align-items-center justify-content-center">
	<div class="row">
	 <div class="col-md-13 offset-md-0" style="background-color: #CDE5E7; border-radius: 20px; box-shadow: 10px 5px 50px #9AA2A2">
       <span class="anchor"></span>
         <div class="card card-outline-secondary">
           <div class="card-header">
			 <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
	         </div> 
           </div>
          <div class="card-body">
          	<div id="logging_stats_div" style="display:none;">
			</div>
			  <div class="panel-group" id="match_configuration">
			    <div class="panel panel-default">
			      <div class="panel-heading">
			        <h2 class="panel-title" style="font-size: 25px; text-shadow: 2px 5px 6px #BBA2B6">
			          <a data-toggle="collapse" data-parent="#match_configuration" href="#load_setup_match">Configuration</a>
			          <!-- <a data-bs-toggle="collapse" data-bs-parent="#match_configuration" href="#load_setup_match">Configuration</a> -->
			        </h2>
			      </div>
			      <div id="load_setup_match" class="panel-collapse collapse">
					<div class="panel-body">
 					    <div class="col-md-auto col-md-5">
						    <label for="select_cricket_scenes" class="col-sm-4 col-form-label text-left" style="text-shadow: 2px 5px 6px #BBA2B6;">Select Scenes</label>
						      <select id="selectedScene" name="selectedScene" 
						      		class="browser-default custom-select custom-select-sm" style="width: 250px">
						      		<option value="BLANK">SELECT SCENE</option>
									<c:forEach items = "${session_viz_scenes}" var = "scenes">
							          	<option value="${scenes.name}">${scenes.name}</option>
									</c:forEach>
						      </select>
						      
						      <label for="previous_xml_data" class="col-sm-4 col-form-label" style="text-shadow: 2px 5px 6px #BBA2B6;">Select XML</label>
						      <select id="previous_xml_data" name="previous_xml_data" 
						      		class="browser-default custom-select custom-select-sm" style="width: 250px">
						      		<option value="BLANK">SELECT FILE</option>
									<c:forEach items = "${scene_files}" var = "files">
							          	<option value="${files.name}">${files.name}</option>
									</c:forEach>
						      </select>
						 </div>
						 
					    <!-- <button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
					  		name="load_scene_btn" id="load_scene_btn" onclick="processUserSelection(this)">
					  		<i class="fas fa-film"></i> Load Scene</button> -->
					  	  <button style="background-color:#2E008B;color:#FEFEFE; font-size: 18px; text-shadow: 2px 5px 6px #BBA2B6; align-content: right;" class="btn btn-sm" type="button"
					  		name="get_container_btn" id="get_container_btn"   onclick="processUserSelection(this)" >
					  		<i class="fas fa-film"></i> Get Container </button>
					  	  <!-- <button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
					  		name="load_previous_data_btn" id="load_previous_data_btn"   onclick="processUserSelection(this)" >
					  		<i class="fas fa-film"></i> Load Previous XML Data </button> -->
					  	   <button style="background-color:#2E008B;color:#FEFEFE; font-size: 18px; text-shadow: 2px 5px 6px #BBA2B6; align-content: right;" class="btn btn-sm" type="button"
					  		name="load_container_btn" id="load_container_btn"   onclick="processUserSelection(this)" >
					  		<i class="fas fa-film"></i> Load XML </button>
					  	  <button style="background-color:#2E008B;color:#FEFEFE; font-size: 18px; text-shadow: 2px 5px 6px #BBA2B6; align-content: right;" class="btn btn-sm" type="button"
						  		name="animatein_graphic_btn" id="animatein_graphic_btn" onclick="processUserSelection(this)"> AnimateIn </button>
					  	  <button style="background-color:#f44336;color:#FEFEFE; font-size: 18px; text-shadow: 2px 5px 6px #BBA2B6; align-content: right;" class="btn btn-sm" type="button"
						  		name="animateout_graphic_btn" id="animateout_graphic_btn" onclick="processUserSelection(this)"> AnimateOut </button>
						  <button style="background-color:#f44336;color:#FEFEFE; font-size: 18px; text-shadow: 2px 5px 6px #BBA2B6; align-content: right;" class="btn btn-sm" type="button"
						  		name="clear_all_btn" id="clear_all_btn" onclick="processUserSelection(this)"> Clear All </button>
						 <div class="form-group row row-bottom-margin ml-2">
						   <div id="RowCol_stats_div" style="display:none;"></div> 
			             </div> 		
				    </div>
			      </div>
			    </div>
			  </div> 
		    <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			  	<!-- <div id="event_stats_div" style="display:none;"></div> -->
			  	<%-- <div id="preview_div" class="form-group row mr-0" >
				  <img id="preview_image" class="pull-right img-responsive" src="<c:url value="/resources/Images/Blank_Preview.png"/>" alt="PreviewImage" width="700" height="700">
				  <!-- <img id="preview_image" class="pull-right img-responsive" src="file:///D:/Temp/Preview.png" alt="PreviewImage" width="300" height="300"> -->
				</div> --%>
           </div>
          </div>
         </div>
       </div>
    </div>
 </div>
<div id="previews" style="display: flex; align-items: flex-start; margin-right: 20px; margin-left: 100px; ">
    <div id="event_stats_div" style="display:none; background-color: white; border-radius: 2px;box-shadow: 10px 5px 50px #9AA2A2;
    	 padding-top: 28px; padding-left: 8px; padding-right: 8px; width: auto; margin-right: 20px;">
    </div>
    <div id="preview_image_div" style="display:none; background-color: white; border-radius: 2px; box-shadow: 10px 5px 50px #9AA2A2; 
    	padding: 8px; width: 40%; height: 500px;">
        <img id="preview_img" style="width: 100%; height: 100%;" alt="Preview Image">
    </div>
</div>
</div> 
</div> 
 <input type="hidden" name="select_sports" id="select_sports" value="${session_selected_sports}"/>
 <input type="hidden" id="manual_file_timestamp" name="manual_file_timestamp" value="${session_Data.manual_file_timestamp}"></input>
 <input type="hidden" name="scenePath" id="scenePath" value = ""></input>
</form:form>
</body>
</html>