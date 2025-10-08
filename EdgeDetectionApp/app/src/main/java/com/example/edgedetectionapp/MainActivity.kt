package com.example.edgedetectionapp

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyRenderer : GLSurfaceView.Renderer {

    private val vertexShaderCode =
        "attribute vec4 vPosition;" +
                "attribute vec2 vTexCoord;" +
                "varying vec2 texCoord;" +
                "void main() {" +
                "  gl_Position = vPosition;" +
                "  texCoord = vTexCoord;" +
                "}"

    private val fragmentShaderCode =
        "precision mediump float;" +
                "uniform sampler2D sTexture;" +
                "varying vec2 texCoord;" +
                "void main() {" +
                "  gl_FragColor = texture2D(sTexture, texCoord);" +
                "}"

    private var program = 0
    private var textureId = 0
    private var vertexBuffer: FloatBuffer? = null
    private var texCoordBuffer: FloatBuffer? = null
    private var textureData: ByteBuffer? = null
    private var textureWidth = 0
    private var textureHeight = 0
    private var needsUpdate = false

    private val vertices = floatArrayOf(
        -1.0f, -1.0f,  // Bottom Left
        1.0f, -1.0f,  // Bottom Right
        -1.0f, 1.0f,  // Top Left
        1.0f, 1.0f   // Top Right
    )

    private val texCoords = floatArrayOf(
        0.0f, 1.0f,  // Top Left
        1.0f, 1.0f,  // Top Right
        0.0f, 0.0f,  // Bottom Left
        1.0f, 0.0f   // Bottom Right
    )

    init {
        // Initialize vertex buffer
        var bb = ByteBuffer.allocateDirect(vertices.size * 4)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer?.put(vertices)
        vertexBuffer?.position(0)

        // Initialize texture coordinate buffer
        bb = ByteBuffer.allocateDirect(texCoords.size * 4)
        bb.order(ByteOrder.nativeOrder())
        texCoordBuffer = bb.asFloatBuffer()
        texCoordBuffer?.put(texCoords)
        texCoordBuffer?.position(0)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        program = GLES20.glCreateProgram()
        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)
        GLES20.glLinkProgram(program)

        // Create texture
        val textures = IntArray(1)
        GLES20.glGenTextures(1, textures, 0)
        textureId = textures[0]
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_NEAREST
        )
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glUseProgram(program)

        synchronized(this) {
            if (needsUpdate) {
                // Upload new texture data
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
                GLES20.glTexImage2D(
                    GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, textureWidth, textureHeight, 0,
                    GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, textureData
                )
                needsUpdate = false
            }
        }

        // Get handle to vertex shader's vPosition member
        val positionHandle = GLES20.glGetAttribLocation(program, "vPosition")
        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer)

        // Get handle to texture coordinates
        val texCoordHandle = GLES20.glGetAttribLocation(program, "vTexCoord")
        GLES20.glEnableVertexAttribArray(texCoordHandle)
        GLES20.glVertexAttribPointer(texCoordHandle, 2, GLES20.GL_FLOAT, false, 0, texCoordBuffer)

        // Get handle to fragment shader's sTexture member
        val textureHandle = GLES20.glGetUniformLocation(program, "sTexture")
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glUniform1i(textureHandle, 0)

        // Draw the square
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        // Disable vertex arrays
        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(texCoordHandle)
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
        return shader
    }

    fun updateTexture(bytes: ByteArray?, width: Int, height: Int) {
        if (bytes == null) return
        synchronized(this) {
            textureData = ByteBuffer.wrap(bytes)
            textureWidth = width
            textureHeight = height
            needsUpdate = true
        }
    }
}