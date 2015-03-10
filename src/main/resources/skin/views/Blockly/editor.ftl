<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <link rel="stylesheet" href="${skinPath}/css/editor.css" type="text/css" media="screen" charset="utf-8" />

  <link rel="stylesheet" href="${skinPath}/bootstrap/css/bootstrap.min.css">
  <link rel="stylesheet" href="${skinPath}/bootstrap/css/bootstrap-theme.min.css">
  <link rel="stylesheet" href="${skinPath}/prism/prism.css">

  <script src="${skinPath}/js/jquery.js"></script>
  <script src="${skinPath}/bootstrap/js/bootstrap.min.js"></script>
  <script src="${skinPath}/prism/prism.js"></script>

  <title>
    Nuxeo Automation Scripting : Blockly Editor
  </title>
</head>
<body>

<div class="container-fluid" role="main">
<br/>
<div class="jumbotron" style="padding:4px">
    <h1> Nuxeo Automation Scripting : Blockly Editor </h1>
</div>

<script>
  function blocklyLoaded(blockly) {
    // Called once Blockly is fully loaded.
    window.Blockly = blockly;

    jQuery("#filterBtn").click(filter);
    jQuery("#clearFilterBtn").click(function() {
       jQuery("#filterValue").val("");
       filter();
    });
    function myUpdateFunction() {
      var code = Blockly.JavaScript.workspaceToCode();
      //document.getElementById('codeGen').value = code;
      jQuery("#codeGen").html(code);
      Prism.highlightElement(jQuery("#codeGen")[0]);

      var xml = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
    var xml_text = Blockly.Xml.domToPrettyText(xml);
    jQuery("#xmlGen").html(xml_text);


  }

  var xmlToLoad = jQuery(jQuery("#xmlToLoad")[0]).val();
  if (xmlToLoad.length>0) {
   var xml = Blockly.Xml.textToDom(xmlToLoad);
     Blockly.Xml.domToWorkspace(Blockly.mainWorkspace, xml);
  }

  Blockly.addChangeListener(myUpdateFunction);
  }

  function switchTab(tab) {
    jQuery(".nav-tabs>li").removeClass("active");
    jQuery(tab).addClass("active");
    var contentId = jQuery(tab)[0].id.replace('tab_', 'content_');
    console.log(contentId);
    jQuery(".tabContent").css("display", "none")
    jQuery('#'+contentId).css("display", "block")
  }

  function copyXML() {
    var xml = jQuery(jQuery("#xmlGen")[0]).html();
    jQuery(jQuery("#xmlGenSave")[0]).html(xml);
  }

  function copyJS() {
    var js = Blockly.JavaScript.workspaceToCode();
    jQuery(jQuery("#jsGenSave")[0]).html(js);
  }

  function filter(e) {
   var value = jQuery("#filterValue").val();
   var url = "${Context.baseURL}${Root.path}/toolbox?filter=" +  value;
   jQuery.get(url, function(xmlData) { console.log(xmlData); Blockly.updateToolbox(xmlData)}, "text");
  }

</script>

<ul class="nav nav-tabs" role="tablist">
        <li id="tab_blockly" role="presentation" class="active" onclick="switchTab(this)"><a href="#">Blockly Editor</a></li>
        <li id="tab_js" role="presentation"  onclick="switchTab(this)"><a href="#">Generated JavaScript</a></li>
        <li id="tab_xml" role="presentation"  onclick="switchTab(this)"><a href="#">XML Blocks</a></li>
</ul>

<div class="tabContent" id="content_blockly">
<div class="row">
  <div class="col-lg-3">
    <div class="input-group">
      <input type="text" class="form-control" placeholder="operation name" id="filterValue">
      <span class="input-group-btn">
        <button class="btn btn-default" type="button" id="filterBtn">Filter</button>
        <button class="btn btn-default" type="button" id="clearFilterBtn">x</button>
      </span>
    </div>
    </div></div>
  <iframe src="${Root.path}/blocklyFrame" border="0" width="100%" height="90%"></iframe>
</div>
<div class="tabContent" id="content_js" style="display:none">

<table>
<tr>
  <td>

    <pre><code class="language-javascript" id="codeGen"></code></pre>
    </td>
    <td style="padding:10px">

    <form action="${Root.path}/downloadJS" method="POST" enctype="multipart/form-data"  onsubmit="copyJS();">
            <div class="panel panel-default">
              <div class="panel-heading">
                <h3 class="panel-title">Save Generated JS File</h3>
              </div>
              <div class="panel-body">
              <input type="text" name="jsFileName" value="automation-blocks.js" class="form-control"></input><br/>
            <button type="submit" class="btn btn-lg btn-primary">Save</button>
            <textarea style="visibility:hidden" id="jsGenSave" name="js" cols="20" rows="1"></textarea>
              </div>
            </div>
   </form>
     </td>
     </tr>
</table>

</div>
<div class="tabContent" id="content_xml" style="display:none">
<table>
<tr>
  <td>
      <textarea class="language-xml" id="xmlGen" cols="120" rows="30"></textarea>
    </td>
    <td style="padding:10px">

      <form action="${Root.path}/" method="POST" enctype="multipart/form-data" >
            <div class="panel panel-default">
              <div class="panel-heading">
                <h3 class="panel-title">Load XML File</h3>
              </div>
              <div class="panel-body">
              <input type="file" name="xmlFile" class="form-control"></input><br/>
            <button type="submit" class="btn btn-lg btn-primary">Load</button>
            <br/>
            <br/>
            <A href="${Root.path}/?sample=sample1.xml">Load Sample 1</A>
              </div>
            </div>
      </form>  <br/>

        <form action="${Root.path}/save" method="POST" enctype="multipart/form-data"  onsubmit="copyXML();">
            <div class="panel panel-default">
              <div class="panel-heading">
                <h3 class="panel-title">Save as XML File</h3>
              </div>
              <div class="panel-body">
              <input type="text" name="xmlFileName" value="automation-blocks.xml" class="form-control"></input><br/>
            <button type="submit" class="btn btn-lg btn-primary">Save</button>
            <textarea style="visibility:hidden" id="xmlGenSave" name="xml" cols="20" rows="1"></textarea>
              </div>
            </div>
      </form>
    </td>
</tr>
</table>
</div>

<textarea id="xmlToLoad" cols="40" rows="2" style="display:none">
${xml}</textarea>

</div>
</body>
</html>