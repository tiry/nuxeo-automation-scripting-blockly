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
    <h1> Nuxeo Automation Scripting : Blockly Editor </h1>
    
<script>
  function blocklyLoaded(blockly) {
    // Called once Blockly is fully loaded.
    window.Blockly = blockly;
        
    function myUpdateFunction() {
  		var code = Blockly.JavaScript.workspaceToCode();
  		//document.getElementById('codeGen').value = code;
  		jQuery("#codeGen").html(code);
  		Prism.highlightElement(jQuery("#codeGen")[0]);
  		
  		var xml = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
		var xml_text = Blockly.Xml.domToPrettyText(xml);
		jQuery("#xmlGen").html(xml_text);		

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
</script>

<ul class="nav nav-tabs" role="tablist">
        <li id="tab_blockly" role="presentation" class="active" onclick="switchTab(this)"><a href="#">Blockly Editor</a></li>
        <li id="tab_js" role="presentation"  onclick="switchTab(this)"><a href="#">Generated JavaScript</a></li>
        <li id="tab_xml" role="presentation"  onclick="switchTab(this)"><a href="#">XML Blocks</a></li>
</ul>

<div class="tabContent" id="content_blockly">
	<iframe src="${Root.path}/blocklyFrame" border="0" width="100%" height="90%"></iframe>
</div>
<div class="tabContent" id="content_js" style="display:none">        
    <pre><code class="language-javascript" id="codeGen"></code></pre>
    <button type="button" class="btn btn-lg btn-primary">Generate Automation Contrib</button>        
</div>
<div class="tabContent" id="content_xml" style="display:none">
    <textarea class="language-xml" id="xmlGen" cols="120" rows="30"></textarea>
    <br/>
    <button type="button" class="btn btn-lg btn-primary">Load from XML</button>
</div>
    
</body>
</html>