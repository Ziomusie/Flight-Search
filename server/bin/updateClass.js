/**
 * Created by Wojciech on 2016-11-25.
 */
var bodyParser = require('body-parser');
var DAYS_FORWARD_SEARCH = 7;

var log = require('./logger.js');

module.exports  = function(app,db){
	app.put('/api/updateClass', function (req, res) {
		log.start(req);
		var name = req.query.name;
		var className =req.query.newClass;
		db.any("UPDATE Class SET class ='"+className+"'  where name ='"+name+"'")
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