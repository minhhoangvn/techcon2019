def label = "build-pod-${env.JOB_NAME}-${env.BUILD_NUMBER}".replace('-', '_').replace('/', '_')

def containerVars = [
          containerEnvVar(key: 'gitlabActionType', value: '${gitlabActionType}'),
          containerEnvVar(key: 'gitlabSourceRepoSshUrl', value: '${gitlabSourceRepoSshUrl}'),
          containerEnvVar(key: 'gitlabBranch', value: '${gitlabBranch}'),
          containerEnvVar(key: 'gitlabSourceBranch', value: '${gitlabSourceBranch}'),
          containerEnvVar(key: 'gitlabTargetBranch', value: '${gitlabTargetBranch}'),
          containerEnvVar(key: 'gitlabUserEmail', value: '${gitlabUserEmail}')
      ]

def createContainerTemplate(imageName, containerName, containerVars, containerCommand = '/bin/sh -c', containerCommandArgs = 'cat', requestCpu = '256m', requestMemory = '256Mi'){
  return containerTemplate( name: containerName, image: imageName, ttyEnabled: true, command: containerCommand, args: containerCommandArgs, envVars: containerVars,  resourceRequestCpu: requestCpu, resourceRequestMemory: requestMemory)
}

def retrieveGitDiffLine(){
  def shortCommit = sh(returnStdout: true, script: 'git show -s --format=format:"*%s*  _by %an_" HEAD').trim()
  def gitCommitLogs = sh(returnStdout: true, script: 'git log origin/${gitlabTargetBranch} --first-parent  --oneline').trim()
  def gitCommitLog = gitCommitLogs.split("\n")
  def gitDiffLines = sh(returnStdout: true, script: "git diff ${getGitCommitId(gitCommitLog[0])} ${getGitCommitId(gitCommitLog[1])}").trim()
  writeFile (file: 'diff_lines.txt', text: gitDiffLines)
}

def getGitCommitId(commitLog){
  return (commitLog.split(" ")[0]).trim()
}

podTemplate(
  label: label,
  containers: [
    createContainerTemplate('python:3.6.9-stretch', 'pyenv', containerVars, '/bin/sh -c', 'cat', '512m', '500Mi'),
    createContainerTemplate('katalonstudio/katalon', 'sandbox', containerVars, '/bin/sh -c', 'cat', '2048m', '2000Mi')
  ],
  volumes: [hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')
  ],
  imagePullSecrets: [ 'gcr-json-key' ]
)
{
  node(label) {
      checkout([$class: 'GitSCM', branches: [[name: 'origin/${gitlabBranch}']], userRemoteConfigs: [[credentialsId: 'minhhoang-ssh', url: '${gitlabSourceRepoSshUrl}']]])
      def shortCommit = sh(returnStdout: true, script: 'git show -s --format=format:"*%s*  _by %an_" HEAD').trim()
      retrieveGitDiffLine()
      stage("TIL Process"){
        container("pyenv"){
          sh "cp ${WORKSPACE}/test-impact-demo/final_mapping_result.json ${WORKSPACE}/final_mapping_result.json"
          sh "python diff.py"
          sh "cat diff_methods.json"
          sh "cat final_mapping_result.json"
          sh "python til.py"
          sh "ls -la"
          sh "cat impacted_test.txt"
        }
      }
      stage('Start Application') {
        container('pyenv') {
          dir("$WORKSPACE/demo-app"){
            sh "pip install -r requirements.txt"
            sh "nohup python run.py > run.log &"
          }
        }
      }

      stage('Run E2E Tests') {
        container('sandbox') {
          dir("$WORKSPACE/test-impact-demo"){
            sh "katalon-execute.sh --args -noSplash -consoleLog -retry=0 -testSuitePath=\"Test Suites/TIL\" -executionProfile=\"default\" -browserType=\"Chrome\" -installPlugin=\"${WORKSPACE}/plugins-1.0.12.jar\" --tilFile=\"${WORKSPACE}/impacted_test.txt\""
          }
        }
      }
  }
}
