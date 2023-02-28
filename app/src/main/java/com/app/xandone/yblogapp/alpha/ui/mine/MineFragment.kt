package com.xandone.manager2.ui.mine

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.xandone.manager2.R
import com.xandone.manager2.databinding.FragMineBinding
import com.xandone.manager2.extention.bindClick

/**
 * @author: xiao
 * created on: 2023/2/17 16:44
 * description:
 */
class MineFragment : Fragment() {

    private lateinit var mBinding: FragMineBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragMineBinding.inflate(layoutInflater)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindClick(mBinding.btn1) {
            when (this.id) {
                R.id.btn1 -> Log.d("dgdfgf", "1111111")
            }
        }
    }

    companion object {
        fun getInstance(): MineFragment {
            return MineFragment()
        }
    }
}