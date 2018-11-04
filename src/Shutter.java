import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.input.Keyboard;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@ScriptMeta(name = "DChest Shutter", category = ScriptCategory.OTHER, developer = "Dungeonqueer", desc = "Ruins drop parties")
public class Shutter extends Script implements RenderListener {
    private int ruinedChests;
    private List<String> embellishments, taunts;
    private Random random;

    @Override
    public void onStart() {
        random = new Random();
        try {
            embellishments = new BufferedReader(new InputStreamReader(new URL("https://gist.githubusercontent.com/David-Rodden/b09dc8be2074c5ec2c9f4cf4cb7c1ef5/raw/a31ec8c7fea55583029a9e79cc5be2a91d1b7ce5/embellishments").openStream())).lines().collect(Collectors.toList());
            taunts = new BufferedReader(new InputStreamReader(new URL("https://gist.githubusercontent.com/David-Rodden/309d6299c6dcb0cd0413fea04162115a/raw/febe00c4b4efbceb38a5f14b7b8e309d44ad4d6c/taunts").openStream())).lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ruinedChests = 0;
        super.onStart();
    }

    @Override
    public int loop() {
        final SceneObject chest = findChest();
        if (chest == null) return 50;
        chest.interact("Shut");
        Time.sleepUntil(() -> findChest() == null, 500);
        if (findChest() == null) ruinedChests++;
        if (taunts == null || random.nextInt(10) != 0) return 0;
        Keyboard.sendText(embellishments.get(random.nextInt(embellishments.size())) + taunts.get(random.nextInt(taunts.size())));
        Keyboard.pressEnter();
        return 0;
    }

    private SceneObject findChest() {
        return SceneObjects.getNearest(sceneObject -> sceneObject != null && sceneObject.getName().matches("Chest") && sceneObject.containsAction("Shut"));
    }

    @Override
    public void notify(final RenderEvent renderEvent) {
        final Graphics graphics = renderEvent.getSource();
        graphics.setColor(Color.CYAN);
        graphics.drawString("Chests ruined: " + ruinedChests, 20, 20);
        if (taunts != null) return;
        graphics.setColor(Color.RED);
        graphics.drawString("Taunts not set", 20, 30);
    }
}
