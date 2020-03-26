package somker.pro.com.mybluetooth.utils

import android.content.Context
import android.widget.TextView

/**
 * Created by Smoker on 2020/2/9.
 * 说明：更改textview 的 Drawable
 */
class DrawableUtils {
    companion object{

        fun changeTvDrawableLeft(context: Context, myTextview: TextView, drawableId: Int) {
            if (drawableId == 0) {
                myTextview.setCompoundDrawables(null, null, null, null)
                return
            }
            val drawable = context.resources.getDrawable(drawableId)
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            myTextview.setCompoundDrawables(drawable, null, null, null)
        }

        fun changeTvDrawableTop(context: Context, myTextview: TextView, drawableId: Int) {
            if (drawableId == 0) {
                myTextview.setCompoundDrawables(null, null, null, null)
                return
            }
            val drawable = context.resources.getDrawable(drawableId)
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            myTextview.setCompoundDrawables(null, drawable, null, null)
        }

        fun changeTvDrawableRight(context: Context, myTextview: TextView, drawableId: Int) {
            if (drawableId == 0) {
                myTextview.setCompoundDrawables(null, null, null, null)
                return
            }
            val drawable = context.resources.getDrawable(drawableId)
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            myTextview.setCompoundDrawables(null, null, drawable, null)
        }

        fun changeTvDrawableBottom(context: Context, myTextview: TextView, drawableId: Int) {
            if (drawableId == 0) {
                myTextview.setCompoundDrawables(null, null, null, null)
                return
            }
            val drawable = context.resources.getDrawable(drawableId)
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            myTextview.setCompoundDrawables(null, null, null, drawable)
        }


        fun changeTvDrawableLeftAndRight(
            context: Context,
            myTextview: TextView,
            drawableIdLeft: Int,
            drawableIdRight: Int
        ) {
            if (drawableIdLeft == 0 || drawableIdRight == 0) {
                myTextview.setCompoundDrawables(null, null, null, null)
                return
            }
            val drawableLeft = context.resources.getDrawable(drawableIdLeft)
            val drawableRight = context.resources.getDrawable(drawableIdRight)
            /// 这一步必须要做,否则不会显示.
            drawableLeft.setBounds(0, 0, drawableLeft.minimumWidth, drawableLeft.minimumHeight)
            drawableRight.setBounds(0, 0, drawableRight.minimumWidth, drawableRight.minimumHeight)

            myTextview.setCompoundDrawables(drawableLeft, null, drawableRight, null)
        }

    }
}