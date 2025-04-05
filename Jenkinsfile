pipeline {
    agent any
    options {
        skipDefaultCheckout()
    }
    environment {
      //Cấu hình các biến cần thiết
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Determine Changed Services') {
            steps {
                script {
                    def changedServices = sh(script: '''
                        git fetch origin main
                        git diff --name-only origin/main...HEAD | awk -F/ '{print $1}' | sort -u
                    ''', returnStdout: true).trim().split('\n')

                    def allServices = ['spring-petclinic-vets-service', 'spring-petclinic-visits-service', 'spring-petclinic-customers-service']
                    env.SERVICES_TO_BUILD = allServices.intersect(changedServices).join(',')
                }
            }
        }

        stage('Build and Test') {
            when {
                expression { return env.SERVICES_TO_BUILD?.trim() }
            }
            steps {
                script {
                    def services = env.SERVICES_TO_BUILD.split(',')
                    for (s in services) {
                        dir("${s}") {
                            // Sử dụng Maven mặc định để build và test
                            sh "mvn clean test"
                            
                            // Báo cáo kết quả unit test
                            junit '**/target/surefire-reports/*.xml'
                            
                            // Sử dụng JaCoCo (hoặc Cobertura) để tạo coverage report
                            jacoco execPattern: '**/target/jacoco.exec', classPattern: '**/target/classes', sourcePattern: '**/src/main/java'
                            
                            // Nếu bạn muốn tiếp tục package ứng dụng, có thể chạy lại lệnh clean package
                            sh "mvn clean package"
                        }
                    }
                }
            }
        }
    }
    post {
        always {
            echo "Pipeline complete"
        }
    }
}
