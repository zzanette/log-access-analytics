pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh '''
                    mvn clean install
                '''
            }
            post {
                success {
                    archiveArtifacts 'target/log-access-analytics.jar'
                    archiveArtifacts 'packer.json'
                    archiveArtifacts 'deploy/*'
                }
            }
        }
        stage('Packer Build AMI') {
             steps {
                  sh '''
                    wget -c https://releases.hashicorp.com/packer/1.1.0/packer_1.1.0_linux_amd64.zip
                    unzip -o packer_1.1.0_linux_amd64.zip
                    packer_out=$(./packer build -machine-readable packer.json | awk -F, '$0 ~/artifact,0,id/ {print $6}')
                    ami=$(echo "$packer_out" | tail -n 2 | grep -oP 'ami.+')
                    echo '' > amivar.tf.template
                    echo 'variable "APP_INSTANCE_AMI" { default = "${AMI_GENERATED}" }' >> amivar.tf.template
                    export AMI_GENERATED="$ami" && envsubst < amivar.tf.template > amivar.tf
                    cat amivar.tf
                    aws s3 cp amivar.tf s3://service-logger/ami-id/
                   '''
             }
        }
        stage('Terraform provision') {
            steps {
                withCredentials([
                      sshUserPrivateKey(credentialsId: 'ec2-user-public-key', keyFileVariable: 'EC2_PUBLIC_KEY', passphraseVariable: '', usernameVariable: ''),
                      sshUserPrivateKey(credentialsId: 'ec2-user-private-key', keyFileVariable: 'EC2_PRIVATE_KEY', passphraseVariable: '', usernameVariable: '')
                ]) {
                    sh '''
                        rm -rf terraform-log-access-analytics
                        git clone https://github.com/zzanette/terraform-log-access-analytics.git
                        cd terraform-log-access-analytics
                        aws s3 cp s3://service-logger/ami-id/amivar.tf ./
                        cat amivar.tf
                        wget -c https://releases.hashicorp.com/terraform/0.12.20/terraform_0.12.20_linux_amd64.zip
                        unzip -o terraform_0.12.20_linux_amd64.zip
                        ./terraform init
                        ./terraform import aws_eip.app-ip eipalloc-017f99bacfc6cc3c6
                        ./terraform apply -auto-approve -var PATH_TO_PUBLIC_KEY=${EC2_PUBLIC_KEY} -var PATH_TO_PRIVATE_KEY=${EC2_PRIVATE_KEY}
                     '''
                  }

            }
        }
    }
}
