package com.example.moneycounter4.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 *@author zhangzhe
 *@date 2021/6/7
 *@description
 */

class CombineLatestMediatorLiveData<T1, T2, R>(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    combiner: (T1?, T2?) -> R?
) : MediatorLiveData<R>() {
    init {
        addSource(source1) {
            value = combiner(it, source2.value)
        }
        addSource(source2) {
            value = combiner(source1.value, it)
        }
    }
}

class CombineLatestMediatorLiveDataOfThree<T1, T2, T3, R>(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    source3: LiveData<T3>,
    combiner: (T1?, T2?, T3?) -> R?
) : MediatorLiveData<R>() {
    init {
        addSource(source1) {
            value = combiner(it, source2.value, source3.value)
        }
        addSource(source2) {
            value = combiner(source1.value, it, source3.value)
        }
        addSource(source3) {
            value = combiner(source1.value, source2.value, it)
        }
    }
}

class CombineLatestMediatorLiveDataOfFour<T1, T2, T3, T4, R>(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    source3: LiveData<T3>,
    source4: LiveData<T4>,
    combiner: (T1?, T2?, T3?, T4?) -> R?
) : MediatorLiveData<R>() {
    init {
        addSource(source1) {
            value = combiner(it, source2.value, source3.value, source4.value)
        }
        addSource(source2) {
            value = combiner(source1.value, it, source3.value, source4.value)
        }
        addSource(source3) {
            value = combiner(source1.value, source2.value, it, source4.value)
        }
        addSource(source4) {
            value = combiner(source1.value, source2.value, source3.value, it)
        }
    }
}