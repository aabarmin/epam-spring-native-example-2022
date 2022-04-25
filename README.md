# GraalVM and Spring Native code examples

Directory structure:

- `slides` - slides to demonstrate
- `examples` - examples to be demonstrated

# Examples

- [Installing GraalVM using SDKMan](#installing-graalvm-using-sdkman)
- [Hello World example](#hello-world-example)
- [Hello World Native Image example](#hello-world-native-image-example)
- [Hello World + reflection example](#hello-world--reflection-example)

## Installing GraalVM using SDKMan

1. Install SDKMan from https://sdkman.io
2. Install GraalVM using SDKMan:
   - `sdk list java`
   - `sdk install java <version>`
3. Configure Java version to be used in the current terminal session:
   - `sdk use java <version>`
   - `sdk default java <version>`

## Hello World example

The first example is a simple `Hello, World!` application which is built using GraalVM.

Example is in the `./examples/example-basic` folder.

Execute the following command to build and run it:

```shell
$ sdk use java 22.0.0.2.r17-grl
$ mvn clean compile
$ cd target/classes
$ java dev.abarmin.graalvm.HelloWorldApplication
```

## Hello World Native image example

The next step is to build a native image using GraalVM. Before starting it is necessary to make sure that `native-image` plugin is installed.

Execute the following command to make sure `native-image` is installed.

```shell
$ gu list
```

If not installed, execute the command below to install:

```shell
$ gu install native-image
```

Next we'll build a native image for the existing "Hello, World" project.

```shell
$ mvn clean compile
$ cd target/classes
$ native-image dev.abarmin.graalvm.HelloWorldApplication
```

## Hello World Native image with Apache Maven example

In order not to build the native image on your own, it is possible to use Apache Maven native image plugin. First, it is necessary to add the plugin and in the configuration section add the `<mainClass />` value:

```xml
<configuration>
    <mainClass>dev.abarmin.graalvm.HelloWorldApplication</mainClass>
</configuration>
```

To build the image using Apache Maven, execute the following command:

```shell
$ mvn clean package -P native
```

## Hello World + reflection example

Moving forward to show limitations of the GraalVM and Native Image. The main limitation is that all the compiler should be aware of all the classes used during the compilation. In the `example-reflection` folder there is a new Apache Maven project which receives class name from the command prompt and next loads the class into the JVM, creates an instance of the given class and goes through its declared methods.

- If JVM is used, the code is working as expected.
- If native image is built without `--no-fallback` option the code is working but requires JVM for execution.
- If native image is built with `--no-fallback` option, it's necessary to add configuration for reflection.

When the image is built it is possible to run it. When running the following error message will be displayed:

```shell
$ target git:(main) ✗ ./hello-world-app dev.abarmin.graalvm.HelloWorldProvider

Class to be loaded: dev.abarmin.graalvm.HelloWorldProvider
Exception in thread "main" java.lang.ClassNotFoundException: dev.abarmin.graalvm.HelloWorldProvider
	at java.lang.Class.forName(DynamicHub.java:1338)
	at java.lang.Class.forName(DynamicHub.java:1313)
	at dev.abarmin.graalvm.HelloWorldApplication.main(HelloWorldApplication.java:21)
```

In order to fix the problem will add configuration files one by one. First, we need to add a file which allows to load classes via `Class.forName()`.

Adding `native-image.properties` file to the `META-INF/native-image/dev.abarmin.graalvm/example-reflection` folder. Content should be the following:

```
Args = -H:ReflectionConfigurationFiles=./classes/${.}/reflection-config.json
```

This file refers to the `reflection-config.json` file. First, I'm adding the following content to the file:

```json
[
  {
    "name": "dev.abarmin.graalvm.HelloWorldProvider"
  }
]
```

When the project is built and executed, another error is displayed:

```shell
$ target git:(main) ✗ ./hello-world-app dev.abarmin.graalvm.HelloWorldProvider

Class to be loaded: dev.abarmin.graalvm.HelloWorldProvider
Class is loaded
Exception in thread "main" java.lang.NoSuchMethodException: dev.abarmin.graalvm.HelloWorldProvider.<init>()
	at java.lang.Class.getConstructor0(DynamicHub.java:3585)
	at java.lang.Class.getDeclaredConstructor(DynamicHub.java:2754)
	at dev.abarmin.graalvm.HelloWorldApplication.main(HelloWorldApplication.java:24)
```

This error message means that the class has bean loaded but the constructor cannot be called - it is necessary to make the native image compiler aware of this constructor. Updating the `reflection-config.json` file by adding the following:

```json
[
  {
    "name": "dev.abarmin.graalvm.HelloWorldProvider",
    "methods": [
      {
        "name": "<init>",
        "parameterTypes": []
      }
    ]
  }
]
```

After that the class will be loaded, instance will be created however reflection will not work. There are two options to make it working - add all the methods to the list or use the following config:

```json
[
  {
    "name": "dev.abarmin.graalvm.HelloWorldProvider",
    "queryAllDeclaredMethods": true,
    "methods": [
      {
        "name": "<init>",
        "parameterTypes": []
      }
    ]
  }
]
```

Alternatively, we can implement a `Feature` interface in the following way:

```java
public class RuntimeReflectionConfigurationFeature implements Feature {
  @Override
  public void beforeAnalysis(BeforeAnalysisAccess access) {
    RuntimeReflection.register(HelloWorldProvider.class);
    RuntimeReflection.registerForReflectiveInstantiation(HelloWorldProvider.class);
    RuntimeReflection.register(HelloWorldProvider.class.getDeclaredMethods());
  }
}
```

The feature needs to be registered in the `native-image.properties` file using the following parameter:

```
Args = --features=dev.abarmin.graalvm.RuntimeReflectionConfigurationFeature
```

Another important aspect is that tests are ordinary executed using JVM version of the application. If you take a look into the test code you'll see that the `MessageProvidersTest` test is executed successfully. However, it will fail in case of execution against the native image:

```shell
$ ./mvnw clean package -P native-test

...

JUnit Platform on Native Image - report
----------------------------------------

dev.abarmin.graalvm.MessageProvidersTest > [1] dev.abarmin.graalvm.HelloWorldProvider, Hello, World! SUCCESSFUL

dev.abarmin.graalvm.MessageProvidersTest > [2] dev.abarmin.graalvm.TestMessageProvider, Hello from test FAILED

Failures (1):
  JUnit Jupiter:MessageProvidersTest:test(String, String):[2] dev.abarmin.graalvm.TestMessageProvider, Hello from test
    MethodSource [className = 'dev.abarmin.graalvm.MessageProvidersTest', methodName = 'test', methodParameterTypes = 'java.lang.String, java.lang.String']
    => java.lang.ClassNotFoundException: dev.abarmin.graalvm.TestMessageProvider
       [...]

Test run finished after 7 ms
[         3 containers found      ]
[         0 containers skipped    ]
[         3 containers started    ]
[         0 containers aborted    ]
[         3 containers successful ]
[         0 containers failed     ]
[         2 tests found           ]
[         0 tests skipped         ]
[         2 tests started         ]
[         0 tests aborted         ]
[         1 tests successful      ]
[         1 tests failed          ]
```

## Spring Native example

When a new Spring Boot project is created, it is possible to add Spring Native to the list of dependencies and during the `package` phase pre-configured Apache Maven plugins will build not only fat jars but also native images.

To use this opportunity you have two options:

- add `-P native` to the Apache Maven command (`./mvnw clean package -P native`)
- use `spring-boot:build-image` Apache Maven goal

In the first case, the executable will be built locally using locally installed GraalVM and Native Image distribution. In the second case, an executable will be built inside a docker container using Buildpacks. As a result, you'll get a Docker image with the native executable inside. That's good because there is no cross-compilation - you can't build a native executable on MacOS and next pack it into the container with Linux inside.

The build itself takes time however the executable then starts very quick:

```shell
Example goes here...
```

Nevertheless, everything that is happening under the hood is generation of the `reflect-config.json` file by Spring AOT plugin. Let's take a look into it's capabilities.

If you run the `spring-aot:generate` goal you'll see that many files are generated for the existing Spring application.
