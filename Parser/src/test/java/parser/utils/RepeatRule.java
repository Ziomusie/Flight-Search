package parser.utils;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepeatRule implements TestRule {
	private static final Logger log = LoggerFactory.getLogger(RepeatRule.class);
	
	private static class RepeatStatement extends Statement {
		private final int times;
		private final Statement statement;
		
		RepeatStatement(int times, Statement statement) {
			this.times = times;
			this.statement = statement;
		}
		
		@Override
		public void evaluate() throws Throwable {
			for (int i = 0; i < times; i++) {
				log.info("Test no {}", i+1);
				System.out.println("");
				statement.evaluate();
				System.out.println("\n");
			}
		}
	}
	
	@Override
	public Statement apply(Statement base, Description description) {
		Statement result = base;
		Repeat repeat = description.getAnnotation(Repeat.class);
		if (repeat != null) {
			result = new RepeatStatement(repeat.value(), base);
		}
		return result;
	}
}
