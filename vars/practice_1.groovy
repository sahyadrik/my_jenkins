def call(body){
    pipeline{
        agent{ label 'node_1'}
        options {
            skipStagesAfterUnstable()
        }
        environment{
            GIT_ACCOUNT = credentials('Git')
            REPO_PATH = "$WORKSPACE/maven-practice1"
            TARGET_PATH = "$WORKSPACE/maven-practice1/webapp/target"
            TOMCAT_PATH = "/var/lib/tomcat10/webapps"
        }
        stages{
            stage ('Checkout') {
                steps {
                    cleanWs()
                    echo "Checking out the git repository"
                    sh "git clone --recursive https://github.com/sahyadrik/maven-practice1.git"  
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
                        junit 'maven-practice1/server/target/surefire-reports/*.xml'
                    }
                }
            }
            stage('Deploy') {
                steps {
                    echo "Deploying the package to Tomcat"
                    sh """
                        sudo rm -rf $TOMCAT_PATH/webapp.war 
                        sudo cp  $TARGET_PATH/webapp.war $TOMCAT_PATH
                        sudo systemctl restart tomcat10
                    """
                }
                post{
                    always {
                        script{
                            def webAppUrl = 'http://batmanubuntu:8080/webapp'
                            currentBuild.description = "Access the web application here: \
                            <a href='${webAppUrl}' target='_blank'>WebApp</a>"
                        }
                    }
                }
            }
        }
    }
}