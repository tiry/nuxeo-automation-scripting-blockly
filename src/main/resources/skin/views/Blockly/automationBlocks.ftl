'use strict';

goog.provide('Blockly.Blocks.automation');

goog.require('Blockly.Blocks');


<#list operations as op>

Blockly.Blocks['${op.id}'] = {
  /**
   * Block for repeat n times (internal number).
   * @this Blockly.Block
   */
  init: function() {
    //this.setHelpUrl(Blockly.Msg.CONTROLS_REPEAT_HELPURL);
    this.setColour(220);
    this.appendDummyInput()
        .appendField("${op.id}")
        .appendField(new Blockly.FieldVariable('Input'), 'INPUT')
<#list op.documentation.params as param>        
        .appendField(new Blockly.FieldVariable('${param.name}'), '${param.name}')
</#list>        
        .appendField("");
    this.setPreviousStatement(true);
    this.setNextStatement(true);  
    //this.setOutput(true);
    this.setInputsInline(false);  
    this.setTooltip("${op.documentation.label}");
  }
};
</#list>
