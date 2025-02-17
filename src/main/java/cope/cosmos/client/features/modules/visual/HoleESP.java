package cope.cosmos.client.features.modules.visual;

import cope.cosmos.client.features.modules.Category;
import cope.cosmos.client.features.modules.Module;
import cope.cosmos.client.features.setting.Setting;
import cope.cosmos.client.manager.managers.HoleManager.Hole;
import cope.cosmos.util.client.ColorUtil;
import cope.cosmos.util.render.RenderBuilder;
import cope.cosmos.util.render.RenderBuilder.Box;
import cope.cosmos.util.render.RenderUtil;

import java.awt.*;

public class HoleESP extends Module {
    public static HoleESP INSTANCE;

    public HoleESP() {
        super("HoleESP", Category.VISUAL, "Highlights nearby safe holes");
        INSTANCE = this;
    }

    public static Setting<Double> range = new Setting<>("Range", 0.0, 5.0, 20.0, 0).setDescription("Range to scan for holes");

    public static Setting<Box> main = new Setting<>("Main", Box.FILL).setDescription("Visual style for the main render");
    public static Setting<Double> mainHeight = new Setting<>("Height", -1.0, 0.1, 3.0, 1).setParent(main).setDescription("Height of the main render");
    public static Setting<Double> mainWidth = new Setting<>("Width", 0.0, 1.5, 3.0, 1).setParent(main).setDescription("Line width of the main render").setVisible(() -> main.getValue().equals(Box.BOTH) || main.getValue().equals(Box.CLAW) || main.getValue().equals(Box.OUTLINE));

    public static Setting<Box> outline = new Setting<>("Outline", Box.OUTLINE).setDescription("Visual style for the outline render");
    public static Setting<Double> outlineHeight = new Setting<>("Height", -1.0, 0.1, 3.0, 1).setParent(outline).setDescription("Height of the outline render");
    public static Setting<Double> outlineWidth = new Setting<>("Width", 0.0, 1.5, 3.0, 1).setParent(outline).setDescription("Line width of the outline render").setVisible(() -> outline.getValue().equals(Box.BOTH) || outline.getValue().equals(Box.CLAW) || outline.getValue().equals(Box.OUTLINE));

    public static Setting<Boolean> depth = new Setting<>("Depth", true).setDescription("Enables vanilla depth");
    public static Setting<Boolean> doubles = new Setting<>("Doubles", true).setDescription("Considers double holes as safe holes");
    public static Setting<Boolean> voids = new Setting<>("Void", false).setDescription("Highlights void and roof holes");

    public static Setting<Boolean> colors = new Setting<>("Colors", true).setDescription("Colors for the rendering");
    public static Setting<Color> obsidianColor = new Setting<>("Obsidian", ColorUtil.getPrimaryAlphaColor(45)).setParent(colors).setDescription("Color of the obsidian holes");
    public static Setting<Color> mixedColor = new Setting<>("Mixed", ColorUtil.getPrimaryAlphaColor(45)).setParent(colors).setDescription("Color of the mixed holes");
    public static Setting<Color> bedrockColor = new Setting<>("Bedrock", ColorUtil.getPrimaryAlphaColor(45)).setParent(colors).setDescription("Color of the bedrock holes");
    public static Setting<Color> voidColor = new Setting<>("Void", new Color(255, 0, 0, 45)).setParent(colors).setDescription("Color of the void holes").setVisible(() -> voids.getValue());

    @Override
    public void onRender3D() {
        getCosmos().getHoleManager().getHoles().forEach(hole -> {
            if (Math.sqrt(mc.player.getDistanceSq(hole.getHole())) < range.getValue()) {
                drawHole(hole);
            }
        });
    }

    public void drawHole(Hole hole) {
        switch (hole.getType()) {
            case VOID:
                RenderUtil.drawBox(new RenderBuilder().position(hole.getHole()).color(voidColor.getValue()).box(Box.FILL).setup().line(1.5F).depth(true).blend().texture());
                break;
            case OBSIDIAN:
                RenderUtil.drawBox(new RenderBuilder().position(hole.getHole()).height(mainHeight.getValue() - 1).length(0).width(0).color(obsidianColor.getValue()).box(main.getValue()).setup().line(mainWidth.getValue().floatValue()).cull(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).shade(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).alpha(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).depth(depth.getValue()).blend().texture());
                RenderUtil.drawBox(new RenderBuilder().position(hole.getHole()).height(outlineHeight.getValue() - 1).length(0).width(0).color(obsidianColor.getValue()).box(outline.getValue()).setup().line(outlineWidth.getValue().floatValue()).cull(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).shade(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).alpha(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).depth(depth.getValue()).blend().texture());
                break;
            case MIXED:
                RenderUtil.drawBox(new RenderBuilder().position(hole.getHole()).height(mainHeight.getValue() - 1).length(0).width(0).color(mixedColor.getValue()).box(main.getValue()).setup().line(mainWidth.getValue().floatValue()).cull(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).shade(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).alpha(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).depth(depth.getValue()).blend().texture());
                RenderUtil.drawBox(new RenderBuilder().position(hole.getHole()).height(outlineHeight.getValue() - 1).length(0).width(0).color(mixedColor.getValue()).box(outline.getValue()).setup().line(outlineWidth.getValue().floatValue()).cull(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).shade(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).alpha(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).depth(depth.getValue()).blend().texture());
                break;
            case BEDROCK:
                RenderUtil.drawBox(new RenderBuilder().position(hole.getHole()).height(mainHeight.getValue() - 1).length(0).width(0).color(bedrockColor.getValue()).box(main.getValue()).setup().line(mainWidth.getValue().floatValue()).cull(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).shade(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).alpha(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).depth(depth.getValue()).blend().texture());
                RenderUtil.drawBox(new RenderBuilder().position(hole.getHole()).height(outlineHeight.getValue() - 1).length(0).width(0).color(bedrockColor.getValue()).box(outline.getValue()).setup().line(outlineWidth.getValue().floatValue()).cull(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).shade(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).alpha(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).depth(depth.getValue()).blend().texture());
                break;
            case DOUBLEOBSIDIANX:
                if (doubles.getValue()) {
                    RenderUtil.drawBox(new RenderBuilder().position(hole.getHole()).height(mainHeight.getValue() - 1).length(1).width(0).color(obsidianColor.getValue()).box(main.getValue()).setup().line(mainWidth.getValue().floatValue()).cull(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).shade(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).alpha(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).depth(depth.getValue()).blend().texture());
                    RenderUtil.drawBox(new RenderBuilder().position(hole.getHole()).height(outlineHeight.getValue() - 1).length(1).width(0).color(obsidianColor.getValue()).box(outline.getValue()).setup().line(outlineWidth.getValue().floatValue()).cull(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).shade(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).alpha(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).depth(depth.getValue()).blend().texture());
                }

                break;
            case DOUBLEMIXEDX:
                if (doubles.getValue()) {
                    RenderUtil.drawBox(new RenderBuilder().position(hole.getHole()).height(mainHeight.getValue() - 1).length(1).width(0).color(mixedColor.getValue()).box(main.getValue()).setup().line(mainWidth.getValue().floatValue()).cull(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).shade(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).alpha(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).depth(depth.getValue()).blend().texture());
                    RenderUtil.drawBox(new RenderBuilder().position(hole.getHole()).height(outlineHeight.getValue() - 1).length(1).width(0).color(mixedColor.getValue()).box(outline.getValue()).setup().line(outlineWidth.getValue().floatValue()).cull(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).shade(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).alpha(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).depth(depth.getValue()).blend().texture());
                }

                break;
            case DOUBLEBEDROCKX:
                if (doubles.getValue()) {
                    RenderUtil.drawBox(new RenderBuilder().position(hole.getHole()).height(mainHeight.getValue() - 1).length(1).width(0).color(bedrockColor.getValue()).box(main.getValue()).setup().line(mainWidth.getValue().floatValue()).cull(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).shade(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).alpha(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).depth(depth.getValue()).blend().texture());
                    RenderUtil.drawBox(new RenderBuilder().position(hole.getHole()).height(outlineHeight.getValue() - 1).length(1).width(0).color(bedrockColor.getValue()).box(outline.getValue()).setup().line(outlineWidth.getValue().floatValue()).cull(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).shade(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).alpha(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).depth(depth.getValue()).blend().texture());
                }

                break;
            case DOUBLEOBSIDIANZ:
                if (doubles.getValue()) {
                    RenderUtil.drawBox(new RenderBuilder().position(hole.getHole()).height(mainHeight.getValue() - 1).length(0).width(1).color(obsidianColor.getValue()).box(main.getValue()).setup().line(mainWidth.getValue().floatValue()).cull(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).shade(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).alpha(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).depth(depth.getValue()).blend().texture());
                    RenderUtil.drawBox(new RenderBuilder().position(hole.getHole()).height(outlineHeight.getValue() - 1).length(0).width(1).color(obsidianColor.getValue()).box(outline.getValue()).setup().line(outlineWidth.getValue().floatValue()).cull(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).shade(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).alpha(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).depth(depth.getValue()).blend().texture());
                }

                break;
            case DOUBLEMIXEDZ:
                if (doubles.getValue()) {
                    RenderUtil.drawBox(new RenderBuilder().position(hole.getHole()).height(mainHeight.getValue() - 1).length(0).width(1).color(mixedColor.getValue()).box(main.getValue()).setup().line(mainWidth.getValue().floatValue()).cull(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).shade(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).alpha(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).depth(depth.getValue()).blend().texture());
                    RenderUtil.drawBox(new RenderBuilder().position(hole.getHole()).height(outlineHeight.getValue() - 1).length(0).width(1).color(mixedColor.getValue()).box(outline.getValue()).setup().line(outlineWidth.getValue().floatValue()).cull(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).shade(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).alpha(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).depth(depth.getValue()).blend().texture());
                }

                break;
            case DOUBLEBEDROCKZ:
                if (doubles.getValue()) {
                    RenderUtil.drawBox(new RenderBuilder().position(hole.getHole()).height(mainHeight.getValue() - 1).length(0).width(1).color(bedrockColor.getValue()).box(main.getValue()).setup().line(mainWidth.getValue().floatValue()).cull(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).shade(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).alpha(main.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).depth(depth.getValue()).blend().texture());
                    RenderUtil.drawBox(new RenderBuilder().position(hole.getHole()).height(outlineHeight.getValue() - 1).length(0).width(1).color(bedrockColor.getValue()).box(outline.getValue()).setup().line(outlineWidth.getValue().floatValue()).cull(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).shade(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).alpha(outline.getValue().equals(Box.GLOW) || main.getValue().equals(Box.REVERSE)).depth(depth.getValue()).blend().texture());
                }

                break;
        }
    }
}