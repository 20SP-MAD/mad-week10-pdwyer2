# Firebase
[Firebase Overview](https://firebase.google.com/docs/?authuser=0)


## Some useful Firebase Features for developers
- Authentication
- Realtime database
- Storage
- Hosting


## Creating a Firebase Project
- Create the project in Android Studio
- Set up the Firebase account (need a Google account)
- Use the Firebase console to create a project
- Link the Firebase project with your app
	- Download the JSON file and place in the Android app root directory (use Project View)

## Adding Gradle imports
- Use Firebase Assistant (in Tools menu in Android Studio)
- Add Gradle imports to your project: [Add Firebase to Your Android Project  |  Firebase](https://firebase.google.com/docs/android/setup)

## Using Firebase in your app
Initialize the app as a Firebase app in the `onCreate` method of your launch activity

```
FirebaseApp.initializeApp(this);
```

## FirebaseUI
[FirebaseUI](https://github.com/firebase/FirebaseUI-Android) is an open-source library for Android that allows you to quickly connect common UI elements to Firebase APIs.
- [AuthUI](https://github.com/firebase/FirebaseUI-Android/tree/master/auth)
- [Realtime Database](https://github.com/firebase/FirebaseUI-Android/tree/master/database)

---

## Authentication

### Set up
- Add authentication methods in the Firebase console
- Add dependencies for FirebaseAuth and FirebaseUI in Gradle App module
  - Refer to [FirebaseUI documentation](https://github.com/firebase/FirebaseUI-Android) for the most recent versions
- Configuration for Google sign-in is then provided automatically by the google-services plugin


### Add `AuthStateListener`
- Triggered when the users authentication state changes
  - Important to attach in `onResume` and detach `onPause`
- Declare an instance of FirebaseAuth and FirebaseAuth.AuthStateListener at class level and retrieve reference in `onCreate`
- Send unauthenticated users to sign in
	- use `FirebaseUIAuth` which provides all of the necessary UI components for authentication using FirebaseAuth

---
## Firebase Realtime Database
- Syncs in real time
- Apps remain responsive even when the app is online
	- Changes are stored locally
	- When the user the connects to the network, the data synchronizes
- Firebase Realtime Database is used by apps such as Instacart
- Users must be authenticated to read and write data. This can be configured in the Firebase console.

### Structure
- All data is stored as JSON objects
- Your database is the root node
- Data added as a node (key-value)
	- Keys are always strings
	- Nodes can have different types: int, float, boolean, string, other nested objects
	- Can also store arrays but not recommended
- Firebase can create unique keys to avoid collisions
	- push ids (20-character unique IDs generated when you write lists of data to Firebase)
	- To learn more read: [The Firebase Blog: The 2^120 Ways to Ensure Unique Identifiers](https://firebase.googleblog.com/2015/02/the-2120-ways-to-ensure-unique_68.html)

### Database Implementation
- Add the Gradle dependency


### Writing Data
- When writing data, get a reference to a `FirebaseDatabase` object and a `DatabaseReference` object
	- A `FirebaseDatabase` is the entry point for the app to access the database. Retrieve a reference using the static method `FirebaseDatabase.getInstance()`
	- A `DatabaseReference` object references a specific part of the database.
    - The reference object first gets a reference to the root node of the database using the `getReference()` function.  It then gets the root node using the `child` function with the model object class name as it's parameter(key).  
- To write to the realtime database use the code below

```java
 databaseReference.push().setValue(modelObject);
```
- For details on other writing options see the [Firebase Documentation](https://firebase.google.com/docs/database/android/read-and-write?authuser=0#basic_write)



### Reading Data
- Use [FirebaseUI for Realtime Database](https://github.com/firebase/FirebaseUI-Android/tree/master/database) to make it easier to save to a recycler view
  - Add the FirebaseUI Gradle dependency
- Implement an Adapter class
	- Have adapter extend `FirebaseRecyclerAdapter<ModelObject, ViewHolder>`
	- Add default constructor and unimplemented methods
- To set on the adapter on the RecyclerView
  - Get a reference to the Firebase database
  - Set up a database query
    - You can order data (ascending only) and filter.  See details in [documentation](https://firebase.google.com/docs/database/android/lists-of-data?authuser=0)
- Build a `FirebaseRecyclerOptions` object using the query as a parameter
- Pass the options object to the constructor for the adapter class
- Set the adapter on the `RecyclerView`
- start listening in `onResume` and stop listening in `onPause`

---
## Offline Capabilities
- Firebase applications work even if your app temporarily loses its network connection. In addition, Firebase provides tools for persisting data locally, managing presence, and handling latency.
- Enable disk persistence using: `FirebaseDatabase.getInstance().setPersistenceEnabled(true);`
- To learn more, see [Offline Capabilities](https://firebase.google.com/docs/database/android/offline-capabilities?authuser=0)
