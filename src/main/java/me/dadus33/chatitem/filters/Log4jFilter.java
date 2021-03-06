package me.dadus33.chatitem.filters;

import me.dadus33.chatitem.utils.Storage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.CopyOnWriteArrayList;

import static org.apache.logging.log4j.core.Filter.Result.NEUTRAL;


public class Log4jFilter implements Filter {

    private boolean stopped;
    private final static CopyOnWriteArrayList<String> ls = new CopyOnWriteArrayList<>();
    public Storage c;

    public Log4jFilter(Storage st){
        c = st;
        ((Logger) LogManager.getRootLogger()).addFilter(this);
    }


    @Override
    public Result getOnMismatch() {
        return NEUTRAL;
    }

    @Override
    public Result getOnMatch() {
        return NEUTRAL;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object... objects) {
        return checkMessage(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object o, Throwable throwable) {
        return checkMessage(((Message)o).getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message message, Throwable throwable) {
        return checkMessage(message.getFormattedMessage());
    }

    @Override
    public Result filter(LogEvent logEvent) {
        return checkMessage(logEvent.getMessage().getFormattedMessage());
    }

    @Override
    public void start() {
        stopped = false;
    }

    @Override
    public void stop() {
        stopped = true;
    }

    @Override
    public boolean isStarted() {
        return !this.stopped;
    }

    @Override
    public boolean isStopped() {
        return this.stopped;
    }

    private Result checkMessage(String msg){
        for(String placeholder : c.PLACEHOLDERS){
            if(msg.contains(placeholder)){
                ls.add(msg);
                for(Player p : Bukkit.getOnlinePlayers()){
                    msg = msg.replaceAll("\\p{C}", "");
                    if(msg.endsWith(p.getName()) || msg.lastIndexOf(p.getName())+2+p.getName().length()>=msg.length()){
                        return Result.DENY;
                    }
                }

            }
        }

        return Result.NEUTRAL;

    }

    public void setStorage(Storage nst){
        this.c = nst;
    }
}
