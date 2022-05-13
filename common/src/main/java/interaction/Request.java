package interaction;

import utils.RouteInfo;

import java.io.Serializable;
import java.util.List;

public class Request implements Serializable {
    private List<String> args;
    private RouteInfo info;
    private User user;

    public Request(List<String> args, RouteInfo info, User user) {
        this.args = args;
        this.info = info;
        this.user = user;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public RouteInfo getInfo() {
        return info;
    }

    public void setInfo(RouteInfo info) {
        this.info = info;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Request{" +
                "args=" + args +
                ", info=" + info +
                ", user=" + user +
                '}';
    }

    public Request() {}
}
