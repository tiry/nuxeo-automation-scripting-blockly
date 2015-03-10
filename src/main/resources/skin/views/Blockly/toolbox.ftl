    <xml id="toolbox" style="display: none">
      <category name="Control">
      <block type="controls_if"></block>
      <block type="controls_whileUntil"></block>
      <block type="controls_for"></block>
      <block type="controls_forEach"></block>
      <block type="controls_flow_statements"></block>
     </category>
     <category name="Functions" custom="PROCEDURE"></category>
     <category name="Variables" custom="VARIABLE"></category>
     <category name="Text">
      <block type="text"></block>
      <block type="text_length"></block>
      <block type="text_print"></block>
      <block type="text_prompt_ext">
        <value name="TEXT">
          <block type="text"></block>
        </value>
      </block>
    </category>
    <category name="Math">
      <block type="math_number"></block>
      <block type="math_arithmetic"></block>
      <block type="math_single"></block>
      <block type="math_trig"></block>
      <block type="math_constant"></block>
      <block type="math_number_property"></block>
      <block type="math_change">
        <value name="DELTA">
          <block type="math_number">
            <field name="NUM">1</field>
          </block>
        </value>
      </block>
      <block type="math_round"></block>
      <block type="math_on_list"></block>
      <block type="math_modulo"></block>
      <block type="math_constrain">
        <value name="LOW">
          <block type="math_number">
            <field name="NUM">1</field>
          </block>
        </value>
        <value name="HIGH">
          <block type="math_number">
            <field name="NUM">100</field>
          </block>
        </value>
      </block>
      <block type="math_random_int">
        <value name="FROM">
          <block type="math_number">
            <field name="NUM">1</field>
          </block>
        </value>
        <value name="TO">
          <block type="math_number">
            <field name="NUM">100</field>
          </block>
        </value>
      </block>
      <block type="math_random_float"></block>
    </category>
    <category name="Logic">
        <block type="logic_compare"></block>
        <block type="logic_operation"></block>
        <block type="logic_boolean"></block>
    </category>
    <sep></sep>
    <category name="Automation">
      <category name="Helpers">
          <block type="Automation.SwallowOutput"></block>
          <block type="Automation.GetDocumentProperty"></block>
          <block type="Automation.JSExpression"></block>
      </category>
      <#list This.getCategories() as cat>
      <category name="${cat}">
         <#list This.getOperationIdsForCategory(cat) as opId>
          <block type="${opId}"></block>
         </#list>
      </category>
      </#list>
   </category>
    </xml>
