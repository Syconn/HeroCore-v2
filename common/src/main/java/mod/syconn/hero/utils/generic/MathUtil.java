package mod.syconn.hero.utils.generic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.syconn.hero.utils.Constants;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class MathUtil {

    public static final Vec3 V3D_POS_X = new Vec3(1, 0, 0);
    public static final Vec3 V3D_NEG_X = new Vec3(-1, 0, 0);
    public static final Vec3 V3D_POS_Y = new Vec3(0, 1, 0);
    public static final Vec3 V3D_NEG_Y = new Vec3(0, -1, 0);
    public static final Vec3 V3D_POS_Z = new Vec3(0, 0, 1);
    public static final Vec3 V3D_NEG_Z = new Vec3(0, 0, -1);

    public static final Vector3f V3F_POS_X = new Vector3f(1, 0, 0);
    public static final Vector3f V3F_NEG_X = new Vector3f(-1, 0, 0);
    public static final Vector3f V3F_POS_Y = new Vector3f(0, 1, 0);
    public static final Vector3f V3F_NEG_Y = new Vector3f(0, -1, 0);
    public static final Vector3f V3F_POS_Z = new Vector3f(0, 0, 1);
    public static final Vector3f V3F_NEG_Z = new Vector3f(0, 0, -1);

    public static float remap(float x, float iMin, float iMax, float oMin, float oMax) {
        return (x - iMin) / (iMax - iMin) * (oMax - oMin) + oMin;
    }

    public static Vec3 anglesToLook(float pitch, float yaw) {
        var x = -Mth.sin(yaw * Mth.RAD_TO_DEG) * Mth.cos(pitch * Mth.RAD_TO_DEG);
        var y = -Mth.sin(pitch * Mth.RAD_TO_DEG);
        var z = Mth.cos(yaw * Mth.RAD_TO_DEG) * Mth.cos(pitch * Mth.RAD_TO_DEG);

        return new Vec3(x, y, z).normalize();
    }

    public static Vec3 lookToAngles(Vec3 forward) {
        forward = forward.normalize();

        var yaw = -(float)Math.atan2(forward.x, forward.z);
        var pitch = -(float)Math.asin(forward.y);

        return new Vec3(pitch * Mth.DEG_TO_RAD, yaw * Mth.DEG_TO_RAD, 0);
    }

    public static float toRadians(float degrees) {
        return degrees * Mth.RAD_TO_DEG;
    }

    public static void translateRotation(PoseStack poseStack, Direction direction, float x, float y, float z) {
        switch (direction) {
            case NORTH -> poseStack.translate(-x, y, -z);
            case SOUTH -> poseStack.translate(x, y, z);
            case EAST -> poseStack.translate(z, y, -x);
            case WEST -> poseStack.translate(-z, y, x);
        }
    }

    public static void translateRotation(PoseStack poseStack, Direction direction, Vec3 vec3) {
        translateRotation(poseStack, direction, (float) vec3.x, (float) vec3.y, (float) vec3.z);
    }

    public static Quaternionf getEastRotation(Direction direction) {
        return switch (direction) {
            case DOWN -> new Quaternionf().rotationXYZ(0, 0, (float)(Math.PI / -2));
            case UP -> new Quaternionf().rotationXYZ(0, 0, (float)(Math.PI / 2));
            case NORTH -> new Quaternionf().rotationXYZ(0, (float)(Math.PI / 2), 0);
            case SOUTH -> new Quaternionf().rotationXYZ(0, (float)(Math.PI / -2), 0);
            case WEST -> new Quaternionf().rotationXYZ(0, (float)Math.PI, 0);
            case EAST -> new Quaternionf().rotationXYZ(0, 0, 0);
        };
    }

    public static Quaternionf getNorthRotation(Direction direction) {
        return switch (direction) {
            case NORTH -> Axis.YP.rotationDegrees(0);
            case EAST -> Axis.YP.rotationDegrees(270);
            case SOUTH -> Axis.YP.rotationDegrees(180);
            case WEST -> Axis.YP.rotationDegrees(90);
            case UP -> Axis.XP.rotationDegrees(90);
            case DOWN -> Axis.XP.rotationDegrees(270);
        };
    }

    public static Vec3i reflect(Vec3i incident, Vec3i normal) {
        var reflection = normal.multiply(2 * normal.distManhattan(incident)).subtract(incident);
        return reflection.multiply(-1);
    }

    public static int wrap(int value, int max) {
        var range = max + 1;
        value = ((value % range) + range) % range;
        return value;
    }

    @SafeVarargs
    public static <R> R randomChoice(R... choices) {
        return choices[Constants.RANDOM.nextIntBetweenInclusive(0, choices.length - 1)];
    }
}
