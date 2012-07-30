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
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/javascript/dojo/dijit/themes/dijit.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/javascript/dojo/dijit/themes/tundra/tundra.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/defaults/bbcode.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/bscw/bbcode.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/defaults/default.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/bscw/default.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/defaults/iconconfig.css"/>
        <link rel="stylesheet" type="text/css" href="http://www.granatum.org/pub/static/styles/bscw/iconconfig.css"/>        
        <style type="text/css">        
                td.cSub, td.hSub, td.hsSub, td.cInd, td.hInd {
                    background-repeat: repeat-y;
                    background-image: url('http://www.granatum.org/pub/static/icons/fillSub.gif');
                }        
        </style>         
        <title>Granatum Error Page</title>
    </head>
    <body>
            <div id="main_div" class="main">
            <div id="banner" class="banner ruled_banner">
            <div class="logo"><img alt="" title="" src="http://www.granatum.org/pub/static/icons/logo.jpg" class="plain"  style=" width:300px; height:48px;" /></div>
            <div class="company"><img alt="" title="" src="http://www.granatum.org/pub/static/icons/server_logo_bscw.jpg" class="plain"  style=" width:289px; height:48px;" /></div>
        </div>
        <br><p>Error occured: <%= request.getParameter("errormsg") %> </p>
    </body>
</html>
