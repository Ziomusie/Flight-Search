ODPALAMY bin/www
localhost port 4000 (localhost:4000)

/api/getAirlines?name= - zwraca linie lotnicze z nazw� pasuj�c� do podanego ci�gu znak�w, zwraca JSONA z kolekcj� pasuj�cych wynik�w z polami id, name, website
/api/getListOfAirlines - zwraca wszystkie linie lotnicze znajdujace sie w bazie w postaci JSONA (json tak jak wy�ej)
/api/getAirport?name - zwraca lotnisko z nazw� posuj�c� do podanego ci�gu znak�w, zwraca JSONA z kolekcj� pasuj�cych lotnisk z polami: id, airportCode, airportName,
/api/getListOfAirports - zwraca wszystkie lotniska w bazie w postaci JSONA (json tak jak wy�ej)
/api/getListOfFlights - zwraca wszystkie loty w bazie
/api/addFlight - jeszcze nie smiga w 100%!