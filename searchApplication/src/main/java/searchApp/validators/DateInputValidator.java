package searchApp.validators;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.validation.base.ValidatorBase;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Created by Mateusz-PC on 01.08.2016.
 */
public class DateInputValidator extends ValidatorBase{

	private JFXDatePicker departureDate;

	private JFXDatePicker returnDate;

	public DateInputValidator(JFXDatePicker departureDate, JFXDatePicker returnDate) {
		this.departureDate = departureDate;
		this.returnDate = returnDate;
	}

	@Override
	protected void eval() {
		if (departureDate.getValue() == null) {
			hasErrors.set(true);
			setMessage("Data wylotu nie może być pusta");
			setVisible(true);
		} else if (returnDate.getValue() != null && departureDate.getValue().isAfter(returnDate.getValue())) {
			hasErrors.set(true);
			setMessage("Data wylotu nie może być po dacie powrotu");
		} else if (departureDate.getValue().isBefore(LocalDate.now()) || (returnDate.getValue() != null && returnDate.getValue().isBefore(LocalDate.now()))){
			hasErrors.set(true);
			setMessage("Data wylotu już mineła");
		} else {
			hasErrors.set(false);
		}
	}

}
