package interaction;

import utils.animation.Route;

import java.io.Serializable;
import java.util.ArrayList;

public class Response  implements Serializable  {
    public String msg;
    public Status status;
    public ArrayList<Route> routeList;
    public utils.Route route;

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

    public Response routeList(ArrayList<Route> routeList) {
        this.routeList = routeList;
        return this;
    }

    public Response route(utils.Route route) {
        this.route = route;
        return this;
    }

    public Response(String msg, Status status, ArrayList<Route> routeList, utils.Route route) {
        this.msg = msg;
        this.status = status;
        this.routeList = routeList;
        this.route = route;
    }

    @Override
    public String toString() {
        return "Response{" +
                "msg='" + msg + '\'' +
                ", status=" + status +
                ", routeList=" + routeList +
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
