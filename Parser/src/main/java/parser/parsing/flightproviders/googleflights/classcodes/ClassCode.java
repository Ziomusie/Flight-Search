package parser.parsing.flightproviders.googleflights.classcodes;

public enum ClassCode {
	/*
	 * Klasy dla lotów głównych:
	 */
	
	MAIN_ROOT,
	MAIN_PRICE,
	MAIN_OPERATOR,
	MAIN_HOURS,
	MAIN_DURATION,
	MAIN_DIRECT_TEXT,
	MAIN_FLIGHT_CLASS,
	/**
	 * Element z tekstem "Wybierz lot" na niebieskim tle.
	 * Potrzebne do tego, żeby czekać na zwinięcie się panelu przesiadki.
	 */
	MAIN_CHOOSE_FLIGHT,
	
	/*
	 * Klasy dla przesiadek:
	 */
	
	/**
	 * Blok obejmujący informacje nt. jednej przesiadki - tam gdzie jest logo przewoźnika, godzina, lotniska itd.
	 * Nieparzyste wiersze są niepotrzebne - jest tam ten szary tekst o mieście przesiadki.
	 */
	CHANGES_CHANGE_ROWS,
	/**
	 * Opis lotnisk (z i do).
	 */
	CHANGES_AIRPORTS,
	/**
	 * Godziny lotu - godzina startu i lądowania.
	 */
	CHANGES_HOURS,
	/**
	 * Czas trwania lotu (po prawej stronie).
	 */
	CHANGES_DURATION,
	/**
	 * Przewoźnik (cały tekst napisany szarą czcionką).
	 */
	CHANGES_OPERATOR,
	/**
	 * "X" do zamknięcia info o przesiadce.
	 */
	CHANGES_CLOSE_BUTTON,
	/**
	 * Przycisk "Udostępnij" na dole rozwiniętego panelu przesiadki.
	 * Potrzebne do tego, żeby czekać na rozwinięcie panelu przesiadki.
	 */
	CHANGES_SHARE_BUTTON
}
