# GraalVM and Spring Native code examples

Directory structure:

- `slides` - slides to demonstrate
- `examples` - examples to be demonstrated

# Examples

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
