/**
 * Created by Wojciech on 2016-11-25.
 */
var bodyParser = require('body-parser');
var DAYS_FORWARD_SEARCH = 7;

var log = require('./logger.js');

module.exports  = function(app,db){
	app.get('/api/getListOfClasses', function (req, res) {
		log.start(req);
		db.any("SELECT * from Class")
			.then(function (data) {
				log.dataRecieved(data);
				var result = [];
				for (var i in data) {
					if (data.hasOwnProperty(i)) {
						result.push({ID: data[i].id,Name: data[i].name, Class: data[i].class});
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