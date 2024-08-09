pipeline {
    agent any
    
    environment {
        DOCKER_REGISTRY = "benepick.kr-central-2.kcr.dev"
        IMAGE_NAME = "benepick-backend"
        IMAGE_TAG = "latest" // 필요한 경우 다른 태그로 변경
        DOCKER_IMAGE = "${DOCKER_REGISTRY}/benepick-container/${IMAGE_NAME}:${IMAGE_TAG}"
        REGISTRY_CREDENTIALS_ID = "docker-registry-credentials"
        GITHUB_CREDENTIALS_ID = "github-token"
        SSH_CREDENTIALS_ID = "deploy-server-ssh" // SSH 인증 정보
        REMOTE_SERVER = "ubuntu@10.0.0.120"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'API-306-separate-jenkins-and-web', credentialsId: GITHUB_CREDENTIALS_ID, url: 'https://github.com/KEA-8PI/BenePick-Backend.git'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build(DOCKER_IMAGE)
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry("https://${DOCKER_REGISTRY}", "${REGISTRY_CREDENTIALS_ID}") {
                        dockerImage.push()
                    }
                }
            }
        }
        stage('Deploy to Remote Server') {
            steps {
                script {
                    sshagent(["${SSH_CREDENTIALS_ID}"]) {
                        withCredentials([usernamePassword(credentialsId: REGISTRY_CREDENTIALS_ID, passwordVariable: 'DOCKER_REGISTRY_PASSWORD', usernameVariable: 'DOCKER_REGISTRY_USERNAME')]) {
                            sh """
                            ssh ${REMOTE_SERVER} << EOF
                                echo \$DOCKER_PASSWORD | docker login -u \$DOCKER_USERNAME --password-stdin ${DOCKER_REGISTRY}
                                docker pull ${DOCKER_IMAGE}
                                docker stop ${IMAGE_NAME} || true
                                docker rm ${IMAGE_NAME} || true
                                docker run -d --restart unless-stopped --name ${IMAGE_NAME} -p 8080:8080 ${DOCKER_IMAGE}
                            EOF
                            """
                        }
                    }
                }
            }
        }
    }
}
