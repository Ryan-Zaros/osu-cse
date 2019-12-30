import components.stack.Stack;
import components.stack.Stack1L;

/**
 * Customized JUnit test fixture for {@code Stack2}.
 */
public class Stack2Test extends StackTest {

    @Override
    protected final Stack<String> constructorTest() {
        return new Stack2<String>();
    }

    @Override
    protected final Stack<String> constructorRef() {
        return new Stack1L<String>();
    }

}
