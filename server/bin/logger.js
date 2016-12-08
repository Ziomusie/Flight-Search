var Logger = function() {
	var log = console.log;
	
	var lastStartedService = {};
	
	var consoleLogDisabledInfo = function(message) {
		log('Logged using default logger! Message: %s', message);
	};
	console.log = console.warn = console.error = console.info = console.trace = consoleLogDisabledInfo;
	
	var currentTime = function() {
		return new Date().getTime();
	};
	
	var logServiceEntry = function(requestMethod, serviceName) {
		log('%s: %s start', requestMethod, serviceName);
	};
	
	var logServiceEnded = function() {
		var requestMethod = lastStartedService.request.method;
		var serviceName = lastStartedService.name;
		var duration = currentTime() - lastStartedService.started;
		log('%s: %s ended (%d ms)\n', requestMethod, serviceName, duration);
	};
	
	var logServiceFailed = function(errorMessage) {
		log('ERROR OCCURED! Message: %s', errorMessage);
		logServiceEnded();
	};
	
	var serviceNameFromRequest = function(request) {
		var splitted = request.url.split('/');
		return splitted[splitted.length-1];
	};
	
	this.serverStarted = function(server) {
		log('App listening on port %s\n', server.address().port);
	};
	
	this.message = function(message) {
		log(message);
	};
	
	this.start = function(request) {
		var serviceName = serviceNameFromRequest(request);
		logServiceEntry(request.method, serviceName);
		lastStartedService = {
			name: serviceName,
			started: currentTime(),
			request: request
		};
		if ('POST' == request.method) {
			log('Parameters:', request.body);
		}
	};
	
	this.success = function() {
		logServiceEnded();
		lastStartedService = null;
	};
	
	this.failed = function(error) {
		logServiceFailed(error);
		lastStartedService = null;
	};
	
	this.dataRecieved = function(data) {
		if (typeof data == 'object' && typeof data.length != 'undefined') {
			log('Recieved %d rows', data.length);
		}
	};
};

module.exports = new Logger();