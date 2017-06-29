/*
 * Copyright 2010-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.codegen.poet.client;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import javax.lang.model.element.Modifier;
import software.amazon.awssdk.codegen.emitters.GeneratorTaskParams;
import software.amazon.awssdk.codegen.model.intermediate.IntermediateModel;
import software.amazon.awssdk.codegen.poet.ClassSpec;
import software.amazon.awssdk.codegen.poet.PoetExtensions;
import software.amazon.awssdk.codegen.poet.PoetUtils;

public class ClientSimpleMethods implements ClassSpec {

    private final IntermediateModel model;
    private final PoetExtensions poetExtensions;

    public ClientSimpleMethods(GeneratorTaskParams params) {
        this.model = params.getModel();
        this.poetExtensions = params.getPoetExtensions();
    }

    @Override
    public TypeSpec poetSpec() {

        ClassName className = poetExtensions.getClientClass("SimpleMethodsIntegrationTest");
        ClassName interfaceClass = poetExtensions.getClientClass(model.getMetadata().getSyncInterface());

        ParameterizedTypeName name = ParameterizedTypeName.get(List.class, String.class);
        return PoetUtils.createClassBuilder(className)
                        .addModifiers(Modifier.PUBLIC)
                        .addField(FieldSpec.builder(name, "IGNORED_METHODS")
                                           .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                                           .initializer("$T.asList(\"close\", \"init\", \"createErrorResponseHandler\")",
                                                        Arrays.class)
                                           .build())
                        .addField(interfaceClass, "client", Modifier.PRIVATE, Modifier.STATIC)
                        .addMethod(setup())
                        .addMethod(simpleMethodsTest())
                        .build();
    }

    @Override
    public ClassName className() {
        return poetExtensions.getClientClass("SimpleMethodsIntegrationTest");
    }

    private MethodSpec setup() {
        ClassName beforeClass = ClassName.get("org.junit", "BeforeClass");
        ClassName interfaceClass = poetExtensions.getClientClass(model.getMetadata().getSyncInterface());
        return MethodSpec.methodBuilder("setup")
                         .addAnnotation(beforeClass)
                         .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                         .addStatement("client = $T.create()", interfaceClass)
                         .build();
    }

    private MethodSpec simpleMethodsTest() {
        ClassName testClass = ClassName.get("org.junit", "Test");
        return MethodSpec.methodBuilder("simpleMethods_Succeed")
                         .addAnnotation(testClass)
                         .addException(Exception.class)
                         .addModifiers(Modifier.PUBLIC)
                         .addStatement("$T[] methods = client.getClass().getDeclaredMethods()", Method.class)
                         .beginControlFlow("for (Method method : methods)")
                         .beginControlFlow("if (method.getParameterCount() == 0 && !method.isSynthetic() && " +
                                           "!IGNORED_METHODS.contains(method.getName()))")
                         .addStatement("System.out.println(\"Invoking: \" + method.getName())")
                         .addStatement("method.invoke(client, null)")
                         .endControlFlow()
                         .endControlFlow()
                         .build();
    }
}
