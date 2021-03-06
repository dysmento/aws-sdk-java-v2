${fileHeader}
package ${metadata.fullModelPackageName};

import javax.annotation.Generated;
<#if shouldEmitStructuredPojoInterface>
import software.amazon.awssdk.protocol.StructuredPojo;
import software.amazon.awssdk.protocol.ProtocolMarshaller;
</#if>

<#if shape.documentation?has_content || awsDocsUrl?has_content>
/**
<#if shape.documentation?has_content> * ${shape.documentation}</#if>
<#if awsDocsUrl?has_content> * ${awsDocsUrl}</#if>
 */
</#if>
<#if shape.deprecated>
@Deprecated
</#if>
@Generated("software.amazon.awssdk:aws-java-sdk-code-generator")
public class ${shape.shapeName} <#if baseClassFqcn??>extends ${baseClassFqcn}</#if>
    implements Cloneable<#if shouldEmitStructuredPojoInterface>, StructuredPojo</#if> {

    <@VariableDeclarationMacro.content shape/>

    <#if shape.additionalConstructors?has_content>
        <@ConstructorDefinitionMacro.content shape/>
    </#if>

    <@MethodDefinitionMacro.content customConfig shape shape.shapeName/>

    <#-- Injection point for adding custom code to model classes -->
    <#if CustomModelClassMethodsMacro??>
        <@CustomModelClassMethodsMacro.content shape/>
    </#if>

    <@OverrideMethodsMacro.content shape/>

    @Override
    public ${shape.shapeName} clone() {
        try {
            return (${shape.shapeName}) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(
                    "Got a CloneNotSupportedException from Object.clone() "
                    + "even though we're Cloneable!",
                    e);
        }
    }

    <#if shouldEmitStructuredPojoInterface>
    @software.amazon.awssdk.annotation.SdkInternalApi
    @Override
    public void marshall(ProtocolMarshaller protocolMarshaller) {
       ${transformPackage}.${shape.shapeName}Marshaller.getInstance().marshall(this, protocolMarshaller);
    }
    </#if>
}
