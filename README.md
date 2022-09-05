Building the back end for users registration to enter their data by the json request sent.

When registering it validates if the email is registered before. 
If the email is new It will send a confirming token to his email (expires after 15 Min).
When the user clicks on the generated token he will be an enabled user who can Sign in to the application succesfully.

Other features:
Passwords are stored in a database after being encrypted.
Unique token will be sent to the user,but there would be multiple tokens per user.
