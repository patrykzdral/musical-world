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

       /* stage('Results') {
            steps {
                archiveArtifacts artifacts: '*/target/*exec.jar,*/target/failsafe-reports/TEST-*.xml,*/target/surefire-reports/TEST-*.xml', allowEmptyArchive: true
                junit '*/target/failsafe-reports/TEST-*.xml'
                junit '*/target/surefire-reports/TEST-*.xml'
            }

            post {
                failure {
                    notifyStarted("Results failed in Jenkins!")
                }
            }
        }

        stage('VPS Deployment') {
            steps {
                dir("${WORKSPACE}/scripts/") {
                    sh 'mkdir -p ../deployment/'
                    sh 'sudo chown jenkins:jenkins -R ../deployment'

                    sh "chmod +x getJarName.sh"
                    script {
                        JAR_NAME = sh(
                                script: './getJarName.sh',
                                returnStdout: true
                        ).trim()
                    }

                    sh "chmod +x getJarVersion.sh"
                    script {
                        JAR_VERSION = sh(
                                script: "./getJarVersion.sh ${JAR_NAME}",
                                returnStdout: true
                        ).trim()
                    }

                    sh "chmod +x runDeployment.sh"
                    sh "./runDeployment.sh ${JAR_NAME} ${JAR_VERSION}"
                }
            }

            post {
                failure {
                    notifyStarted("VPS Deployment failed in Jenkins!")
                }
            }
        } */

        /*   stage('Docker Hub Push') {
               steps {
                   dir("${WORKSPACE}/scripts/") {
                       sh "chmod +x runDockerHubPush.sh"
                       sh "./runDockerHubPush.sh ${JAR_VERSION}"
                   }
               }

               post {
                   failure {
                       notifyStarted("Docker Hub Push failed in Jenkins!")
                   }
               }
           }*/
    }

    post {
        success {
            notifyStarted("All is well! Code is tested, built and deployed.")
        }
    }
}