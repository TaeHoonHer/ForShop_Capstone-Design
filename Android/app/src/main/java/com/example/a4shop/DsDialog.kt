package com.example.a4shop

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.a4shop.databinding.ActivityDspopupBinding
import com.example.a4shop.databinding.ActivityPopupBinding

class DsDialog(
    private val context : AppCompatActivity,
    private val yesAction: (MDcontents) -> Unit
) {

    private lateinit var binding : ActivityDspopupBinding
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감

    fun show() {
        binding = ActivityDspopupBinding.inflate(context.layoutInflater)

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(binding.root)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //Yes 버튼 동작 (비율 데이터, 이미지 파일 MyDataAdapter에 넘기기)
        binding.yes.setOnClickListener {
            val mdContents = MDcontents(0, 0, 0, 0)
            yesAction(mdContents)
            dlg.dismiss()
        }

        //No 버튼 동작
        binding.no.setOnClickListener {
            dlg.dismiss()
        }

        dlg.show()
    }


}