@NonCPS
def notifyStarted(String message) {
    slackSend(color: '#FFFF00', message: "${message} Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
}

pipeline {
    agent any

    environment {
        JAR_VERSION = ''
        JAR_NAME = ''
    }

    stages {
        stage('Fetch') {
            steps {
                script {
                    echo "General SCM. Cloning repositories from Github..."
                }
            }

            post {
                failure {
                    echo "Fetch failed in Jenkins!"
                }
            }
        }

        stage('Flyway DB Migration') {
            steps {
                dir("${WORKSPACE}/musical-world-db-deploy/") {
                    sh 'mvn flyway:clean -P develop'
                    sh 'mvn flyway:migrate -P develop'
                }
            }

            post {
                failure {
                    echo "Flyway DB Migration failed in Jenkins!"
                }
            }
        }

        stage('Build') {
            steps {
                sh "'${M2_HOME}/bin/mvn' clean install -Dtest=SomePatternThatDoesntMatchAnything -DskipITs -DfailIfNoTests=false"
            }

            post {
                failure {
                    echo "Build failed in Jenkins!"
                }
            }
        }

        stage('Tests') {
            parallel {
                stage('unit tests') {
                    steps {
                        sh "'${M2_HOME}/bin/mvn' surefire:test"
                    }

                    post {
                        failure {
                            echo "Unit tests failed in Jenkins!"
                        }
                    }
                }

                stage('integration tests') {
                    steps {
                        sh "'${M2_HOME}/bin/mvn' failsafe:integration-test -P integration-test-without-smoke-tests"
                    }

                    post {
                        failure {
                            echo "Integration tests failed in Jenkins!"
                        }
                    }
                }

                stage('smoke tests') {
                    steps {
                        sh "'${M2_HOME}/bin/mvn' failsafe:integration-test -P smoke-tests"
                    }

                    post {
                        failure {
                            echo "Smoke tests failed in Jenkins!"
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            echo "All is well! Code is tested, built and deployed."
        }
    }
}