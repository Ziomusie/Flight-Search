/**
 * Created by Wojciech on 2016-11-25.
 */
var bodyParser = require('body-parser');
var DAYS_FORWARD_SEARCH = 0;

var log = require('./logger.js');

module.exports  = function(app,db){
	app.get('/api/getFlights', function(req, res) {
		log.start(req);
		var departureAirportCode = req.query.depCode;
		var destinationAirportCode = req.query.destCode;
		var departureTime = req.query.startTime;
		var orderBy = req.query.orderBy;
		var maxPrice = req.query.maxPrice;
		var maxTime = req.query.maxTime;
		var queryBuilder = "";
		if (orderBy == undefined)			// SPRAWDZIĆ ORDER BY!!!
			orderBy = '4,5';
		var result = [];
		var flight = [];
		db.any("UPDATE Airport SET SearchCounter = SearchCounter+1 where AirportCode='"+departureAirportCode+"' or AirportCode ='"+destinationAirportCode+"'")
		.then(function () {
			log.message('Search counter incremented');
		});
		console.log(req.query.maxChanges);
		if (req.query.maxChanges >= 0 || req.query.maxChanges == undefined)
		{
			queryBuilder+= "select f1.flighttime as f1FlightTime, f1.departuretime::text as f1DepartureTime, f1.arrivaltime::text as f1arrivaltime, f1.price as f1Price,f1.additionalinformation as f1addInfo, f1.flightclass as f1class,f1.lastactualization::text as f1lastact,"+
				"a1.airportname as ap1name, a1.airportcode as ap1code,"+
				"a2.airportname as ap2name, a2.airportcode as ap2code,"+
				"airl1.name as airlineName1, airl1.website as airlineWebsite1,"+
				"c1.cityname as city1,"+
				"c2.cityname as city2, "+
				"null as f2FlightTime, null as f2DepartureTime, null as f2arrivaltime, null as f2Price,null as f2addInfo, null as f2class,null as f2lastact,"+
				"null as ap3name, null as ap3code,"+
				"null as airlineName2, null as airlineWebsite2,"+
				"null as city3, "+
				"null as f3FlightTime, null as f3DepartureTime, null as f3arrivalTime, null as f3Price,null as f3addInfo, null as f3class,null as f3lastact,"+
				"null as ap4name, null as ap4code,"+
				"null as airlineName3, null as airlineWebsite3,"+
				"null as city4, "+
				"null as f4FlightTime, null as f4DepartureTime , null as f4arrivalTime, null as f4Price,null as f4addInfo, null as f4class,null as f4lastact,"+
				"null as ap5name, null as ap5code,"+
				"null as airlineName4, null as airlineWebsite4,"+
				"null as city5 "+
				"from flight f1 "+
				"INNER JOIN airport a1 on f1.departureairportid=a1.id "+
				"INNER JOIN airport a2 on f1.destinationairportid=a2.id "+
				"INNER JOIN airline airl1 on f1.airlineID=airl1.id "+
				"INNER JOIN city c1 on a1.cityid=c1.id "+
				"INNER JOIN city c2 on a2.cityid=c2.id "+
				"WHERE a1.airportcode='" + departureAirportCode +"' "+
				" AND a2.airportcode='" + destinationAirportCode +"' "+
				" AND date_trunc('day',f1.DepartureTime) >=  '" + departureTime +"' "+
				" AND date_trunc('day',f1.DepartureTime) <= DATE '" + departureTime + "' + " + DAYS_FORWARD_SEARCH;

			if (req.query.maxPrice != undefined && (req.query.maxTime != undefined)) //kiedy i cena i czas
			{
				queryBuilder += " and price<='" + maxPrice + "' and flighttime<='" + maxTime + "' ";
			}
			else if ((req.query.maxPrice != undefined && req.query.maxTime == undefined)) 	//kiedy tylko na cene
			{
				queryBuilder += " and price<=" + maxPrice
			}
			else if (req.query.maxTime != undefined && req.query.maxPrice == undefined) {	//kiedy tylko na czas
				queryBuilder += " and flighttime<='" + maxTime+ "' ";
			}

		}
		if (req.query.maxChanges >= 1) {
			queryBuilder +=	" union all select f1.flighttime as f1FlightTime, f1.departuretime::text as f1DepartureTime, f1.arrivaltime::text as f1arrivaltime, f1.price as f1Price,f1.additionalinformation as f1addInfo,f1.flightclass as f1class,f1.lastactualization::text as f1lastact,"+
				"a1.airportname as ap1name, a1.airportcode as ap1code,"+
				"a2.airportname as ap2name, a2.airportcode as ap2code,"+
				"airl1.name as airlineName1, airl1.website as airlineWebsite1,"+
				"c1.cityname as city1,"+
				"c2.cityname as city2, "+
				"f2.flighttime as f2FlightTime, f2.departuretime::text as f2DepartureTime,f2.arrivaltime::text as f2arrivaltime, f2.price as f2Price,f2.additionalinformation as f2addInfo,f2.flightclass as f2class, f2.lastactualization::text as f2lastact,"+
				"a3.airportname as ap3name, a3.airportcode as ap3code,"+
				"airl2.name as airlineName2, airl2.website as airlineWebsite2,"+
				"c3.cityname as city3, "+
				"null as f3FlightTime, null as f3DepartureTime, null as f3arrivalTime, null as f3Price,null as f3addInfo, null as f3class,null as f3lastact,"+
				"null as ap4name, null as ap4code,"+
				"null as airlineName3, null as airlineWebsite3,"+
				"null as city4, "+
				"null as f4FlightTime, null as f4DepartureTime, null as f4arrivalTime, null as f4Price,null as f4addInfo, null as f4class,null as f4lastact,"+
				"null as ap5name, null as ap5code,"+
				"null as airlineName4, null as airlineWebsite4,"+
				"null as city5 "+
				"from flight f1 "+
				"INNER JOIN flight f2 on f1.destinationairportid=f2.departureairportid "+
				"INNER JOIN airport a1 on f1.departureairportid=a1.id "+
				"INNER JOIN airport a2 on f1.destinationairportid=a2.id and f2.departureairportid=a2.id "+
				"INNER JOIN airport a3 on f2.destinationairportid=a3.id "+
				"INNER JOIN airline airl1 on f1.airlineID=airl1.id "+
				"INNER JOIN airline airl2 on f2.airlineID=airl2.id "+
				"INNER JOIN city c1 on a1.cityid=c1.id "+
				"INNER JOIN city c2 on a2.cityid=c2.id "+
				"INNER JOIN city c3 on a3.cityid=c3.id "+
				"WHERE a1.airportcode='" + departureAirportCode +"' "+
				" AND a3.airportcode='" + destinationAirportCode +"' "+
				" AND date_trunc('day',f1.DepartureTime) >=  '" + departureTime +"' "+
				" AND date_trunc('day',f1.DepartureTime) <= DATE '" + departureTime + "' + " + DAYS_FORWARD_SEARCH+
				" AND f1.arrivaltime<=f2.departureTime "+
				" AND f1.arrivaltime + interval '6 hours'>=f2.departureTime ";
			if (req.query.maxPrice != undefined && (req.query.maxTime != undefined)) { //kiedy i cena i czas
				queryBuilder += " and f1.price + f2.price<=" + maxPrice +
					" and (DATE_PART('day',f2.arrivaltime-f1.departuretime)*1440) + (DATE_PART('hour',f2.arrivaltime-f1.departuretime)*60)+ DATE_PART('minute',f2.arrivaltime-f1.departuretime) <= EXTRACT(hour FROM TIME '" + maxTime + "')*60+ EXTRACT(minute FROM TIME '" + maxTime + "') "
			}
			else if (req.query.maxTime != undefined && req.query.maxPrice == undefined) {	//kiedy tylko na czas
				queryBuilder += " and (DATE_PART('day',f2.arrivaltime-f1.departuretime)*1440) + (DATE_PART('hour',f2.arrivaltime-f1.departuretime)*60)+ DATE_PART('minute',f2.arrivaltime-f1.departuretime) <= EXTRACT(hour FROM TIME '" + maxTime + "')*60+ EXTRACT(minute FROM TIME '" + maxTime + "') "
			}
			else if ((req.query.maxPrice != undefined && req.query.maxTime == undefined)) { 	//kiedy tylko na cene
				queryBuilder += " and f1.price + f2.price<=" + maxPrice
			}

		}
		if (req.query.maxChanges >= 2) {
			queryBuilder += " union all select f1.flighttime as f1FlightTime, f1.departuretime::text as f1DepartureTime,f1.arrivaltime::text as f1arrivaltime, f1.price as f1Price,f1.additionalinformation as f1addInfo,f1.flightclass as f1class,f1.lastactualization::text as f1lastact,"+
				"a1.airportname as ap1name, a1.airportcode as ap1code,"+
				"a2.airportname as ap2name, a2.airportcode as ap2code,"+
				"airl1.name as airlineName1, airl1.website as airlineWebsite1,"+
				"c1.cityname as city1,"+
				"c2.cityname as city2, "+
				"f2.flighttime as f2FlightTime, f2.departuretime::text as f2DepartureTime, f2.arrivaltime::text as f2arrivaltime,f2.price as f2Price,f2.additionalinformation as f2addInfo,f2.flightclass as f2class,f2.lastactualization::text as f2lastact,"+
				"a3.airportname as ap3name, a3.airportcode as ap3code,"+
				"airl2.name as airlineName2, airl2.website as airlineWebsite2,"+
				"c3.cityname as city3,"+
				"f3.flighttime::text as f3FlightTime, f3.departuretime::text as f3DepartureTime, f3.arrivaltime::text as f3arrivaltime, f3.price::text as f3Price,f3.additionalinformation as f3addInfo,f3.flightclass as f3class,f3.lastactualization::text as f3lastact,"+
				"a4.airportname as ap4name, a4.airportcode as ap4code,"+
				"airl3.name as airlineName3, airl3.website as airlineWebsite3,"+
				"c4.cityname as city4, "+
				"null as f4FlightTime, null as f4DepartureTime, null as f4arrivalTime, null as f4Price,null as f4addInfo, null as f4class,null as f4lastact,"+

				"null as ap5name, null as ap5code,"+
				"null as airlineName4, null as airlineWebsite4,"+
				"null as city5 "+
				"from flight f1 "+
				"INNER JOIN flight f2 on f1.destinationairportid=f2.departureairportid "+
				"INNER JOIN flight f3 on f2.destinationairportid=f3.departureairportid "+
				"INNER JOIN airport a1 on f1.departureairportid=a1.id "+
				"INNER JOIN airport a2 on f1.destinationairportid=a2.id and f2.departureairportid=a2.id "+
				"INNER JOIN airport a3 on f2.destinationairportid=a3.id and f3.departureairportid=a3.id "+
				"INNER JOIN airport a4 on f3.destinationairportid=a4.id "+
				"INNER JOIN airline airl1 on f1.airlineID=airl1.id "+
				"INNER JOIN airline airl2 on f2.airlineID=airl2.id "+
				"INNER JOIN airline airl3 on f3.airlineID=airl3.id "+
				"INNER JOIN city c1 on a1.cityid=c1.id "+
				"INNER JOIN city c2 on a2.cityid=c2.id "+
				"INNER JOIN city c3 on a3.cityid=c3.id "+
				"INNER JOIN city c4 on a4.cityid=c4.id "+
				"WHERE a1.airportcode='" + departureAirportCode + "' "+
				" AND a4.airportcode='" + destinationAirportCode + "' "+
				" AND date_trunc('day',f1.DepartureTime) >=  '" + departureTime +"' "+
				" AND date_trunc('day',f1.DepartureTime) <= DATE '" + departureTime + "' + " + DAYS_FORWARD_SEARCH+
				" AND f1.arrivaltime<=f2.departureTime"+
				" AND f1.arrivaltime + interval '6 hours'>=f2.departureTime";
			if (req.query.maxPrice != undefined && (req.query.maxTime != undefined)) { //kiedy i cena i czas
				queryBuilder += " and f1.price + f2.price+f3.price<=" + maxPrice +
					" and (DATE_PART('day',f3.arrivaltime-f1.departuretime)*1440) + (DATE_PART('hour',f3.arrivaltime-f1.departuretime)*60)+ DATE_PART('minute',f3.arrivaltime-f1.departuretime) <= EXTRACT(hour FROM TIME '" + maxTime + "')*60+ EXTRACT(minute FROM TIME '" + maxTime + "') "
			}
			else if (req.query.maxTime != undefined && req.query.maxPrice == undefined) {	//kiedy tylko na czas
				queryBuilder += " and (DATE_PART('day',f3.arrivaltime-f1.departuretime)*1440) + (DATE_PART('hour',f3.arrivaltime-f1.departuretime)*60)+ DATE_PART('minute',f3.arrivaltime-f1.departuretime) <= EXTRACT(hour FROM TIME '" + maxTime + "')*60+ EXTRACT(minute FROM TIME '" + maxTime + "') "
			}
			else if ((req.query.maxPrice != undefined && req.query.maxTime == undefined)) { 	//kiedy tylko na cene
				queryBuilder += " and f1.price + f2.price+f3.price<=" + maxPrice
			}

		}
		if (req.query.maxChanges >= 3) {
			queryBuilder += " union all select f1.flighttime as f1FlightTime, f1.departuretime::text as f1DepartureTime,f1.arrivaltime::text as f1arrivaltime, f1.price as f1Price,f1.additionalinformation as f1addInfo,f1.flightclass as f1class,f1.lastactualization::text as f1lastact,"+
				"a1.airportname as ap1name, a1.airportcode as ap1code,"+
				"a2.airportname as ap2name, a2.airportcode as ap2code,"+
				"airl1.name as airlineName1, airl1.website as airlineWebsite1,"+
				"c1.cityname as city1,"+
				"c2.cityname as city2, "+
				"f2.flighttime as f2FlightTime, f2.departuretime::text as f2DepartureTime,f2.arrivaltime::text as f2arrivaltime, f2.price as f2Price,f2.additionalinformation as f2addInfo,f2.flightclass as f2class,f2.lastactualization::text as f2lastact,"+
				"a3.airportname as ap3name, a3.airportcode as ap3code,"+
				"airl2.name as airlineName2, airl2.website as airlineWebsite2,"+
				"c3.cityname as city3,"+
				"f3.flighttime::text as f3FlightTime, f3.departuretime::text as f3DepartureTime, f3.arrivaltime::text as f3arrivaltime,f3.price::text as f3Price,f3.additionalinformation as f3addInfo,f3.flightclass as f3class,f3.lastactualization::text as f3lastact,"+
				"a4.airportname as ap4name, a4.airportcode as ap4code,"+
				"airl3.name as airlineName3, airl3.website as airlineWebsite3,"+
				"c4.cityname as city4,"+
				"f4.flighttime::text as f4FlightTime, f4.departuretime::text as f4DepartureTime, f4.arrivaltime::text as f4arrivaltime,f4.price::text as f4Price,f4.additionalinformation as f4addInfo,f4.flightclass as f4class,f4.lastactualization::text as f4lastact, "+
				"a5.airportname as ap5name, a5.airportcode as ap5code,"+
				"airl4.name as airlineName4, airl4.website as airlineWebsite4,"+
				"c5.cityname as city5 "+
				"from flight f1 "+
				"INNER JOIN flight f2 on f1.destinationairportid=f2.departureairportid "+
				"INNER JOIN flight f3 on f2.destinationairportid=f3.departureairportid "+
				"INNER JOIN flight f4 on f3.destinationairportid=f4.departureairportid "+
				"INNER JOIN airport a1 on f1.departureairportid=a1.id "+
				"INNER JOIN airport a2 on f1.destinationairportid=a2.id and f2.departureairportid=a2.id "+
				"INNER JOIN airport a3 on f2.destinationairportid=a3.id and f3.departureairportid=a3.id "+
				"INNER JOIN airport a4 on f3.destinationairportid=a4.id and f4.departureairportid=a4.id "+
				"INNER JOIN airport a5 on f4.destinationairportid=a5.id "+
				"INNER JOIN airline airl1 on f1.airlineID=airl1.id "+
				"INNER JOIN airline airl2 on f2.airlineID=airl2.id "+
				"INNER JOIN airline airl3 on f3.airlineID=airl3.id "+
				"INNER JOIN airline airl4 on f4.airlineID=airl4.id "+
				"INNER JOIN city c1 on a1.cityid=c1.id "+
				"INNER JOIN city c2 on a2.cityid=c2.id "+
				"INNER JOIN city c3 on a3.cityid=c3.id "+
				"INNER JOIN city c4 on a4.cityid=c4.id "+
				"INNER JOIN city c5 on a5.cityid=c5.id "+
				"WHERE a1.airportcode='" + departureAirportCode +"' "+
				" AND a5.airportcode='" + destinationAirportCode +"' "+
				" AND date_trunc('day',f1.DepartureTime) >=  '" + departureTime +"' "+
				" AND date_trunc('day',f1.DepartureTime) <= DATE '" + departureTime + "' + " + DAYS_FORWARD_SEARCH+
				" AND f1.arrivaltime<=f2.departureTime"+
				" AND f1.arrivaltime + interval '6 hours'>=f2.departureTime"+
				" AND f2.arrivaltime<=f3.departureTime"+
				" AND f2.arrivaltime + interval '6 hours'>=f3.departureTime"+
				" AND f3.arrivaltime<=f4.departureTime"+
				" AND f3.arrivaltime + interval '6 hours'>=f4.departureTime ";
			if (req.query.maxPrice != undefined && (req.query.maxTime != undefined)) { //kiedy i cena i czas
				queryBuilder += " and f1.price + f2.price+f3.price+f4.price<=" + maxPrice +
					" and (DATE_PART('day',f4.arrivaltime-f1.departuretime)*1440) + (DATE_PART('hour',f4.arrivaltime-f1.departuretime)*60)" +
					"+ DATE_PART('minute',f4.arrivaltime-f1.departuretime) <= EXTRACT(hour FROM TIME '" + maxTime + "')*60+ EXTRACT(minute FROM TIME '" + maxTime + "') "
			}
			else if (req.query.maxTime != undefined && req.query.maxPrice == undefined) {	//kiedy tylko na czas
				queryBuilder +=" AND (DATE_PART('day',f4.arrivaltime-f1.departuretime)*1440) + (DATE_PART('hour',f4.arrivaltime-f1.departuretime)*60)+ DATE_PART('minute',f4.arrivaltime-f1.departuretime) <= EXTRACT(hour FROM TIME '" + maxTime + "')*60+ EXTRACT(minute FROM TIME '" + maxTime + "') "
			}
			else if ((req.query.maxPrice != undefined && req.query.maxTime == undefined)) { 	//kiedy tylko na cene
				queryBuilder += " AND f1.price + f2.price+f3.price+f4.price<=" + maxPrice;
			}

		}
		queryBuilder +=" order by f1DepartureTime ";
		console.log(queryBuilder);
		db.any(queryBuilder)
			.then(function (data) {
				log.dataRecieved(data);
				var destinationCity;
				for (var i in data) {
					if (data.hasOwnProperty(i) && data[i].city3 == null && data[i].city4 == null && data[i].city5 == null) {
						destinationCity=data[i].city2;
						flight.push({
							DepartureCity: data[i].city1,
							DestinationCity: data[i].city2,
							DepartureAirportName: data[i].ap1name,
							DepartureAirportCode: data[i].ap1code,
							DestinationAirportName: data[i].ap2name,
							DestinationAirportCode: data[i].ap2code,
							DepartureTime: data[i].f1departuretime,
							ArrivalTime: data[i].f1arrivaltime,
							FlightTime: data[i].f1flighttime,
							AirlineName: data[i].airlinename1,
							AirlineWebsite: data[i].airlinewebsite1,
							AdditionalInformation: data[i].f1addinfo,
							FlightClass: data[i].f1class,
							Price: data[i].f1price,
							LastActualization: data[i].f1lastact
						});
						result.push({
							departureCity: data[0].city1,
							destinationCity: destinationCity, /*must be checked*/
							flights: flight
						});
						flight=[];
					}
					else if (data.hasOwnProperty(i) && data[i].city3 != null && data[i].city4 == null && data[i].city5 == null) {
						destinationCity=data[i].city3;
						flight.push(
							{
								DepartureCity: data[i].city1,
								DestinationCity: data[i].city2,
								DepartureAirportName: data[i].ap1name,
								DepartureAirportCode: data[i].ap1code,
								DestinationAirportName: data[i].ap2name,
								DestinationAirportCode: data[i].ap2code,
								DepartureTime: data[i].f1departuretime,
								ArrivalTime: data[i].f1arrivaltime,
								FlightTime: data[i].f1flighttime,
								AirlineName: data[i].airlinename1,
								AirlineWebsite: data[i].airlinewebsite1,
								AdditionalInformation: data[i].f1addinfo,
								FlightClass: data[i].f1class,
								Price: data[i].f1price,
								LastActualization: data[i].f1lastact
							},
							{
								ID: data[i].f2id,
								DepartureCity: data[i].city2,
								DestinationCity: data[i].city3,
								DepartureAirportName: data[i].ap2name,
								DepartureAirportCode: data[i].ap2code,
								DestinationAirportName: data[i].ap3name,
								DestinationAirportCode: data[i].ap3code,
								DepartureTime: data[i].f2departuretime,
								ArrivalTime: data[i].f2arrivaltime,
								FlightTime: data[i].f2flighttime,
								AirlineName: data[i].airlinename2,
								AirlineWebsite: data[i].airlinewebsite2,
								AdditionalInformation: data[i].f2addinfo,
								FlightClass: data[i].f2class,
								Price: data[i].f2price,
								LastActualization: data[i].f2lastact
							});
						result.push({
							departureCity: data[0].city1,
							destinationCity: destinationCity, /*must be checked*/
							flights: flight
						});
						flight=[];
					}
					else if (data.hasOwnProperty(i) && data[i].city3 != null && data[i].city4 != null && data[i].city5 == null) {
						destinationCity=data[i].city4;
						flight.push(
							{
								DepartureCity: data[i].city1,
								DestinationCity: data[i].city2,
								DepartureAirportName: data[i].ap1name,
								DepartureAirportCode: data[i].ap1code,
								DestinationAirportName: data[i].ap2name,
								DestinationAirportCode: data[i].ap2code,
								DepartureTime: data[i].f1departuretime,
								ArrivalTime: data[i].f1arrivaltime,
								FlightTime: data[i].f1flighttime,
								AirlineName: data[i].airlinename1,
								AirlineWebsite: data[i].airlinewebsite1,
								AdditionalInformation: data[i].f1addinfo,
								FlightClass: data[i].f1class,
								Price: data[i].f1price,
								LastActualization: data[i].f1lastact
							},
							{
								ID: data[i].f2id,
								DepartureCity: data[i].city2,
								DestinationCity: data[i].city3,
								DepartureAirportName: data[i].ap2name,
								DepartureAirportCode: data[i].ap2code,
								DestinationAirportName: data[i].ap3name,
								DestinationAirportCode: data[i].ap3code,
								DepartureTime: data[i].f2deptime,
								ArrivalTime: data[i].f2arrivaltime,
								FlightTime: data[i].f2flightlime,
								AirlineName: data[i].airlinename2,
								AirlineWebsite: data[i].airlinewebsite2,
								AdditionalInformation: data[i].f2addinfo,
								FlightClass: data[i].f2class,
								Price: data[i].f2price,
								LastActualization: data[i].f2lastact
							},
							{
								ID: data[i].f3id,
								DepartureCity: data[i].city3,
								DestinationCity: data[i].city4,
								DepartureAirportName: data[i].ap3name,
								DepartureAirportCode: data[i].ap3code,
								DestinationAirportName: data[i].ap4name,
								DestinationAirportCode: data[i].ap4code,
								DepartureTime: data[i].f3deptime,
								ArrivalTime: data[i].f3arrivaltime,
								FlightTime: data[i].f3flightlime,
								AirlineName: data[i].airlinename3,
								AirlineWebsite: data[i].airlinewebsite3,
								AdditionalInformation: data[i].f3addinfo,
								FlightClass: data[i].f3class,
								Price: data[i].f3price,
								LastActualization: data[i].f3lastact
							}
						);
						result.push({
							departureCity: data[0].city1,
							destinationCity: destinationCity, /*must be checked*/
							flights: flight
						});
						flight=[];
					}
					else if (data.hasOwnProperty(i) && data[i].city3 != null && data[i].city4 != null && data[i].city5 != null) {
						destinationCity=data[i].city5;
						flight.push(
							{
								DepartureCity: data[i].city1,
								DestinationCity: data[i].city2,
								DepartureAirportName: data[i].ap1name,
								DepartureAirportCode: data[i].ap1code,
								DestinationAirportName: data[i].ap2name,
								DestinationAirportCode: data[i].ap2code,
								DepartureTime: data[i].f1departuretime,
								FlightTime: data[i].f1flighttime,
								ArrivalTime: data[i].f1arrivaltime,
								AirlineName: data[i].airlinename1,
								AirlineWebsite: data[i].airlinewebsite1,
								AdditionalInformation: data[i].f1addinfo,
								FlightClass: data[i].f1class,
								Price: data[i].f1price,
								LastActualization: data[i].f1lastact
							},
							{
								ID: data[i].f2id,
								DepartureCity: data[i].city2,
								DestinationCity: data[i].city3,
								DepartureAirportName: data[i].ap2name,
								DepartureAirportCode: data[i].ap2code,
								DestinationAirportName: data[i].ap3name,
								DestinationAirportCode: data[i].ap3code,
								DepartureTime: data[i].f2deptime,
								ArrivalTime: data[i].f2arrivaltime,
								FlightTime: data[i].f2flightlime,
								AirlineName: data[i].airlinename2,
								AirlineWebsite: data[i].airlinewebsite2,
								AdditionalInformation: data[i].f2addinfo,
								FlightClass: data[i].f2class,
								Price: data[i].f2price,
								LastActualization: data[i].f2lastact
							},
							{
								ID: data[i].f3id,
								DepartureCity: data[i].city3,
								DestinationCity: data[i].city4,
								DepartureAirportName: data[i].ap3name,
								DepartureAirportCode: data[i].ap3code,
								DestinationAirportName: data[i].ap4name,
								DestinationAirportCode: data[i].ap4code,
								DepartureTime: data[i].f3deptime,
								ArrivalTime: data[i].f3arrivaltime,
								FlightTime: data[i].f3flightlime,
								AirlineName: data[i].airlinename3,
								AirlineWebsite: data[i].airlinewebsite3,
								AdditionalInformation: data[i].f3addinfo,
								FlightClass: data[i].f3class,
								Price: data[i].f3price,
								LastActualization: data[i].f3lastact
							},
							{
								ID: data[i].f4id,
								DepartureCity: data[i].city4,
								DestinationCity: data[i].city5,
								DepartureAirportName: data[i].ap4name,
								DepartureAirportCode: data[i].ap4code,
								DestinationAirportName: data[i].ap5name,
								DestinationAirportCode: data[i].ap5code,
								DepartureTime: data[i].f4deptime,
								ArrivalTime: data[i].f4arrivaltime,
								FlightTime: data[i].f4flightlime,
								AirlineName: data[i].airlinename4,
								AirlineWebsite: data[i].airlinewebsite4,
								AdditionalInformation: data[i].f4addinfo,
								FlightClass: data[i].f4class,
								Price: data[i].f4price,
								LastActualization: data[i].f4lastact
							}
						);
						result.push({
							departureCity: data[0].city1,
							destinationCity: destinationCity, /*must be checked*/
							flights: flight
						});
						flight=[];
					}
				}
				res.contentType('application/json');
				res.send(JSON.stringify(result));
				log.success();
			})
			.catch(function (error) {
				log.failed(error);
				res.status(400).send({error: error.message});
			});
	});
}