<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/defaults/boxlayout.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/small/boxlayout.css"/>  
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/bscw/boxlayout.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/defaults/banner.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/small/banner.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/bscw/banner.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/defaults/navigationArea.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/small/navigationArea.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/bscw/navigationArea.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/defaults/dialog.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/small/dialog.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/bscw/dialog.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/defaults/bbcode.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/bscw/bbcode.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/defaults/default.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/bscw/default.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/defaults/iconconfig.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/bscw/iconconfig.css"/>        
        <link rel="stylesheet" type="text/css" href="./css/autocomplete.css"/>        
        <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js"></script>
        <script src="http://code.jquery.com/jquery-latest.js"></script>
        <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script>

<style type="text/css">        
        td.cSub, td.hSub, td.hsSub, td.cInd, td.hInd {
            background-repeat: repeat-y;
            background-image: url('http://www.granatum.org/pub/static/icons/fillSub.gif');
        }        
</style>        
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Granatum: Resource Annotator</title>

<!--   
    var concepts_selected = new Array();
    var tree = null;
    var conceptsTree = null;
    
    function doOnRowSelected(rowID, celInd, state)
    {
        concepts_selected[rowID] = state;
    }    
    
    function loadGrid(category)
    {
        grid = new dhtmlXGridObject("box_grid");
        grid.setImagePath("./js/dhtmlxSuite/dhtmlxGrid/codebase/imgs/");
        grid.setHeader("Concept,Select");//set column names
        grid.setInitWidths("*,100");        
        grid.setColTypes("ro,ch");
        grid.setSkin("light");//set grid skin
        grid.attachEvent("onCheck", doOnRowSelected);
        grid.init();//initialize grid
        grid.loadXML('AnnotateResource?op=getConcepts&category=' + category);
    }
 
   function loadMockCategories(category)
    {
        var content = document.getElementById("box_grid");
        content.innerHTML = "<div id=\"box_tree\" style=\"width:99%;height:100%px;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; text-align=center\"/>";
        conceptsTree = new dhtmlXTreeObject("box_tree", "100%", "100%", 0);
        conceptsTree.setImagePath("./js/dhtmlxSuite/dhtmlxTree/codebase/imgs/");
        conceptsTree.enableCheckBoxes(1);
        conceptsTree.loadXML('AnnotateResource?op=getMockConcepts&category=' + category);
    }
  
    function loadRestCategories()
    {
        var content = document.getElementById("generic_cats_box");
        content.onclick = "";
        content.style = "";
        content.innerHTML = "<div id=\"generic_cats_box_tree\" style=\"width:99%;height:100%px;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; text-align=center\"/>";
        tree = new dhtmlXTreeObject("generic_cats_box_tree", "100%", "100%", 0);
        tree.setImagePath("./js/dhtmlxSuite/dhtmlxTree/codebase/imgs/");
        tree.enableCheckBoxes(1);
        tree.loadXML('AnnotateResource?op=getRestofConcepts');
    }
    
    function loadResponse(category)
    {       
        var content = document.getElementById("box");
        content.innerHTML = "<div id=\"box_grid\" style=\"width:99%;height:300px\"/>";
//        loadGrid(category);
        loadMockCategories(category);
    }
-->
<script>
    function replaceValue()
    {
        var selections = "";
        
        for(var i=0;i<$('#autosugg span').length;i++)
        {
            var trimmedStr = $('#autosugg span').eq(i).text().replace("/(\s+)?.$/", "");
            //var trimmedStr = $('#autosugg span').eq(i).text();
            var start = trimmedStr.indexOf("<");
            var end   = trimmedStr.indexOf(">");

            selections += (trimmedStr.split("<", $.trim((trimmedStr.length)[0])) ) + "$" + trimmedStr.substr(start+1, end-1) + ",";
        }

        document.forms['annotationform'].elements['selections'].value = selections;
    }

    function forwardBackToBSCW()
    {
        window.location = '<%= request.getParameter("back_obj") %>';
    }

    // jquery autocomplete support
    $(function(){
		$("#to").autocomplete({
					
		//define callback to format results
		source: function(req, add){
					
			//pass request to server
			$.getJSON("http://granatum.ubitech.eu:8080/SemanticSocialAnnotator/AnnotateResource?callback=?&op=makeSuggestions&action=makeSuggestions", req, function(data) {
//			$.getJSON("http://localhost:8080/SemanticSocialAnnotator/AnnotateResource?callback=?&op=makeSuggestions&action=makeSuggestions", req, function(data) {							
			//create array for response objects
        		var suggestions = [];
							
        		//process response
			$.each(data, function(i, val){								
				suggestions.push(val.name + "   <" + val.classname + ">");
			});
							
			//pass array to callback
			add(suggestions);
			});
		},
					
        	//define select handler
                select: function(e, ui) {
               		//create formatted friend
                        var friend = ui.item.value,
                        span = $("<span>").text(friend),
			a = $("<a>").addClass("remove").attr({
				href: "javascript:",
                                title: "Remove " + friend
			}).text("x").appendTo(span);
						
                        //add friend to friend div
                        $('#autosugg').append(span);
//                        alert($(this).val());
                        $(this).val('');
                        return false;
		},
			
                //define select handler
            	change: function() {	
			//prevent 'to' field being updated and correct position
        		$("#to").val("").css("top", 2);
		}
		});
				
		//add click handler to autosugg div
                $("#autosugg").click(function(){			
        		//focus 'to' field
			$("#autosugg").focus();
		});
				
		//add live handler for clicks on remove links
		$(".remove", document.getElementById("autosugg")).live("click", function(){			
			//remove current friend
			$(this).parent().remove();
					
			//correct 'to' field position
			if($("#autosugg span").length === 0) {
        			$("#to").css("top", 0);
                        }
	});				
    });
</script>
<script type="text/javascript" src="http://www.granatum.org/pub/static/javascript/ckeditor/ckeditor.js"></script>     
</head>

<body>
        
<div id="body_div" style="height:100%;">
<table summary="" id="main" class="main" style="width:100%; min-width:1145px;"><tr><td class="main">
        
<div id="main_div" class="main">
    <div id="banner" class="banner ruled_banner">
    <div class="logo"><img alt="" title="" src="http://www.granatum.org/pub/static/icons/logo.jpg" class="plain"  style=" width:300px; height:48px;" /></div>
    <div class="msg"></div>
    <div class="logout"></div>
    <div class="company"><img alt="" title="" src="http://www.granatum.org/pub/static/icons/server_logo_bscw.jpg" class="plain"  style=" width:289px; height:48px;" /></div>
</div>
<p class="clear" />
 
<input type="image" style="width:0; height:0" name="firefix" value="" disabled="disabled" />

<div class="dialog_container wide" style="text-align:center">
    <div class="dialog_header" style="text-align:center">	    
        <h1>Resource Annotator</h1> 
        <div class="xbutton"><input type="submit" class="xbutton close" name="_cancel_" value="" onClick="forwardBackToBSCW()" /></div>						
    </div>
	<!-- EOF header --> 
<!--  main form -->
<!--<form method="post" name="annotationform" action="AnnotateResource?op=<%= request.getParameter("mode") %>" onSubmit="replaceValue();">-->
<form method="post" name="annotationform" action="AnnotateResource?op=<%= request.getParameter("mode") %>&action=<%= request.getParameter("mode") %>&a1=<%= request.getParameter("a1") %>&a2=<%= request.getParameter("a2") %>" onSubmit="replaceValue();">
<div class="dialog_body">

<!-- form data -->
 <div>
  <input type="hidden" name="op" value="addnote" />  
  <input type="hidden" name="act" value="addnotes" />	
 </div>
 <!-- form content -->
 <div id="content_div" class="content">
 
 <div class="dialog_segment last"><div class="box_container">
 <table summary="" width="100%" border="0" cellspacing="0" cellpadding="0">

 <tr>
  <th scope="row"><label for="subject">Title:</label></th>
  <td class="narrowCol nC1st"></td>
  <td class="wideCol">
      <select id="subject" class="long inputfield" name="subject">
         <option>----</option>          
         <option value="Cellural Processes">Cellural Processes</option>
         <option value="Angiogenesis">&nbsp;Angiogenesis</option>
         <option value="Apoptosis">&nbsp;Apoptosis</option>
         <option value="Cell Cycle">&nbsp;Cell Cycle</option>
         <option value="Cancer">Cancer</option>
         <option value="Growth Factors and Cytocines">&nbsp;Growth Factors and Cytocines</option>         
      </select>
  </td>  
 </tr>

 <tr>
  <th scope="row"><label for="cat">Category:</label></th>
  <td class="narrowCol nC1st"></td>  
  <td class="wideCol"><input type="text" id="to" class="long inputfield" name="tgs" value="" /></td>    

 </tr>

 </table>
 </div></div>
 </div>
 <!-- style="padding-top: 10px;padding-right: 0px;padding-bottom: 0.25in;padding-left: 10px; width:99%;height:200px" -->
<div id="box" >
    <div id="autosugg" class="ui-helper-clearfix" style="padding-top: 10px;padding-right: 0px;padding-bottom: 0.25in;padding-left: 10px; width:98%;height:150px">
       <!-- <div id="to" /> -->
    </div>
</div>
 
<div id="breadcrump" style="width:100%;height:50px"> </div>
    <!--
    <div id="generic_cats_box" onClick="loadRestCategories()"><div style="text-align:center">Click here to load rest of categories</div></div>
    -->

<!--  Buttons Footer -->

    <div id="buttons_div" class="bordered_button_row">
     <div class="button_row">
       <div class="buttons left">
    <!--    <a href="javascript:void(0)" target="_blank" onclick="xFocus('4205?op=help&amp;action=addnotes','Help','toolbar=no,location=no,directories=no,menubar=no,status=yes,scrollbars=yes,resizable=yes,width=800,height=600', false, ''); return false;"><img alt="Help" title="Help" src="http://www.granatum.org/pub/static/icons/help_button.gif" class="plain"  style=" width:23px; height:23px;" /></a>-->
       </div>

       <div class="buttons right">            
          <input class="button ok" type="submit" name="_ok_" value="   OK   "/>
          <input class="button cancel" type="submit" name="_cancel_" value=" Cancel " onclick="forwardBackToBSCW()"/>  
       </div>
      </div>
    </div>
 
    </div>
    <!-- EOF dialog_body -->
    <div class="dialog_footer">&nbsp;</div><!-- EOF footer -->

    </div><!-- EOF dialog_container -->

    <input type='hidden' name='selections'  value='null'/>
    <input type='hidden' name='moreselections'  value='null'/> 
    <input type='hidden' name='objectID'  value='<%= request.getParameter("objectID") %>'>
    <input type='hidden' name='backObj'   value='<%= request.getParameter("back_obj") %>'>
</form>
        
</div>
</td></tr>
</table>
</div>    

</body>
</html>