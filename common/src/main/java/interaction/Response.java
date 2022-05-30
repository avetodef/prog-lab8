package interaction;

import lombok.AllArgsConstructor;
import utils.animation.AnimationRoute;

import java.io.Serializable;
import java.util.ArrayList;

@AllArgsConstructor
public class Response  implements Serializable  {
    public String msg;
    public Status status;
    public ArrayList<AnimationRoute> animationRouteList;
    public utils.Route route;
    public ArrayList<utils.Route> arrayRoute;

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

    public Response routeList(ArrayList<AnimationRoute> animationRouteList) {
        this.animationRouteList = animationRouteList;
        return this;
    }

    public Response route(utils.Route route) {
        this.route = route;
        return this;
    }


    public Response(String msg, Status status, ArrayList<AnimationRoute> animationRouteList, utils.Route route) {
        this.msg = msg;
        this.status = status;
        this.animationRouteList = animationRouteList;
        this.route = route;
    }

    @Override
    public String toString() {
        return "Response{" +
                "msg='" + msg + '\'' +
                ", status=" + status +
                ", animationRouteList=" + animationRouteList +
                ", route=" + route +
                ", arrayRoute=" + arrayRoute +
                '}';
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
