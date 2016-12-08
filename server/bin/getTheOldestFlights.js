/**
 * Created by Wojciech on 2016-11-25.
 */
var bodyParser = require('body-parser');
var DAYS_FORWARD_SEARCH = 7;

var log = require('./logger.js');

module.exports  = function(app,db){
	app.get("/api/getTheOldestFlights", function (req, res)
	{
		log.start(req);
		var limit = req.query.limit;
		var airportLimit = req.query.airportLimit;
		if (typeof airportLimit == 'undefined') {
			airportLimit = limit;
		}
		var querry1  = "SELECT * FROM "+
			"(select distinct on (f.departureairportid, f.departureairportid, date ) f.*, a1.airportcode as ap1code, a2.airportcode as ap2code, to_char(f.departuretime, 'yyyy-mm-dd') as date from flight f "+
		"INNER JOIN airport a1 on f.departureairportid=a1.id "+
		"inner join airport a2 on f.destinationairportid=a2.id "+
		"WHERE f.departuretime >= now() "+
		"order by f.departureairportid, f.departureairportid, date) t "+
		"where t.ap1code in (select airportCode as apCode from Airport order by SearchCounter desc limit "+airportLimit+") "+
		"ORDER BY t.lastActualization limit 5 ";
			var querry2 = "SELECT * FROM " +
			" (select distinct on (f.departureairportid, f.departureairportid, date ) f.*, a1.airportcode as ap1code, a2.airportcode as ap2code, to_char(f.departuretime, 'yyyy-mm-dd') as date from flight f " +
			" INNER JOIN airport a1 on f.departureairportid=a1.id " +
			"inner join airport a2 on f.destinationairportid=a2.id " +
			"WHERE f.departuretime >= now() " +
			"order by f.departureairportid, f.departureairportid, date) t " +
			" ORDER BY t.lastActualization limit "+limit;
		db.any(querry2)
			.then(function (data) {
				log.dataRecieved(data);
				var result = [];
				for (var i in data) {
					if (data.hasOwnProperty(i)) {
						result.push({
							DepartureAirportCode: data[i].ap1code,
							DestinationAirportCode: data[i].ap2code,
							DepartureTime: data[i].departuretime
						});
					}
				}
				db.any(querry1)
					.then(function (data) {
						log.dataRecieved(data);
						for (var i in data) {
							if (data.hasOwnProperty(i)) {
								result.push({
									DepartureAirportCode: data[i].ap1code,
									DestinationAirportCode: data[i].ap2code,
									DepartureTime: data[i].departuretime
								});
							}
						}
					})
				.catch(function (error) {
					log.message(error);
				});
				res.contentType('application/json');
				res.send(JSON.stringify(result));
				log.success();
			})
			.catch(function (error) {
				log.failed(error);
			});
	});
}