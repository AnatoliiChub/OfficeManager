# OfficeManager App
**Saal Digital Assessment task**

## Table of contents
- [Requirements](https://github.com/AnatoliiChub/OfficeManager?tab=readme-ov-file#requirements)
- [Technologies and Architecture](https://github.com/AnatoliiChub/OfficeManager?tab=readme-ov-file#technologies-and-architecture)
  - [Database](https://github.com/AnatoliiChub/OfficeManager?tab=readme-ov-file#database)
- [UI/UX](UI/UX)
  - [Add object screen](https://github.com/AnatoliiChub/OfficeManager?tab=readme-ov-file#objects-screen)
  - [Objects screen](https://github.com/AnatoliiChub/OfficeManager?tab=readme-ov-file#add-object-screen)
  - [Edit object screen](https://github.com/AnatoliiChub/OfficeManager?tab=readme-ov-file#edit-object-screen)
  - [Select a relation to Add screen](https://github.com/AnatoliiChub/OfficeManager?tab=readme-ov-file#select-a-relation-to-add-screen)
  - [Video Demo](https://github.com/AnatoliiChub/OfficeManager?tab=readme-ov-file#video-demo)

## Requirements

I've implemented the app according to requirements and highlighted key points below:
1. **Object type** - since on the screenshots it has a separated input text field, I suppose that type can be dynamic. In other words - there is no limited number of types, and users are able to create new types.
2. I've implemented Create, Edit and Delete functionality for the objects. Also I've implemented a **Popup menu** like it was mentioned on the screenshots. But I did not add a button for the menu, the user is able to open it by **long press** on the item list.
3. I've implemented Add and Delete functionality for the objects. Users are able to add new one or remove existing relations.
4. I've implemented a search feature to find objects by their attributes. It's working in the next way if one of the fields(name, description or type) contains the text it displays the object.
5. All the data stored persistently in SQLite database.
6. I did not have enough time to implement validation, so the user is able to create items with empty fields.
7. Since there are no requirements regarding relations, the user is able to add an unlimited amount of relations but all relations inside the same object should be unique, duplication is not possible. If a user tries to save an object with a duplicated relation it will show an error.
8. Also it will show an error if the user tries to add an already used relation(which is already related to another object).
Since time was limited I just implemented a default placeholder for all the errors.

## Technologies and Architecture
I used a modern stack of technologies: Jetpack Compose, Kotlin Coroutines, Kotlin Flow, Room and Hilt for the dependency injection.
I prefer to use Hilt for dependency injection because it can provide compile-time safety(in comparison with Koin it's an advantage), supports Jetpack Compose and has full integration with Android. Also I have a lot of experience with Dagger 2, since Hilt is based on Dagger 2, it's also an advantage for me.
Despite having a lot of experience with RxJava I used Kotlin coroutines and Flow because in my opinion the RxJava framework has a chance to become deprecated or not maintainable in the next few years.
### Database
I've used the Room to have the ability to make a database normalization. Since the type attribute is dynamic to normalize database structure I created a separate table for type since it's a string and there are no limits for the size.
Also I've created a "Join" table and call it "Relations" you can see the database structure on the picture below:
<div align="center">
<img src="https://github.com/user-attachments/assets/dacae8b4-aee6-4702-bab1-1d5070660530" width="800" height="500" />
</div>
I did not encrypt the database with sqlCipher, probably it's out of scope of the testing task.
I've used MVVM architecture for the app. Since Viewmodel does not have any reference to View it will be easy to test it. I did not use UseCases as usual, because there is no business logic, only CRUD operations to the database.
I used a Result pattern for UiState to avoid bugs related to different sources of ui state.

## UI/UX

From the user perspective the application contains 4 screens: Objects, AddObject, Edit Object and Select relation to Add.
### Objects screen
<div align="center">
<img src="https://github.com/user-attachments/assets/a89e47bd-57ed-4f5d-a7cf-403f5826ed49" width="300" height="600" />
</div>
The screen contains a search field, list of items and FAB button.
To add a new object just press a FAB button.
To edit items users have 2 options: just click on list item or long press on list item to open Popup menu and select Edit option.
To delete items use a long press on the item and select the Delete option from the popup menu.

### Add object screen
If the user clicks on the FAB button on the Object screen the app redirects it to the Add Object screen.
<div align="center">
<img src="https://github.com/user-attachments/assets/85883110-c99c-4658-b9c0-5740fe9d0b48" width="300" height="600" />
</div>
It contains a navigation button in the topbar, 3 fields to fill out attributes: name, description and type and relations section. The Relations section contains a list of relations and an "Add relations" button. Also it contains a FAB button.
To add a new relation user should press on the "Add relations" button, it's located right under the "Relations :" label on the screenshot, if the object has some relations the button will be located at the end of the list. It redirects users to the "Select a relation to Add" screen.
To save an object, the user should press the FAB button. If the user left the screen and did not press a save button, the object will not be created.

### Edit object screen
<div align="center">
<img src="https://github.com/user-attachments/assets/a2a10b4e-194b-403b-9c74-6552d0750827" width="300" height="600" />
</div>
Looks the same as Add object screen.

### Select a relation to Add screen
<div align="center">
<img src="https://github.com/user-attachments/assets/44448e2a-0659-4b49-b842-d5d7510cc001" width="300" height="600" />
</div>
It opens when the user wants to select a new relation.
It looks like an Object screen. But on that screen the user is not able to manage objects(Edit, Delete). When the user presses on the list item on this screen it brings back to the previous (Add/Edit) screen and adds a new relation to the relation list.

### Video Demo
[demo.webm](https://github.com/user-attachments/assets/e6bcc3e7-3989-4d84-8266-0766c7632413)


