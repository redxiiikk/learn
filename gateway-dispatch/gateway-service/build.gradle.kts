group = "com.github.redxiiikk.learn.gateway-dispatch"
version = "1.0-SNAPSHOT"

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("com.github.redxiiikk.spring.cloud:spring-cloud-starter-gateway-extension:0.0.1-SNAPSHOT")
}
