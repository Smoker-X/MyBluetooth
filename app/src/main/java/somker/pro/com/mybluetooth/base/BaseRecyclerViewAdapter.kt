package somker.pro.com.mybluetooth.base

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ClickUtils

/**
 * Created by Smoker on 2019/12/31.
 * 说明：BaseRecyclerViewAdapter
 */
abstract class BaseRecyclerViewAdapter<T : RecyclerView.ViewHolder> : RecyclerView.Adapter<T>() {

    protected lateinit var mContext :Context

    private var clickListener: OnRecyclerViewItemClick? = null

    private var longListener: OnRecyclerViewItemLongClick? = null

    /**
     * 添加点击事件
     */
    fun addClickListener(listener: OnRecyclerViewItemClick) {
        this.clickListener = listener
    }

    /**
     * 点击长按事件
     */
    fun addLongClickListener(listener: OnRecyclerViewItemLongClick) {
        this.longListener = listener
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): T {
        mContext = p0.context
        val mView = LayoutInflater.from(mContext).inflate(onCreateLayout(p0, p1), p0, false)
        return onCreateHolder(mView, p1)
    }

    @LayoutRes
    abstract fun onCreateLayout(parent: ViewGroup, itemType: Int): Int

    abstract fun onCreateHolder(mView: View, itemType: Int): T


    protected fun touchClickListener(view: View, position: Int) {
        clickListener?.onRecyclerViewItemClick(view, position)
    }

    protected fun touchLongClickListener(view: View, position: Int) {
        longListener?.onRecyclerViewItemLongClick(view, position)
    }


    /**
     * item 的点击事件
     */
    interface OnRecyclerViewItemClick {
        fun onRecyclerViewItemClick(view: View, position: Int)
    }


    /**
     * item 的长按事件
     */
    interface OnRecyclerViewItemLongClick {
        fun onRecyclerViewItemLongClick(view: View, position: Int)
    }


    abstract inner class BaseRecyclerViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView), View.OnLongClickListener {

        protected fun addClick(@NonNull clickView: View) {
            ClickUtils.applySingleDebouncing(clickView) {
                touchClickListener(it, adapterPosition)
            }
        }

        protected fun addLongClick(@NonNull clickView: View) {
            clickView.setOnLongClickListener(this)
        }

        override fun onLongClick(v: View?): Boolean {
            v?.let {
                touchLongClickListener(it, adapterPosition)
            }
            return false
        }
    }
}