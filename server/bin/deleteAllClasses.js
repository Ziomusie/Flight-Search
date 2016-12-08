/**
 * Created by Wojciech on 2016-11-25.
 */
var bodyParser = require('body-parser');
var DAYS_FORWARD_SEARCH = 7;

var log = require('./logger.js');

module.exports  = function(app,db){
	app.delete('/api/deleteAllClasses', function (req, res) {
		log.start(req);
		db.any('DELETE FROM Class')
			.then(function (data) {
				log.success();
				res.sendStatus(200);
			})
			.catch(function (error) {
				log.failed(error);
				res.sendStatus(400);
			});
	});
}