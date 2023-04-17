/*
 * Copyright 2023 Eugene Menshenin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mezhendosina.sgo.app.utils

import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.setPadding
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.R

fun RecyclerView.setupCardDesign() {
    this.setBackgroundResource(R.drawable.shape_diary)

    val px8 = resources.getDimensionPixelSize(R.dimen.diary_vertical_padding)
    val px16 = resources.getDimensionPixelSize(R.dimen.card_diary_horizontal_padding)

    this.updateLayoutParams<MarginLayoutParams> {
        setMargins(px16, px8, px16, 0)

    }
    this.setPadding(0, px8, 0, px8)
}

fun RecyclerView.setupListDesign() {
    this.setBackgroundResource(0)
    val px8 = resources.getDimensionPixelSize(R.dimen.list_diary_horizontal_padding)
    this.updateLayoutParams<MarginLayoutParams> {
        setMargins(px8, 0, px8, 0)
    }
    this.setPadding(0)

}

