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
        
	<link rel="stylesheet" type="text/css" href="./js/dhtmlxSuite/dhtmlxGrid/codebase/dhtmlxgrid.css"/>
	<link rel="stylesheet" type="text/css" href="./js/dhtmlxSuite/dhtmlxGrid/codebase/dhtmlxgrid_skins.css"/>
        <script src="./js/dhtmlxSuite/dhtmlxGrid/codebase/dhtmlxcommon.js"></script>        
	<script src="./js/dhtmlxSuite/dhtmlxGrid/codebase/dhtmlxgrid.js"></script>
        <script src="./js/dhtmlxSuite/dhtmlxGrid/codebase/dhtmlxgridcell.js"></script>
        <script src="./js/dhtmlxSuite/dhtmlxGrid/codebase/excells/dhtmlxgrid_excell_link.js"></script>
        <script src="./js/dhtmlxSuite/dhtmlxGrid/codebase/ext/dhtmlxgrid_filter.js" type="text/javascript"></script>        

	<link rel="stylesheet" type="text/css" href="./js/dhtmlxSuite/dhtmlxTree/codebase/dhtmlxtree.css">
	<script src="./js/dhtmlxSuite/dhtmlxTree/codebase/dhtmlxcommon.js"></script>
	<script src="./js/dhtmlxSuite/dhtmlxTree/codebase/dhtmlxtree.js"></script>        
        
<style type="text/css">        
        td.cSub, td.hSub, td.hsSub, td.cInd, td.hInd {
            background-repeat: repeat-y;
            background-image: url('http://www.granatum.org/pub/static/icons/fillSub.gif');
        }        
</style>        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Granatum: Resource Annotator</title>


<script>
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

    function replaceValue()
    {
        var selections = "";
        var moreselections = "";
        
        if(tree!=null)
            moreselections = tree.getAllChecked();
        
        if(conceptsTree!=null)
            selections = conceptsTree.getAllChecked();
            
        document.forms['annotationform'].elements['selections'].value     = selections;
        document.forms['annotationform'].elements['moreselections'].value = moreselections;
    }

    function loadHelpMessage(text)
    {
        document.getElementById("help_message_box").innerHTML = "<div style='width:220px; height:40px; text-align:center; background-color:#6699FF'>" + 
                                                                text +
                                                                "</div>";
    }
/*
    function replaceValue()
    {
        var selections = "";
        var moreselections = "";
        
        if(tree!=null)
            moreselections = tree.getAllChecked();
        
        for(var key in concepts_selected)
        {
            if(concepts_selected[key]==true)
                selections = key + "," + selections;
        }

       
        document.forms['annotationform'].elements['selections'].value     = selections;
        document.forms['annotationform'].elements['moreselections'].value = moreselections;
    }
  */  
    function forwardBackToBSCW()
    {
        window.location = '<%= request.getParameter("back_obj") %>';
    }
    
</script>

<script type="text/javascript" src="http://www.granatum.org/pub/static/javascript/menu.js"></script>

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

<div class="dialog_container wide">
	<div class="dialog_header">	    
		<h1>Resource Annotator</h1> 
                <div class="xbutton"><input type="submit" class="xbutton close" name="_cancel_" value="" onClick="forwardBackToBSCW()" /></div>
						
	</div>
	<!-- EOF header --> 
<!--  main form -->
<form method="post" name="annotationform" action="AnnotateResource?op=<%= request.getParameter("mode") %>" onSubmit="replaceValue();">
<div class="dialog_body">

<!-- form data -->
 <div>
  <input type="hidden" name="op" value="addnote" />
  
  <input type="hidden" name="bscw_v_post" value="DC6piJo9FtbZLsNq6AQ9ukHzKOI4zdNB" />
<input type="hidden" name="act" value="addnotes" />	
 </div>
 <!-- form content -->
 <div id="content_div" class="content">
 
    
<div class="dialog_segment last"><div class="box_container">
<table summary="" width="100%" border="0" cellspacing="0" cellpadding="0">

    
 <tr>
  <th scope="row"><label for="subject">Comment:</label></th>
  <td class="narrowCol nC1st"></td>
  <td class="wideCol"><input type="text" id="subject" class="long inputfield" name="subject" value=""/></td>
 </tr>
<!--
 <tr>
  <th scope="row"><label for="body">Body:</label></th>
  <td class="narrowCol nC1st"></td>
  <td class="wideCol"><input type="text" id="body" class="long inputfield" name="body" value="" /></td>
 </tr>
--> 
 <tr>
  <th scope="row"><label for="cat">Category:</label></th>

  <td class="narrowCol nC1st"></td>
  <td class="wideCol">
      <select id="cat" class="medium inputfield" name="category">
         <option>----</option>          
         <option value="Resource_exp_data"  onClick='loadResponse("Experimental Data")'>Experimental Data</option>
         <option value="Resource_protocols"  onClick='loadResponse("Experimental Data")'>Protocols</option>
         <option value="Resource_publications"  onClick='loadResponse("Experimental Data")'>Publications</option>
         <option value="Resource_announce"  onClick='loadResponse("Experimental Data")'>General Announcements</option>
         <option value="Resource_workflow_desc"  onClick='loadResponse("Experimental Data")'>Workflow Descriptions</option>
         <option value="Resource_mol_desc"  onClick='loadResponse("Experimental Data")'>Molecule structure description</option>
         <option value="Resource_protein_desc"  onClick='loadResponse("Experimental Data")'>Protein structure description</option>
         <option value="Resource_workflow_results"  onClick='loadResponse("Experimental Data")'>Workflow results</option>
      </select>
  </td>
 </tr>

</table></div></div>

 </div>
<div id="box" style="padding-top: 10px;padding-right: 0px;padding-bottom: 0.25in;padding-left: 10px; width:99%;height:300px"> </div>
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
      <input class="button ok" type="submit" name="_ok_" value="   OK   " />
<!--      <input class="button cancel" type="submit" name="_cancel_" value=" Cancel " />    -->
  </div>
</div>
 </div>
 
</div>
<!-- EOF dialog_body -->
<div class="dialog_footer">&nbsp;
  </div><!-- EOF footer -->

</div><!-- EOF dialog_container -->

 <input type='hidden' name='selections'  value='null'/>
 <input type='hidden' name='moreselections'  value='null'/> 
 <input type='hidden' name='objectID'  value='<%= request.getParameter("objectID") %>'>
 <input type='hidden' name='backObj'   value='<%= request.getParameter("back_obj") %>'>
</form>
        
    </div>

    </td></tr></table>  
    

</div>    
<!--
<div id="help_message_box" style="text-align:center"></div>
-->
    </body>
</html>
