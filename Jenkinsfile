pipeline {
  agent any
  environment {
    DOCKER_CREDENTIALS_ID = 'docker-hub-cred'
    REPO_URL = 'https://github.com/MyTruong28022004/spring-petclinic-microservices-fork.git'
  }
  parameters {
    string(name: 'BRANCH_NAME', defaultValue: 'main', description: 'Branch to build')
  }
  stages {
    stage('Checkout') {
      steps {
        git branch: "${params.BRANCH_NAME}", url: "${REPO_URL}"
      }
    }

    stage('Detect Changed Services') {
      steps {
        script {
          def prevCommit = sh(script: 'git rev-parse HEAD^ || git rev-parse HEAD', returnStdout: true).trim()
          def changedFiles = sh(script: "git diff --name-only ${prevCommit} HEAD", returnStdout: true).trim().split("\n")

          def services = [
            'spring-petclinic-customers-service',
            'spring-petclinic-vets-service',
            'spring-petclinic-visits-service',
            'spring-petclinic-genai-service'
          ]

          def changedServices = services.findAll { service ->
            changedFiles.any { it.startsWith("${service}/") }
          }

          echo "Changed services: ${changedServices}"
          env.CHANGED_SERVICES = changedServices.join(',')
        }
      }
    }

    stage('Skip Build (No Services Changed)') {
      when {
        not {
          expression { return env.CHANGED_SERVICES?.trim() }
        }
      }
      steps {
        echo "No changed services detected. Skipping build and push."
      }
    }

    stage('Build & Push Changed Images') {
      when {
        expression { return env.CHANGED_SERVICES?.trim() }
      }
      steps {
        script {
          def commitId = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
          def changedServices = env.CHANGED_SERVICES.split(',').findAll { it?.trim() }

          // Ánh xạ service -> image name
          def imageMap = [
            'spring-petclinic-customers-service': 'mytruong28022004/spring-petclinic-customers-service',
            'spring-petclinic-vets-service'    : 'mytruong28022004/spring-petclinic-vets-service',
            'spring-petclinic-visits-service'   : 'mytruong28022004/spring-petclinic-visits-service',
            'spring-petclinic-genai-service'   : 'mytruong28022004/spring-petclinic-genai-service'
          ]

          changedServices.each { service ->
            def imageName = imageMap[service]
            echo "Building and pushing image for: ${service} as ${imageName}"

            dir(service) {
              // Build image using Maven wrapper and Docker profile
              sh "../mvnw clean install -P buildDocker -DskipTests"

              withCredentials([usernamePassword(credentialsId: env.DOCKER_CREDENTIALS_ID, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                sh 'echo $DOCKER_PASSWORD | docker login --username $DOCKER_USERNAME --password-stdin'
                sh "docker tag ${imageName} ${imageName}:${commitId}"
                sh "docker push ${imageName}:${commitId}"
                sh "docker push ${imageName}:latest"
              }
            }
          }
        }
      }
    }
  }
  post {
    always {
      echo "Cleaning up Docker images..."
      sh 'docker system prune -f || true'
    }
  }
}
