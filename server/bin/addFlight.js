/**
 * Created by Wojciech on 2016-11-25.
 */
var bodyParser = require('body-parser');
var DAYS_FORWARD_SEARCH = 7;

var log = require('./logger.js');

module.exports  = function(app,db){
	app.post("/api/addFlight", function (req, res) {
		log.start(req);
		req.body.forEach(function(listItem, i){		//koniecznie foreach, w przeciwnym wypadku wykona sie asynchronicznie (problem z cena)
			var departureAirportCode = req.body[i].departCode;
			var destinationAirportCode = req.body[i].destCode;
			var destinationAirportID=0;
			var departureAirportID=0;
			var airlineID;
			var airlineName = req.body[i].airlineName;
			var departureTime = req.body[i].departTime;
			var arrivalTime = req.body[i].arTime;
			var flightTime = req.body[i].flightTime;
			var price = req.body[i].price;
			var addInformation = req.body[i].addInformation;
			var flightClass = req.body[i].flightClass;

			if (airlineName.indexOf(' ') != -1) {
				airlineName = airlineName.toLowerCase().substr(0,airlineName.indexOf(' '));
			}else{
				airlineName = airlineName.toLowerCase();
			}
			db.any("select * from airport a where airportcode='"+departureAirportCode+"' or airportcode='"+destinationAirportCode+"'")
				.then(function (data) {
					log.dataRecieved(data);
					if(data.length==2) {
						if(data[0].airportcode == departureAirportCode) {
							departureAirportID = data[0].id;
							destinationAirportID = data[1].id;
						}
						else{
							departureAirportID = data[1].id;
							destinationAirportID = data[0].id;
						}
						db.any("select * from airline where lower(name) ilike '"+airlineName+"%'")
							.then(function (data) {
								log.dataRecieved(data);
								if(data.length==1) {
									airlineID = data[0].id;
									db.any("Select * from flight where DepartureAirportID='" + departureAirportID + "' and DestinationAirportID='" + destinationAirportID + "' and AirlineID='" + airlineID + "' and DepartureTime=to_timestamp('" + departureTime + "','YYYY-MM-DD HH24:MI')" + " and ArrivalTime=to_timestamp('" + arrivalTime + "','YYYY-MM-DD HH24:MI')")
										.then(function (data) {
											if (data.length == 0) {
												db.any("INSERT INTO flight (DepartureAirportID,DestinationAirportID, AirlineID, DepartureTime, ArrivalTime, FlightTime,Price,AdditionalInformation,FlightClass, LastActualization) values(" + departureAirportID + "," + destinationAirportID + "," + airlineID + ",to_timestamp('" + departureTime + "','YYYY-MM-DD HH24:MI'),to_timestamp('" + arrivalTime + "','YYYY-MM-DD HH24:MI'),'" + flightTime + "'::time," + price + " ,'"+ addInformation + "', '"+flightClass+"', CURRENT_TIMESTAMP)")
													.then(function (data) {
														log.success();
													})
													.catch(function (error) {
														log.failed(error);
													});
											}
											else {
												db.any("UPDATE Flight SET Price =" + price + ", LastActualization =CURRENT_TIMESTAMP where id=" + data[0].id)
													.then(function () {
														log.success();
													})
													.catch(function (error) {
														log.failed(error);
													});
											}
										})
										.catch(function (error) {
											log.failed(error);
										});
								}
								else if (data.length > 1) {
									log.failed('Airline name duplicated!');
								}
								else {
									log.failed('There is no airline with name ' + airlineName);
								}
							})
							.catch(function (error) {
								log.failed(error);
							});
					}
					else{
						log.failed('Airport code duplicated!');
					}
				})
				.catch(function (error) {
					log.failed(error);
				})
		});
		res.sendStatus(200);
	});
}