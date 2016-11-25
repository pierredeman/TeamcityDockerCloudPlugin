import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test

apply {
    plugin("java")
    plugin("jacoco")
}

val javaCompile = tasks.getByName("compileJava") as JavaCompile
javaCompile.apply {
    sourceCompatibility = "1.7"
    targetCompatibility = "1.7"
    if (project.hasProperty("rt7jar")) {
        options.compilerArgs.add("-Xbootclasspath:${project.properties.get("rt7jar")}")
    }
    options.encoding = "UTF-8"
}

val javaCompileTest = tasks.getByName("compileTestJava") as JavaCompile
javaCompileTest.apply {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
    if (project.hasProperty("rt8jar")) {
        options.compilerArgs.add("-Xbootclasspath:${project.properties.get("rt8jar")}")
    }
    options.encoding = "UTF-8"
}

dependencies {
    compile("de.gesellix:unix-socket-factory:2016-04-06T22-21-19")
    compile("org.glassfish.jersey.core:jersey-client:2.23.1")
    compile("org.glassfish.jersey.media:jersey-media-json-jackson:2.23.1")
    compile("org.glassfish.jersey.connectors:jersey-apache-connector:2.23.1")
    add("provided", "org.apache.logging.log4j:log4j-api:2.5")
    add("provided", "org.apache.logging.log4j:log4j-api:2.5")
    add("provided", "org.atmosphere:atmosphere-runtime:2.2.4")
    add("provided", "org.jetbrains.teamcity:cloud-interface:10.0")
    add("provided", "org.jetbrains.teamcity:server-api:10.0")
    testCompile("org.testng:testng:6.9.13.6")
    testCompile("org.assertj:assertj-core:3.5.2")
}

val test = tasks.getByName("test") as Test
test.apply {
    useTestNG()
    if (project.hasProperty("docker.test.tcp.address")) {
        jvmArgs("-Ddocker.test.tcp.address=${project.properties.get("docker.test.tcp.address")}")
    }
    if (project.hasProperty("docker.test.unix.socket")) {
        jvmArgs("-Ddocker.test.unix.socket=${project.properties.get("docker.test.unix.socket")}")
    }
}

/*
apply plugin: 'java'
apply plugin: "jacoco"

compileJava {
    sourceCompatibility = 1.7
    targetCompatibility = 1.7

    if (project.hasProperty('rt7jar')) {
        options.compilerArgs << "-Xbootclasspath:${project.rt7jar}"
    }
}
compileTestJava {
    // Enable Java 1.8 goodies for testing. God I love gradle.
    sourceCompatibility = 1.8
    targetCompatibility = 1.8

    if (project.hasProperty('rt8jar')) {
        options.compilerArgs << "-Xbootclasspath:${project.rt8jar}"
    }
}

tasks.withType(JavaCompile) {
    options.encoding = 'UTF-8'
}

dependencies {
    compile 'de.gesellix:unix-socket-factory:2016-04-06T22-21-19'
    compile 'org.glassfish.jersey.core:jersey-client:2.23.1'
    compile 'org.glassfish.jersey.media:jersey-media-json-jackson:2.23.1'
    compile 'org.glassfish.jersey.connectors:jersey-apache-connector:2.23.1'
    provided 'org.apache.logging.log4j:log4j-api:2.5'
    provided 'org.atmosphere:atmosphere-runtime:2.2.4'
    provided 'org.jetbrains.teamcity:cloud-interface:10.0'
    provided 'org.jetbrains.teamcity:server-api:10.0'
    testCompile 'org.testng:testng:6.9.13.6'
    testCompile 'org.assertj:assertj-core:3.5.2'
}

jacocoTestReport {
    afterEvaluate {
        classDirectories = files(classDirectories.files.collect {

            fileTree(dir: it, exclude:
                    [
                            // Exclude the ApacheConnector related classes from test code coverage. We do not own most
                            // of the code there.
                            'run/var/teamcity/cloud/docker/client/apcon/**',
                            // Also exclude the package linked to the Atmosphere framework. It only contains a couple
                            // of minimalist classes with a direct dependency Atmosphere which cannot be mocked.
                            'run/var/teamcity/cloud/docker/web/atmo/**'
                    ])
        })
    }
}

test {
    useTestNG()
    if (project.hasProperty('docker.test.tcp.address')) {
        jvmArgs "-Ddocker.test.tcp.address=${project.'docker.test.tcp.address'}"
    }
    if (project.hasProperty('docker.test.unix.socket')) {
        jvmArgs "-Ddocker.test.unix.socket=${project.'docker.test.unix.socket'}"
    }
}

task('quickTest', type: Test) {
    useTestNG()
    options {
        excludeGroups 'integration', 'longRunning'
    }
}

if (project.hasProperty('run.var.teamcity.cloud.docker.tcSvcMsg') && Boolean.valueOf(project.'run.var.teamcity.cloud.docker.tcSvcMsg')) {
    println "##teamcity[jacocoReport dataPath='server/build/jacoco/test.exec' includes='run.var.*' excludes='run.var.teamcity.cloud.docker.client.apcon.* run.var.teamcity.cloud.docker.web.atmo.*']"
}
*/