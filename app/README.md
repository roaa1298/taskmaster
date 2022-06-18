# my Tasks App

## home page 
- Here is the **home page** of the myTask app, it contains two buttons:
   - **Add Task** : move to the Add Task page.
   - **All Tasks** : move to the All tasks page.  
    
![home](screenshots/home.jpeg)

### After the editing in lab27:   
   - username section.  
   - three buttons for three different tasks.  

![home](screenshots/main-page.jpeg)

- The menu that have the settings item.  

![home](screenshots/menu-sett.jpeg)  

### After the editing in lab28:
   - add a recyclerView to view 4 tasks.
   - you can click on the task item, and it will appropriately launch the detail page with the correct Task title displayed.  
  
![home](screenshots/home-lab28.jpeg)  

### After the editing in lab29:
   - Displaying all the tasks records from the task table in the database.  
   
![home](screenshots/database.jpeg)  

### After the editing in lab31:
- I did espresso testing for this page.  

### After the editing in lab32:   
- receive the data from DynamoDB.

![home](screenshots/main_aws.jpeg)  

### After the editing in lab33:  
- showing the Tasks that related to a specific Team.

![home](screenshots/Team1Tasks.png)    

![home](screenshots/Team2Tasks.png)    

![home](screenshots/Team3Tasks.png)   

- editing the hieght of the recyclerView.

![home](screenshots/homeTeam1.png)   

![home](screenshots/homeTeam2.png)   

### After the editing in lab36:
- Add logout button to exit from the current Authenticated user. 
- Display the nickname of the current Authenticated user.

![logout](screenshots/logoutButtonInMenu.png)

![mainActivityWithUsername](screenshots/mainActivityWithUsername.png)

### After the editing in lab41:  
- add Analytics on the home page.

![analytics](screenshots/analytics.png)

### After the editing in lab42:
- Adding **the banner Ad** at the bottom of the home page.
  
![home](screenshots/bannerAd.jpeg)

- Adding two buttons for the **rewarded** and the **interstitial** Ads.
  
![home](screenshots/allAds.jpeg)

- **The interstitial Ad** : is a full-screen ads that cover the interface of an app until closed by the user.
  
![home](screenshots/interstitialAd.jpeg)

- **The rewarded Ad** : Ads that reward users for watching short videos and interacting with playable ads and surveys.
  
![home](screenshots/rewardedAd.jpeg)

![home](screenshots/rewardedAd2.jpeg)

- **The rewarded Ad type** 
  
![home](screenshots/rewardType.jpeg)

- **The rewarded Ad Amount**
  
![home](screenshots/rewardAmount.jpeg)

- **These buttons just for testing the ads, we can put these ads during natural transitions/pauses in our app.**

## Add Task page
- Here is the **Add Task** page that contains two text fields to add the task name and description.
- And there is a submit button **(add Task)**, it is increase the total number of tasks and show it at the bottom, and show a toast message that contains **"submitted!"** text.
- And the Back button to go back to the home page.

![addTask](screenshots/add_task1.jpeg)   

### After the editing in lab31:
- I did espresso testing for this page.  

### After the editing in lab32:  
- add the data to the DynamoDB.

![add](screenshots/add_aws.jpeg)

### After the editing in lab33:
- Choose a Team for every Task.

![add](screenshots/AddToTeam1.png)   

![add](screenshots/AddToTeam2.png)    

![add](screenshots/AddToTeam3.png)    

### After the editing in lab37:  
- Add a task with an image.

![add](screenshots/addTaskWithImage.png)

- selecting an image.

![select](screenshots/ImagePicker.png)

![select](screenshots/selectingAnImage.png)

### After the editing in lab38:  
- Pick an image from another app and share it to my application to add a task with this image.

![add](screenshots/shareImage.jpeg)

![add](screenshots/addTaskWithSharedImage.jpeg)

### After the editing in lab39:
- When the user adds a task, their location will be retrieved and included as part of the saved Task.

![add](screenshots/locationReq.jpeg)

## Increase the total
- Here is the total increased after i clicked on the **add Task** button.

![addTask](screenshots/total.jpeg)

## Toast message "submitted!"
- Here is the message that appeared when i clicked on the **add Task** button.

![submit](screenshots/add_task_final.jpeg)

## All Tasks page
- Here is the All Tasks page that contain an image.
- And the Back button to go back to the home page.

![AllTasks](screenshots/all_tasks.jpeg)  

### After the editing in lab31:
- I did espresso testing for this page.

## Settings page  
- Here when the user enters his username and click the save button the username will be saved in a shared preference and we can access this username everywhere inside the application.  
- And a toast message will appear to tell the user that the username is saved.
- If the user didn't enter anything the button will be disabled.  
- If the user starts typing the button will be enabled.  

![settings](screenshots/settings.jpeg)    

![settings](screenshots/saved.jpeg)    

![settings](screenshots/button-enabled.jpeg)   

### After the editing in lab31:
- I did espresso testing for this page.

### After the editing in lab33:
- Choose a team to show its tasks in the home page.

![settings](screenshots/SettingTeam1.png)   

![settings](screenshots/SettingTeam2.png)  

## Task Detail page  
- This is the task Details page that have a simple image and random text for the task description.  
- And i added the title of the task in the header.  

![task1](screenshots/task1.jpeg)     

![task2](screenshots/task2.jpeg)     

![task3](screenshots/task3.jpeg)    

### After the editing in lab29:  
- display the task description and state in addition to the title.  

![task5](screenshots/task5.jpeg)  

![task6](screenshots/task6.jpeg)  

### After the editing in lab31:  
- I added the task title in the top of the task description.  
- I did espresso testing for this page.

![task6](screenshots/title.jpeg)

### After the editing in lab32:   

![detail](screenshots/detail_aws.jpeg)  

### After the editing in lab37:  
- Here is a task that doesn't have an Image, so i uploaded a default image.

![detail](screenshots/withDefaultImage.png)

- Displaying the image that related to the task that we uploaded with the task.

![detail](screenshots/uploadImage.png)

![detail](screenshots/taskS3.png)

### After the editing in lab38:  
- Here is the task that i have added with a shared image from another app.

![detail](screenshots/TaskDetailWithSharedImage.jpeg)

### After the editing in lab39:  
- On the Task Detail activity, the location of a Task will be displayed if it exists.

![detail](screenshots/locationCoordinate.jpeg)

### After the editing in lab41:  
- add audio button to read the description of the task (convert the text int speech).
- add translator button to translate the description of the task into arabic language.

![detail](screenshots/englishText.jpeg)

![detail](screenshots/arabicText.jpeg)

## Splash Screen  

![Splash_Screen](screenshots/splashScreen.png)

### After the editing in lab34:  
- i created an apk for the final release.
- i created a splash screen.

## Sign Up Page (After the editing in lab36)
- The user can enter his information and register to my app.
- A confirmation code will be sent to the user's email to confirm his registration.

![signUp](screenshots/signUpActivity.png)

![signUp](screenshots/signUpInformation.png)

- If the user tried to signUp with an exists email, an alert dialog will display to till the user that the username already exists in the system.  

![emailAlreadyExists](screenshots/emailAlreadyExists.png)

### confirmation Page (After the editing in lab36)
- Enter the number from the user's email to confirm his registration.

![confirmation](screenshots/verificationCode2.png)

![confirmation](screenshots/verificationNumber.png)

## sign In Page  (After the editing in lab36)
- After confirming the user registration, the user can sign in to the app with his email and password.

![signIn](screenshots/loginActivity.png)

![signIn](screenshots/loginActivity2.png)

