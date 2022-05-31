package com.lyni.permission.sources

import androidx.fragment.app.FragmentActivity
import com.lyni.permission.contracts.WeakContextHolder
import java.lang.ref.WeakReference

internal class ActivitySource(activity: FragmentActivity) : WeakContextHolder(WeakReference(activity))
