<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
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
  		document.getElementById('codeGen').value = code;
	}
	Blockly.addChangeListener(myUpdateFunction);
  }
</script>

<table width="100%" border="0" height="100%">
<tr>
<td width="100%">
	<iframe src="${Root.path}/blocklyFrame" border="0" width="100%" height="90%"></iframe>
</td>
<td> <center><h2> JavaScript code </h2></center> 
    <textarea id="codeGen" cols="80" rows="40"></textarea>
</td>
</tr>
</table>

</body>
</html>