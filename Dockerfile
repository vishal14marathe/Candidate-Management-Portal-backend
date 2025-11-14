# Use Eclipse Temurin JDK 17 (most reliable for Docker)
FROM eclipse-temurin:17-jdk-alpine

# Add JVM options for better container performance
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UnlockExperimentalVMOptions -XX:+UseContainerSupport"

# Set working directory
WORKDIR /app

# Copy the JAR file (matches finalName in pom.xml)
COPY target/candidate-management.jar app.jar

# Create a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]