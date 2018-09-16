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
                    notifyStarted("Fetch failed in Jenkins!")
                }
            }
        }

        stage('Flyway DB Migration') {
            steps {
                dir("${WORKSPACE}/musical-world-db-deploy/") {
                    sh "'${M2_HOME}/bin/mvn' flyway:clean -P develop -Denv.DATASOURCE_URL=$DATASOURCE_URL -Denv.DATASOURCE_USERNAME=$DATASOURCE_USERNAME -Denv.DATASOURCE_PASSWORD=$DATASOURCE_PASSWORD"
                    sh "'${M2_HOME}/bin/mvn' flyway:migrate -P develop -Denv.DATASOURCE_URL=$DATASOURCE_URL -Denv.DATASOURCE_USERNAME=$DATASOURCE_USERNAME -Denv.DATASOURCE_PASSWORD=$DATASOURCE_PASSWORD"
                }
            }

            post {
                failure {
                    notifyStarted("Flyway DB Migration failed in Jenkins!")
                }
            }
        }

        stage('Build') {
            steps {
                sh "'${M2_HOME}/bin/mvn' clean install -Dtest=SomePatternThatDoesntMatchAnything -DskipITs -DfailIfNoTests=false"
            }

            post {
                failure {
                    notifyStarted("Build failed in Jenkins!")
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
                            notifyStarted("Unit tests failed in Jenkins!")
                        }
                    }
                }

                stage('integration tests') {
                    steps {
                        sh "'${M2_HOME}/bin/mvn' failsafe:integration-test -P integration-test-without-smoke-tests"
                    }

                    post {
                        failure {
                            notifyStarted("Integration tests failed in Jenkins!")
                        }
                    }
                }

                stage('smoke tests') {
                    steps {
                        sh "'${M2_HOME}/bin/mvn' failsafe:integration-test -P smoke-tests"
                    }

                    post {
                        failure {
                            notifyStarted("Smoke tests failed in Jenkins!")
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            notifyStarted("All is well! Code is tested, built and deployed.")
        }
    }
}