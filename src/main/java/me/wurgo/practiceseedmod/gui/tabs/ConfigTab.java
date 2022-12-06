package me.wurgo.practiceseedmod.gui.tabs;

import me.wurgo.practiceseedmod.core.config.ConfigUtils;
import me.wurgo.practiceseedmod.core.config.ConfigWrapper;
import me.wurgo.practiceseedmod.gui.ModConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public abstract class ConfigTab extends Screen {
    public enum Tabs {
        MISC(MiscConfigTab.class),
        BLOCKS(BlocksConfigTab.class),
        ENTITIES(EntitiesConfigTab.class);

        private final Class<? extends ConfigTab> screen;

        Tabs(Class<? extends ConfigTab> screen) {
            this.screen = screen;
        }

        public Class<? extends ConfigTab> getScreen() {
            return this.screen;
        }

        public String getName() {
            try {
                return this.getScreen().getDeclaredConstructor().newInstance().title.getString();
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public final MinecraftClient client;
    public final ConfigWrapper wrapper;
    public final int btnWidth = 200;
    public final int btnHeight = 20;
    public final int spacingY = 24;

    public ConfigTab(String name) {
        super(new LiteralText(name));

        this.wrapper = new ConfigWrapper();
        this.client = MinecraftClient.getInstance();
    }

    public abstract void addButtons();

    @SuppressWarnings("emptyMethod")
    public void buttonCheck() {}

    public String getToggleText(boolean value) {
        return value ? "Yes" : "No";
    }

    public int getButtonAmount() {
        return this.buttons.size();
    }

    public int compareAB(int a, int b, int limit) {
        return ConfigUtils.compareAB(a, b, limit);
    }

    public int compareBA(int b, int a, int limit) {
        return ConfigUtils.compareBA(b, a, limit);
    }

    @Override
    protected void init() {
        if (this.client == null) { return; }

        this.addButtons();

        this.addButton(
                new ButtonWidget(
                        this.width / 2 - this.btnWidth / 2,
                        this.height / 4 + spacingY * (this.getButtonAmount() + 1),
                        this.btnWidth,
                        this.btnHeight,
                        ScreenTexts.DONE,
                        b -> this.onClose()
                )
        );
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 13, 16777215);

        this.buttonCheck();

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        this.wrapper.save();
        this.client.openScreen(new ModConfigScreen());
    }
}
