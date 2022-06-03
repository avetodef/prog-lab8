package interaction;

import lombok.AllArgsConstructor;
import lombok.Setter;
import utils.Route;
import utils.animation.AnimationRoute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Deque;

@AllArgsConstructor
public class Response implements Serializable {
    public String msg;
    public Status status;
    @Setter
    public Deque<Route> collection;
    public utils.Route route;
    @Setter
    public ArrayList<AnimationRoute> animationRouteList;
    @Setter
    public ArrayList<Route> routeArrayList;


    //            #1
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    //            #2

    public Response msg(String msg) {
        this.msg = msg;
        return this;
    }

    public Response status(Status status) {
        this.status = status;
        return this;
    }

    public Response route(utils.Route route) {
        this.route = route;
        return this;
    }

    @Override
    public String toString() {
        return "Response{" +
                "msg='" + msg + '\'' +
                ", status=" + status +
                ", collection=" + collection +
                ", route=" + route +
                ", animationRouteList=" + animationRouteList +
                ", routeArrayList=" + routeArrayList +
                '}';
    }

    public Response routeList(ArrayList<AnimationRoute> animationRouteList) {
        this.animationRouteList = animationRouteList;
        return this;
    }


    public Response(String msg, Status status) {
        this.msg = msg;
        this.status = status;
    }

    public Response() {
    }

    public String getMsg() {
        return msg;
    }

    public Status getStatus() {
        return status;
    }

}
