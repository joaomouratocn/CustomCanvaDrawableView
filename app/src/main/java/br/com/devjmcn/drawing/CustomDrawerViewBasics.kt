package br.com.devjmcn.drawing

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.content.res.AppCompatResources

class CustomDrawerViewBasics(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var xPosition = 0f
    private var yPosition = 0f

    private val path by lazy {
        Path()
    }

    private val drawable by lazy {
        AppCompatResources.getDrawable(context, R.drawable.rocket_svgrepo_com)
    }

    private val paintTrace by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.BLACK
            strokeWidth = 5f
            style = Paint.Style.STROKE
        }
    }

    private val painText by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.GRAY
            textSize = 40f
            /*
                FILL -> cria um traço de preenchimento
                STROKE -> preenche toda a forma
             */
            style = Paint.Style.FILL_AND_STROKE
        }
    }

    init {
        startYAnimation()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        xPosition = (width / 2f) - drawable?.intrinsicWidth!!
    }

    private fun startYAnimation() {
        val startY = 1300f - drawable?.intrinsicHeight!! // Define ponto de inicio da animação
        val endY = 100f //define o ponto final da animação

        val animator = ValueAnimator.ofFloat(startY, endY).apply{ // criando a animação
            duration = 10000 //tempo em milissegundos
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE

            addUpdateListener { animation ->
                yPosition = animation.animatedValue as Float
                invalidate()
            }
        }
        animator.start() // executando a animação
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //Criando o desenho na tela
        drawImage(canvas)
        drawText(canvas)
        drawRectangle(canvas)
        drawCircle(canvas)
        drawLine(canvas)
        drawArc(canvas)
    }

    private fun drawImage(canvas: Canvas) {
        /*
            neste metodo passa-se a coordenada do ponto esquerdo mais alto e a coordenada do
            ponto direito mais baixo. Assim o drawable é desenhado no retangulo criado
         */
        drawable?.let {
            it.setBounds(
                xPosition.toInt(),
                yPosition.toInt(),
                (xPosition + it.intrinsicWidth).toInt(),
                (yPosition + it.intrinsicHeight).toInt()
            )
            it.draw(canvas)
        }
    }

    private fun drawRectangle(canvas: Canvas) {
        /*
            desenha um retangulo, passando o ponto esquerdo mais alto,
            direito mais baixo e paint que irar desenhar o traço
         */
        canvas.drawRect(
            10f,
            80f,
            width.toFloat() - 10f,
            height.toFloat(),
            paintTrace
        )
    }

    private fun drawLine(canvas: Canvas) {
        /*
            passa as coordenadas dos dois pontos XY / XY e
            traça um linha apartir da coordenadas
         */
        canvas.drawLine(
            width.toFloat() / 2,
            40f,
            width.toFloat() - 10f,
            40f,
            paintTrace
        )
    }

    private fun drawCircle(canvas: Canvas) {
        /*
            desenha um circulo, passa-se a coordenada x e y do ponto central,
            o radio do circulo e paint que ira fazer o traço
         */
        canvas.drawCircle(
            width.toFloat() - 200,
            300f,
            width.toFloat() / 10,
            paintTrace
        )
    }

    private fun drawText(canvas: Canvas) {
        /*
            desenha um texto, passa-se o text as coordenadas x e y de onde o texto de iniciar,
            paint para realizar o desenho
        */
        canvas.drawText(
            context.getString(R.string.str_drawerview_android),
            30f,
            50f,
            painText
        )
    }

    private fun drawArc(canvas: Canvas) {
        val radius = 100
        val cx = 130f
        val cy = height.toFloat() - 10

        /*
            desenha um arco, passando o ponto esquerdo mais alto e o direito mais baixo,
            depois passa o angulo deve iniciar o arco e quantos graus deve percorrer
         */
        path.addArc(
            cx - radius,
            cy - radius,
            cx + radius,
            cy + radius,
            0f,
            -180f
        )
        path.close()

        canvas.drawPath(path, paintTrace)
    }
//Metodo para definir programaticamente as dimensões da view
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desireWith = 600 // largura da View
        val desiredHeight = 1300 // altura desejada

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val with = when(widthMode){
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(widthMeasureSpec)
            MeasureSpec.AT_MOST -> desiredHeight.coerceAtMost(MeasureSpec.getSize(widthMeasureSpec))
            else -> desireWith
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(heightMeasureSpec)
            MeasureSpec.AT_MOST -> desiredHeight.coerceAtMost(MeasureSpec.getSize(heightMeasureSpec))
            else -> desiredHeight
        }

        setMeasuredDimension(with, height)
    }
}