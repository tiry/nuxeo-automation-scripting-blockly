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
  }
</script>

<iframe src="${Root.path}/blocklyFrame" border="0" width="100%" height="90%"></iframe>

</body>
</html>