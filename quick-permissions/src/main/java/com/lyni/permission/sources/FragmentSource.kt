package com.lyni.permission.sources

import androidx.fragment.app.Fragment
import com.lyni.permission.contracts.WeakContextHolder
import java.lang.ref.WeakReference

internal class FragmentSource(fragment: Fragment) : WeakContextHolder(WeakReference(fragment.requireContext()))
