## STEPS TO CREATE AZURE RESOURCES

**1.** Choose Resources which you want to create (CREATE_STORAGE, CREATE_COSMOSDB, CREATE_REDIS = true/false).

**2.** Change MY_SUFFIX (student id)

3. Choose Regions to use
   
5. Choose Azure App Name
   
7. Choose Azure Function Name
   
**6.** Compile the Resource Creation using: mvn clean compile assembly:single

**7.** Run the Resource Creation using: java -cp target/scc2324-mgt-1.0-jar-with-dependencies.jar scc.mgt.AzureManagement 

**8.** Configure Azure App Deployment with mvn azure-webapp:config - set the same app name and resource group as in azureprops.sh file.

**9.** Deploy the app: mvn clean compile package azure-webapp:deploy

**10.** Run commands from azureprops-westeurope.sh to create environment variables. (on Windows you can use git bash and run command: sh filename.sh)

**11.** If you want Delete Resources run: java -cp target/scc2324-mgt-1.0-jar-with-dependencies.jar scc.mgt.AzureManagement --delete
