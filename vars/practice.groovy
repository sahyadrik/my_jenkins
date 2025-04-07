def call(body){
    pipeline{
        agent{ label 'node_1'}
        environment{
            GIT_ACCOUNT = credentials('Git')
        }
        stages{
            cleanWs()
            stage ('Checkout') {
                steps {
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
                        mvn -B -DskipTests clean package
                    """
                }
            }
            stage ('Test') {
                steps {
                    echo "Running the Tests"
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