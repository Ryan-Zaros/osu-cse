import components.stack.Stack;
import components.stack.Stack1L;
import components.stack.Stack3;

/**
 * Customized JUnit test fixture for {@code Stack1L}.
 */
public class Stack1LTest extends StackTest {

    @Override
    protected final Stack<String> constructorTest() {

        return new Stack1L<>();
    }

    @Override
    protected final Stack<String> constructorRef() {

        return new Stack3<>();
    }

}