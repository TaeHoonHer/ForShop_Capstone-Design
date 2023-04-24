package com.example.a4shop

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.a4shop.databinding.ActivityPopupBinding

class MyDialog(
    private val context : AppCompatActivity,
    private val okAction: (String) -> Unit
) {

    private lateinit var binding : ActivityPopupBinding
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감

    fun show() {
        binding = ActivityPopupBinding.inflate(context.layoutInflater)

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(binding.root)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //ok 버튼 동작
        binding.ok.setOnClickListener {
            val keyword = binding.keyword.text.toString()
            okAction(keyword)
            dlg.dismiss()
        }

        //cancel 버튼 동작
        binding.cancel.setOnClickListener {
            dlg.dismiss()
        }

        dlg.show()
    }


}