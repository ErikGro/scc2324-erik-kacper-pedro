# scc2324-erik-kacper
## Group members
- Erik Großkopf 68858 (MEI)
- Pedro Ribeiro 55921 (MIEI)
- Kacper Motyka 68806 (MEI)

## Deadlines
- **20/October** – delivery of checkpoint – at this point, you are expected to have implemented the basic functionality + application-level caching
- **13/November** – final delivery of the project.
  
# Backend for a house rental company (short term)

## Features
- Users can make houses available for renting
- Users can rent houses
- Users can also pose questions about a house
	- A question can only be answered by the user that owns the house, and there can be only one answer for each question

## REST-API
-   User **(/rest/user)** GET/PUT/POST/DELETE: create user, delete user, update user. After a user is deleted, houses and rentals from the user appear as been performed by a default “Deleted User” user.
-   Media **(/rest/media)** GET/POST: upload media, download media.
-   House **(/rest/house)** GET/PUT/POST/DELETE: create a house, update a house (HouseAvailability), delete a house. 
-   Rental **(/rest/house/{id}/rental)** GET/PUT/POST: base URL for rental information, supporting operations for creating, updating and listing rental information.
-   Question **(/rest/house/{id}/question/{id}/?type=reply)** GET/POST: create question or reply
- Questions **(/rest/house/{id}/questions)** GET/POST: list questions for a house

## Models
### CosmosDB
-   **User**
	- id
	- nickname
	- name
	- (hash of the) password
	- photo-id
-   **House**
	- id
	- landlordID
	- name
	- location
	- description
	- at least one photo
	- price per day - default
	- price per day - discount
- **HouseAvailability** // From when to when the landlords want to make his house available
	- houseID
	- startdate
	- enddate
- **HouseRent** // From when to when users rent a house
	- houseID
	- tenantUserID
	- startdate
	- enddate
	- price
- **Question**
	- id
	- userID
	- houseID
	- text
- **Answer**
	- questionID
	- text


### Blob storage
- images and videos used in the system