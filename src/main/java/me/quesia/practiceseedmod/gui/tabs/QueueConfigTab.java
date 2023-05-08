package me.quesia.practiceseedmod.gui.tabs;

import me.quesia.practiceseedmod.PracticeSeedMod;
import me.quesia.practiceseedmod.core.Seed;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

public class QueueConfigTab extends ConfigTab {
    private Screen parent;
    private QueueListWidget queueListWidget;
    private ButtonWidget playButton;
    private ButtonWidget removeButton;

    public QueueConfigTab() {
        super("Queue");
    }

    public QueueConfigTab(Screen parent) {
        this();
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.queueListWidget = new QueueListWidget(this.client);
        this.children.add(this.queueListWidget);

        int btnWidth = this.btnWidth / 2;

        this.playButton = this.addButton(
                new ButtonWidget(
                        this.width / 2 - btnWidth / 2 - btnWidth - 5,
                        this.height - 38,
                        btnWidth,
                        this.btnHeight,
                        new LiteralText("Play").formatted(Formatting.GREEN),
                        b -> {
                            QueueListWidget.QueueEntry entry = this.queueListWidget.getSelected();
                            if (entry != null) {
                                PracticeSeedMod.QUEUE.remove(entry.index);
                                PracticeSeedMod.playNextSeed(entry.seed);
                            }
                        }
                )
        );
        this.playButton.active = this.queueListWidget.getSelected() != null;
        this.addButton(
                new ButtonWidget(
                        this.width / 2 - btnWidth / 2,
                        this.height - 38,
                        btnWidth,
                        this.btnHeight,
                        ScreenTexts.DONE,
                        b -> this.onClose()
                )
        );
        this.removeButton = this.addButton(
                new ButtonWidget(
                        this.width / 2 - btnWidth / 2 + btnWidth + 5,
                        this.height - 38,
                        btnWidth,
                        this.btnHeight,
                        new LiteralText("Remove").formatted(Formatting.RED),
                        b -> {
                            QueueListWidget.QueueEntry entry = this.queueListWidget.getSelected();
                            if (entry != null) {
                                PracticeSeedMod.QUEUE.remove(entry.index);
                                this.queueListWidget.children().remove(entry);
                                this.queueListWidget.setSelected(null);
                            }
                        }
                )
        );
        this.removeButton.active = this.queueListWidget.getSelected() != null;
    }

    @Override
    public void addButtons() {}

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        if (this.queueListWidget != null) {
            this.queueListWidget.render(matrices, mouseX, mouseY, delta);
            if (this.queueListWidget.children().isEmpty()) {
                this.drawCenteredString(matrices, this.textRenderer, "No seeds in queue. :(", this.width / 2, this.height / 2 - this.textRenderer.fontHeight, 0xFFFFFF);
            }
        }

        this.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 13, 16777215);

        if (this.playButton != null) { this.playButton.active = this.queueListWidget.getSelected() != null; }
        if (this.removeButton != null) { this.removeButton.active = this.queueListWidget.getSelected() != null; }

        for (AbstractButtonWidget button : this.buttons) {
            button.render(matrices, mouseX, mouseY, delta);
        }
    }

    @Override
    public void tick() {
        if (this.queueListWidget != null) {
            if (PracticeSeedMod.QUEUE.size() != this.queueListWidget.children().size()) {
                this.queueListWidget = new QueueListWidget(this.client);
            }
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        if (this.parent != null) { this.client.openScreen(this.parent); }
    }

    private class QueueListWidget extends EntryListWidget<QueueListWidget.QueueEntry> {
        public QueueListWidget(MinecraftClient client) {
            super(client, QueueConfigTab.this.width, QueueConfigTab.this.height, 32, QueueConfigTab.this.height - 65 + 4, 18);
            int index = 0;
            for (Seed seed : PracticeSeedMod.QUEUE) {
                this.addEntry(new QueueEntry(index, seed));
                index++;
            }
        }

        @Override
        protected int getScrollbarPositionX() {
            return super.getScrollbarPositionX() + 20;
        }

        @Override
        public int getRowWidth() {
            return super.getRowWidth() + 50;
        }

        @Override
        protected void renderBackground(MatrixStack matrices) {
            QueueConfigTab.this.renderBackground(matrices);
        }

        @Override
        protected boolean isFocused() {
            return QueueConfigTab.this.getFocused() == this;
        }

        public class QueueEntry extends EntryListWidget.Entry<QueueEntry> {
            private final int index;
            private final Seed seed;

            public QueueEntry(int index, Seed seed) {
                this.index = index;
                this.seed = seed;
            }

            @Override
            public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
                QueueConfigTab.this.textRenderer.drawWithShadow(matrices, String.valueOf(this.seed.seed()), QueueConfigTab.this.width / 2.0F - QueueConfigTab.this.textRenderer.getWidth(String.valueOf(this.seed.seed())) / 2.0F, y + 1, 0xFFFFFF, false);
                if (hovered) {
                    QueueConfigTab.this.renderTooltip(matrices, QueueConfigTab.this.textRenderer.wrapLines(new LiteralText(this.seed.notes()), 250), mouseX, mouseY);
                }
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                QueueConfigTab.QueueListWidget.this.setSelected(this);
                return super.mouseClicked(mouseX, mouseY, button);
            }
        }
    }
}
