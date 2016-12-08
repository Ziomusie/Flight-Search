package searchApp.tasks;

import com.jfoenix.controls.JFXSpinner;
import javafx.concurrent.Task;
import javafx.scene.layout.Pane;
import model.JourneyRequest;
import model.JourneyResponse;
import parser.ConnectionFinder;
import searchApp.builders.ResultTableBuilder;
import searchApp.builders.rowBuilders.ParserTableFiller;

import java.util.concurrent.ExecutionException;

/**
 * Created by Mateusz-PC on 16.07.2016.
 */
public class SearchTask extends Task<Boolean> {

    private JFXSpinner spinner;

    private Pane rootPane;

	private JourneyRequest journeyRequest;

	private ResultTableBuilder resultTableBuilder;

	public SearchTask(Pane rootPane, ResultTableBuilder resultTableBuilder, JourneyRequest request) {
        this.rootPane = rootPane;
        this.resultTableBuilder = resultTableBuilder;
		journeyRequest = request;
		spinner = AdvancedSearchTask.initSpinner(rootPane);
    }

	@Override
    protected Boolean call() throws Exception {
		return true;
    }

    @Override
    protected void running() {
        spinner.setVisible(true);
        resultTableBuilder.getResultTable().setVisible(false);
    }

	@Override
	protected void failed() {
		spinner.setVisible(false);
		rootPane.getChildren().remove(spinner);
		System.out.println("Wystąpił błąd");
	}

	public JourneyRequest getJourneyRequest() {
		return journeyRequest;
	}

	public JFXSpinner getSpinner() {
		return spinner;
	}

	public Pane getRootPane() {
		return rootPane;
	}

	public ResultTableBuilder getResultTableBuilder() {
		return resultTableBuilder;
	}
}
