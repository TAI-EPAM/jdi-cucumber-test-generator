<#if suit.tags?has_content>
<#assign t = "">
<#list suit.tags?split(" ") as tags><#if tags?has_content><#assign t += tags?ensure_starts_with('@') + " "></#if></#list>
${t?trim}
</#if>
Feature: ${suit.description}
    <#list suit.cases as case>

    <#if case.tags?has_content>
    <#assign s = "">
    <#list case.tags as tags><#if tags.name?has_content><#assign s += tags.name?ensure_starts_with('@') + " "></#if></#list>
    ${s?trim}
    </#if>
    Scenario: ${case.description}
         <#list case.steps as step>
            ${step.type.typeName} ${step.description}
         </#list>
    </#list>