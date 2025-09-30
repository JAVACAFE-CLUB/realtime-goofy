group = "com.goofy"
version = "0.0.1-SNAPSHOT"

dependencies {
    /** spring */
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-integration")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    /** arrow-kt */
    implementation("io.arrow-kt:arrow-core:${DependencyVersion.ARROW_FX}")
    implementation("io.arrow-kt:arrow-fx-coroutines:${DependencyVersion.ARROW_FX}")
    implementation("io.arrow-kt:arrow-fx-stm:${DependencyVersion.ARROW_FX}")

    /** swagger */
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:${DependencyVersion.SPRINGDOC}")
    runtimeOnly("com.github.therapi:therapi-runtime-javadoc-scribe:${DependencyVersion.JAVADOC_SCRIBE}")
    kapt("com.github.therapi:therapi-runtime-javadoc-scribe:${DependencyVersion.JAVADOC_SCRIBE}")

    /** db */
    runtimeOnly("com.mysql:mysql-connector-j")

    /** dev */
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    val isMacOS = System.getProperty("os.name").startsWith("Mac OS X")
    val architecture = System.getProperty("os.arch").lowercase()
    if (isMacOS && architecture == "aarch64") {
        developmentOnly("io.netty:netty-resolver-dns-native-macos:${DependencyVersion.MAC_DNS}:osx-aarch_64")
        testImplementation("io.netty:netty-resolver-dns-native-macos:${DependencyVersion.MAC_DNS}:osx-aarch_64")
    }

    /** logger */
    implementation("io.github.oshai:kotlin-logging-jvm:${DependencyVersion.KOTLIN_LOGGING}")
    implementation("net.logstash.logback:logstash-logback-encoder:${DependencyVersion.LOGBACK_ENCODER}")

    /** test */
    testImplementation("org.testcontainers:mysql:${DependencyVersion.TEST_CONTAINER_MYSQL}")
    testImplementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:${DependencyVersion.P6SPY_LOG}")
}
