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
      switch(e.target.tagName.toLowerCase()) {
        case "input": 
        case "textarea":
          // Possibly do something with form fields...
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
          if(whichKey.includes('_')) {
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
            userSelectionData('LOGGER_FORM_KEYPRESS', whichKey);
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
      processManualProcedures('CHECK_CONNECTION');
    }, 1000);
  </script>
  <!-- Additional Responsive & Modern CSS -->
  <style type="text/css">
   body {
    font-size: 1.95rem; /* 22px converted to rem (assuming 16px base) */
   }
   h1, h2, h3, h4, h5, h6,a {
    font-size: larger; /* This remains relative to the base font-size */
   }
   button, input, select, textarea {
    font-size: 1.7rem; /* 18px converted to rem */
   }
   .card-title, .panel-title, .form-group label {
     font-size: 1.8rem; /* 20px converted to rem */
   }
    /* Subtle gradient background for entire page */
    body {
    margin: 0;
    padding: 0;
    color: #2E008B;
    font-family: Arial, sans-serif;
    background: url('<c:url value='/resources/Images/img_2.jpg'/>') no-repeat center center fixed;
    background-size: cover;
	}
	
	body::before {
	    content: "";
	    position: fixed;
	    top: 0;
	    left: 0;
	    width: 100%;
	    height: 100%;
	    background: rgba(255, 255, 255, 0.33);
	    z-index: -1;
	}

    /* Container for the main content */
    #main_div.content.py-1 {
      width: 100vw;
      height: 100vh;
      box-shadow: 0 4px 10px rgba(0,0,0,0.2);
      border-radius: 12px;
      overflow: hidden;
    }
    /* The outer .col-md-13 offset-md-0 container */
    .col-md-13.offset-md-0 {
      border-radius: 20px;
      box-shadow: 0 5px 25px rgba(0,0,0,0.2);
      padding: 5px;
      margin-bottom: 20px;
    }
    /* Card styling improvements */
    .card.card-outline-secondary {
      background: #fff;
      border-radius: 8px;
      box-shadow: 0 4px 10px rgba(0,0,0,0.1);
      margin-bottom: 20px;
      border: none; /* Hide default card border */
    }
    .card-header {
      background-color: #CDE5E7;
      border-radius: 8px 8px 0 0;
      padding: 15px;
      border-bottom: 1px solid #ddd;
    }
    .card-body {
      padding: 20px;
    }
    /* The 'Configuration' panel heading */
    .panel-heading {
      background: none;
      border: none;
    }
    .panel-title {
      text-shadow: 2px 5px 6px #BBA2B6;
      font-size: 25px;
    }

    /* Buttons with a slight box-shadow & hover effect */
    .btn-sm {
      padding: 8px 16px;
      border-radius: 5px;
      transition: background-color 0.3s ease, box-shadow 0.3s ease;
    }
    .btn-sm:hover {
      filter: brightness(0.9);
      box-shadow: 0 2px 4px rgba(0,0,0,0.15);
    }
    /* Previews section styling */
    #previews {
      display: flex;
      align-items: flex-start;
      justify-content: space-between;
      margin-left: 5%;
      width: 90%;
      margin-top: 20px;
      margin-bottom: 40px;
    }
    #event_stats_div, #preview_image_div {
      background-color: #fff;
      border-radius: 8px;
      box-shadow: 10px 5px 50px #9AA2A2;
      padding: 28px 8px;
      margin-right: 20px;
    }
    #event_stats_div {
	    display: block; /* Ensure it's visible */
	    overflow-y: auto; /* Enable vertical scrolling */
	    max-height: 65vh; /* Set a maximum height for scrolling */
	}
    
    /* Media query for smaller screens */
    @media (max-width: 768px) {
      /* Stack the Scenes/XML and Buttons vertically */
      .panel-body > div[style*="display: flex;"] {
        flex-direction: column !important;
        gap: 20px !important;
      }
      /* Previews also stack vertically */
      #previews {
        flex-direction: column;
        align-items: stretch;
      }
      #event_stats_div, #preview_image_div {
        margin-right: 0;
        margin-bottom: 20px;
        width: 100% !important;
      }
      /* Adjust .col-md-13 container for smaller screens if needed */
      .col-md-13.offset-md-0 {
        margin: 10px;
        border-radius: 10px;
      }
    }
    /* Make table cells wrap text and keep the table 100% width */
	.table {
	  width: 100%;
	  table-layout: fixed; /* Ensures columns share remaining width evenly */
	  border-collapse: collapse;
	}
	
	/* Let table cells wrap text instead of overflowing */
	.table th,
	.table td {
	  white-space: normal;        /* Allows line breaks */
	  word-wrap: break-word;      /* Breaks long words */
	  overflow-wrap: break-word;  /* CSS3 standard property */
	  vertical-align: top;        /* So labels & inputs align nicely at the top */
	  padding: 8px;
	  border: 1px solid #ddd;     /* Example border; remove if undesired */
	}
	
	/* Make inputs fill the entire table cell width */
	.table td input[type="text"],
	.table td input[type="file"] {
	  width: 100%;
	  box-sizing: border-box; /* Ensures padding/border are included in total width */
	}
	
	/* Ensure labels also wrap properly */
	.table td label {
	  display: block;
	  white-space: normal;
	  word-wrap: break-word;
	  overflow-wrap: break-word;
	}
	html, body {
	    overflow-x: hidden; /* Prevent horizontal scrolling */
	    overflow-y: auto; /* vertical scrolling on larger screens */
	    margin: 0;
	    padding: 0;
	}
    /* Responsive table container */
	.table-responsive {
	  width: 100%;
	  overflow-x: auto;         /* Enables horizontal scrolling if needed */
	  -webkit-overflow-scrolling: touch; /* Smooth scrolling on mobile devices */
	  border: 1px solid #ddd;   /* Optional border */
	  border-radius: 4px;       /* Rounded corners for the container */
	  margin-bottom: 20px;      /* Spacing at the bottom */
	}
	
	/* Make sure the table inside fills the container */
	.table-responsive table {
	  width: 100%;
	  max-width: 100%;
	  border-collapse: collapse;
	}
	#event_stats_div {
	  display: none;
	  background-color: white;
	  border-radius: 2px;
	  box-shadow: 10px 5px 50px #9AA2A2;
	  padding: 28px 8px;
	  width: 50%;
	  max-width: 100%;
	  height: 600px;        /* or 'auto' + a max-height, if you prefer */
	  margin-right: 20px;
	  overflow-x: hidden;   /* Hide horizontal scrolling */
	  overflow-y: auto;     /* Allow vertical scrolling only */
	}
	@media (min-width: 1024px) {
	  .col-md-13.offset-md-0 {
	    width: 100%; /* Adjust this value as needed */
	    margin: 0 auto; /* Center the container */
	  }
	}
	/* Container row that splits into left and right columns */
	.configuration-row {
	  display: flex;
	  align-items: flex-start;
	  justify-content: space-between;
	  gap: 1rem; /* spacing between left and right sections */
	  margin-bottom: 1rem;
	}
	
	/* Left and right columns share the space equally on large screens */
	.left-col, .right-col {
	  flex: 1; /* Each takes up 50% on large screens */
	}
	
	/* Stack row-items vertically in the left column */
	.left-col {
	  display: flex;
	  flex-direction: column;
	  gap: 1rem;
	}
	
	/* Each label + select pair is one row-item */
	.row-item {
	  display: flex;
	  align-items: center;
	  gap: 6rem; /* 10px => 0.625rem */
	}
	
	/* Right column: buttons, allow wrapping if needed */
	.right-col {
	  display: flex;
	  flex-wrap: wrap; /* buttons go to next line if there's no space */
	  gap: 0.625rem;
	  justify-content: flex-end;
	}
	
	/* Basic styling for labels */
	.configuration-row label {
	  font-weight: bold;
	  color: #2E008B;
	  white-space: nowrap; /* keep label on one line */
	}
	
	/* Basic styling for selects */
	.configuration-row select {
	  /* Let them expand on wide screens; override with media queries below if desired */
	  width: 100% !important;
	  max-width: 100% !important; /* 240px => 15rem, adjust as needed */
	}
	
	/* Purple Buttons */
	.purple-btn {
	  background-color: #2E008B;
	  color: #FEFEFE;
	  font-size: 1.75rem; /* 18px => 1.125rem */
	  text-shadow: 0.125rem 0.3125rem 0.375rem #BBA2B6;
	  transition: background-color 0.3s ease, box-shadow 0.3s ease;
	}
	.purple-btn:hover {
	  background-color: #1D0066; /* A darker shade of purple */
  	  color: #fff;  
	  box-shadow: 0 0.125rem 0.25rem rgba(0,0,0,0.15);
	}
	
	/* Red Buttons */
	.red-btn {
	  background-color: #f44336;
	  color: #FEFEFE;
	  font-size: 1.75rem;
	  text-shadow: 0.125rem 0.3125rem 0.375rem #BBA2B6;
	  transition: background-color 0.3s ease, box-shadow 0.3s ease;
	}
	.red-btn:hover {
	  color: #fff; 
	  box-shadow: 0 0.125rem 0.25rem rgba(0,0,0,0.15);
	}
	/* For mobile devices (max-width: 768px) */
	@media (max-width: 768px) {
	    body {
	        font-size: 1.4rem; /* Adjust base font size for better readability */
	    }
	
	    /* Stack .panel-body and rows vertically */
	    .panel-body {
	        flex-direction: column; /* Stack content vertically */
	        gap: 1.5rem;
	    }
	
	    /* Adjust table layout */
	    .table th, .table td {
	        font-size: 1.4rem; /* Smaller font size for mobile */
	        padding: 12px; /* Increase padding for touch */
	    }
	
	    .configuration-row .left-col,
	    .configuration-row .right-col {
	        min-width: 100%; /* Full width for columns on mobile */
	        margin-bottom: 1rem;
	    }
	
	    .card-body {
	        padding: 15px; /* Reduce padding for tighter mobile layout */
	    }
	
	    /* Adjust the card body padding */
	    .card.card-outline-secondary {
	        margin-bottom: 15px; /* Reduce card spacing for smaller screens */
	    }
	
	    /* Adjust buttons */
	    .btn-sm {
	        font-size: 1.4rem;
	        padding: 12px 18px;
	    }
	
	    .purple-btn,
	    .red-btn {
	        font-size: 1.4rem; /* Smaller button size for mobile */
	        padding: 12px 20px;
	    }
	
	    /* Ensure the table is responsive */
	    .table-responsive {
	        overflow-x: scroll; /* Allow horizontal scrolling for tables */
	    }
	}	
  </style>
</head>
<body onload="reloadPage('MANUAL');">
<form:form name="manual_form" autocomplete="off" action="manual" method="POST" 
	modelAttribute="session_Data" enctype="multipart/form-data">
<div id="main_div" class="content py-1" style="width: 100vw; height: 100vh;">
<div class="container-fluid h-100 d-flex align-items-center justify-content-center" style="width: 90%; margin: 0 auto;">
	<div class="row">
	 <div class="col-md-13 offset-md-0">
       <span class="anchor"></span>
         <div class="card card-outline-secondary">
           <div class="card-header">
			 <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
	         </div> 
           </div>
          <div class="card-body">
          <div id="CheckConnection_div"></div> 
          	<div id="logging_stats_div" style="display:none;">
			</div>
			  <div class="panel-group" id="match_configuration">
			    <div class="panel panel-default">
			      <div class="panel-heading">
			        <h2 class="panel-title" style="font-size: 25px; text-shadow: 2px 5px 6px #BBA2B6">
			          <a data-toggle="collapse" data-parent="#match_configuration" href="#load_setup_match">Configuration</a>
			        </h2>
			      </div>
			      <div id="load_setup_match" class="panel-collapse collapse">
					<div class="panel-body">
						<!-- Configuration Row -->
							<div class="configuration-row"style="width: 100%">
							  <!-- Left Section -->
							  <div class="left-col">
							    <div class="row-item">
							      <label for="select_cricket_scenes">Select Scenes</label>
							      <select id="selectedScene" name="selectedScene" class="browser-default custom-select custom-select-sm"style="width: 90%;">
							        <option value="BLANK">SELECT SCENE</option>
							        <c:forEach items="${session_viz_scenes}" var="scenes">
							          <option value="${scenes.name}">${scenes.name}</option>
							        </c:forEach>
							      </select>
							    </div>
							    <div class="row-item">
							      <label for="previous_xml_data">Select XML</label>
							      <select id="previous_xml_data" name="previous_xml_data" class="browser-default custom-select custom-select-sm" style="width: 90%;">
							        <option value="BLANK">SELECT FILE</option>
							        <c:forEach items="${scene_files}" var="files">
							          <option value="${files.name}">${files.name}</option>
							        </c:forEach>
							      </select>
							    </div>
							  </div>
							
							  <!-- Right Section -->
							  <div class="right-col">
							    <button class="btn btn-sm purple-btn" type="button" name="get_container_btn" id="get_container_btn" onclick="processUserSelection(this)">
							      <i class="fas fa-film"></i> Get Container
							    </button>
							    <button class="btn btn-sm purple-btn" type="button" name="load_container_btn" id="load_container_btn" onclick="processUserSelection(this)">
							      <i class="fas fa-film"></i> Load XML
							    </button>
							    <button class="btn btn-sm purple-btn" type="button" name="animatein_graphic_btn" id="animatein_graphic_btn" onclick="processUserSelection(this)">
							      AnimateIn
							    </button>
							    <button class="btn btn-sm red-btn" type="button" name="animateout_graphic_btn" id="animateout_graphic_btn" onclick="processUserSelection(this)">
							      AnimateOut
							    </button>
							    <button class="btn btn-sm red-btn" type="button" name="clear_all_btn" id="clear_all_btn" onclick="processUserSelection(this)">
							      Clear All
							    </button>
							    <button class="btn btn-sm red-btn" type="button" name="connection_btn" id="connection_btn" onclick="processUserSelection(this)">
							     RE CONNECT
							    </button>
							  </div>
							</div>
						 <div class="form-group row row-bottom-margin ml-2">
						   <div id="RowCol_stats_div" style="display:none;"></div> 
			             </div> 		
				    </div>
			      </div>
			    </div>
			  </div> 
		    <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
           </div>
          </div>
         </div>
       </div>
    </div>
 </div>
 <div id="previews" style="display: flex; align-items: flex-start; justify-content: space-between; margin-left: 5%; width: 90%;">
    <div id="event_stats_div" style="display:none; background-color: white; border-radius: 2px; box-shadow: 10px 5px 50px #9AA2A2;
         padding: 28px 8px; width: 50%; height: 50%; margin-right: 20px;">
    </div>
    <div id="preview_image_div" style="display:none; background-color: white; border-radius: 2px; box-shadow: 10px 5px 50px #9AA2A2; 
        padding: 8px; width: 50%; height: 65vh;">
        <img id="preview_img" style="width: 100%; height: 100%;" alt="Preview Image">
    </div>
</div>
</div> 
 <input type="hidden" name="select_sports" id="select_sports" value="${session_selected_sports}"/>
 <input type="hidden" id="manual_file_timestamp" name="manual_file_timestamp" value="${session_Data.manual_file_timestamp}"/>
 <input type="hidden" name="scenePath" id="scenePath" value=""/>
</form:form>
</body>
</html>
