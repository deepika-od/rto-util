buildDeployService {
	email = "deepika.maheshwari@theodpcorp.com"
	platform  = "generic"
	livenessProbe="false"
	readinessProbe="false"	
    maxMemory = "6Gi"
    deployLocations = "aws"
    skipDockerBuild = "true"
    skipAllDeploy = "true"
	snyk = ["org": "cool", "environment":"backend", "devBranch":"dev" ,"block":"false"]
  	envs = [
	] 
	metaData = [ 
	    'SLA': 'Application', 
	    'serviceNowClass': 'Application', 
	    'serviceNowAppName': 'rto-common', 
	    'clarityProjectId': 'PR003819',
    	'teamName': 'Technology Architecture',
    	'serviceOwnerEmail': 'deepika.maheshwari@theodpcorp.com',
    	'appSupportTeamEmail': 'deepika.maheshwari@theodpcorp.com',
    	'costcenter': '30056',
    	'devManagerEmail': 'alvaro.fernandez@theodpcorp.com''
	   ]
    buildCommands = [
        "mvn clean deploy --batch-mode --settings /etc/maven/settings.xml"
    ]
}
