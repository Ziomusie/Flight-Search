ODPALAMY bin/www
localhost port 4000 (localhost:4000)

/api/getAirlines?name= - zwraca linie lotnicze z nazw¹ pasuj¹c¹ do podanego ci¹gu znaków, zwraca JSONA z kolekcj¹ pasuj¹cych wyników z polami id, name, website
/api/getListOfAirlines - zwraca wszystkie linie lotnicze znajdujace sie w bazie w postaci JSONA (json tak jak wy¿ej)
/api/getAirport?name - zwraca lotnisko z nazw¹ posuj¹c¹ do podanego ci¹gu znaków, zwraca JSONA z kolekcj¹ pasuj¹cych lotnisk z polami: id, airportCode, airportName,
/api/getListOfAirports - zwraca wszystkie lotniska w bazie w postaci JSONA (json tak jak wy¿ej)
/api/getListOfFlights - zwraca wszystkie loty w bazie
/api/addFlight - jeszcze nie smiga w 100%!