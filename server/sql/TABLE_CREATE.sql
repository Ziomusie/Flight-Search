drop table Class;
drop table Flight;
drop table Airline;
drop table Airport;
drop table City;

CREATE TABLE City(
	ID SERIAL PRIMARY KEY,
	CityName varchar(200) NOT NULL,
	Country varchar(200) NOT NULL,
	TimeZone int NOT NULL

);


CREATE TABLE Airport(
	ID SERIAL PRIMARY KEY,
	AirportCode char(3) NOT NULL,
	AirportName varchar(100) NOT NULL,
	CityID int REFERENCES City,
	SearchCounter int DEFAULT 0
);

CREATE TABLE Airline(
	ID SERIAL PRIMARY KEY,
	Name VARCHAR(200) NOT NULL,
	Website VARCHAR(200) NOT NULL
);

CREATE TABLE Flight(
	ID SERIAL PRIMARY KEY,
	DepartureAirportID int REFERENCES Airport NOT NULL,
	DestinationAirportID int REFERENCES Airport NOT NULL,
	AirlineID int REFERENCES Airline,
	DepartureTime timestamp  NOT NULL,
	ArrivalTime timestamp  NOT NULL,
	FlightTime time NOT NULL,
	Price int NOT NULL,
	AdditionalInformation varchar(400),
	FlightClass varchar(100),
	LastActualization TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Class(
	ID SERIAL PRIMARY KEY,
	Name varchar(200) NOT NULL,
	Class varchar(200) NOT NULL
);