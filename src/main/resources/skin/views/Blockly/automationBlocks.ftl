'use strict';

goog.provide('Blockly.Blocks.automation');

goog.require('Blockly.Blocks');
goog.require('Blockly.FieldTextArea');

// override some default definitions

goog.require('Blockly.JavaScript.loops');

Blockly.JavaScript['controls_forEach'] = function(block) {
  // For each loop.
  var variable0 = Blockly.JavaScript.variableDB_.getName(
      block.getFieldValue('VAR'), Blockly.Variables.NAME_TYPE);
  var argument0 = Blockly.JavaScript.valueToCode(block, 'LIST',
      Blockly.JavaScript.ORDER_ASSIGNMENT) || '[]';
  var branch = Blockly.JavaScript.statementToCode(block, 'DO');
  branch = Blockly.JavaScript.addLoopTrap(branch, block.id);
  var code = '';
  // Cache non-trivial values to variables to prevent repeated look-ups.
  var listVar = listVar = Blockly.JavaScript.variableDB_.getDistinctName(
        variable0 + '_list', Blockly.Variables.NAME_TYPE);
  code += 'var ' + listVar + ' = ' + argument0 + ';\n';
  var indexVar = Blockly.JavaScript.variableDB_.getDistinctName(
      variable0 + '_index', Blockly.Variables.NAME_TYPE);
  branch = Blockly.JavaScript.INDENT + variable0 + ' = ' +
      listVar + '[' + indexVar + '];\n' + branch;
  code += 'for (var ' + indexVar + ' in ' + listVar + ') {\n' + branch + '}\n';
  return code;
};


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
      .appendField('GetDocumentProperty')
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
  } else if (expr == 'name') {
     suffix += "getName()";
  } else if (expr == 'type') {
     suffix += "getType()";
  } else {
     suffix += "getPropertyValue('" + expr + "')";
  }
  return [code + suffix,  Blockly.JavaScript.ORDER_NONE];
};



// Automation TextCode
Blockly.Blocks['Automation.TextCode'] = {
  init: function() {
    this.setColour(220);
    this.appendDummyInput()
      .appendField(new Blockly.FieldTextArea(''), 'CODE'); //FieldTextArea
   this.setOutput(true);
 }
};

Blockly.JavaScript['Automation.TextCode'] = function (block) {
  var code = block.getFieldValue('CODE');
  code = code.split("\n").join("\\n");
  return ['"' + code + '"' ,  Blockly.JavaScript.ORDER_NONE];
};

// JSExpression
Blockly.Blocks['Automation.JSExpression'] = {
  init: function() {
    this.setColour(220);
    this.appendDummyInput()
      .appendField('JavaScript Expression')
      .appendField(new Blockly.FieldTextInput('[]'), 'EXPR');
   this.appendValueInput('INPUT')
   this.setOutput(true);
 }
};

Blockly.JavaScript['Automation.JSExpression'] = function (block) {
  var expr = block.getFieldValue('EXPR');
  return [expr,  Blockly.JavaScript.ORDER_NONE];
};


<#list operations as op>

Blockly.Blocks['${op.id}'] = {

  init: function() {
    //this.setHelpUrl(Blockly.Msg.CONTROLS_REPEAT_HELPURL);
    this.setColour(220);
    this.appendDummyInput()
        .appendField("${op.id}")

<#if op.hasInput()>
   var inputField = this.appendValueInput('INPUT')
        .appendField('  Input').setAlign(Blockly.ALIGN_RIGHT);
   <#if op.getInputTypes()??>
       inputField.setCheck(${op.getInputTypes()});
   </#if>
</#if>
<#list op.params as param>
    <#if param.getType()=="boolean">
       this.appendDummyInput().appendField('  ${param.name}').appendField(new Blockly.FieldCheckbox('FALSE'), '${param.name}').setAlign(Blockly.ALIGN_RIGHT);
    <#elseif param.getType()=="integer">
       this.appendValueInput('${param.name}')
           .appendField('  ${param.name}').setCheck('Number').setAlign(Blockly.ALIGN_RIGHT);
    <#else>
       this.appendValueInput('${param.name}')
           .appendField('  ${param.name}').setAlign(Blockly.ALIGN_RIGHT);
    </#if>
</#list>
<#if op.hasOutput()>
   <#if op.getOutputType()??>
     this.setOutput(true, '${op.getOutputType()}');
   <#else>
     this.setOutput(true);
   </#if>
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
       code +=",";
    <#else>
       var pVal = Blockly.JavaScript.valueToCode(block,'${param.name}',Blockly.JavaScript.ORDER_MEMBER);
       if (typeof pVal != 'undefined' && !pVal=='') {
         code += '"${param.name}":' + pVal;
         code +=",";
       }
    </#if>

</#list>
  if (code.lastIndexOf(",")==code.length-1) {
    code = code.substring(0, code.length-1);
  }
  code += '})'

  return [code,  Blockly.JavaScript.ORDER_NONE];
}

</#list>
