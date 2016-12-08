/**
 * Created by Wojciech on 2016-11-25.
 */
var bodyParser = require('body-parser');
var DAYS_FORWARD_SEARCH = 7;

var log = require('./logger.js');

module.exports  = function(app,db){
	app.get('/api/getListOfAirports', function (req, res) {
		log.start(req);
		db.any("SELECT id, AirportCode, AirportName, CityID, SearchCounter from airport")
			.then(function (data) {
				log.dataRecieved(data);
				var result = {};
				for (var i in data) {
					if (data.hasOwnProperty(i)) {
						result[data[i].airportcode] = data[i].airportname;
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