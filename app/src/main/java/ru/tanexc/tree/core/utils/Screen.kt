package ru.tanexc.tree.core.utils

import android.os.Parcelable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.FormatOverline
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.FormatOverline
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import ru.tanexc.tree.R


@Parcelize
sealed class Screen(
    val label: Int,
    @IgnoredOnParcel
    val iconOutlined: ImageVector,
    @IgnoredOnParcel
    val iconFilled: ImageVector,
    val tips: Int
): Parcelable {

    object Child : Screen(
        label = R.string.child,
        iconOutlined = Icons.Outlined.AccountTree,
        iconFilled = Icons.Filled.AccountTree,
        tips = R.string.child_tips
    ), Parcelable

    object Node : Screen(
        label = R.string.node,
        iconOutlined = Icons.Outlined.FormatOverline,
        iconFilled = Icons.Filled.FormatOverline,
        tips = R.string.node_tips
    ), Parcelable

    object Settings : Screen(
        label = R.string.settings,
        iconOutlined = Icons.Outlined.Tune,
        iconFilled = Icons.Filled.Tune,
        tips = R.string.settings_tips
    )
}