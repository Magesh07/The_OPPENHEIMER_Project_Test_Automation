# The_OPPENHEIMER_Project_Test_Automation

## Installation (pre-requisites)
 1. Minimum Java Version - JDK 11 (make sure Java class path is set)
 2. Minimum Maven apache-maven-3.3.3 (make sure .m2 class path is set)
 3. Eclipse(Version: 2022)
 4. Eclipse Plugins :
       - TestNG for Eclipse
	     - GIT
           
## Framework set up

###To Clone Repository
1. Copy the GitHub URL of the repository to the clipboard
2. Open Eclipse and choose Import –> Projects from Git (with smart import)
3. Choose the Clone URI option in the Git import wizard and click Next
4. Confirm the URI, Host and Repository path parameters and click Next
5. Choose the Git branches to clone from the remote repository and click Next
6. Confirm the Directory into which the repository will be cloned and click Next
7. Choose the Maven project to import into Eclipse from GitHub and click Finish

Clone repository from here(https://github.com/Magesh07/The_OPPENHEIMER_Project_Test_Automation.git) from master branch or download zip and set it up in your local workspace. 
Update Maven Project By navigating to Right-click on the Project->Maven->UpdateProject->Ok

Run the Oppenheimer Project App from the below link,
https://github.com/strengthandwill/oppenheimer-project-dev

Run the Tests :
Windows->Preferences->Java->Installed JREs->Select the JDK as per the below screenshot(Only JDK should be mapped)

![image](https://user-images.githubusercontent.com/29499353/188939723-d4286237-f717-427d-bf02-a8dcc1417b8b.png)

- Right-click on testng.xml file->RunAs->TestNGSuite
   or 
- Right-click on pom.xml file->RunAs->MavenTest

## Reporters:
Once you ran your tests you can get the test automation reports in below path:
<ProjectFolder>\report\ExtentReportResults.html
![Report](https://user-images.githubusercontent.com/29499353/188941543-96b07f28-bd99-43ac-a561-0914d311526d.png)

