<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <script src="${skinPath}/blockly/blockly_compressed.js"></script>
    <script src="${skinPath}/blockly/gen/javascript_compressed.js"></script>
    <script src="${skinPath}/blockly/blocks_compressed.js"></script>
    <script src="${skinPath}/blockly/field_textarea.js"></script>
    <script src="${skinPath}/blockly/msg/js/en.js"></script>
    <script src="${Root.path}/blocks"></script>
    <style>
      html, body {
        background-color: #fff;
        margin: 0;
        padding: 0;
        overflow: hidden;
        height: 100%;
      }
      .blocklySvg {
        height: 100%;
        width: 100%;
      }
    </style>
    <script>
      function init() {
        Blockly.inject(document.body, {toolbox: document.getElementById('toolbox')});
        // Let the top-level application know that Blockly is ready.
        window.parent.blocklyLoaded(Blockly);
      }
    </script>
  </head>
  <body onload="init()">
    <#include "views/Blockly/toolbox.ftl"/>
  </body>
</html>