package somker.pro.com.mybluetooth.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout

/**
 * Created by Smoker on 2020/1/21.
 * 说明：LinearLayout 基类
 */
abstract  class BaseLinearLayout :LinearLayout{
    constructor(context: Context?) : super(context){
        initLayout(context)
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        initLayout(context)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initLayout(context)
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun initAfter()

    private fun initLayout(context: Context?) {
        View.inflate(context,getLayoutId(), this)
        initAfter()
    }


}