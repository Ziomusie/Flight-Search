INSERT INTO city (cityname, timezone) VALUES ('TEST',0);

select * from flight;

select * from airport where id=4
select * from airport where id=14

select f.*, a.airportcode  from flight f INNER JOIN airport a on f.departureairportid=a.id inner join airport a2 on f.destinationairportid=a2.id where a.airportcode='AKN' and a2.airportcode='AXP'

select * from airline where lower(name)  like '%belle%'
select * from airline


SELECT Customers.CustomerName, Orders.OrderID
FROM Customers
INNER JOIN Orders
ON Customers.CustomerID=Orders.CustomerID

				select f1.departureairportid, f2.destinationairportid,
				a1.airportcode as ap1code,
				a2.airportcode as ap2code,
				a3.airportcode as ap3code
				from flight f1
				INNER JOIN flight f2 on f1.destinationairportid=f2.departureairportid
				INNER JOIN airport a1 on f1.departureairportid=a1.id
				INNER JOIN airport a2 on f1.destinationairportid=a2.id and f2.departureairportid=a2.id
				inner join airport a3 on f2.destinationairportid=a3.id
				where a1.airportcode='AAH' and a3.airportcode='AAL'

select * from flight where airport code

select a1.airportcode,a2.airportcode, f1.price, f1.departuretime,f1.arrivaltime, a2.airportcode,a3.airportcode,f2.price,f2.departuretime, f2.arrivaltime  from flight f1
INNER JOIN flight f2 on f1.destinationairportid = f2.departureairportid
INNER JOIN airport a1 on f1.departureairportid=a1.id
INNER JOIN airport a2 on f1.destinationairportid=a2.id and f2.departureairportid=a2.id
inner join airport a3 on f2.destinationairportid=a3.id
where a1.airportcode='WAT' and  a3.airportcode='WAX' and f1.arrivaltime<f2.departuretime


select a.airportcode, a2.airportcode,a2.id from flight f
INNER JOIN airport a on f.departureairportid=a.id
INNER JOIN airport a2 on f.destinationairportid=a2.id
where a.airportcode='AAH'

select a.airportcode, a2.airportcode from flight f
INNER JOIN airport a on f.departureairportid=a.id
INNER JOIN airport a2 on f.destinationairportid=a2.id
where a.airportcode='ABC'

select * from flight where departureairportid=14



async.parallel([
    function(callback) {
        db.any("select f.*, a1.airportname as ap1name, a1.airportcode as ap1code, a2.airportname as ap2name, a2.airportcode as ap2code, airl.name as airlinename, airl.website as airlinewebsite, c1.cityname as depcity, c2.cityname as destcity from flight f INNER JOIN airport a1 on f.departureairportid=a1.id inner join airport a2 on f.destinationairportid=a2.id inner join airline airl on f.airlineID=airl.id inner join city c1 on a1.cityid=c1.id inner join city c2 on a2.cityid=c2.id " +
						"where a1.airportcode='" + departureAirportCode + "' and a2.airportcode='" + destinationAirportCode +
						"' and price<='" + maxPrice + "' and flighttime<='" + maxTime + "' and date_trunc('day',f.DepartureTime) >=  '" + departureTime + "' and date_trunc('day',f.DepartureTime) <= DATE '" + departureTime + "' + " + DAYS_FORWARD_SEARCH +
						"order by " + orderBy)
						.then(function (data) {
							console.log(data);
							for (var i in data) {
								flight.push({
									ID: data[i].id,
									DepartureCity: data[i].depcity,
									DestinationCity: data[i].destcity,
									DepartureAirportName: data[i].ap1name,
									DepartureAirportCode: data[i].ap1code,
									DestinationAirportName: data[i].ap2name,
									DestinationAirportCode: data[i].ap2code,
									DepartureTime: data[i].departuretime,
									FlightTime: data[i].flighttime,
									AirlineName: data[i].airlinename,
									AirlineWebsite: data[i].airlinewebsite,
									Price: data[i].price
								});
							}
							if (data.length != 0) {
								result.push({
									departureCity: data[0].depcity,
									destinationCity: data[0].destcity,
									flights: flight
								});
							}
							addToRequest(result);
							console.log("po blokadzie");
							//	block=false;
							callback()
						})
						.catch(function (error) {
							console.log("ERROR:", error);
							callback()
						});
    },
    function(callback) {
    	sendRequest(res);
    	callback();
    }
], function(err) {
    console.log('Both a and b are saved now');
});