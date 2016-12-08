/**
 * Created by Wojciech on 2016-11-25.
 */
var bodyParser = require('body-parser');
var DAYS_FORWARD_SEARCH = 7;

var log = require('./logger.js');

module.exports  = function(app,db){
	app.get('/api/getAirport', function (req, res) {
		log.start(req);
		var nameObject = req.query.name;
		var limit;
		limit = 1000;
		var sort = false;
		if (req.query.limit != undefined) {
			limit = req.query.limit;
		}
		if (req.query.sort != undefined)
			sort = req.query.sort;
//	console.log(limit);
		var name = "";
		name += nameObject;
		if(sort == false) {
			db.any("SELECT id, airportcode, airportname , cityid from airport where LOWER(airportname) LIKE '" + name.toLowerCase() + "%' LIMIT " +limit)
				.then(function (data) {
					log.dataRecieved(data);
					var result = [];
					for (var i in data) {
						if (data.hasOwnProperty(i)) {
							result.push({ID: data[i].id,AirportCode: data[i].airportcode,AirportName: data[i].airportname,CityID: data[i].city});
						}
					}
					res.contentType('application/json');
					res.send(JSON.stringify(result));
					log.success();
				})
				.catch(function (error) {
					log.failed(error);
				});
		}
		else
		{
			db.any("SELECT id, airportcode, airportname, cityid AS city from airport where LOWER(airportname) LIKE '" + name.toLowerCase() + "%' ORDER BY airportname LIMIT " +limit)
				.then(function (data) {
					log.dataRecieved(data);
					var result = [];
					for (var i in data) {
						if (data.hasOwnProperty(i)) {
							result.push({ID: data[i].id,AirportCode: data[i].airportcode,AirportName: data[i].airportname,CityID: data[i].city});
						}
					}
					res.contentType('application/json');
					res.send(JSON.stringify(result));
					log.success();
				})
				.catch(function (error) {
					log.failed(error);
				});
		}
	});
}