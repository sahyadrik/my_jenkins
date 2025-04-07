def call(body){
    pipeline{
        agent{ label 'node_1'}
        environment{
            GIT_ACCOUNT = credentials('Git')
        }
        stages{            
            stage ('Checkout') {
                steps {
                    cleanWs()
                    echo "Checking out the git repository"
                    sh "git clone --recursive https://github.com/sahyadrik/simple-java-maven-app.git"  
                }
            }
            stage ('Build') {
                steps {
                    echo "Building with maven"
                    sh """
                        cd $WORKSPACE/simple-java-maven-app
                        pwd
                        /opt/maven/bin/mvn -B -DskipTests clean package
                    """
                }
            }
            stage ('Test') {
                steps {
                    echo "Running the Tests"
                    sh ' /opt/maven/bin/mvn test '
                }
                post{
                    always {
                        junit 'target/surefire-reports/*.xml'
                    }
                }
            }
            stage ('Deploy') {
                steps {
                    echo "Deploying the package to the artifactory"
                }
            }
        }
    }
}