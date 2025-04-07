pipeline {
    agent Node_Pi  // Run on any available agent

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out the code from repository (if applicable)'
                // If you have a Git repository, use the following:
                // git 'https://your-repository-url.git'
            }
        }

        stage('Build') {
            steps {
                echo 'Building the application...'
                // Add build commands here, e.g., compile or Docker build
                // Example: sh 'mvn clean install' for a Maven project
            }
        }

        stage('Test') {
            steps {
                echo 'Running tests...'
                // Add test commands here, e.g., run unit tests
                // Example: sh 'mvn test' for a Maven project
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying the application...'
                // Add deployment steps here, e.g., deploy to a server
                // Example: sh './deploy.sh' to run a custom deploy script
            }
        }
    }

    post {
        always {
            echo 'This will always run, no matter if the pipeline passes or fails.'
        }

        success {
            echo 'This runs only if the pipeline is successful.'
        }

        failure {
            echo 'This runs only if the pipeline fails.'
        }
    }
}
