package interaction;


import java.io.Serializable;
import java.util.List;

public class Request implements Serializable {
    private List<String> args;
    private User user;

    public Request(List<String> args, User user) {
        this.args = args;
        this.user = user;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Request{" +
                "args=" + args +
                ", user=" + user +
                '}';
    }

    public Request() {}
}
