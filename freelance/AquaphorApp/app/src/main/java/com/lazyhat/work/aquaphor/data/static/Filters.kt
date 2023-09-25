package com.lazyhat.work.aquaphor.data.static

import com.lazyhat.work.aquaphor.R
import com.lazyhat.work.aquaphor.data.models.FilterInfo
import com.lazyhat.work.aquaphor.data.models.Instruction

//Вся информация по фильтрам
object Filters {
    val availableInfos = listOf(
        "Baby Pro" to FilterInfo(
            R.drawable.baby_pro,
            Instruction(
                R.drawable.baby_pro_inst,
                R.string.baby_pro_instruction_install,
                R.string.baby_pro_instruction_replace
            )
        ),
        "Osmo Pro 50" to FilterInfo(
            R.drawable.osmo_pro,
            Instruction(
                R.drawable.osmo_pro_inst,
                R.string.osmo_pro_50_instruction_install,
                R.string.osmo_pro_50_instruction_replace
            )
        ),
        "Eco Pro" to FilterInfo(
            R.drawable.eco_pro,
            Instruction(
                R.drawable.eco_pro_inst,
                R.string.eco_pro_instruction_install,
                R.string.eco_pro_instruction_replace
            )
        ),
        "DWM-202s Pro" to FilterInfo(
            R.drawable.dwm_202s_pro,
            Instruction(
                R.drawable.dwm_202s_pro_inst,
                R.string.dwm_202s_pro_instruction_install,
                R.string.dwm_202s_pro_instruction_replace
            )
        ),
        "Trio Fe" to FilterInfo(
            R.drawable.trio_fe,
            Instruction(
                R.drawable.trio_fe_inst,
                R.string.trio_fe_instruction_install_replace,
                0
            )
        )
    )
}