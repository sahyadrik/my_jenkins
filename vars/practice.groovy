def call(body){
    pipeline{
        agent{ label 'node_1'}

        stages{
            stage ('Checkout') {
                steps {
                    echo "Checking out the git repository"
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
                    echo "Deploying out the git repository"
                }
            }
        }
    }
}