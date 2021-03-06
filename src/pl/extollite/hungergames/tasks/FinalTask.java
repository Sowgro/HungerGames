package pl.extollite.hungergames.tasks;


import cn.nukkit.player.Player;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.data.ConfigData;
import pl.extollite.hungergames.game.Game;
import pl.extollite.hungergames.game.Status;

public class FinalTask implements Runnable {

    private int timer;
    private int teleportCountdownEnd;
    private int id;
    private Game game;

    public FinalTask(Game g) {
        this.timer = ConfigData.finalCountdownStart - ConfigData.finalCountdownEnd + ConfigData.finalFreeze;
        this.teleportCountdownEnd = ConfigData.finalFreeze + 1;
        this.game = g;

        this.id = HG.getInstance().getServer().getScheduler().scheduleRepeatingTask(HG.getInstance(),this, 20).getTaskId();
    }

    @Override
    public void run() {
        if (timer > teleportCountdownEnd) {
            game.tipAll(HG.getInstance().getLanguage().getGame_teleport().replace("%seconds%", String.valueOf(timer - ConfigData.finalFreeze)));
        } else if(timer == teleportCountdownEnd){
            game.tipAll(HG.getInstance().getLanguage().getGame_teleport().replace("%seconds%", String.valueOf(timer - ConfigData.finalFreeze)));
            game.startFinal();
        } else if (timer == 0){
            game.titleAll("Fight!");
            game.setStatus(Status.FINAL);
            for (Player p : game.getPlayers()) {
                game.unFreeze(p);
            }
            stop();
        } else{
            game.titleAll(String.valueOf(timer));
        }
        timer--;
    }

    public void stop() {
        HG.getInstance().getServer().getScheduler().cancelTask(id);
    }
}
