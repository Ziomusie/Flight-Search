/**
 * Created by Wojciech on 2016-11-25.
 */
var bodyParser = require('body-parser');
var DAYS_FORWARD_SEARCH = 7;

var log = require('./logger.js');

module.exports  = function(app,db){
	app.get('/api/getListOfFlights', function(req, res) {
		log.start(req);
		db.any("SELECT id, DepartureAirportID, DestinationAirportID, DepartureTime::text, ArrivalTime::text, FlightTime, Price,AdditionalInformation, lastActualization from flight ")
			.then( function (data) {
				log.dataRecieved(data);
				var result = [];
				for (var i in data) {
					if (data.hasOwnProperty(i)) {
						result.push({
							ID: data[i].id,
							DepartureAirportID: data[i].departureairportid,
							DestinationAirportID: data[i].destinationairportid,
							DepartureTime:data[i].departuretime,
							ArrivalTime:data[i].arrivaltime,
							FlightTime:data[i].flighttime,
							Price:data[i].price,
							AdditionalInformation:data[i].additionalinformation,
							lastActualization:data[i].lastactualization
						});
					}
				}
				res.contentType('application/json');
				res.send(JSON.stringify(result));
				log.success();
			})
			.catch(function (error) {
				log.failed(error);
			});
	});
}