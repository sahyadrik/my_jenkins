def call(body){
    pipeline{
        agent{ label 'node_1'}
        environment{
            GIT_ACCOUNT = credentials('Git')
        }
        stages{
            stage ('Checkout') {
                steps {
                    echo "Checking out the git repository"
                    echo $GIT_ACCOUNT
                    sh "git clone --recursive https://github.com/sahyadrik/my_jenkins.git"  
                }
            }
            stage ('Build') {
                steps {
                    echo "Building with maven"
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