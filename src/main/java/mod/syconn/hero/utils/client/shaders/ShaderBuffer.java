package mod.syconn.hero.utils.client.shaders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.syconn.hero.utils.generic.ModelUtil;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector4f;

public enum ShaderBuffer {
    RENDER;

    private static final float[][] cubeVertices = new float[8][3];
    private static final float[][] skewCubeVertices;

    private static final float[][] _normalsBox = {
            { -1.0f, 0.0f, 0.0f },
            { 0.0f, 1.0f, 0.0f },
            { 1.0f, 0.0f, 0.0f },
            { 0.0f, -1.0f, 0.0f },
            { 0.0f, 0.0f, 1.0f },
            { 0.0f, 0.0f, -1.0f }
    };

    private static final int[][] _facesBox = {{ 0, 1, 2, 3 }, { 3, 2, 6, 7 }, { 7, 6, 5, 4 }, { 4, 5, 1, 0 }, { 5, 6, 2, 1 }, { 7, 4, 0, 3 }};
    private static final int[] _counterClockwiseVertIndices = new int[] { 0, 1, 2, 3 };
    private static final int[] _clockwiseVertIndices = new int[] { 3, 2, 1, 0 };

    static {
        // cube
        cubeVertices[0][0] = cubeVertices[1][0] = cubeVertices[2][0] = cubeVertices[3][0] = -0.5f;
        cubeVertices[4][0] = cubeVertices[5][0] = cubeVertices[6][0] = cubeVertices[7][0] = 0.5f;
        cubeVertices[0][1] = cubeVertices[1][1] = cubeVertices[4][1] = cubeVertices[5][1] = -0.5f;
        cubeVertices[2][1] = cubeVertices[3][1] = cubeVertices[6][1] = cubeVertices[7][1] = 0.5f;
        cubeVertices[0][2] = cubeVertices[3][2] = cubeVertices[4][2] = cubeVertices[7][2] = -0.5f;
        cubeVertices[1][2] = cubeVertices[2][2] = cubeVertices[5][2] = cubeVertices[6][2] = 0.5f;
        skewCubeVertices = deepCopyIntMatrix();
    }

    private static float[][] deepCopyIntMatrix() {
        var result = new float[ShaderBuffer.cubeVertices.length][];
        for (var r = 0; r < ShaderBuffer.cubeVertices.length; r++) result[r] = ShaderBuffer.cubeVertices[r].clone();
        return result;
    }

    private VertexConsumer vertexConsumer;
    private PoseStack.Pose pose;
    private float r;
    private float g;
    private float b;
    private float a;
    private int overlay;
    private int light;

    private boolean _renderClockwise = false;
    private int _skipFace = -1;
    private float scaleX = 1;
    private float scaleY = 1;
    private float scaleZ = 1;

    public void init(VertexConsumer vertexConsumer, PoseStack.Pose pose, float r, float g, float b, float a, int overlay, int light) {
        this.vertexConsumer = vertexConsumer;
        this.pose = pose;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.overlay = overlay;
        this.light = light;
    }

    public void setPose(PoseStack.Pose pose)
    {
        this.pose = pose;
    }

    public void setColor(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public void setColor(int argb) {
        this.a = ((argb >> 24) & 0xFF) / 255f;
        this.r = ((argb >> 16) & 0xFF) / 255f;
        this.g = ((argb >> 8) & 0xFF) / 255f;
        this.b = (argb & 0xFF) / 255f;
    }

    public void setColor(int baseRgb, int alpha) {
        this.a = (alpha & 0xFF) / 255f;
        this.r = ((baseRgb >> 16) & 0xFF) / 255f;
        this.g = ((baseRgb >> 8) & 0xFF) / 255f;
        this.b = (baseRgb & 0xFF) / 255f;
    }

    public void setOverlay(int overlay)
    {
        this.overlay = overlay;
    }

    public void setLight(int light)
    {
        this.light = light;
    }

    public void vertex(Vector3f pos, Vector3f normal, float u, float v) {
        var pos4 = new Vector4f(pos.x, pos.y, pos.z, 1);
        normal = new Vector3f(normal);

        pos4.mul(pose.pose());
        normal.mul(pose.normal());

        vertexConsumer.vertex(pos4.x, pos4.y, pos4.z, r, g, b, a, u, v, overlay, light, normal.x, normal.y, normal.z);
    }

    public void vertex(float x, float y, float z, float nx, float ny, float nz, float u, float v) {
        vertex(new Vector3f(x, y, z), new Vector3f(nx, ny, nz), u, v);
    }

    public void line(float x1, float y1, float z1, float x2, float y2, float z2) {
        var start = new Vector3f(x1, y1, z1);
        var end = new Vector3f(x2, y2, z2);
        var normal = new Vector3f(x2, y2, z2);
        normal.sub(start);
        normal.normalize();

        vertex(start, normal, 0, 0);
        vertex(end, normal, 0, 0);
    }

    public void line(Vec3 start, Vec3 end) {
        var normal = new Vec3(end.x, end.y, end.z).subtract(start).normalize();

        vertex((float)start.x, (float)start.y, (float)start.z, (float)normal.x, (float)normal.y, (float)normal.z, 0, 0);
        vertex((float)end.x, (float)end.y, (float)end.z, (float)normal.x, (float)normal.y, (float)normal.z, 0, 0);
    }

    public void axes(float scale) {
        setColor(1, 0, 0, 1);
        line(Vec3.ZERO, ModelUtil.X.multiply(scale, scale, scale));
        setColor(0, 1, 0, 1);
        line(Vec3.ZERO, ModelUtil.Y.multiply(scale, scale, scale));
        setColor(0, 0, 1, 1);
        line(Vec3.ZERO, ModelUtil.Z.multiply(scale, scale, scale));
    }

    public void box() {
        box(cubeVertices);
    }

    public void drawSolidBoxSkew(float thickness, float topX, float topY, float topZ, float bottomX, float bottomY, float bottomZ) {
        box(thickness, thickness, topX, topY, topZ, bottomX, bottomY, bottomZ);
    }

    public void drawSolidBoxSkewTaper(float thicknessTop, float thicknessBottom, float topX, float topY, float topZ, float bottomX, float bottomY, float bottomZ) {
        box(thicknessTop, thicknessBottom, topX, topY, topZ, bottomX, bottomY, bottomZ);
    }

    public void skipFace(int skipFace) {
        _skipFace = skipFace;
    }

    private void box(float thicknessTop, float thicknessBottom, float topX, float topY, float topZ, float bottomX, float bottomY, float bottomZ) {
        skewCubeVertices[0][0] = skewCubeVertices[1][0] = -thicknessBottom + bottomX;
        skewCubeVertices[2][0] = skewCubeVertices[3][0] = -thicknessTop + topX;
        skewCubeVertices[4][0] = skewCubeVertices[5][0] = thicknessBottom + bottomX;
        skewCubeVertices[6][0] = skewCubeVertices[7][0] = thicknessTop + topX;

        skewCubeVertices[0][1] = skewCubeVertices[1][1] = bottomY;
        skewCubeVertices[4][1] = skewCubeVertices[5][1] = bottomY;
        skewCubeVertices[2][1] = skewCubeVertices[3][1] = topY;
        skewCubeVertices[6][1] = skewCubeVertices[7][1] = topY;

        skewCubeVertices[0][2] = -thicknessBottom + bottomZ;
        skewCubeVertices[7][2] = -thicknessTop + topZ;
        skewCubeVertices[4][2] = -thicknessBottom + bottomZ;
        skewCubeVertices[3][2] = -thicknessTop + topZ;
        skewCubeVertices[1][2] = thicknessBottom + bottomZ;
        skewCubeVertices[6][2] = thicknessTop + topZ;
        skewCubeVertices[5][2] = thicknessBottom + bottomZ;
        skewCubeVertices[2][2] = thicknessTop + topZ;

        box(skewCubeVertices);
    }

    private void box(float[][] verts) {
        var indices = _renderClockwise ? _clockwiseVertIndices : _counterClockwiseVertIndices;

        for (var i = 0; i < 6; i++) {
            if (i == _skipFace) continue;
            vertex(verts[_facesBox[i][indices[0]]][0] * scaleX, verts[_facesBox[i][indices[0]]][1] * scaleY, verts[_facesBox[i][indices[0]]][2] * scaleZ, _normalsBox[i][0], _normalsBox[i][1], _normalsBox[i][2], 0, 0);
            vertex(verts[_facesBox[i][indices[1]]][0] * scaleX, verts[_facesBox[i][indices[1]]][1] * scaleY, verts[_facesBox[i][indices[1]]][2] * scaleZ, _normalsBox[i][0], _normalsBox[i][1], _normalsBox[i][2], 1, 0);
            vertex(verts[_facesBox[i][indices[2]]][0] * scaleX, verts[_facesBox[i][indices[2]]][1] * scaleY, verts[_facesBox[i][indices[2]]][2] * scaleZ, _normalsBox[i][0], _normalsBox[i][1], _normalsBox[i][2], 1, 1);
            vertex(verts[_facesBox[i][indices[3]]][0] * scaleX, verts[_facesBox[i][indices[3]]][1] * scaleY, verts[_facesBox[i][indices[3]]][2] * scaleZ, _normalsBox[i][0], _normalsBox[i][1], _normalsBox[i][2], 0, 1);
        }
    }

    public void setScale(float x, float y, float z) {
        scaleX = x;
        scaleY = y;
        scaleZ = z;
    }

    public void resetScale() {
        scaleX = 1;
        scaleY = 1;
        scaleZ = 1;
    }

    public void invertCull(boolean invert) {
        _renderClockwise = invert;
    }
}
