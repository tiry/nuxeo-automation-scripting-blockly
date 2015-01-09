'use strict';

goog.provide('Blockly.Blocks.automation');

goog.require('Blockly.Blocks');

// Swallow Block
Blockly.Blocks['Automation.SwallowOutput'] = {
  init: function() {
    this.setColour(220);
    this.appendValueInput('INPUT')
           .appendField(' << ');
   this.setPreviousStatement(true);
   this.setNextStatement(true);  
 }
};

Blockly.JavaScript['Automation.SwallowOutput'] = function (block) {
  var code = Blockly.JavaScript.valueToCode(block, 'INPUT', Blockly.JavaScript.ORDER_ASSIGNMENT);  
  if (code!=null && code!='') {
    code = code + ";\n";
  }      
  return code;
};
 
// GetDocumentproperty  
Blockly.Blocks['Automation.GetDocumentProperty'] = {
  init: function() {
    this.setColour(220);
    this.appendDummyInput()
      .appendField('expression')
      .appendField(new Blockly.FieldTextInput('dc:title'), 'EXPR');
   this.appendValueInput('INPUT')
   this.setOutput(true); 
 }
};

Blockly.JavaScript['Automation.GetDocumentProperty'] = function (block) {
  var expr = block.getFieldValue('EXPR');
  var suffix = ".";
  var code = Blockly.JavaScript.valueToCode(block, 'INPUT', Blockly.JavaScript.ORDER_MEMBER);
  if (expr == 'lifeCycle') {
     suffix += "getCurrentLifeCycle()";     
  } else {
     suffix += "getPropertyValue('" + expr + "')";
  } 
  return [code + suffix,  Blockly.JavaScript.ORDER_NONE];
};

<#list operations as op>

Blockly.Blocks['${op.id}'] = {
  
  init: function() {
    //this.setHelpUrl(Blockly.Msg.CONTROLS_REPEAT_HELPURL);
    this.setColour(220);
    this.appendDummyInput()
        .appendField("${op.id}")
    
<#if op.hasInput()>
   this.appendValueInput('INPUT')
        .appendField(' - Input');
</#if>        
<#list op.params as param>    
    <#if param.getType()=="boolean">
       this.appendDummyInput().appendField(' - ${param.name}').appendField(new Blockly.FieldCheckbox('FALSE'), '${param.name}');
    <#else>
       this.appendValueInput('${param.name}')
           .appendField(' - ${param.name}');
    </#if>    
</#list>        
<#if op.hasOutput()>
   this.setOutput(true); 
<#else>
   this.setPreviousStatement(true);
   this.setNextStatement(true);  
</#if>        
    this.setInputsInline(false);  
    this.setTooltip("${op.label}");
  }
};

Blockly.JavaScript['${op.id}'] = function(block) {

  var code = '${op.id}('
  <#if op.hasInput()>
      code += Blockly.JavaScript.valueToCode(block, 'INPUT', Blockly.JavaScript.ORDER_MEMBER);
  <#else>
      code += 'null';
  </#if>
  code +=', {'
<#list op.params as param>    
    <#if param.getType()=="boolean">
       code += '"${param.name}":' + block.getFieldValue('${param.name}');
    <#else>
       code += '"${param.name}":' + Blockly.JavaScript.valueToCode(block,'${param.name}',  Blockly.JavaScript.ORDER_MEMBER);
    </#if>    
</#list>          
  code += '})'  

  return [code,  Blockly.JavaScript.ORDER_NONE];
}

</#list>