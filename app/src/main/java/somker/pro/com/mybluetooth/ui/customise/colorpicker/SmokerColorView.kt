package somker.pro.com.mybluetooth.ui.customise.colorpicker

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.LogUtils
import somker.pro.com.mybluetooth.R
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt



/**
 * Created by Smoker on 2020/3/25.
 * 说明：自定义颜色选择
 */
class SmokerColorView :RelativeLayout ,View.OnTouchListener{

    private lateinit var colorImg :ImageView

    private lateinit var indicatorImg :ImageView

    private lateinit var colorBitmap :Bitmap
    /*颜色改变的回调*/
    private var colorCallBack :OnColorChangerCallBack? = null
    /*颜色图片 的宽高*/
    private var cImgW : Int = 0
    private var cImgH : Int = 0
    /*颜色图片 的 半径*/
    private var radius :Int = 0

    /*指示器的 的宽高*/
    private var idcW :Int = 0
    private var idcH :Int = 0

    /*bitmap 和 实际展示控件的尺寸比例*/
    private var bitmapScaleX = 0.0
    private var bitmapScaleY = 0.0



    private val sScaleTypeArray = arrayOf(
        ImageView.ScaleType.MATRIX,
        ImageView.ScaleType.FIT_XY,
        ImageView.ScaleType.FIT_START,
        ImageView.ScaleType.FIT_CENTER,
        ImageView.ScaleType.FIT_END,
        ImageView.ScaleType.CENTER,
        ImageView.ScaleType.CENTER_CROP,
        ImageView.ScaleType.CENTER_INSIDE
    )

    /*颜色图片 的中心点*/
    private lateinit var cPoint: Point



    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs ,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr ,0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int , defStyleRes:Int) : super(context, attrs, defStyleAttr ,defStyleRes){
        val typeArray = context.obtainStyledAttributes(attrs , R.styleable.SmokerColorView ,defStyleAttr ,defStyleRes)
        createImageView(context ,typeArray)
        getAttrsParams(typeArray)
    }

    fun setColorChangerListener(listener:OnColorChangerCallBack){
        this.colorCallBack = listener
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createImageView(context: Context, typeArray:TypedArray){
        /*选择颜色图片View*/
        colorImg = ImageView(context)
        val index = typeArray.getInt(R.styleable.SmokerColorView_imgScaleType, -1)
        if (index >= 0) {
            colorImg.scaleType = sScaleTypeArray[index]
        }
        val colorParams =  LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT ,ViewGroup.LayoutParams.MATCH_PARENT)
        colorParams.addRule(CENTER_IN_PARENT , TRUE)
        this.addView(colorImg ,colorParams)


        /*指示器View*/
        indicatorImg = ImageView(context)
        //indicatorImg.setPadding(30 , 30 , 30 ,30)
        idcW = typeArray.getDimensionPixelSize(R.styleable.SmokerColorView_indicator_w ,140)
        idcH = typeArray.getDimensionPixelSize(R.styleable.SmokerColorView_indicator_h , 140)

        val idcParams =  LayoutParams(idcW ,idcH)
        idcParams.addRule(CENTER_IN_PARENT , TRUE)
        this.addView(indicatorImg ,idcParams)

        colorImg.setOnTouchListener(this)
    }


    private fun getAttrsParams(typeArray: TypedArray){

        val img = typeArray.getDrawable(R.styleable.SmokerColorView_imgSrc)
        img?.let {
            //colorImg.setImageDrawable(it)
            colorBitmap = ConvertUtils.drawable2Bitmap(it)
            colorImg.setImageBitmap(colorBitmap)
        }


        val idcImg = typeArray.getDrawable(R.styleable.SmokerColorView_indicatorSrc)
        idcImg?.let {
            indicatorImg.setImageDrawable(it)
        }
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        initColorViewWidthAndHeight()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        initViewCenterCoordinate()
        event?.let { ev ->
            val x = ev.rawX
            val y = ev.rawY
            LogUtils.e("move ==> x :$x  y:$y")
            computeDistance(x, y)
        }
        return true
    }

    /**
     * 初始化color ImageView 的宽高
     * 这个view 的宽高是和父类一样的
     */
    private fun initColorViewWidthAndHeight() {
        if (cImgW == 0 || cImgH == 0){
            cImgW = measuredWidth
            cImgH = measuredHeight
            radius = cImgW / 2
        }
        LogUtils.e("colorW : $cImgW   colorH : $cImgH")
    }


    /**
     * 初始化颜色view的中心点坐标
     */
    private fun initViewCenterCoordinate(){
        if (!this::cPoint.isInitialized){
            val cR = this.right
            val cB = this.bottom
            val cX = cR - cImgW / 2
            val cY = cB - cImgH / 2
            if (cX != 0 && cY != 0){
                cPoint = Point(cX ,cY)
                LogUtils.e("中心点坐标 => x:$cX ,y:$cY")
            }

        }
    }

    /**
     * 计算触摸点 到 中心点的距离
     * @param x 触摸的x坐标值
     * @param y 触摸的y坐标值
     */
    private fun computeDistance(x:Float ,y:Float){
        if (!this::cPoint.isInitialized){
            LogUtils.e("中心坐标点未实例化.......")
            return
        }

        /*(x1 - x2)^2 + (y1 - y2)^2  再开方*/
        val xV = abs((x - cPoint.x))
        val yV  = abs((y - cPoint.y))
        val distance = sqrt(xV.pow(2) + yV.pow(2))
        LogUtils.e("半径：$radius   距离：$distance")
        if (distance > radius){
            //距离大于半径，说明触摸位置不在颜色图片区域内
            //因此根据半径来算出点的最大有效坐标，赋予指示器
            LogUtils.e("===>>  超过有效区域")
            computeCoordinate(x, y ,distance)
        }else{
            //距离小于等于半径 ，说明触摸位置在颜色图片区域内
            LogUtils.e("===>>  有效区域")
            refreshIndicatorCoordinate(x ,y)
        }
    }

    /**
     *  刷新指示器的坐标
     *  @param x 目标x轴值
     *  @param y 目标y轴值
     */
    private fun refreshIndicatorCoordinate(x:Float , y:Float){
        val oX = indicatorImg.left + this.left
        val oY = indicatorImg.top + this.top
        /*计算偏移量 还要减去指示器的一半，这样点才在指示器中间*/
        val offsetX = x - oX - idcW / 2
        val offsetY = y - oY - idcH / 2
        indicatorImg.offsetLeftAndRight(offsetX.toInt())
        indicatorImg.offsetTopAndBottom(offsetY.toInt())

        /*计算图片对应的色值*/
        computeBitmapCoordinate()
    }

    /**
     * 计算新坐标 (超出有效区域，取与圆形的焦点的坐标，用三角函数比列计算)
     * @param x 触摸的x坐标值
     * @param y 触摸的y坐标值
     * @param distance 触摸点 到 中心点的距离
     */
    private fun computeCoordinate(x:Float ,y:Float ,distance :Float){
        val yV = cPoint.y - y
        val nY = cPoint.y - radius * yV / distance

        val xV = cPoint.x - x
        val nx = cPoint.x - radius * xV / distance
        refreshIndicatorCoordinate(nx ,nY)
    }


    /**
     * 计算指示器所在点对应图片bitmap的坐标值
     * 直接获取指示器的中心点坐标
     * 然后还要根据 bitmap 的大小 和 实际展示大小做比例转换
     *
     */
    private fun computeBitmapCoordinate(){
        val bxScale = indicatorImg.left + idcW / 2
        val byScale = indicatorImg.top  + idcH / 2
        initBitmapScale()
        var bx = (bxScale * bitmapScaleX).toInt()
        var by = (byScale * bitmapScaleY).toInt()

        colorCallBack?.let {
            bx = if (bx >= colorBitmap.width) colorBitmap.width - 1 else bx
            by = if (by >= colorBitmap.height) colorBitmap.height - 1 else by
            val color = colorBitmap.getPixel(bx, by)
            it.onColorChanger(color)
        }
    }


    /**
     * 初始化 bitmap 和 展示的比例
     */
    private fun initBitmapScale(){
        if (bitmapScaleX == 0.0 || bitmapScaleY == 0.0){
            bitmapScaleX = colorBitmap.width.toDouble()/cImgW.toDouble()
            bitmapScaleY = colorBitmap.height.toDouble()/cImgH.toDouble()
        }
    }
}