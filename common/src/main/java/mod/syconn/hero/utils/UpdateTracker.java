package mod.syconn.hero.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.architectury.platform.Platform;
import mod.syconn.hero.client.screen.widgets.ExpandedButton;
import mod.syconn.hero.utils.generic.FontUtil;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UpdateTracker {

    private static final String LINK = "https://raw.githubusercontent.com/Syconn/Syconn-Mod-Updates/refs/heads/main/HeroCore.json";

    private final JsonObject jsonData;

    public UpdateTracker() {
        this.jsonData = this.getTrackerData();
    }

    private JsonObject getTrackerData() {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder().uri(URI.create(LINK)).GET().build();

        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                try {
                    return new Gson().fromJson(response.body(), JsonObject.class);
                } catch (JsonSyntaxException e) {
                    return null;
                }
            }
        } catch (IOException | InterruptedException ignored) { }
        return null;
    }

    public boolean isValid() {
        return this.jsonData != null;
    }

    public String latestVersion() {
        var version = Platform.getMinecraftVersion() + "-latest";
        if (!jsonData.has("promos") && jsonData.get("promos").getAsJsonObject().has(version)) return "";
        return jsonData.get("promos").getAsJsonObject().get(version).getAsString();
    }

    public String homepageLink() {
        if (!jsonData.has("homepage")) return "";
        return jsonData.get("homepage").getAsString();
    }

    public boolean shouldUpdate() {
        if (this.isValid()) return this.isOutdated(Platform.getMod(Constants.MOD).getVersion(), this.latestVersion());
        return false;
    }

    public void clientPlayerJoined(LocalPlayer player) {
        var mod = Platform.getMod(Constants.MOD).getName();
        if (this.shouldUpdate()) player.sendSystemMessage(FontUtil.newChatWithLinks(mod + " version " + this.latestVersion() + " the is out of date, consider updating to "
                + this.latestVersion() + " at " + this.homepageLink()));
    }

    private boolean isOutdated(String currentVersion, String latestVersion) {
        String[] currentParts = currentVersion.split("\\.");
        String[] latestParts = latestVersion.split("\\.");

        int maxLength = Math.max(currentParts.length, latestParts.length);

        for (int i = 0; i < maxLength; i++) {
            int current = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;
            int latest = i < latestParts.length ? Integer.parseInt(latestParts[i]) : 0;

            if (current < latest) return true;
            if (current > latest) return false;
        }

        return false;
    }

    public static class UpdateScreen extends Screen {

        private final Screen lastScreen;

        public UpdateScreen(Screen lastScreen) {
            super(Component.literal("Update Screen"));
            this.lastScreen = lastScreen;
        }

        @Override
        protected void init() {
            int l = this.height / 4 + 48;
            this.addRenderableWidget(new ExpandedButton(this.width / 2 - 100, l + 72 + 12, 98, 20, Component.literal("Update"), b -> {
                Util.getPlatform().openUri(Constants.TRACKER.homepageLink());
                this.minecraft.setScreen(this.lastScreen);
            }));
            this.addRenderableWidget(new ExpandedButton(this.width / 2 + 2, l + 72 + 12, 98, 20, Component.literal("Continue"), b -> this.minecraft.setScreen(this.lastScreen)));
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            this.renderDirtBackground(guiGraphics);
            super.render(guiGraphics, mouseX, mouseY, partialTick);

            var mod = Platform.getMod(Constants.MOD).getName();
            var tracker = Constants.TRACKER;
            if (this.minecraft != null) {
                guiGraphics.drawCenteredString(this.minecraft.font, mod + " version " + tracker.latestVersion() + " is out of date,", this.width / 2, 50, 14737632);
                guiGraphics.drawCenteredString(this.minecraft.font, "consider updating to " + tracker.latestVersion() + " at", this.width / 2, 65, 14737632);
                guiGraphics.drawCenteredString(this.minecraft.font, FontUtil.newChatWithLinks(tracker.homepageLink()), this.width / 2, 80, 14737632);
            }
        }
    }
}