pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh '''
          export M2_HOME=/opt/apache-maven-3.5.0
          export M2="$M2_HOME"/bin
          export PATH=$PATH:$M2
          mvn clean
          mvn package
        '''
      }
    }
    stage('Keep war files') {
      steps {
        archiveArtifacts(artifacts: 'target/*.war,target/*.war.original', onlyIfSuccessful: true)
      }
    }
    stage('Rename main war') {
      steps {
        sh 'mv ${WORKSPACE}/target/throdi-backend-1.0.0.war.original ${WORKSPACE}/target/ROOT.war'
      }
    }
    stage('Deploy to staging') {
      steps {
        sh '''
          #!/bin/sh

          az webapp stop --resource-group Throdi --name ThrodiBackend --slot staging
          sleep 10

          custom_dir=/var/lib/jenkins/custom

          # check if the app.log file exists
          lftp -c "open -u ThrodiBackend__staging\\\\\\$ThrodiBackend__staging,cQjl0F3nKw9k4Lmiho5YHZTxSkauRHt0EKe4Qo6ZfRZhxfdlgeBfMoFwGjqS ftp://waws-prod-am2-121.ftp.azurewebsites.windows.net/site/wwwroot; ls app.log" > /tmp/result 2> /dev/null

          log_exists=0
          if [[ -s /tmp/result ]]; then
              log_exists=1
          fi

          if ((log_exists == 1)); then
              lftp -c "open -u ThrodiBackend__staging\\\\\\$ThrodiBackend__staging,cQjl0F3nKw9k4Lmiho5YHZTxSkauRHt0EKe4Qo6ZfRZhxfdlgeBfMoFwGjqS ftp://waws-prod-am2-121.ftp.azurewebsites.windows.net/site/wwwroot;
                       get -E -e app.log -o ${custom_dir}/logs_devel/app.log;
                       mrm webapps/*;
                       put ${WORKSPACE}/target/ROOT.war -o webapps/ROOT.war"

              if [ ${BRANCH_NAME} != "master" ]; then
                  mv ${custom_dir}/logs_devel/app.log ${custom_dir}/logs_devel/$(date -u +"%FT%H%MZ").log
              else
                  mv ${custom_dir}/logs_prod/app.log ${custom_dir}/logs_prod/$(date -u +"%FT%H%MZ").log
              fi
          else
              lftp -c "open -u ThrodiBackend__staging\\\\\\$ThrodiBackend__staging,cQjl0F3nKw9k4Lmiho5YHZTxSkauRHt0EKe4Qo6ZfRZhxfdlgeBfMoFwGjqS ftp://waws-prod-am2-121.ftp.azurewebsites.windows.net/site/wwwroot;
                       mrm webapps/*;
                       put ${WORKSPACE}/target/ROOT.war -o webapps/ROOT.war"
          fi

          az webapp start --resource-group Throdi --name ThrodiBackend --slot staging
        '''
      }
    }
    stage('Check if app runs correctly') {
      steps {
        sh '''
            sleep 180
            attempts=0
            response=$(curl "http://throdibackend-staging.azurewebsites.net/api/hotels/1" | jq .name)

            while [[ attempts < 7 && response == "null" ]]; do
                response=$(curl "http://throdibackend-staging.azurewebsites.net/api/hotels/1" | jq .name)
                attempts=$((attempts + 1))
                sleep 60
            done

            if ((response != 200)); then
                exit 1
            fi
        '''
      }
    }
    stage('Swap slots') {
      steps {
        sh '''
          if [ ${BRANCH_NAME} != "master" ]; then
            az webapp deployment slot swap --resource-group Throdi --name ThrodiBackend --slot staging --target-slot devel
          else
              az webapp deployment slot swap --resource-group Throdi --name ThrodiBackend --slot staging --target-slot production
          fi
        '''
      }
    }
  }
  post {
    success {
        sh '''
          if [ ${BRANCH_NAME} != "master" ]; then
            /var/lib/jenkins/custom/send_email "Successful deployment to devel" ${BUILD_URL}
          else
            /var/lib/jenkins/custom/send_email "Successful deployment to master" ${BUILD_URL}
          fi
        '''
    }
    failure {
        sh '''
          if [ ${BRANCH_NAME} != "master" ]; then
            /var/lib/jenkins/custom/send_email "Failed deployment to devel" ${BUILD_URL}
          else
            /var/lib/jenkins/custom/send_email "Failed deployment to master" ${BUILD_URL}
          fi
        '''
    }
  }
}