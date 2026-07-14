package mod.syconn.hero.utils.generic;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ModelUtil {

    public static final Vec3 X = new Vec3(1, 0, 0);
    public static final Vec3 Y = new Vec3(0, 1, 0);
    public static final Vec3 Z = new Vec3(0, 0, 1);

    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};

        var times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[1] = Shapes.empty();
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
        }

        return buffer[0];
    }

    public static void translateRotation(PoseStack poseStack, Direction oldDir, Direction newDir, float x, float y, float z) {
        double dx = x - 0.5;
        double dz = z - 0.5;
        int rotations = Math.floorMod(newDir.get2DDataValue() - oldDir.get2DDataValue(), 4);
        for (int i = 0; i < rotations; i++) {
            double temp = dx;
            dx = -dz;
            dz = temp;
        }
        poseStack.translate(0.5 + dx, y, 0.5 + dz);
    }

    public static float remap(float x, float iMin, float iMax, float oMin, float oMax) {
        return (x - iMin) / (iMax - iMin) * (oMax - oMin) + oMin;
    }
}
