# Reserve #
***Easily reserve Cornell rooms with our mobile app***

## Repo Links ##
[Android Repo]https://github.com/FranciscoASC21/Reserve/<br>
[Backend Repo]https://github.com/Jensen615/reserve-backend<br>

## App Screenshots ##
<img src="https://user-images.githubusercontent.com/91110564/144696752-dbe1df49-0c6e-4fe2-bab5-d87c961f6d5f.png" width="200">    <img src="https://cdn.discordapp.com/attachments/911454161734893588/916544734804181032/Screenshot_20211203-232059.png" width="200">    <img src="https://cdn.discordapp.com/attachments/911454161734893588/916544734560915536/Screenshot_20211203-232103.png" width="200">    <img src="https://cdn.discordapp.com/attachments/911454161734893588/916544735324278794/Screenshot_20211203-232046.png" width="200">    <img src="https://cdn.discordapp.com/attachments/911454161734893588/916544735114588180/Screenshot_20211203-232055.png" width="200">    <img src="https://cdn.discordapp.com/attachments/911454161734893588/916544734317662219/Screenshot_20211203-232111.png" width="200">

## App Description ##
Reserve allows users to search for rooms to reserve on Cornell's Central, West, North, and East campuses. Users can select a room and access information such as its capacity and images of the room if available. Users can perform a mock 1 hour booking of a room using our intuitive reservation process. Users can favorite their favorite rooms and view them in a separate tab. Users can view their current bookings in the bookings tab. We also have day and night mode!

## Addressing Requirements: ##

### Android Requirements ###
- Implement a RecyclerView with a custom adapter<br> We have three recycler views for each of the bottom three tabs
- Use 3rd-Party API, Networking, or Persistent Storage<br> We implemented our backend API. We use get requests to populate our recycler views and get reservation times. We use put requests to add new reservations to the backend.
- Build 3 fully functional screens<br> We have a total of 5 screens: A home, favorites, and booked reservations view, an expanded card view, and a reservation request view.

### Backend Requirements ###
- Have an API specification for each route<br> https://docs.google.com/document/d/1lCx80UllNHCadULUhduSKDOk36jMQw_mKBUw7UsajdY/edit?usp=sharing
- Have at least 4 routes (at least 1 GET, 1 POST)<br>
  - @app.route("/rooms/<int:room_id>/")
  - @app.route("/rooms/", methods=["POST"])
  - @app.route("/rooms/<int:room_id>/", methods=["POST"])
  - @app.route("/rooms/<int:room_id>/", methods=["DELETE"])

- Use a database with at least two tables, and at least one relation<br>
  - Rooms Table (reservations = db.relationship("reservations", cascade="delete"))
  - Reservations Table (room_id = db.Column(db.Integer, db.ForeignKey("rooms.id")))
  - Times Table (room_id = db.Column(db.Integer, db.ForeignKey("rooms.id")))

- Deploy to Heroku<br> https://reserve-backend.herokuapp.com/
