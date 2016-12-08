/**
 * Created by Wojciech on 2016-11-25.
 */
var bodyParser = require('body-parser');
var DAYS_FORWARD_SEARCH = 7;

var log = require('./logger.js');

module.exports  = function(app,db){
	app.post('/api/addClass', function (req, res) {
		log.start(req);
		var name = req.body.name;
		var className =req.body.class;
		db.any("INSERT INTO Class (Name,Class) values('"+name+"','"+className+"')")
			.then(function (data) {
				res.sendStatus(201);
				log.success();
			})
			.catch(function (error) {
				log.failed(error);
				res.sendStatus(400);
			});
	});
}