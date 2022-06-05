package guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import console.MessageHandler;

public class A {


    SimpleInterface s;

    @Inject
    public A(SimpleInterface s) {
        this.s = s;
    }

    public static void main(String[] args) {

        Injector injector = Guice.createInjector(new A.SModule());

        A a = injector.getInstance(A.class);

        a.wqewqeqewqeqwe();

    }

    private void wqewqeqewqeqwe() {
        s.print("qqqqqq");
    }

    public static class SModule extends AbstractModule {
        @Override

        protected void configure() {
            bind(SimpleInterface.class).to(ConsoleMessager.class);
        }
    }
}
