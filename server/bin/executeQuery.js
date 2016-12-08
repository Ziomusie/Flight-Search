/**
 * Created by Wojciech on 2016-11-25.
 */
var bodyParser = require('body-parser');
var DAYS_FORWARD_SEARCH = 7;

var log = require('./logger.js');

module.exports  = function(app,dbReadOnly){
	app.post('/api/executeQuery', function (req, res) {
		var query = req.body.query;
		log.start(req);
		dbReadOnly.any(query)
			.then(function (data) {
				log.dataRecieved(data);
				var result = data;
				res.status(200).send({results: data});
				log.success();
			})
			.catch(function (error) {
				log.failed(error);
				res.status(400).send({error: error.message});
			});
	});
}