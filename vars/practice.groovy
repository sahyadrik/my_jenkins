def call(body){
    pipeline{
        agent{ label 'node_1'}
        options {
            skipStagesAfterUnstable()
        }
        environment{
            GIT_ACCOUNT = credentials('Git')
            REPO_PATH = "$WORKSPACE/simple-java-maven-app"
            TARGET_F_PATH = "$WORKSPACE/simple-java-maven-app/target"
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
                        cd $REPO_PATH
                        pwd
                        /opt/maven/bin/mvn -B -DskipTests clean package
                    """
                }
            }
            stage ('Test') {
                steps {
                    echo "Running the Tests"
                    sh """ 
                        cd $REPO_PATH
                        pwd
                        /opt/maven/bin/mvn test 
                    """
                }
                post{
                    always {
                        junit 'simple-java-maven-app/target/surefire-reports/*.xml'
                    }
                }
            }
            stage ('Deployment_Confirmation') {
                steps {
                    echo "Delivering the product"
                    sh '$REPO_PATH/jenkins/scripts/deliver.sh -p $TARGET_F_PATH'
                }
            }
            stage ('Deploy') {
                steps {
                    echo "Deploying the package to Tomcat"
                    withCredentials ([usernamePassword(credentialsId:'tomcat_pi'), usernameVariable: 'TOMCAT_USER', passwordVariable: 'TOMCAT_PASS')]) {
                    sh """
                        WAR_FILE=\$(ls $TARGET_F_PATH/*.war | head -n 1)
                        echo "Deploying \$WAR_FILE to Tomcat..."

                        curl -v --upload-file \$WAR_FILE \
                        "http://batman.local:8080/manager/text/deploy?path=/myapp&update=true" \
                        --user \$TOMCAT_USER:\$TOMCAT_PASS
                    """
                    }
                }
            }
        }
    }
}